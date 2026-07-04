package fyi.shamim.aiagents.controller;

import fyi.shamim.aiagents.dto.FollowUpRequestDto;
import fyi.shamim.aiagents.dto.ReviewDto;
import fyi.shamim.aiagents.enums.ReviewStatus;
import fyi.shamim.aiagents.service.SecurityReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Md Shamim
 * Date: 7/1/26
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@RestController
@RequestMapping("/api/security-review")
@RequiredArgsConstructor
public class SecurityReviewController {

    private final SecurityReviewService securityReviewService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> reviewDiagram(@RequestPart("diagram") MultipartFile diagram) {

        log.info("Received diagram for security review: {}", diagram.getOriginalFilename());
        String reviewId = securityReviewService.enqueueAndExecute(diagram);

        return Map.of("reviewId", reviewId);
    }

    @GetMapping("/{id}")
    public ReviewDto getReviewById(@PathVariable String id) {

        log.info("Fetching security review status for id: {}", id);

        return securityReviewService.getSecurityReview(id);
    }

    @PostMapping(path = "/{id}/ask", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_MARKDOWN_VALUE)
    public String followUp(@PathVariable String id, @RequestBody FollowUpRequestDto followUpRequestDto) {

        log.info("Received follow-up question for review id {}: {}", id, followUpRequestDto.question());
        var state = securityReviewService.getSecurityReview(id);

        if (state.getStatus() == ReviewStatus.QUEUED || state.getStatus() == ReviewStatus.RUNNING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Review still running. Try again when it is DONE!");
        }

        if (followUpRequestDto.question() == null || followUpRequestDto.question().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question is required!");
        }

        return securityReviewService.followUpWithVision(id, followUpRequestDto.question());
    }

}
