package fyi.shamim.rag.advanced.preprocessor;

import fyi.shamim.rag.config.RagConfigData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/16/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Component
public class DomainSynonymTransformer implements QueryTransformer {

    private final Map<Pattern, String> replacements;

    public DomainSynonymTransformer(RagConfigData ragConfigData) {

        Map<String, String> synonyms = ragConfigData.getSynonyms();
        this.replacements = new LinkedHashMap<>();

        if (!CollectionUtils.isEmpty(synonyms)) {

            synonyms.forEach((k, v) -> {
                this.replacements.put(Pattern.compile("\\b" + k + "\\b", Pattern.CASE_INSENSITIVE), v);
            });

        }

    }


    @Override
    public Query transform(Query query) {

        String text = query.text();

        if (StringUtils.isEmpty(text)) {
            return query;
        }

        String cleanText = getCleanText(text);
        cleanText = applyDomainSynonyms(cleanText);

        log.info("Transformed query: {}", cleanText);

        return Query.builder()
                .text(cleanText)
                .build();
    }

    private String getCleanText(String text) {

        return text.trim()
                .replaceAll("(?i)^(please|can you|could you)\\s+", "")
                .replaceAll("\\s+\\?$", "");
    }

    private String applyDomainSynonyms(String cleanText) {

        for (var entry : this.replacements.entrySet()) {
            cleanText = entry.getKey().matcher(cleanText).replaceAll(entry.getValue());
        }

        return cleanText;
    }

}
