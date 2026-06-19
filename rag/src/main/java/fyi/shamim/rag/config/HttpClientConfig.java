package fyi.shamim.rag.config;

import fyi.shamim.rag.advanced.httpclient.cohere.CohereClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/19/26
 * Email: mdshamim723@gmail.com
 */

@Configuration
public class HttpClientConfig {

    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(RestClient restClient, Environment environment) {

        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .embeddedValueResolver(environment::resolvePlaceholders)
                .build();
    }

    @Bean
    public CohereClient cohereClient(HttpServiceProxyFactory httpServiceProxyFactory) {

        return httpServiceProxyFactory.createClient(CohereClient.class);
    }

}
