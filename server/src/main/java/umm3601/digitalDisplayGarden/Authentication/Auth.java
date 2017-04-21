package umm3601.digitalDisplayGarden.Authentication;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

import com.google.gson.Gson;
import spark.utils.IOUtils;
// import sun.security.rsa.RSAPublicKeyImpl;
import java.security.*;
import java.security.spec.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;

import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.source.*;
import com.nimbusds.jwt.*;
import com.nimbusds.jwt.proc.*;

public class Auth {

    // Google recommends that this URL be hardcoded into programs. (see: https://developers.google.com/identity/protocols/OpenIDConnect#discovery)
    private static final String googleOpenIDConfigurationEndpoint = "https://accounts.google.com/.well-known/openid-configuration";
    private final String clientId;
    private final String clientSecret;
    private final Map<String, OAuth20Service> quedAuth;
    private final Gson gson;
//    private static final String PROTECTED_RESOURCE_URL = "https://accounts.google.com/o/oauth2/v2/auth";

    public Auth(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.quedAuth = new HashMap<String, OAuth20Service>();
        this.gson = new Gson();
    }

    public String getAuthURL(){
        final String secretState = "secret" + new Random().nextInt(999_999);
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
//        additionalParams.put("response_type", "code");
        final String authorizationUrl = service.getAuthorizationUrl(additionalParams);
        quedAuth.put(secretState, service);

        return authorizationUrl;
    }



    public String getEmails(String state, String code){

        try {
            OAuth20Service service = quedAuth.get(state);
            OAuth2AccessToken accessToken = service.getAccessToken(code);
            accessToken = service.refreshAccessToken(accessToken.getRefreshToken());


            GoogleToken googleToken = gson.fromJson(accessToken.getRawResponse(), GoogleToken.class);
            OpenIDConfiguration openIDConfiguration = getJwksUrl();

            String body = parseAndValidate(googleToken.id_token, new URL(openIDConfiguration.jwks_uri));

            return body;

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

}
