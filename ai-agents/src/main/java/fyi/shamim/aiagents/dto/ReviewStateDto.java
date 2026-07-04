package fyi.shamim.aiagents.dto;

import fyi.shamim.aiagents.enums.ReviewStatus;
import lombok.*;

import java.time.Instant;
import java.util.Objects;

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
public class ReviewStateDto {

    private String id;

    @Builder.Default
    private ReviewStatus status = ReviewStatus.QUEUED;

    private String reportMarkdown;

    private String fileName;

    private String xml;

    private String errorMessage;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();

    public void updateStatus(ReviewStatus status) {
        this.status = Objects.requireNonNull(status);
        this.updatedAt = Instant.now();
    }

    public void updateReportMarkdown(String reportMarkdown) {
        this.reportMarkdown = reportMarkdown;
        this.updatedAt = Instant.now();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.updatedAt = Instant.now();
    }

}
