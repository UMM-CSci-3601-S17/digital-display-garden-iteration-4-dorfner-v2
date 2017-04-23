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
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutionException;

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

    // Public/Private Keypair for signing our own JSON Web Tokens
    private KeyPair keyPair;

    public Auth(String clientId, String clientSecret) throws NoSuchAlgorithmException {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.gson = new Gson();
        this.authUsers = new ArrayList<>();
        authUsers.add("gordo580@morris.umn.edu");
        authUsers.add("schr1230@morris.umn.edu");
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
    public String getAuthURL(String originatingURL){
        // I think we have to create a new service for every request we send out
        // since each one needs a different secretState
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .scope("email") // replace with desired scope
                .state(generateSharedGoogleSecret(originatingURL))
                .callback("http://localhost:2538/callback")
                .build(GoogleApi20.instance());

        final Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("access_type", "offline");
        additionalParams.put("prompt", "consent");
        final String authorizationUrl = service.getAuthorizationUrl(additionalParams);

        return authorizationUrl;
    }

    /**
     *
     * @param jwt - a JSON Web Token, signed by us, that contains the authorization
     *            info
     * @return true if this is a JWT token, that we signed, that is not expired, else return false
     */
    public boolean checkAuthorization(String jwt) {
        if (null == jwt) {
            // null JWTs are obviously invalid
            return false;
        }
        try {

            SignedJWT parsedState = SignedJWT.parse(jwt);
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey)keyPair.getPublic());
            if (parsedState.verify(verifier)) {
                // the signature is valid, so check the expiration date
                TimeStampToken timeStampToken = gson.fromJson(parsedState
                        .getPayload()
                        .toJSONObject()
                        .toString(), TimeStampToken.class);
                DateTime exp = new DateTime(timeStampToken.exp);
                return exp.isAfterNow();
            } else {
                // cookie had invalid signature
                return false;
            }
        } catch (ParseException e) {
            // the cookie is not a valid jwt, reject
            return false;
        } catch (JOSEException e) {
            // the cookie "couldn't be verified"
            return false;
        }
    }

    String generateCookieBody(int secondsToLive) {
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        JWSSigner signer = new RSASSASigner(privateKey);

        DateTime expDate = new DateTime((new Date()).getTime() + secondsToLive * 1000);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("digital-display-garden")
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



    String generateSharedGoogleSecret(String originatingURL) {
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        JWSSigner signer = new RSASSASigner(privateKey);

        // Expire in 60 seconds
        DateTime expDate = new DateTime((new Date()).getTime() + 60 * 1000);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("digital-display-garden")
                .claim("originating_url", originatingURL)
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

    /**
     * This method verifies that we signed the TimeStamp
     * token and returns the payload parsed into a RedirectToken
     * @param state a JWT that we generated to send to Google
     * @return The parsed body of the JWT, or null if an error occurred
     */
    RedirectToken unpackSharedGoogleSecret(String state) {
        try {
            SignedJWT parsedState = SignedJWT.parse(state);
            JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey)keyPair.getPublic());
            if (parsedState.verify(verifier)) {
                return gson.fromJson(parsedState
                        .getPayload()
                        .toJSONObject()
                        .toString(), RedirectToken.class);

            } else {
                // failed signature
                return null;
            }
        } catch (ParseException e) {
            // the state couldn't be parsed, so it must be invalid
            return null;
        } catch (JOSEException e) {
            // we got a generic error when verifying the JWT, so it must be invalid
            return null;
        }
    }


    public String verifyCallBack(String state, String code) {
        // parse the state and ensure its validity
        RedirectToken verifiedState = unpackSharedGoogleSecret(state);
        DateTime expTime = new DateTime(verifiedState.exp);
        if(expTime.isAfterNow()) {
            // error
        }

        try {
            // Use the callback code to get a token from Google with info
            // about the caller
            OAuth2AccessToken accessToken = globalService.getAccessToken(code);
            accessToken = globalService.refreshAccessToken(accessToken.getRefreshToken());

            // parse the token for the fields we want
            GoogleToken googleToken = gson.fromJson(accessToken.getRawResponse(), GoogleToken.class);

            // Get the URL for Google's OpenID description
            OpenIDConfiguration openIDConfiguration = getJwksUrl();

            // Confirm that the token is signed properly and extract the body as JSON
            String stringBody = parseAndValidate(googleToken.id_token, new URL(openIDConfiguration.jwks_uri));

            // Parse the JSON for the fields we care about
            GoogleJwtBody body = gson.fromJson(stringBody, GoogleJwtBody.class);

            // Confirm that the user is on our whitelist
            boolean authorized = userIsVerified(body.email);
            if (authorized) {
                return verifiedState.originating_url;
            } else {
                return null;
            }

        } catch (IOException|InterruptedException|ExecutionException e) {
            // unknown errors
            // todo: add handling
            e.printStackTrace();
            return null;
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
        // 24 hours/day * 60 minutes/hour * 60 seconds/minute = 86400 seconds/day
        int timeToLive = 86400;
        String cookieBody = generateCookieBody(timeToLive);
        Cookie c = new Cookie("ddg", cookieBody, timeToLive);

        return c;
    }

}
