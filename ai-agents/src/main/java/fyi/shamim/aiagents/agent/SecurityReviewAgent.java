package fyi.shamim.aiagents.agent;

import fyi.shamim.aiagents.dto.ReviewStateDto;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/29/26
 * Email: mdshamim723@gmail.com
 */

public interface SecurityReviewAgent {

    String execute(ReviewStateDto stateDto);

    String followUp(String reviewId, String question);

}
