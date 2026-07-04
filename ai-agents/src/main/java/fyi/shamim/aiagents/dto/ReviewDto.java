package fyi.shamim.aiagents.dto;

import fyi.shamim.aiagents.enums.ReviewStatus;
import lombok.*;

import java.time.Instant;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 6/29/26
 * Email: mdshamim723@gmail.com
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReviewDto {

    private String id;
    private ReviewStatus status;
    private String reportMarkdown;
    private String errorMessages;
    private Instant createdAt;
    private Instant updatedAt;

}
