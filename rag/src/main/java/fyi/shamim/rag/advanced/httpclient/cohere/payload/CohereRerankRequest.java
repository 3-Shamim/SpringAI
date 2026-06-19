package fyi.shamim.rag.advanced.httpclient.cohere.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/18/26
 * Email: mdshamim723@gmail.com
 */

public record CohereRerankRequest(
        String model,
        String query,
        @JsonProperty("top_n")
        int topN,
        List<String> documents
) {
}
