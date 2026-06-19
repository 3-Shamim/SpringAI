package fyi.shamim.rag.advanced.postprocessor;

import fyi.shamim.rag.advanced.httpclient.cohere.payload.CohereRerankResponse;
import fyi.shamim.rag.advanced.service.CohereService;
import fyi.shamim.rag.config.RagConfigData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/18/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class RerankPostProcessor implements DocumentPostProcessor {

    private final CohereService cohereService;
    private final RagConfigData ragConfigData;

    @Override
    public List<Document> process(Query query, List<Document> documents) {

        try {

            List<String> textList = documents.stream().map(Document::getFormattedContent).toList();
            CohereRerankResponse scoreRes = cohereService.scores(query.text(), textList);

            List<Document> rerankDocuments = new ArrayList<>();
            Set<Integer> seen = new HashSet<>();
            int topN = Math.min(ragConfigData.getRerank().getTopN(), documents.size());

            for (CohereRerankResponse.Score result : scoreRes.results()) {
                int index = result.index();
                rerankDocuments.add(documents.get(index));
                seen.add(index);
            }

            for (int i = 0; i < documents.size(); i++) {

                if (rerankDocuments.size() >= topN) {
                    continue;
                }

                if (!seen.contains(i)) {
                    rerankDocuments.add(documents.get(i));
                }

            }

            return rerankDocuments;
        } catch (Exception e) {
            log.warn("Rerank error: {}, returning original documents.", e.getMessage(), e);
            //if reranker fails, don't block the answer
            return documents;
        }
    }

    private double[] getScores(CohereRerankResponse scoreRes, int size) {

        double[] scores = new double[size];

        Arrays.fill(scores, Double.NEGATIVE_INFINITY);

        for (CohereRerankResponse.Score result : scoreRes.results()) {

            int index = result.index();

            if (index >= 0 && index < scores.length) {
                scores[index] = result.relevanceScore();
            }

        }

        setScoreForUntouched(scores);

        return scores;
    }

    private void setScoreForUntouched(double[] scores) {

        for (int i = 0; i < scores.length; i++) {

            if (scores[i] == Double.NEGATIVE_INFINITY) {
                scores[i] = -1.0;  //set to -1 so they sort last
            }

        }

    }

}
