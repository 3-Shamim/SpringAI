package fyi.shamim.rag.advanced.service.impl;

import fyi.shamim.rag.advanced.httpclient.cohere.CohereClient;
import fyi.shamim.rag.advanced.httpclient.cohere.payload.CohereRerankRequest;
import fyi.shamim.rag.advanced.httpclient.cohere.payload.CohereRerankResponse;
import fyi.shamim.rag.advanced.service.CohereService;
import fyi.shamim.rag.config.RagConfigData;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/18/26
 * Email: mdshamim723@gmail.com
 */

@Service
public class CohereServiceImpl implements CohereService {

    private final RagConfigData.RerankProperties rerank;
    private final CohereClient cohereClient;

    public CohereServiceImpl(RagConfigData ragConfigData, CohereClient cohereClient) {
        this.rerank = ragConfigData.getRerank();
        this.cohereClient = cohereClient;
    }

    @Override
    public CohereRerankResponse scores(String query, List<String> documents) {

        return cohereClient.rerank(
                String.format("Bearer %s", rerank.getApiKey()),
                new CohereRerankRequest(
                        rerank.getModel(),
                        query,
                        rerank.getTopN(),
                        documents
                )
        );
    }

}
