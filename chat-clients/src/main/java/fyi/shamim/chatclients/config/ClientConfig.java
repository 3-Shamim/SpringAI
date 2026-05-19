package fyi.shamim.chatclients.config;

import fyi.shamim.chatclients.clients.OpenAiClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 5/19/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
@ImportHttpServices(value = {OpenAiClient.class})
public class ClientConfig {
}
