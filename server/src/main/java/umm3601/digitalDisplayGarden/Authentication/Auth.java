package umm3601.digitalDisplayGarden.Authentication;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.impl.Base64Codec;
import io.jsonwebtoken.impl.Base64UrlCodec;
import io.jsonwebtoken.impl.TextCodec;
import spark.utils.IOUtils;
import sun.security.rsa.RSAPublicKeyImpl;
import java.security.*;
import java.security.spec.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.security.PublicKey;
import java.util.*;

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
            GoogleKeyList googleKeyList = getCerts(openIDConfiguration.jwks_uri);

//            System.out.println(Jwts.parser().setSigningKeyResolver(new SigningKeyResolver() {
//                @Override
//                public Key resolveSigningKey(JwsHeader header, Claims claims) {
//                    return null;
//                }
//
//                @Override
//                public Key resolveSigningKey(JwsHeader header, String plaintext) {
//                    return null;
//                }
//            }));
            TextCodec tc = new Base64UrlCodec();
            String[] chunks = googleToken.id_token.split("\\.");
            JwtHeader jwtHeader = gson.fromJson(tc.decodeToString(chunks[0]), JwtHeader.class);

            GoogleKeyInfo googleKeyInfo = null;
            for (int i = 0; i < googleKeyList.keys.length; i++) {
                GoogleKeyInfo gki = googleKeyList.keys[i];

                if (gki.kid.equals(jwtHeader.kid)){
                    System.out.println(gki.kid);
                    googleKeyInfo = gki;
                    break;
                }
            }
            if (googleKeyInfo == null) {
                System.out.println("Blargggggggg\n\nadfasf\n\n");
            }


            BigInteger n = new BigInteger(tc.decode(googleKeyInfo.n));
            BigInteger e = new BigInteger(tc.decode(googleKeyInfo.e));
            // byte[] header = tc.decode(chunks[0]);
            byte[] message = tc.decode(chunks[1]);

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n,e);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(keySpec);
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update((chunks[0] + "." + chunks[1]).getBytes());
            System.out.println(sig.verify(tc.decode(chunks[2])));

            // PublicKey publicKey = new RSAPublicKeyImpl(n,e);

//            System.out.println();
            System.out.println(tc.decodeToString(chunks[0]));
//            System.out.println(tc.decodeToString(chunks[2]));
            // System.out.println(googleToken.id_token);
            // System.out.println(Jwts.parser()
            //     .setSigningKey(publicKey)
            //                    .parseClaimsJws(googleToken.id_token).getBody());
//            System.out.println(googleToken.id_token);
            return "Fooooo";
//            return accessToken.toString();
//            final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
//            service.signRequest(accessToken, request);
//            final Response response = service.execute(request);
//            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
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

    public GoogleKeyList getCerts(String url) throws IOException {
        try {
            InputStream in = new URL(url).openStream();
            return gson.fromJson(IOUtils.toString(in),GoogleKeyList.class);
        } catch (MalformedURLException e) {
            System.out.println("Failed in Fetching Google certs");
            throw e;
        }
    }

}
