package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.DTO.ReviewRequest;
import com.rakshith.JobApplication.DTO.ReviewResponse;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Entity.Review;
import com.rakshith.JobApplication.Repository.CompanyRepository;
import com.rakshith.JobApplication.Repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private CompanyRepository companyRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyRepository companyRepository) {
        this.reviewRepository = reviewRepository;
        this.companyRepository = companyRepository;
    }

    //add Review
    @Override
    public Boolean addCompanyReview(ReviewRequest reviewRequest, Long companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company != null) {
            addReviewToTheSpecificCompany(reviewRequest,company);
            return true;
        }
        return false;
    }

    //Get All Review
    @Override
    public List<ReviewResponse> getAllCompanyReview(Long companyId) {
        return reviewRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    //Update Reviews
    @Override
    public Boolean updateCompanyReview(ReviewRequest updatedReview, Long reviewId, Long companyId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);

        for (Review review1 : reviews) {
            if (review1.getId().equals(reviewId)) {
                review1.setTitle(updatedReview.getTitle());
                review1.setDescription(updatedReview.getDescription());
                review1.setRating(updatedReview.getRating());

                reviewRepository.save(review1);
                return true;
            }
        }
        return false;
    }

    //Get Company Specific Review
    @Override
    public ReviewResponse getCompanySpecificReview(Long reviewId, Long companyId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        for(Review review:reviews){
            if(review.getId().equals(reviewId)){
                return mapToReviewResponse(review);
            }
        }
       return null;
    }

    //Delete Review
    @Override
    public Boolean deleteReviewById(Long reviewId, Long companyId) {

        List<Review> reviews =
                reviewRepository.findByCompanyId(companyId);

        for (Review review : reviews) {
            if (review.getId().equals(reviewId)) {
                Company company = review.getCompany();
                company.getReviews().remove(review);
                review.setCompany(null);
                reviewRepository.delete(review);
                return true;
            }
        }
        return false;
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        ReviewResponse reviewResponse=new ReviewResponse();
        reviewResponse.setId(review.getId());
        reviewResponse.setRating(review.getRating());
        reviewResponse.setTitle(review.getTitle());
        reviewResponse.setDescription(review.getDescription());

        if(review.getCompany()!=null){
            reviewResponse.setCompanyId(review.getCompany().getId());
            reviewResponse.setCompanyName(review.getCompany().getName());
        }
        return reviewResponse;
    }

    private void addReviewToTheSpecificCompany(ReviewRequest reviewRequest,Company company) {
        Review review=new Review();
        review.setDescription(reviewRequest.getDescription());
        review.setRating(reviewRequest.getRating());
        review.setTitle(reviewRequest.getTitle());
        review.setCompany(company);
        reviewRepository.save(review);
    }
}
