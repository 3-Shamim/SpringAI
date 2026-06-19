package fyi.shamim.rag.advanced.httpclient.cohere.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/18/26
 * Email: mdshamim723@gmail.com
 */

public record CohereRerankResponse(
        String id,
        List<Score> results,
        Meta meta
) {

    public record Score(int index,
                        @JsonProperty("relevance_score")
                        double relevanceScore) {
    }

    public record Meta(
            @JsonProperty("api_version")
            ApiVersion apiVersion,
            @JsonProperty("billed_units")
            BilledUnit billedUnit
    ) {
    }

    public record ApiVersion(
            String version,
            @JsonProperty("is_experimental")
            boolean experimental
    ) {
    }

    public record BilledUnit(
            @JsonProperty("search_units")
            int searchUnits
    ) {
    }

}
