package fyi.shamim.aiagents.mapper;

import fyi.shamim.aiagents.dto.ReviewDto;
import fyi.shamim.aiagents.dto.ReviewStateDto;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/1/26
 * Email: mdshamim723@gmail.com
 */

@Component
public class ReviewStateMapper {

    public ReviewDto toDto(ReviewStateDto stateDto) {

        return ReviewDto.builder()
                .id(stateDto.getId())
                .status(stateDto.getStatus())
                .reportMarkdown(stateDto.getReportMarkdown())
                .errorMessages(stateDto.getErrorMessage())
                .createdAt(stateDto.getCreatedAt())
                .updatedAt(stateDto.getUpdatedAt())
                .build();
    }

}
