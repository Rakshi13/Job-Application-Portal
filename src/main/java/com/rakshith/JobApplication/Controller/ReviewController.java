package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.ReviewRequest;
import com.rakshith.JobApplication.DTO.ReviewResponse;
import com.rakshith.JobApplication.Entity.Review;
import com.rakshith.JobApplication.Service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies/{companyId}/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //add a review
    @Operation(
            summary = "Add Review to the specific Company",
            description = "Creates a review to the specific Company."
    )
    @PostMapping
    public ResponseEntity<String> addReview(@Valid @RequestBody ReviewRequest reviewRequest, @PathVariable Long companyId){
        Boolean reviewFound=reviewService.addCompanyReview(reviewRequest,companyId);
        if(reviewFound){
            return new ResponseEntity<>("Review Added Successfully.", HttpStatus.OK);
        }

        return new ResponseEntity<>("Review Not saved.",HttpStatus.NOT_FOUND);

    }

    //Get All reviews
    @Operation(
            summary = "Get all the reviews to the speoific company",
            description = "Return all the reviews to the specific company."
    )
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReview(@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.getAllCompanyReview(companyId),HttpStatus.OK);
    }

    //update Review
    @Operation(
            summary = "Update Company specific review",
            description = "Returns the updated review to the specific company."
    )
    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@Valid @RequestBody ReviewRequest reviewRequest,@PathVariable Long reviewId, @PathVariable Long companyId){
        Boolean reviewFound=reviewService.updateCompanyReview(reviewRequest,reviewId,companyId);
        if(reviewFound){
            return new ResponseEntity<>("Review Updated Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("No Review Found.",HttpStatus.NOT_FOUND);
    }


    //Get Review By ID
    @Operation(
            summary = "Get Review to the specific company based on the Review Id",
            description = "Returns the Review to the specific company based on the Review Id"
    )
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long reviewId,@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.getCompanySpecificReview(reviewId,companyId),HttpStatus.OK);
    }

    //Delete By ID
    @Operation(
            summary = "Delete Review to the specific company.",
            description = "Deleted the Review to the specific company."
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId,@PathVariable Long companyId){
        Boolean reviewFound=reviewService.deleteReviewById(reviewId,companyId);
        if(reviewFound){
            return new ResponseEntity<>("Review Deleted Successfully.",HttpStatus.OK);
        }
        return new ResponseEntity<>("No Review Found",HttpStatus.NOT_FOUND);
    }

}
