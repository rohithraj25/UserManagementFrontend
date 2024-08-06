package com.course.Service;

import java.util.List;

import com.course.model.Course;
import com.course.model.Transaction;

	public interface CourseService {
	    List<Course> allCourses();

	    Course findCourseById(Long courseId);

	    List<Transaction> findTransactionsOfUser(Long userId);

	    List<Transaction> findTransactionsOfCourse(Long courseId);

	    Transaction saveTransaction(Transaction transaction);
	    Course save(Course c);

}
