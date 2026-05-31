package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.ReviewRequest;
import com.rakshith.JobApplication.DTO.ReviewResponse;
import com.rakshith.JobApplication.Entity.Review;

import java.util.List;

public interface ReviewService {

    //ADD REVIEW
    Boolean addCompanyReview(ReviewRequest reviewRequest, Long companyId);

    //GET ALL REVIEWS
    List<ReviewResponse> getAllCompanyReview(Long companyId);

    //UPDATE REVIEW
    Boolean updateCompanyReview(ReviewRequest reviewRequest,Long reviewId,Long companyId);

    //GET COMPANY SPECIFIC REVIEW
    ReviewResponse getCompanySpecificReview(Long reviewId,Long companyId);

    //DELETE REVIEW BY ID
    Boolean deleteReviewById(Long reviewId,Long companyId);
}
