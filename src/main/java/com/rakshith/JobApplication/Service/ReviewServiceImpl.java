package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.DTO.ReviewRequest;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Entity.Review;
import com.rakshith.JobApplication.Repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private CompanyService companyService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService = companyService;
    }

    //add Review
    @Override
    public Boolean addCompanyReview(ReviewRequest reviewRequest, Long companyId) {
        CompanyResponse company = companyService.fetchCompanyById(companyId);
        if (company != null) {
            addReviewToTheSpecificCompany(reviewRequest);
            return true;
        }
        return false;
    }

    //Get All Review
    @Override
    public List<Review> getAllCompanyReview(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    //Update Review
    @Override
    public Boolean updateCompanyReview(ReviewRequest updatedReview, Long reviewId, Long companyId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);

        for (Review review1 : reviews) {
            if (review1.getId().equals(reviewId)) {
                //addReviewToTheSpecificCompany(review1);
                reviewRepository.save(review1);
                return true;
            }
        }
        return false;
    }

    //Get Company Specific Review
    @Override
    public Review getCompanySpecificReview(Long reviewId, Long companyId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    //Delete Review
    @Override
    public Boolean deleteReviewById(Long reviewId, Long companyId) {

        if (reviewRepository.findByCompanyId(companyId) != null && reviewRepository.existsById(reviewId)) {
            Review review=reviewRepository.findById(reviewId).orElse(null);
            Company company=review.getCompany();
            //we need to remove the company review as well. since it is a bidirectional mapping.
            company.getReviews().remove(review);
            review.setCompany(null);
            companyService.modifyCompanyById(companyId,company);
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }

    private void addReviewToTheSpecificCompany(ReviewRequest reviewRequest) {
        Review review=new Review();
        review.setDescription(reviewRequest.getDescription());
        review.setRating(reviewRequest.getRating());
        review.setTitle(reviewRequest.getTitle());
        reviewRepository.save(review);
    }
}
