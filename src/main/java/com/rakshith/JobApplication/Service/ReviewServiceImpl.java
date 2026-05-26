package com.rakshith.JobApplication.Service;

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

    public ReviewServiceImpl(ReviewRepository reviewRepository,CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService=companyService;
    }

    //add Review
    @Override
    public Boolean addCompanyReview(Review review,Long companyId) {
        Company company=companyService.fetchCompanyById(companyId);
        if(company!=null){
            review.setCompany(company);
            reviewRepository.save(review);
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
    public Boolean updateCompanyReview(Review review, Long id) {
        Optional<Review>reviewData=reviewRepository.findById(id);

        if(reviewData.isPresent()){
            Review review1=reviewData.get();
            review1.setTitle(review.getTitle());
            review1.setDescription(review.getDescription());
            review1.setRating(review.getRating());
            reviewRepository.save(review1);
            return true;
        }

        return false;
    }

    //Get Company Specific Review
    @Override
    public Review getCompanySpecificReview(Long reviewId,Long companyId) {
        List<Review>reviews=reviewRepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    //Delete Review
    @Override
    public Boolean deleteReviewById(Long id) {
        if(reviewRepository.existsById(id)){
            reviewRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}
