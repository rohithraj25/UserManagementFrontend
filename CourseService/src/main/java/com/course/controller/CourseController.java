package com.course.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.Service.CourseService;
import com.course.Service.TransactionService;
import com.course.intercomm.UserClient;
import com.course.model.Course;
import com.course.model.Transaction;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private UserClient userClient;

	@Autowired
	private CourseService courseService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private Environment env;

	@Value("${spring.application.name}")
	private String serviceId;

	@GetMapping("/service/port")
	public String getPort() {
		return "Service is working at port : " + env.getProperty("local.server.port");
	}

	@GetMapping("/service/instances")
	public ResponseEntity<?> getInstances() {
		return ResponseEntity.ok(discoveryClient.getInstances(serviceId));
	}

	@GetMapping("/service/user/{userId}")
	public ResponseEntity<?> findTransactionsOfUser(@PathVariable Long userId) {
		return ResponseEntity.ok(courseService.findTransactionsOfUser(userId));
	}

	@GetMapping("/service/all")
	public ResponseEntity<?> findAllCourses() {
		return ResponseEntity.ok(courseService.allCourses());
	}

	@PostMapping("/service/enroll")
	public ResponseEntity<?> saveTransaction(@RequestBody Transaction transaction) {
		if (transaction.getCourse() == null || transaction.getCourse().getId() == null) {
			return new ResponseEntity<>("Course ID must not be null", HttpStatus.BAD_REQUEST);
		}

		// Find the course by id
		Course course = courseService.findCourseById(transaction.getCourse().getId());
		if (course == null) {
			return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
		}

		// Set the found course and the current date of issue
		transaction.setCourse(course);
		transaction.setDateOfIssue(LocalDateTime.now());

		// Save the transaction
		Transaction savedTransaction = transactionService.saveTransaction(transaction);
		return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);

	}

	@PostMapping("/save")
	public ResponseEntity<Course> save(@RequestBody Course c) {
		return new ResponseEntity<>(courseService.save(c), HttpStatus.OK);
	}

	@GetMapping("/service/course/{courseId}")
	public ResponseEntity<?> findStudentsOfCourse(@PathVariable Long courseId) {
		List<Transaction> transactions = courseService.findTransactionsOfCourse(courseId);
		if (CollectionUtils.isEmpty(transactions)) {
			return ResponseEntity.notFound().build();
		}
		List<Long> userIdList = transactions.parallelStream().map(t -> t.getUserId()).collect(Collectors.toList());
		List<String> students = userClient.getNamesOfUsers(userIdList);
		return ResponseEntity.ok(students);
	}
}
