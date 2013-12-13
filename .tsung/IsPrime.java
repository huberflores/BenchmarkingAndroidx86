public class IsPrime {

public static void main(String[] args) {
long startTime = System.nanoTime( );
     int[] numbers = {82342, 332259, 62366, 712525, 91526, 4235763, 12234, 1123461, 21125453, 1124243, 124623523, 142124, 532} ;
        for (int i = 0; i < numbers.length; i++ ) {
         if (numbers[i] == 2) {
         //System.out.println(numbers[i] + " is prime");
         } else if (isPrime(numbers[i])) {
         //System.out.println(numbers[i] + " is prime");
         } else {
         //System.out.println(numbers[i] + " is not prime");
         }
        }
        System.out.println((System.nanoTime( ) - startTime)*1.0e-6);
    }

public static boolean isPrime(int i) {
for (int k = 2; k < i; k++) {
if (i%k == 0) {
return false;
}
}
return true;

}
}
