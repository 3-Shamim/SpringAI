package fyi.shamim.aiagents.service;

import fyi.shamim.aiagents.dto.ReviewDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/1/26
 * Email: mdshamim723@gmail.com
 */

public interface SecurityReviewService {

    String enqueueAndExecute(MultipartFile file);

    ReviewDto getSecurityReview(String id);

    String followUpWithVision(String id, String question);

}
