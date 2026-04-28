# OOP Principles Applied in University Course Management System

This document details all Object-Oriented Programming (OOP) principles and practices applied in the University Course Management System implementation.

---

## 1. Encapsulation (التغليف)

**Encapsulation** is the bundling of data with methods that operate on that data, restricting direct access to object internals.

### Implementation Examples:

#### Private Fields with Controlled Access
```java
public class Student {
    private final String id;              // Immutable after creation
    private String name;                  // Mutable via setter
    private final Set<Course> enrolledCourses;  // Private collection
    private boolean isDeleted;            // Internal state hidden
}
```

#### Package-Private Getters
- Getters return package-private access to prevent external modification
- Collections return defensive copies:
```java
Set<Course> getEnrolledCourses() {
    return new HashSet<>(enrolledCourses);  // Defensive copy
}
```

**Benefits:**
- Prevents unauthorized access to internal state
- Allows validation in setters
- Enables change of internal representation without affecting clients
- Protects data integrity

---

## 2. Abstraction (التخليص)

**Abstraction** hides complex implementation details and exposes only essential features.

### Implementation Examples:

#### Interface Hiding
- `UniversitySystem` acts as a **Facade** pattern
- Complex relationships hidden behind simple methods:
```java
public boolean enrollStudent(String studentId, String courseCode)
```

#### Exception Hierarchy
```java
UniversityException (base)
    ├── DuplicateEntityException
    ├── CourseFullException
    ├── EntityNotFoundException
    └── EnrollmentConflictException
```
Clients handle generic `UniversityException` without knowing internal details.

**Benefits:**
- Reduces complexity for users
- Isolates impact of changes
- Improves code maintainability
- Provides clear contracts

---

## 3. Inheritance (الميراث)

**Inheritance** allows classes to inherit properties and methods from parent classes.

### Implementation Examples:

#### Exception Inheritance Chain
```java
public class DuplicateEntityException extends UniversityException
public class CourseFullException extends UniversityException
```

#### Polymorphic Exception Handling
```java
try {
    university.enrollStudent("S001", "CS101");
} catch (UniversityException e) {  // Catches ALL university exceptions
    // Handle all exception types uniformly
}
```

**Benefits:**
- Code reuse through shared base class
- Polymorphic behavior
- Consistent exception handling
- Extensible type hierarchy

---

## 4. Polymorphism (التعدد الأشكال)

**Polymorphism** allows objects of different classes to be treated as objects of a common superclass.

### Implementation Examples:

#### Method Overriding - Comparator Pattern
```java
public class StudentNameComparator implements Comparator<Student> {
    @Override
    public int compare(Student s1, Student s2) {
        return s1.getName().compareToIgnoreCase(s2.getName());
    }
}

public class CourseCapacityComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        return Integer.compare(c1.getMaxCapacity(), c2.getMaxCapacity());
    }
}
```

#### Interface-Based Sorting
```java
public List<Student> getStudentsSortedByName() {
    return students.values().stream()
            .sorted(new StudentNameComparator())  // Polymorphic comparator
            .collect(Collectors.toList());
}
```

**Benefits:**
- Interchangeable sorting strategies
- Extensible without modifying existing code
- Adheres to Open/Closed Principle
- Runtime flexibility

---

## 5. Composition over Inheritance (التركيب على الإرث)

**Composition** builds complex objects by combining simpler ones.

### Implementation Examples:

#### UniversitySystem Composition
```java
public class UniversitySystem {
    private final Map<String, Student> students;      // Composed collection
    private final Map<String, Course> courses;        // Composed collection
    private final Map<String, Enrollment> enrollments; // Composed collection
}
```

#### Enrollment as Composition
```java
public class Enrollment {
    private final Student student;   // Composed object
    private final Course course;     // Composed object
}
```

**Benefits:**
- Greater flexibility than inheritance
- Runtime behavior changes
- Avoids inheritance hierarchy issues
- Better code organization

---

## 6. SOLID Principles

### Single Responsibility Principle (SRP)
**Each class has one reason to change.**

- `Student`: Manages student data and state
- `Course`: Manages course data and capacity
- `Enrollment`: Manages student-course relationship
- `UniversitySystem`: Manages system-wide operations
- Each `Comparator`: Handles one specific sorting criteria

### Open/Closed Principle (OCP)
**Open for extension, closed for modification.**

- New comparison strategies can be added without modifying `UniversitySystem`
- New exception types can be added without changing exception handling logic
- Use of interfaces (`Comparator`) allows extension

### Liskov Substitution Principle (LSP)
**Subtypes must be substitutable for base types.**

- All exception types can substitute `UniversityException`
- All `Comparator` implementations work where `Comparator` is expected
- No violation of behavioral contracts

### Interface Segregation Principle (ISP)
**Clients shouldn't depend on interfaces they don't use.**

- Specific exception types for specific error conditions
- Separate comparators for different sorting needs
- No "fat" interfaces

### Dependency Inversion Principle (DIP)
**Depend on abstractions, not concretions.**

```java
public List<Student> getStudentsSortedByName() {
    return students.values().stream()
            .sorted(new StudentNameComparator())  // Depends on Comparator interface
            .collect(Collectors.toList());
}
```

---

## 7. Design Patterns Applied

### 7.1 Facade Pattern
```java
public class UniversitySystem {
    // Simplified interface to complex subsystem
    public boolean enrollStudent(String studentId, String courseCode) { ... }
}
```
**Purpose:** Hide complexity of managing students, courses, and enrollments.

### 7.2 Strategy Pattern
```java
public interface Comparator<T> { ... }
public class StudentNameComparator implements Comparator<Student> { ... }
public class CourseCapacityComparator implements Comparator<Course> { ... }
```
**Purpose:** Interchangeable sorting algorithms.

### 7.3 DTO Pattern (Data Transfer Object)
```java
public static class Statistics {
    private final long totalActiveStudents;
    private final long totalCourses;
    private final double averageEnrollmentPerCourse;
}
```
**Purpose:** Immutable data container for statistics.

### 7.4 Composite Pattern
```java
public class Enrollment {
    private final Student student;
    private final Course course;
}
```
**Purpose:** Represent many-to-many relationships.

### 7.5 Soft Delete Pattern
```java
public class Student {
    private boolean isDeleted;  // Soft delete flag
    
    void softDelete() { this.isDeleted = true; }
    void restore() { this.isDeleted = false; }
}
```
**Purpose:** Preserve data while marking as deleted.

---

## 8. Additional OOP Best Practices

### 8.1 Immutability
- `id` and `code` fields are `final`
- `UUID` generation for enrollment IDs
- Defensive copying of collections

### 8.2 Encapsulation
- All fields are `private`
- No public setters for critical fields
- Validation in constructors and setters

### 8.3 Information Hiding
- Implementation details hidden
- Package-private access for internal APIs
- Clean public interfaces

### 8.4 Type Safety
- Generic collections: `Map<String, Student>`
- Strong typing throughout
- Compile-time checks

### 8.5 Fail-Fast Validation
```java
public Student(String id, String name) {
    if (id == null || id.trim().isEmpty()) {
        throw new IllegalArgumentException("Student ID cannot be null or empty");
    }
    // ...
}
```

### 8.6 Exception Handling
- Checked exceptions for recoverable errors
- Unchecked exceptions for programming errors
- Meaningful error messages

### 8.7 Java Collections Framework
- `HashMap` for O(1) lookups by ID/code
- `HashSet` for O(1) membership tests
- `ArrayList` for ordered collections
- `TreeMap` for sorted grouping
- `Stream API` for functional operations

---

## 9. Advanced OOP Techniques

### 9.1 Method Chaining (Builder Pattern Style)
```java
university.addStudent("S001", "Alice")
         .enrollStudent("S001", "CS101");
```

### 9.2 Stream API (Functional OOP)
```java
students.values().stream()
    .filter(s -> !s.isDeleted())
    .sorted(new StudentNameComparator())
    .collect(Collectors.toList());
```

### 9.3 Generic Programming
```java
public interface Comparator<T> {
    int compare(T o1, T o2);
}
```

### 9.4 Package Organization
```
com.ensta.university/
    model/       # Domain objects
    service/     # Business logic
    comparator/  # Strategy implementations
    exception/   # Exception hierarchy
```

---

## 10. Anti-Patterns Avoided

### 10.1 God Object
- Responsibilities properly distributed
- No single class doing everything

### 10.2 Primitive Obsession
- Custom types instead of primitives
- Value objects for domain concepts

### 10.3 Feature Envy
- Methods operate on their own data
- No excessive cross-class method calls

### 10.4 Data Classes
- Classes have behavior, not just data
- Methods encapsulate logic

### 10.5 Switch Statements
- Polymorphism instead of switches
- Strategy pattern for variant behavior

---

## 11. Code Metrics

### Coupling
- **Low coupling:** Classes depend on interfaces
- **Loose coupling:** Minimal interdependencies

### Cohesion
- **High cohesion:** Related functionality grouped
- **Single responsibility:** Clear class purposes

### Cyclomatic Complexity
- **Low complexity:** Simple, readable methods
- **No deep nesting:** Flat structure preferred

---

## 12. Conclusion

This implementation demonstrates comprehensive OOP principles:

✅ **Encapsulation** - Private fields, controlled access  
✅ **Abstraction** - Facade pattern, interface hiding  
✅ **Inheritance** - Exception hierarchy, code reuse  
✅ **Polymorphism** - Comparator strategies, interface implementations  
✅ **SOLID** - All five principles applied  
✅ **Design Patterns** - Facade, Strategy, DTO, Composite, Soft Delete  
✅ **Best Practices** - Immutability, collections, validation, exceptions  
✅ **Advanced Techniques** - Streams, generics, functional programming  

The system is:
- **Maintainable** - Clear structure, separation of concerns
- **Extensible** - Open for extension, closed for modification
- **Testable** - Decoupled components, clear interfaces
- **Robust** - Validation, exception handling, data integrity
- **Efficient** - Appropriate data structures, O(1) lookups
- **Type-Safe** - Generics, compile-time checks

---

*Generated for TD-TP 8: Collection in Java*  
*ENSTA Bretagne - OOP Course*  
*2026*