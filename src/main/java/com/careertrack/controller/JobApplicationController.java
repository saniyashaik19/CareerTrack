
package com.careertrack.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careertrack.model.JobApplication;
import com.careertrack.model.User;
import com.careertrack.repository.JobApplicationRepository;
import com.careertrack.repository.UserRepository;

@RestController
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationRepository repository;
    private final UserRepository userRepository;

    public JobApplicationController(
            JobApplicationRepository repository,
            UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> addApplication(
            @Valid @RequestBody JobApplication application,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        Optional<User> userResult = userRepository.findById(userId);

        if (!userResult.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Please log in again");
        }

        application.setUser(userResult.get());

        if (application.getApplicationDate() == null) {
            application.setApplicationDate(LocalDate.now());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(repository.save(application));
    }

    // READ: Only the logged-in user's applications
    @GetMapping
    public List<JobApplication> getAllApplications(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return repository.findByUserId(userId);
    }

    // FILTER BY STATUS
    @GetMapping("/status/{status}")
    public List<JobApplication> getApplicationsByStatus(
            @PathVariable String status, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        return repository.findByUserId(userId).stream()
                .filter(a -> status.equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());
    }

    // SEARCH BY COMPANY
    @GetMapping("/search")
    public List<JobApplication> searchByCompanyName(
            @RequestParam String companyName, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        return repository.findByUserId(userId).stream()
                .filter(a -> a.getCompanyName() != null
                        && a.getCompanyName().toLowerCase()
                        .contains(companyName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // DASHBOARD STATISTICS
    @GetMapping("/stats")
    public Map<String, Long> getApplicationStats(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        List<JobApplication> applications = repository.findByUserId(userId);

        Map<String, Long> stats = new HashMap<>();

        stats.put("total", (long) applications.size());
        stats.put("applied", applications.stream()
                .filter(a -> "Applied".equalsIgnoreCase(a.getStatus()))
                .count());
        stats.put("interview", applications.stream()
                .filter(a -> "Interview".equalsIgnoreCase(a.getStatus()))
                .count());
        stats.put("rejected", applications.stream()
                .filter(a -> "Rejected".equalsIgnoreCase(a.getStatus()))
                .count());

        return stats;
    }

    // UPDATE: Only the owner's application
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplication updatedApplication,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        Optional<JobApplication> result = repository.findById(id);

        if (!result.isPresent()
                || result.get().getUser() == null
                || !userId.equals(result.get().getUser().getId())) {
            return ResponseEntity.notFound().build();
        }

        JobApplication application = result.get();

        application.setCompanyName(updatedApplication.getCompanyName());
        application.setJobRole(updatedApplication.getJobRole());
        application.setStatus(updatedApplication.getStatus());

        if (updatedApplication.getApplicationDate() != null) {
            application.setApplicationDate(
                    updatedApplication.getApplicationDate());
        }

        return ResponseEntity.ok(repository.save(application));
    }

    // DELETE: Only the owner's application
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(
            @PathVariable Long id, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        Optional<JobApplication> result = repository.findById(id);

        if (!result.isPresent()
                || result.get().getUser() == null
                || !userId.equals(result.get().getUser().getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Application not found");
        }

        repository.delete(result.get());

        return ResponseEntity.ok("Application deleted successfully");
    }
}
