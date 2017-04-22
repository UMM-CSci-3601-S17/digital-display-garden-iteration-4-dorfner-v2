package umm3601.digitalDisplayGarden.Authentication;

// For interacting with Google OAuth
import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

// For parsing the JWT from Google OAuth
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.JWTClaimsSet;

import com.google.gson.Gson;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class Auth {

    // Google recommends that this URL be hardcoded into programs. (see: https://developers.google.com/identity/protocols/OpenIDConnect#discovery)
    private static final String googleOpenIDConfigurationEndpoint = "https://accounts.google.com/.well-known/openid-configuration";

    // Contains the clientId from Google API
    private final String clientId;

    // Contains the clientSecret from Google API
    private final String clientSecret;

    // Contains a list of states that grows when we issue an
    // authentication URL and is used to confirm that callbacks
    // are legitimate
    private final List<String> states;

    // Used for parsing converting to and from JSON
    private final Gson gson;

    // For authenticating callbacks
    private final OAuth20Service globalService;

    // hard-coded list of users that we will accept
    private List<String> authUsers;

    // list of cookies currently issues to accepted users
    private List<Cookie> authCookies;

    public Auth(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.states = new ArrayList<>();
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
    }

    /**
     * @return A string containing a URL that we can send visitors to in order to authenticate them
     */
    public String getAuthURL(){
        final String secretState = "secret" + new Random().nextInt(999_999);
        // I think we have to create a new service for every request we send out
        // since each one needs a different secretState
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .scope("email") // replace with desired scope
                .state(secretState)
                .callback("http://localhost:2538/callback")
                .build(GoogleApi20.instance());

        final Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("access_type", "offline");
        additionalParams.put("prompt", "consent");
        final String authorizationUrl = service.getAuthorizationUrl(additionalParams);
        states.add(secretState);

        return authorizationUrl;
    }



    public String getEmail(String state, String code){

        try {

            // check that this is a legitimate request
            if (states.contains(state)) {
                states.remove(state);
            } else {
                return "";
            }
            OAuth2AccessToken accessToken = globalService.getAccessToken(code);
            accessToken = globalService.refreshAccessToken(accessToken.getRefreshToken());


            GoogleToken googleToken = gson.fromJson(accessToken.getRawResponse(), GoogleToken.class);
            OpenIDConfiguration openIDConfiguration = getJwksUrl();

            String stringBody = parseAndValidate(googleToken.id_token, new URL(openIDConfiguration.jwks_uri));
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
