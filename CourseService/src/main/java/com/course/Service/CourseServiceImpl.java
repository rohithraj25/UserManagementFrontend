package com.course.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.model.Course;
import com.course.model.Transaction;
import com.course.Repository.CourseRepository;
import com.course.Repository.TransactionRepository;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public List<Course> allCourses() {
		return courseRepository.findAll();
	}

	@Override
	public Course findCourseById(Long courseId) {
		return courseRepository.findById(courseId).orElse(null);
	}

	@Override
	public List<Transaction> findTransactionsOfUser(Long userId) {
		return transactionRepository.findAllByUserId(userId);
	}

	@Override
	public List<Transaction> findTransactionsOfCourse(Long courseId) {
		return transactionRepository.findAllByCourseId(courseId);
	}

	@Override
	public Course save(Course c) {
		return courseRepository.save(c);
	}

	@Override
	public Transaction saveTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}
}
