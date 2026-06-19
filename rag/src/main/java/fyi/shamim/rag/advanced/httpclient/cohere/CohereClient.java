package fyi.shamim.rag.advanced.httpclient.cohere;

import fyi.shamim.rag.advanced.httpclient.cohere.payload.CohereRerankRequest;
import fyi.shamim.rag.advanced.httpclient.cohere.payload.CohereRerankResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/18/26
 * Email: mdshamim723@gmail.com
 */

@HttpExchange
public interface CohereClient {

    @PostExchange(value = "${app.rag.rerank.url}", contentType = MediaType.APPLICATION_JSON_VALUE)
    CohereRerankResponse rerank(
            @RequestHeader(value = "Authorization") String bearer,
            @RequestBody CohereRerankRequest body
    );

}
