package com.wedding.controller;

import com.wedding.model.Review;
import com.wedding.model.User;
import com.wedding.service.PackageService;
import com.wedding.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

// CHANUKA - Feedback & Review Management Controller - IT25102207
//      A customer can only edit/delete their OWN reviews.
//      Admin can edit/delete any review.
@Controller //This annotation marks the class as a Spring MVC controller
@RequestMapping("/reviews") //All URLs inside this controller start with /reviews.
public class ReviewController {

    private final ReviewService reviewService;
    private final PackageService packageService;
    //These service objects are injected through the constructor.

    public ReviewController(ReviewService reviewService, PackageService packageService) {
        this.reviewService  = reviewService;
        this.packageService = packageService;
        //This follows Dependency Injection in Spring Boot.
    }

    // List all reviews
    @GetMapping
    //This handles GET requests for /reviews
    public String list(Model model, HttpSession session) {
        model.addAttribute("reviews", reviewService.getAllReviews());//This sends all reviews to the frontend page.
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "reviews/list";
    }

    // ─── SUBMIT REVIEW FORM ─────────────────────────────────────────────────
    @GetMapping("/submit") //Displays the review submission form
    public String submitPage(HttpSession session, Model model) {
        User current = (User) session.getAttribute("currentUser");//This retrieves the currently logged-in user from the session
        if (current == null) return "redirect:/login";//If the user is not logged in, the system redirects to the login page.
        model.addAttribute("packages", packageService.getAll());
        model.addAttribute("currentUser", current);
        return "reviews/submit";
    }

    @PostMapping("/submit") //This handles form submission for reviews
    public String submitReview(@RequestParam String packageId,
                               @RequestParam String packageName,
                               @RequestParam int rating,
                               @RequestParam String title,
                               @RequestParam String comment,
                               @RequestParam(defaultValue = "") String bookingId,
                               HttpSession session,
                               RedirectAttributes ra) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) return "redirect:/login";

        // OOP: POLYMORPHISM — creates VerifiedReview if bookingId provided, else PublicReview
        //If a booking ID is provided, the system creates a VerifiedReview.
        if (!bookingId.isBlank()) {
            reviewService.submitVerifiedReview(current.getId(), current.getName(),
                    packageId, packageName, rating, title, comment, bookingId);
            //Creates a verified review.
        } else {
            reviewService.submitPublicReview(current.getId(), current.getName(),
                    packageId, packageName, rating, title, comment);
            //Otherwise, it creates a public review.
        }
        ra.addFlashAttribute("success", "Thank you for your feedback!");
        //This sends a temporary success message after submission.
        return "redirect:/reviews";
    }

    // Edit Review
    @GetMapping("/edit/{id}")//this opens the edit page for a specific review.
    public String editPage(@PathVariable String id, HttpSession session, Model model) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) return "redirect:/login";

        Review review = reviewService.findById(id);
        if (review == null) return "redirect:/reviews";

        //customers can only edit their own reviews
        if (!current.getRole().equals("admin") && !review.getUserId().equals(current.getId())) {
            return "redirect:/reviews";
        }

        model.addAttribute("review", review);
        model.addAttribute("currentUser", current);
        return "reviews/edit";
    }

    @PostMapping("/update/{id}")//This method updates the review details.
    public String updateReview(@PathVariable String id,
                               @RequestParam String title,
                               @RequestParam String comment,
                               @RequestParam int rating,
                               HttpSession session,
                               RedirectAttributes ra) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) return "redirect:/login";

        Review review = reviewService.findById(id);
        if (review == null) return "redirect:/reviews";


        if (!current.getRole().equals("admin") && !review.getUserId().equals(current.getId())) {
            ra.addFlashAttribute("error", "You can only edit your own reviews.");
            return "redirect:/reviews";
        }

        boolean ok = reviewService.updateReview(id, title, comment, rating);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "Review updated." : "Update failed.");
        return "redirect:/reviews";
    }

    //Delete Review
    @PostMapping("/delete/{id}") //This method deletes a review.
    public String deleteReview(@PathVariable String id,
                               HttpSession session,
                               RedirectAttributes ra) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) return "redirect:/login";

        Review review = reviewService.findById(id);
        if (review == null) return "redirect:/reviews";

        // customers can only delete their own reviews
        if (!current.getRole().equals("admin") && !review.getUserId().equals(current.getId())) {
            ra.addFlashAttribute("error", "You can only delete your own reviews.");
            //This prevents users from deleting reviews belonging to others.
            return "redirect:/reviews";
        }

        boolean ok = reviewService.deleteReview(id);
        ra.addFlashAttribute(ok ? "success" : "error", ok ? "Review deleted." : "Delete failed.");
        return "redirect:/reviews";
    }
}
