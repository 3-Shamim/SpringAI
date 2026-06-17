package fyi.shamim.rag.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/11/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "app.rag")
@Setter
@Getter
public class RagConfigData {

    private boolean forceRebuild;
    private int topK;
    private double similarityThreshold;
    private int radius;
    private RagChunkProperties chunk;
    private PdfProperties pdf;
    private Map<String, String> synonyms;

    @Setter
    @Getter
    public static class RagChunkProperties {

        private int size;
        private int minChunkSize;
        private int minChunkToEmbed;
        private int maxChunkSize;
        private boolean keepSeparator;

    }

    @Setter
    @Getter
    public static class PdfProperties {

        private String mode;
        private String path;
        private int pagesPerDocument;
        private boolean leftAlignment;
        private int numberOfTopTextLineToDelete;
        private int numberOfBottomTextLineToDelete;

    }

}
