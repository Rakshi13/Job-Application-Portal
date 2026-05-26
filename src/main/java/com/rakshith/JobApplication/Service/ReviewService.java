package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.Entity.Review;

import java.util.List;

public interface ReviewService {

    //ADD REVIEW
    Boolean addCompanyReview(Review review,Long companyId);

    //GET ALL REVIEWS
    List<Review> getAllCompanyReview(Long companyId);

    //UPDATE REVIEW
    Boolean updateCompanyReview(Review review,Long id);

    //GET COMPANY SPECIFIC REVIEW
    Review getCompanySpecificReview(Long reviewId,Long companyId);

    //DELETE REVIEW BY ID
    Boolean deleteReviewById(Long id);
}
