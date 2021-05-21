import org.jfree.data.function.Function2D;

public class ExponentialFunction2D implements Function2D {

     /** The 'a' coefficient. */
     private double a;

     /** The 'b' coefficient. */
     private double b;

     /**
      * Creates a new power function.
      *
      * @param a  the 'a' coefficient.
      * @param b  the 'b' coefficient.
      */
     public ExponentialFunction2D(double a, double b) {
         this.a = a;
         this.b = b;
     }

     /**
      * Returns the value of the function for a given input ('x').
      *
      * @param x  the x-value.
      *
      * @return The value.
      */
     public double getValue(double x) {
         return this.a * Math.exp(b * x);
     }

 }