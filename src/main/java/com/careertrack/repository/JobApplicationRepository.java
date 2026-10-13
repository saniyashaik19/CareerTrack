
package com.careertrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.careertrack.model.JobApplication;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {
	List<JobApplication> findByStatus(String status);
	List<JobApplication> findByCompanyNameContainingIgnoreCase(String companyName);
	long countByStatus(String status);
	List<JobApplication> findByUserId(Long userId);
}

