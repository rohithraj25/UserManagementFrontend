package com.course.model;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;



@Entity
@Data
@jakarta.persistence.Table(name = "transaction")
public class Transaction {
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "course_id", referencedColumnName = "id")
	    private Course course;

	    @Column(name = "user_id")
	    private Long userId;

	    @Column(name = "date_of_issue")
	    private LocalDateTime dateOfIssue;

}
