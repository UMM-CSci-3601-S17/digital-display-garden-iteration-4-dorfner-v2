package umm3601.digitalDisplayGarden;

import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Auth {
    private final String clientId;
    private final String clientSecret;
    private final Map<String, OAuth20Service> quedAuth;
    private static final String PROTECTED_RESOURCE_URL = "https://www.googleapis.com/plus/v1/people/me?fields=emails";

    public Auth(String clientId, String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.quedAuth = new HashMap<String, OAuth20Service>();
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
        final String authorizationUrl = service.getAuthorizationUrl(additionalParams);
        quedAuth.put(secretState, service);

        return authorizationUrl;
    }

    public String getProfile(String state, String code){
        try {
            OAuth20Service service = quedAuth.get(state);
            OAuth2AccessToken accessToken = service.getAccessToken(code);
            accessToken = service.refreshAccessToken(accessToken.getRefreshToken());
            final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
            service.signRequest(accessToken, request);
            final Response response = service.execute(request);
            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }




}
