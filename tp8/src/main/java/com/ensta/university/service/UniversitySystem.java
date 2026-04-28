package com.ensta.university.service;

import com.ensta.university.comparator.CourseCapacityComparator;
import com.ensta.university.comparator.StudentNameComparator;
import com.ensta.university.exception.*;
import com.ensta.university.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Main service class for the University Course Management System.
 * Implements the Facade pattern to provide a simplified interface to the complex system.
 * Manages all entities and their relationships.
 */
public class UniversitySystem {
    private final Map<String, Student> students;
    private final Map<String, Course> courses;
    private final Map<String, Enrollment> enrollments;

    /**
     * Constructor initializes the system with empty collections
     */
    public UniversitySystem() {
        this.students = new HashMap<>();
        this.courses = new HashMap<>();
        this.enrollments = new HashMap<>();
    }

    // ========== Management Operations ==========

    /**
     * Adds a new student to the system
     * @param id Student ID
     * @param name Student name
     * @return The created Student object
     * @throws DuplicateEntityException if student ID already exists
     */
    public Student addStudent(String id, String name) throws DuplicateEntityException {
        if (students.containsKey(id)) {
            throw new DuplicateEntityException("Student with ID '" + id + "' already exists");
        }
        Student student = new Student(id, name);
        students.put(id, student);
        return student;
    }

    /**
     * Adds a new course to the system
     * @param code Course code
     * @param title Course title
     * @param maxCapacity Maximum enrollment capacity
     * @return The created Course object
     * @throws DuplicateEntityException if course code already exists
     */
    public Course addCourse(String code, String title, int maxCapacity) throws DuplicateEntityException {
        if (courses.containsKey(code)) {
            throw new DuplicateEntityException("Course with code '" + code + "' already exists");
        }
        Course course = new Course(code, title, maxCapacity);
        courses.put(code, course);
        return course;
    }

    /**
     * Enrolls a student in a course
     * @param studentId Student ID
     * @param courseCode Course code
     * @return true if enrolled directly, false if added to waiting list
     * @throws EntityNotFoundException if student or course not found
     * @throws EnrollmentConflictException if already enrolled
     * @throws CourseFullException if course is full (no waiting list slots)
     */
    public boolean enrollStudent(String studentId, String courseCode)
            throws EntityNotFoundException, EnrollmentConflictException, CourseFullException {
        Student student = findStudentById(studentId);
        Course course = findCourseByCode(courseCode);

        if (student.isDeleted()) {
            throw new EnrollmentConflictException(
                    "Cannot enroll deleted student '" + studentId + "'");
        }

        String enrollmentKey = studentId + "-" + courseCode;
        if (enrollments.containsKey(enrollmentKey)) {
            throw new EnrollmentConflictException(
                    "Student '" + studentId + "' is already enrolled in course '" + courseCode + "'");
        }

        // Check capacity - if full and no waiting list support, throw exception
        if (course.isFull()) {
            // With waiting list feature: add to waiting list instead of throwing
            // For now, we'll add to waiting list (as per optional twist)
            boolean addedToWaiting = course.addStudent(student);
            if (addedToWaiting) {
                Enrollment enrollment = new Enrollment(student, course);
                enrollments.put(enrollmentKey, enrollment);
                student.addCourse(course);
                return false; // Added to waiting list
            }
            throw new CourseFullException(
                    "Course '" + courseCode + "' is full and waiting list is also full");
        }

        // Normal enrollment
        boolean enrolled = course.addStudent(student);
        if (enrolled) {
            Enrollment enrollment = new Enrollment(student, course);
            enrollments.put(enrollmentKey, enrollment);
            student.addCourse(course);
        }
        return true; // Enrolled directly
    }

    /**
     * Removes a student from a course (soft remove - keeps enrollment record)
     * @param studentId Student ID
     * @param courseCode Course code
     * @throws EntityNotFoundException if student or course not found
     */
    public void removeStudentFromCourse(String studentId, String courseCode)
            throws EntityNotFoundException {
        Student student = findStudentById(studentId);
        Course course = findCourseByCode(courseCode);

        course.removeStudent(student);
        student.removeCourse(course);
        String enrollmentKey = studentId + "-" + courseCode;
        enrollments.remove(enrollmentKey);
    }

    /**
     * Soft delete a student from the system
     * Student is marked as deleted but enrollment records are kept
     * Deleted students are filtered from active listings but can be restored
     * @param studentId Student ID to delete
     * @throws EntityNotFoundException if student not found
     */
    public void softDeleteStudent(String studentId) throws EntityNotFoundException {
        Student student = findStudentById(studentId);
        student.softDelete();
    }

    /**
     * Restore a soft-deleted student
     * @param studentId Student ID to restore
     * @throws EntityNotFoundException if student not found
     */
    public void restoreStudent(String studentId) throws EntityNotFoundException {
        Student student = findStudentById(studentId);
        student.restore();
    }

    /**
     * Hard delete a student from the system
     * Removes student and all related enrollment records
     * @param studentId Student ID to delete
     * @throws EntityNotFoundException if student not found
     */
    public void hardDeleteStudent(String studentId) throws EntityNotFoundException {
        Student student = findStudentById(studentId);

        // First, remove student from all courses
        Set<Course> studentCourses = student.getEnrolledCourses();
        for (Course course : studentCourses) {
            course.removeStudent(student);
            String enrollmentKey = studentId + "-" + course.getCode();
            enrollments.remove(enrollmentKey);
        }

        // Clear student's courses
        student.clearCourses();

        // Remove student from system
        students.remove(studentId);
    }

    /**
     * Delete a course from the system
     * Removes course and all related enrollment records
     * @param courseCode Course code to delete
     * @throws EntityNotFoundException if course not found
     */
    public void deleteCourse(String courseCode) throws EntityNotFoundException {
        Course course = findCourseByCode(courseCode);

        // Remove all students from this course
        Set<Student> courseStudents = course.getEnrolledStudents();
        for (Student student : courseStudents) {
            student.removeCourse(course);
            String enrollmentKey = student.getId() + "-" + courseCode;
            enrollments.remove(enrollmentKey);
        }

        // Clear course students
        course.clearStudents();

        // Remove course from system
        courses.remove(courseCode);
    }

    // ========== Search Operations ==========

    /**
     * Find a student by ID
     * @param studentId Student ID
     * @return The Student object
     * @throws EntityNotFoundException if student not found
     */
    public Student findStudentById(String studentId) throws EntityNotFoundException {
        Student student = students.get(studentId);
        if (student == null) {
            throw new EntityNotFoundException("Student with ID '" + studentId + "' not found");
        }
        return student;
    }

    /**
     * Find a course by code
     * @param courseCode Course code
     * @return The Course object
     * @throws EntityNotFoundException if course not found
     */
    public Course findCourseByCode(String courseCode) throws EntityNotFoundException {
        Course course = courses.get(courseCode);
        if (course == null) {
            throw new EntityNotFoundException("Course with code '" + courseCode + "' not found");
        }
        return course;
    }

    /**
     * List all students in a course (excludes soft-deleted students)
     * @param courseCode Course code
     * @return List of active students in the course
     * @throws EntityNotFoundException if course not found
     */
    public List<Student> listStudentsInCourse(String courseCode) throws EntityNotFoundException {
        Course course = findCourseByCode(courseCode);
        return course.getEnrolledStudents().stream()
                .filter(s -> !s.isDeleted())
                .sorted(new StudentNameComparator())
                .collect(Collectors.toList());
    }

    /**
     * List all courses of a student
     * @param studentId Student ID
     * @return List of courses the student is enrolled in
     * @throws EntityNotFoundException if student not found
     */
    public List<Course> listCoursesOfStudent(String studentId) throws EntityNotFoundException {
        Student student = findStudentById(studentId);
        return new ArrayList<>(student.getEnrolledCourses());
    }

    // ========== Advanced Operations ==========

    /**
     * Display all students sorted by name
     * @return List of students sorted alphabetically by name
     */
    public List<Student> getStudentsSortedByName() {
        return students.values().stream()
                .sorted(new StudentNameComparator())
                .collect(Collectors.toList());
    }

    /**
     * Display all courses sorted by capacity
     * @return List of courses sorted by capacity
     */
    public List<Course> getCoursesSortedByCapacity() {
        return courses.values().stream()
                .sorted(new CourseCapacityComparator())
                .collect(Collectors.toList());
    }

    /**
     * Group students by the number of enrolled courses
     * @return Map where key is number of courses and value is list of students
     */
    public Map<Integer, List<Student>> groupStudentsByCourseCount() {
        return students.values().stream()
                .filter(s -> !s.isDeleted())
                .collect(Collectors.groupingBy(
                        Student::getEnrolledCourseCount,
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    /**
     * Find the course with the highest number of students
     * @return The course with most enrollments, or Optional.empty() if no courses
     */
    public Optional<Course> findCourseWithMostStudents() {
        return courses.values().stream()
                .max(Comparator.comparingInt(Course::getEnrolledStudentCount));
    }

    // ========== Statistics ==========

    /**
     * Get system statistics
     * @return Statistics object with system-wide data
     */
    public Statistics getStatistics() {
        long totalStudents = students.values().stream()
                .filter(s -> !s.isDeleted())
                .count();
        long totalCourses = courses.size();
        double avgEnrollment = courses.isEmpty() ? 0.0 :
                courses.values().stream()
                        .mapToInt(Course::getEnrolledStudentCount)
                        .average()
                        .orElse(0.0);

        return new Statistics(totalStudents, totalCourses, avgEnrollment);
    }

    // ========== Utility Methods ==========

    /**
     * List all active (non-deleted) students
     * @return List of active students
     */
    public List<Student> listActiveStudents() {
        return students.values().stream()
                .filter(s -> !s.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * List all soft-deleted students
     * @return List of deleted students
     */
    public List<Student> listDeletedStudents() {
        return students.values().stream()
                .filter(Student::isDeleted)
                .collect(Collectors.toList());
    }

    /**
     * List all courses
     * @return List of all courses
     */
    public List<Course> listAllCourses() {
        return new ArrayList<>(courses.values());
    }

    /**
     * Check if a student is soft-deleted
     * @param studentId Student ID
     * @return true if student exists and is deleted
     */
    public boolean isStudentDeleted(String studentId) {
        Student student = students.get(studentId);
        return student != null && student.isDeleted();
    }

    /**
     * Get the total count of students (including deleted)
     */
    public int getTotalStudentCount() {
        return students.size();
    }

    /**
     * Get the total count of courses
     */
    public int getTotalCourseCount() {
        return courses.size();
    }

    /**
     * Check if an enrollment exists
     */
    public boolean hasEnrollment(String studentId, String courseCode) {
        return enrollments.containsKey(studentId + "-" + courseCode);
    }

    // ========== Inner Class: Statistics ==========

    /**
     * Immutable statistics record for the university system
     * Uses the DTO pattern to transfer statistical data
     */
    public static class Statistics {
        private final long totalActiveStudents;
        private final long totalCourses;
        private final double averageEnrollmentPerCourse;

        public Statistics(long totalActiveStudents, long totalCourses, double averageEnrollmentPerCourse) {
            this.totalActiveStudents = totalActiveStudents;
            this.totalCourses = totalCourses;
            this.averageEnrollmentPerCourse = averageEnrollmentPerCourse;
        }

        public long getTotalActiveStudents() {
            return totalActiveStudents;
        }

        public long getTotalCourses() {
            return totalCourses;
        }

        public double getAverageEnrollmentPerCourse() {
            return averageEnrollmentPerCourse;
        }

        @Override
        public String toString() {
            return String.format(
                "\n========== University Statistics ==========\n" +
                "Total Active Students:      %d\n" +
                "Total Courses:              %d\n" +
                "Average Enrollment/Course:  %.2f\n" +
                "===========================================\n",
                totalActiveStudents, totalCourses, averageEnrollmentPerCourse
            );
        }
    }
}
