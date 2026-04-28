package com.ensta.university.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a course in the university system.
 */
public class Course {
    private final String code;
    private String title;
    private final int maxCapacity;
    private final Set<Student> enrolledStudents;
    private final Set<Student> waitingList;

    /**
     * Constructor for Course
     * @param code Unique course code
     * @param title Course title
     * @param maxCapacity Maximum number of students allowed (must be positive)
     */
    Course(String code, String title, int maxCapacity) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be null or empty");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Course capacity must be positive");
        }
        this.code = code.trim();
        this.title = title.trim();
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new HashSet<>();
        this.waitingList = new HashSet<>();
    }

    /**
     * Gets the course code
     */
    public String getCode() {
        return code;
    }



    /**
     * Gets the maximum capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Gets an unmodifiable copy of enrolled students
     */
    public Set<Student> getEnrolledStudents() {
        return new HashSet<>(enrolledStudents);
    }

    /**
     * Gets the number of currently enrolled students
     */
    public int getEnrolledStudentCount() {
        return enrolledStudents.size();
    }

    /**
     * Gets an unmodifiable copy of the waiting list
     */
    public Set<Student> getWaitingList() {
        return new HashSet<>(waitingList);
    }

    /**
     * Gets the waiting list count
     */
    public int getWaitingListCount() {
        return waitingList.size();
    }

    /**
     * Checks if the course has available capacity
     */
    boolean hasCapacity() {
        return enrolledStudents.size() < maxCapacity;
    }

    /**
     * Checks if the course is at full capacity
     */
    public boolean isFull() {
        return enrolledStudents.size() >= maxCapacity;
    }


    }

    /**
     * Add a student to the course
     * @return true if enrolled, false if added to waiting list
     */
    boolean addStudent(Student student) {
        if (hasCapacity()) {
            return enrolledStudents.add(student);
        } else {
            return waitingList.add(student);
        }
    }

    /**
     * Remove a student from the course
     * Promotes from waiting list if available
     */
    void removeStudent(Student student) {
        boolean wasEnrolled = enrolledStudents.remove(student);
        boolean wasOnWaitingList = waitingList.remove(student);

        // Promote from waiting list if a spot opened up
        if (wasEnrolled && !waitingList.isEmpty()) {
            for (Student waitingStudent : waitingList) {
                if (enrolledStudents.add(waitingStudent)) {
                    waitingList.remove(waitingStudent);
                    break;
                }
            }
        }
    }

    /**
     * Clear all students from the course (used during hard delete)
     */
    void clearStudents() {
        enrolledStudents.clear();
        waitingList.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return code.equals(course.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("Course{code='%s', title='%s', enrolled=%d/%d, waiting=%d}",
                code, title, enrolledStudents.size(), maxCapacity, waitingList.size());
    }
}

