package tp1;

// Exercise 2 – Fixed version of the buggy code
// Errors found and corrected:
// 1. Class name "main" -> "Exercise2"  (class name must be public & match filename)
// 2. Method "Main"    -> "main"        (entry point must be lowercase)
// 3. "String args"    -> "String[] args" (parameter must be an array)
// 4. "system.out"     -> "System.out"  (Java is case-sensitive)
// 5. Missing semicolon after first println statement
public class Exercise2 {
    public static void main(String[] args) {
        System.out.println("Welcome to Object-Oriented Programming");
        System.out.print("Module: OOP in Java");
    }
}
