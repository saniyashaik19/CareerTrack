package com.careertrack.model;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Entity

public class JobApplication {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message="Company Name is Required")
	
	  private String companyName;
	@NotBlank(message="Job Role is Required")
	
	  private String jobRole;
	@NotBlank(message = "Status is required")
	@Pattern(
	    regexp = "Applied|Interview|Rejected",
	    message = "Status must be Applied, Interview, or Rejected"
	)
	private String status;
	  private LocalDate applicationDate;
	  @ManyToOne
	  @JoinColumn(name = "user_id",nullable=false)
	  private User user;

	
	
	
	
  public User getUser() {
		return user;
	}

	  public void setUser(User user) {
		  this.user = user;
	  }

  public JobApplication() {
	}
  
  public JobApplication(Long id, String companyName, String jobRole, String status, LocalDate applicationDate) {
	super();
	this.id = id;
	this.companyName = companyName;
	this.jobRole = jobRole;
	this.status = status;
	this.applicationDate = applicationDate;
  }
  public Long getId() {
	return id;
  }
  public void setId(Long id) {
	this.id = id;
  }
  public String getCompanyName() {
	return companyName;
  }
  public void setCompanyName(String companyName) {
	this.companyName = companyName;
  }
  public String getJobRole() {
	return jobRole;
  }
  public void setJobRole(String jobRole) {
	this.jobRole = jobRole;
  }
  public String getStatus() {
	return status;
  }
  public void setStatus(String status) {
	this.status = status;
  }
  public LocalDate getApplicationDate() {
	return applicationDate;
  }
  public void setApplicationDate(LocalDate applicationDate) {
	this.applicationDate = applicationDate;
  }
  
}




