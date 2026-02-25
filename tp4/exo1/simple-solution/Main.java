package tp4.exo1.simple;

public class Main {
    public static void main(String[] args) {
        // Create a Person
        Person person = new Person("Alice", 30, "alice@ensta.dz");
        System.out.println(person);

        // Create a Teacher (inherits from Person)
        Teacher teacher = new Teacher("Bob", 45, "bob@ensta.dz", "Mathematics", 20);
        System.out.println(teacher);

        // Teacher-specific behaviors
        teacher.teach();
        teacher.evaluateStudents();

        // Controlled access via setters
        teacher.setSubject("Physics");
        teacher.setExperience(21);
        System.out.println("After update: " + teacher);
    }
}
