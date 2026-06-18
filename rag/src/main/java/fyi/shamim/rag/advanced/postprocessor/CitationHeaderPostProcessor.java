package fyi.shamim.rag.advanced.postprocessor;

import fyi.shamim.rag.advanced.constant.RagConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/18/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class CitationHeaderPostProcessor implements DocumentPostProcessor {

    @Override
    public List<Document> process(Query query, List<Document> documents) {

        if (CollectionUtils.isEmpty(documents)) {
            return documents;
        }

        return documents.stream()
                .map(d -> {

                    Map<String, Object> metadata = d.getMetadata();

                    String newContent = "[%s:%s, %s:%s]\n%s".formatted(
                            RagConstant.SOURCE,
                            getSource(metadata),
                            RagConstant.PAGE_SNAKE,
                            getPageNumber(metadata),
                            d.getFormattedContent()
                    );

                    return new Document(newContent, metadata);
                })
                .toList();
    }

    private String getSource(Map<String, Object> metadata) {
        return String.valueOf(metadata.getOrDefault(RagConstant.SOURCE, RagConstant.UNKNOWN));
    }

    private String getPageNumber(Map<String, Object> metadata) {

        Object pageNumber = metadata.get(RagConstant.PAGE_SNAKE);

        if (pageNumber == null) {
            pageNumber = metadata.get(RagConstant.PAGE_CAMEL);
        }

        if (pageNumber == null) {
            pageNumber = metadata.get(RagConstant.PAGE);
        }

        return pageNumber == null ? RagConstant.UNKNOWN : String.valueOf(pageNumber);
    }

}
