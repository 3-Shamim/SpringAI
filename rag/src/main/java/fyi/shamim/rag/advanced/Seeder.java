package fyi.shamim.rag.advanced;

import fyi.shamim.rag.advanced.service.RagAdvancedIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/15/26
 * Email: mdshamim723@gmail.com
 */

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

    private final RagAdvancedIngestionService ragAdvancedIngestionService;

    @Override
    public void run(String... args) throws Exception {
        ragAdvancedIngestionService.initializePgVectorStore();
    }

}
