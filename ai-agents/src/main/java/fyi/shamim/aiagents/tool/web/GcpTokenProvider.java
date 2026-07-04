package fyi.shamim.aiagents.tool.web;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/28/26
 * Email: mdshamim723@gmail.com
 */

@Component
public class GcpTokenProvider {

    private static final List<String> SCOPES = List.of("https://www.googleapis.com/auth/cloud-platform");

    public String getAccessTokenValue() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials.getApplicationDefault().createScoped(SCOPES);
        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }

}