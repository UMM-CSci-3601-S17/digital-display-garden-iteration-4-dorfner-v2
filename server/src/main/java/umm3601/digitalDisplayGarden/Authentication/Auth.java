package umm3601.digitalDisplayGarden.Authentication;

// For interacting with Google OAuth
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

// For parsing the JWT from Google OAuth
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.JWTClaimsSet;

import com.google.gson.Gson;
import org.joda.time.DateTime;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

import com.nimbusds.jose.*;

public class Auth {

    // Google recommends that this URL be hardcoded into programs. (see: https://developers.google.com/identity/protocols/OpenIDConnect#discovery)
    private static final String googleOpenIDConfigurationEndpoint = "https://accounts.google.com/.well-known/openid-configuration";

    // Contains the clientId from Google API
    private final String clientId;

    // Contains the clientSecret from Google API
    private final String clientSecret;

    // Used for parsing converting to and from JSON
    private final Gson gson;

    // For authenticating callbacks
    private final OAuth20Service globalService;

    // hard-coded list of users that we will accept
    private List<String> authUsers;

    // list of cookies currently issues to accepted users
    private List<Cookie> authCookies;

    // Public/Private Keypair for signing our own JSON Web Tokens
    private KeyPair keyPair;

    public Auth(String clientId, String clientSecret) throws NoSuchAlgorithmException {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.gson = new Gson();
        this.authUsers = new ArrayList<>();
        authUsers.add("gordo580@morris.umn.edu");
        authUsers.add("schr1230@morris.umn.edu");
        this.authCookies = new LinkedList<>();
        this.globalService = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .scope("email") // replace with desired scope
                .callback("http://localhost:2538/callback")
                .build(GoogleApi20.instance());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        this.keyPair = keyPairGenerator.genKeyPair();
    }

    /**
     * @return A string containing a URL that we can send visitors to in order to authenticate them
     */
    public String getAuthURL(){
        // I think we have to create a new service for every request we send out
        // since each one needs a different secretState
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .scope("email") // replace with desired scope
                .state(generateSharedGoogleSecret("todo: replace me"))
                .callback("http://localhost:2538/callback")
                .build(GoogleApi20.instance());

        final Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("access_type", "offline");
        additionalParams.put("prompt", "consent");
        final String authorizationUrl = service.getAuthorizationUrl(additionalParams);

        return authorizationUrl;
    }

    String generateSharedGoogleSecret(String originatingURL) {
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        JWSSigner signer = new RSASSASigner(privateKey);

        // Expire in 60 seconds
        DateTime expDate = new DateTime((new Date()).getTime() + 60 * 1000);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("digital-display-garden")
                .claim("originating-url", originatingURL)
                .claim("exp", expDate.toString())
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256),
                claimsSet
        );
        try {
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            return "";
        }
    }

    String unpackSharedGoogleSecret(String state) {
        try {

            SignedJWT parsedState = SignedJWT.parse(state);
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey)keyPair.getPublic());
            if (parsedState.verify(verifier)) {

                System.out.println(parsedState.getPayload());
                CallBackState callBackState = gson.fromJson(parsedState
                        .getPayload()
                        .toJSONObject()
                        .toString(), CallBackState.class);
                DateTime exp = new DateTime(callBackState.exp);
                System.out.println(exp.isAfterNow());
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Nooooo\n\nooooooooooo");
            return null;
        }
    }



    public String getEmail(String state, String code){

        try {

            // check that this is a legitimate request
            String verifiedState = unpackSharedGoogleSecret(state);
            System.out.println(verifiedState);

            OAuth2AccessToken accessToken = globalService.getAccessToken(code);
            accessToken = globalService.refreshAccessToken(accessToken.getRefreshToken());


            GoogleToken googleToken = gson.fromJson(accessToken.getRawResponse(), GoogleToken.class);
            OpenIDConfiguration openIDConfiguration = getJwksUrl();

            String stringBody = parseAndValidate(googleToken.id_token, new URL(openIDConfiguration.jwks_uri));
            System.out.println(stringBody);
            GoogleJwtBody body = gson.fromJson(stringBody, GoogleJwtBody.class);
            return body.email;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String parseAndValidate(String jwt, URL keyOptions) {

        try {
            ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
            JWKSource keySource = new RemoteJWKSet(keyOptions);
            JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256; // should this really be hardcoded?
            JWSKeySelector keySelector = new JWSVerificationKeySelector(expectedJWSAlg, keySource);
            jwtProcessor.setJWSKeySelector(keySelector);

            SecurityContext ctx = null; // optional context parameter, not required here
            JWTClaimsSet claimsSet = jwtProcessor.process(jwt, null);

            return claimsSet.toJSONObject().toString();
        } catch (Exception e) {
            System.err.println("Nooooo\n\nooooooooooo");
            return null;
        }
    }

    public OpenIDConfiguration getJwksUrl() throws IOException {
        try {
            InputStream in = new URL(googleOpenIDConfigurationEndpoint).openStream();
            return gson.fromJson(IOUtils.toString(in),OpenIDConfiguration.class);
        } catch (MalformedURLException e) {
            System.out.println("Your dev is an idiot. Authentication will fail because we could not verify Google's signatures.");
            return null;
        } catch (IOException e) {
            System.out.println("Failed fetching Google's OpenID configuration.");
            throw e;
        }
    }

    public boolean userIsVerified(String email){
        return authUsers.contains(email);
    }

    public Cookie getCookie(){
        String cookieBody = "" + new Random().nextInt(Integer.MAX_VALUE);
        Cookie c = new Cookie("ddg", cookieBody, 86400);
        authCookies.add(c);

        return c;
    }

}
