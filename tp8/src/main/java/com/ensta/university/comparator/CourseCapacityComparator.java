package com.ensta.university.comparator;

import com.ensta.university.model.Course;
import java.util.Comparator;

/**
 * Comparator for sorting courses by capacity
 */
public class CourseCapacityComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("Courses cannot be null");
        }
        return Integer.compare(c1.getMaxCapacity(), c2.getMaxCapacity());
    }
}
