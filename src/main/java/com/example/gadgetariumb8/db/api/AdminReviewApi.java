package com.example.gadgetariumb8.db.api;

import com.example.gadgetariumb8.db.dto.response.ReviewResponse;
import com.example.gadgetariumb8.db.dto.response.SimpleResponse;
import com.example.gadgetariumb8.db.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Admin Reviews API")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminReviewApi {
    private final ReviewService reviewService;
    @GetMapping
    @Operation(summary = "All Review", description = "This method is needed to display all review")
    public List<ReviewResponse> getAll(@RequestParam String param) {
        return reviewService.getAllReview(param);
    }
    @PostMapping
    @Operation(summary = "Reply to comment", description = "This method is needed to reply to a review")
    public SimpleResponse replyToFeedback(@RequestParam Long id,@RequestBody String answer) {
        return reviewService.replyToFeedback(answer, id);
    }
    @DeleteMapping
    @Operation(summary = "Delete Review", description = "This method is needed to remove a review")
    public SimpleResponse deleteById(@RequestParam Long id) {
        return reviewService.deleteById(id);
    }

}