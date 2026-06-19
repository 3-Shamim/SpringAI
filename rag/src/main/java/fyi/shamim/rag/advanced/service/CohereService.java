package fyi.shamim.rag.advanced.service;

import fyi.shamim.rag.advanced.httpclient.cohere.payload.CohereRerankResponse;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/18/26
 * Email: mdshamim723@gmail.com
 */

public interface CohereService {

    CohereRerankResponse scores(String query, List<String> documents);

}
