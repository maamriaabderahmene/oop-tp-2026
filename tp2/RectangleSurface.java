
package tp2;

// Exercise 1 – Rectangle Surface
import java.util.Scanner;

public class RectangleSurface {
    double length;
    double width;

    RectangleSurface(double length, double width) {
        this.length = length;
        this.width = width;
    }

    double computeSurface() {
        return length * width;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter length: ");
        double length = scanner.nextDouble();
        System.out.print("Enter width: ");
        double width = scanner.nextDouble();
        scanner.close();

        RectangleSurface rect = new RectangleSurface(length, width);
        System.out.println("Surface = " + rect.computeSurface());
    }
}
