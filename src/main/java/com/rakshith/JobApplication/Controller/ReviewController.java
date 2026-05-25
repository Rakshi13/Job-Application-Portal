package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.Entity.Review;
import com.rakshith.JobApplication.Service.ReviewService;
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
    @PostMapping
    public ResponseEntity<String> addReview(@RequestBody Review review,@PathVariable Long companyId){
        Boolean reviewFound=reviewService.addCompanyReview(review,companyId);
        if(reviewFound){
            return new ResponseEntity<>("Review Added Successfully.", HttpStatus.OK);
        }

        return new ResponseEntity<>("Review Not saved.",HttpStatus.NOT_FOUND);

    }

    //Get All reviews
    @GetMapping
    public ResponseEntity<List<Review>> getAllReview(@PathVariable Long companyId){
        return new ResponseEntity<>(reviewService.getAllCompanyReview(companyId),HttpStatus.OK);
    }

    //update Review
    @PutMapping("/{id}")
    public ResponseEntity<String> updateReview(@RequestBody Review review,@PathVariable Long id){
        Boolean reviewFound=reviewService.updateCompanyReview(review,id);
        if(reviewFound){
            return new ResponseEntity<>("Review Updated Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("No Review Found.",HttpStatus.NOT_FOUND);
    }


    //Get Review By ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id){
        Review review=reviewService.getCompanySpecificReview(id);
        if(review!=null){
            return new ResponseEntity<>(review,HttpStatus.OK);
        }
        return new ResponseEntity<>(review,HttpStatus.NOT_FOUND);
    }

    //Delete By ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id){
        Boolean reviewFound=reviewService.deleteReviewById(id);
        if(reviewFound){
            return new ResponseEntity<>("Review Deleted Successfully.",HttpStatus.OK);
        }
        return new ResponseEntity<>("No Review Found",HttpStatus.NOT_FOUND);
    }

}
