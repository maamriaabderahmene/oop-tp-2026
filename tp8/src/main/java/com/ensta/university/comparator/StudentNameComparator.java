package com.ensta.university.comparator;

import com.ensta.university.model.Student;
import java.util.Comparator;

/**
 * Comparator for sorting students by name (alphabetically)
 */
public class StudentNameComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        if (s1 == null || s2 == null) {
            throw new IllegalArgumentException("Students cannot be null");
        }
        return s1.getName().compareToIgnoreCase(s2.getName());
    }
}
