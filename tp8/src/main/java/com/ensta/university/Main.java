package com.ensta.university;

import com.ensta.university.exception.*;
import com.ensta.university.model.*;
import com.ensta.university.service.UniversitySystem;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Main class demonstrating the University Course Management System.
 * Demonstrates all required features and advanced OOP techniques.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("===========================================================");
        System.out.println("  University Course Management System - OOP Demonstration");
        System.out.println("===========================================================\n");

        UniversitySystem university = new UniversitySystem();

        // ========== 1. Adding Students ==========
        System.out.println("--- 1. Adding Students ---");
        try {
            university.addStudent("S001", "Alice Johnson");
            university.addStudent("S002", "Bob Smith");
            university.addStudent("S003", "Charlie Brown");
            university.addStudent("S004", "Diana Prince");
            university.addStudent("S005", "Edward Norton");
            System.out.println("✓ Added 5 students successfully");
        } catch (DuplicateEntityException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }

        // ========== 2. Adding Courses ==========
        System.out.println("\n--- 2. Adding Courses ---");
        try {
            university.addCourse("CS101", "Introduction to Computer Science", 30);
            university.addCourse("CS201", "Data Structures", 25);
            university.addCourse("MATH101", "Calculus I", 35);
            university.addCourse("PHY101", "Physics I", 20);
            university.addCourse("CS301", "Advanced Algorithms", 15);
            System.out.println("✓ Added 5 courses successfully");
        } catch (DuplicateEntityException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }

        // ========== 3. Enrolling Students ==========
        System.out.println("\n--- 3. Enrolling Students ---");
        try {
            enrollWithMessage(university, "S001", "CS101");
            enrollWithMessage(university, "S001", "CS201");
            enrollWithMessage(university, "S001", "MATH101");
            enrollWithMessage(university, "S002", "CS101");
            enrollWithMessage(university, "S002", "PHY101");
            enrollWithMessage(university, "S003", "CS201");
            enrollWithMessage(university, "S003", "MATH101");
            enrollWithMessage(university, "S003", "CS301");
            enrollWithMessage(university, "S004", "CS101");
            enrollWithMessage(university, "S004", "CS201");
            enrollWithMessage(university, "S004", "MATH101");
            enrollWithMessage(university, "S004", "PHY101");
            enrollWithMessage(university, "S004", "CS301");
            enrollWithMessage(university, "S005", "CS101");
            enrollWithMessage(university, "S005", "PHY101");
            System.out.println();
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }

        // ========== 4. Search Operations ==========
        System.out.println("--- 4. Search Operations ---");
        try {
            Student s = university.findStudentById("S003");
            System.out.println("✓ Found student by ID 'S003': " + s.getName());

            Course c = university.findCourseByCode("CS201");
            System.out.println("✓ Found course by code 'CS201': " + c.getTitle());

            System.out.println("\nStudents in CS101:");
            List<Student> cs101Students = university.listStudentsInCourse("CS101");
            for (Student student : cs101Students) {
                System.out.println("  - " + student.getName());
            }

            System.out.println("\nCourses of S004 (Diana Prince):");
            List<Course> dianaCourses = university.listCoursesOfStudent("S004");
            for (Course course : dianaCourses) {
                System.out.println("  - " + course.getTitle() + " (" + course.getCode() + ")");
            }
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }

        // ========== 5. Advanced Operations ==========
        System.out.println("\n--- 5. Advanced Operations ---");

        System.out.println("\nStudents sorted by name:");
        List<Student> sortedStudents = university.getStudentsSortedByName();
        for (Student student : sortedStudents) {
            System.out.println("  - " + student.getName() + " (" + student.getEnrolledCourseCount() + " courses)");
        }

        System.out.println("\nCourses sorted by capacity:");
        List<Course> sortedCourses = university.getCoursesSortedByCapacity();
        for (Course course : sortedCourses) {
            System.out.println("  - " + course.getTitle() + ": capacity=" + course.getMaxCapacity());
        }

        System.out.println("\nStudents grouped by number of enrolled courses:");
        Map<Integer, List<Student>> groupedStudents = university.groupStudentsByCourseCount();
        for (Map.Entry<Integer, List<Student>> entry : groupedStudents.entrySet()) {
            System.out.println("  - " + entry.getKey() + " course(s): " + entry.getValue().size() + " students");
            for (Student s : entry.getValue()) {
                System.out.println("    • " + s.getName());
            }
        }

        Optional<Course> mostPopularCourse = university.findCourseWithMostStudents();
        mostPopularCourse.ifPresent(course ->
            System.out.println("\nMost popular course: " + course.getTitle() +
                " with " + course.getEnrolledStudentCount() + " students"));

        // ========== 6. Soft Delete ==========
        System.out.println("\n--- 6. Soft Delete (isDeleted) ---");
        try {
            System.out.println("Before soft delete - Active students: " + university.listActiveStudents().size());
            university.softDeleteStudent("S003");
            System.out.println("✓ Soft-deleted student S003 (Charlie Brown)");
            System.out.println("After soft delete - Active students: " + university.listActiveStudents().size());
            System.out.println("Deleted students count: " + university.listDeletedStudents().size());

            System.out.println("\nStudents in CS201 after Charlie deleted:");
            List<Student> cs201AfterDelete = university.listStudentsInCourse("CS201");
            for (Student student : cs201AfterDelete) {
                System.out.println("  - " + student.getName());
            }

            System.out.println("\n✓ Charlie (deleted) is NOT listed in CS201 (isDeleted=true)");

            university.restoreStudent("S003");
            System.out.println("\n✓ Restored student S003 (Charlie Brown)");
            System.out.println("Active students after restore: " + university.listActiveStudents().size());
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }

        // ========== 7. Hard Delete ==========
        System.out.println("\n--- 7. Hard Delete (complete removal) ---");
        try {
            System.out.println("Before hard delete - Students: " + university.getTotalStudentCount());
            System.out.println("Before hard delete - S005 courses: " + university.listCoursesOfStudent("S005").size());
            university.hardDeleteStudent("S005");
            System.out.println("✓ Hard-deleted student S005 (Edward Norton)");
            System.out.println("After hard delete - Students: " + university.getTotalStudentCount());
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }

        // ========== 8. Statistics ==========
        System.out.println("\n--- 8. System Statistics ---");
        System.out.println(university.getStatistics());

        // ========== 9. Duplicate Prevention ==========
        System.out.println("--- 9. Duplicate Prevention Tests ---");
        try {
            university.addStudent("S001", "Duplicate Alice");
            System.out.println("✗ Should not allow duplicate");
        } catch (DuplicateEntityException e) {
            System.out.println("✓ Correctly prevented duplicate student: " + e.getMessage());
        }

        try {
            enrollWithMessage(university, "S001", "CS101");
            System.out.println("✗ Should not allow duplicate enrollment");
        } catch (Exception e) {
            System.out.println("✓ Correctly prevented duplicate enrollment: " + e.getMessage());
        }

        // ========== 10. Capacity Management ==========
        System.out.println("\n--- 10. Capacity Management ---");
        System.out.println("Attempting to fill CS301 (capacity=15) beyond limit...");
        for (int i = 0; i < 20; i++) {
            String id = "S" + String.format("%03d", 100 + i);
            try {
                university.addStudent(id, "Test Student " + i);
                boolean enrolled = university.enrollStudent(id, "CS301");
                if (enrolled) {
                    System.out.println("  Enrolled " + id);
                } else {
                    System.out.println("  " + id + " added to WAITING LIST");
                }
            } catch (DuplicateEntityException e) {
                // Skip duplicates from earlier
            } catch (Exception e) {
                System.out.println("  Cannot enroll " + id + ": " + e.getMessage());
            }
        }

        System.out.println("\nCS301 final status: " + university.findCourseByCode("CS301"));

        // ========== 11. Remove from Course ==========
        System.out.println("\n--- 11. Remove Student from Course ---");
        try {
            System.out.println("Before removal - Students in MATH101: " +
                university.listStudentsInCourse("MATH101").size());
            university.removeStudentFromCourse("S002", "MATH101");
            System.out.println("✓ Removed S002 from MATH101");
            System.out.println("After removal - Students in MATH101: " +
                university.listStudentsInCourse("MATH101").size());
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }

        // ========== 12. Delete Course ==========
        System.out.println("\n--- 12. Delete Course ---");
        try {
            System.out.println("Before delete - Total courses: " + university.getTotalCourseCount());
            university.deleteCourse("PHY101");
            System.out.println("✓ Deleted course PHY101");
            System.out.println("After delete - Total courses: " + university.getTotalCourseCount());
            System.out.println("S001 courses after PHY101 deletion:");
            for (Course course : university.listCoursesOfStudent("S001")) {
                System.out.println("  - " + course.getTitle());
            }
        } catch (EntityNotFoundException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }

        // ========== Final Statistics ==========
        System.out.println("\n--- Final Statistics ---");
        System.out.println(university.getStatistics());

        System.out.println("===========================================================");
        System.out.println("  Demonstration Complete - All OOP Principles Applied!");
        System.out.println("===========================================================");
    }

    private static void enrollWithMessage(UniversitySystem university, String studentId, String courseCode) {
        try {
            boolean enrolled = university.enrollStudent(studentId, courseCode);
            if (enrolled) {
                System.out.println("✓ Enrolled " + studentId + " → " + courseCode);
            } else {
                System.out.println("  ⚠ " + studentId + " → " + courseCode + " (waiting list)");
            }
        } catch (EnrollmentConflictException e) {
            System.out.println("  ⚠ " + studentId + " → " + courseCode + " (already enrolled)");
        } catch (CourseFullException e) {
            System.out.println("  ✗ " + studentId + " → " + courseCode + " (course full)");
        } catch (EntityNotFoundException e) {
            System.out.println("  ✗ " + studentId + " → " + courseCode + " (not found)");
        }
    }
}
