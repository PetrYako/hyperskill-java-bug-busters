package memorizingtool;

import java.math.BigDecimal;

public class NumberMemorize extends BaseMemorize<Long> {

    public NumberMemorize() {
        super(Long.class);

        commands.put("/sum", new Class<?>[]{int.class, int.class});
        commands.put("/subtract", new Class<?>[]{int.class, int.class});
        commands.put("/multiply", new Class<?>[]{int.class, int.class});
        commands.put("/divide", new Class<?>[]{int.class, int.class});
        commands.put("/pow", new Class<?>[]{int.class, int.class});
        commands.put("/factorial", new Class<?>[]{int.class});
        commands.put("/sumAll", new Class<?>[]{});
        commands.put("/average", new Class<?>[]{});
    }

    void help() {
        super.help();
        System.out.println(
                "===================================================================================================================\n" +
                        "Number-specific commands:\n" +
                        "===================================================================================================================\n" +
                        "/sum [<int> INDEX1] [<int> INDEX2] - Calculate the sum of the two specified elements\n" +
                        "/subtract [<int> INDEX1] [<int> INDEX2] - Calculate the difference between the two specified " +
                        "elements\n" +
                        "/multiply [<int> INDEX1] [<int> INDEX2] - Calculate the product of the two specified elements\n" +
                        "/divide [<int> INDEX1] [<int> INDEX2] - Calculate the division of the two specified elements\n" +
                        "/pow [<int> INDEX1] [<int> INDEX2] - Calculate the power of the specified element to the " +
                        "specified exponent element\n" +
                        "/factorial [<int> INDEX] - Calculate the factorial of the specified element\n" +
                        "/sumAll - Calculate the sum of all elements\n" +
                        "/average - Calculate the average of all elements\n" +
                        "===================================================================================================================");
    }

    void sum(int i, int j) {
        try {
            long a = list.get(i);
            long b = list.get(j);
            Long res = a + b;
            System.out.printf("Calculation performed: %d + %d = %d\n", a, b, res);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void subtract(int i, int j) {
        try {
            long a = list.get(i), b = list.get(j);
            long res = a - b;
            System.out.printf("Calculation performed: %d - %d = %d\n", a, b, res);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void multiply(int i, int j) {
        try {
            long a = list.get(i), b = list.get(j);
            long res = a * b;
            System.out.printf("Calculation performed: %d * %d = %d\n", a, b, res);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void divide(int i, int j) {
        try {
            long a = list.get(i);
            long b = list.get(j);
            if (b == 0) {
                System.out.println("Division by zero");
                return;
            }
            double res = (double) a / b;
            System.out.printf("Calculation performed: %d / %d = %f\n", a, b, res);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void pow(int i, int j) {
        try {
            long a = list.get(i);
            long b = list.get(j);
            if (b == 0) {
                System.out.printf("Calculation performed: %d ^ %d = %d\n", a, b, 1);
            } else if (b > 0) {
                BigDecimal res = BigDecimal.valueOf(a).pow((int) b);
                System.out.printf("Calculation performed: %d ^ %d = %f\n", a, b, res);
            } else {
                BigDecimal res = BigDecimal.ONE.divide(BigDecimal.valueOf(a).pow((int) Math.abs(b)));
                System.out.printf("Calculation performed: %d ^ %d = %.6f\n", a, b, res);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void factorial(int index) {
        try {
            long res = 1;
            long num = list.get(index);
            if (num < 0) {
                System.out.println("undefined");
                return;
            }
            if (num == 0) {
                System.out.printf("Calculation performed: %d! = %d\n", num, res);
                return;
            }
            for (long i = 2; i <= num; i++) {
                res = res * i;
            }
            System.out.printf("Calculation performed: %d! = %d\n", num, res);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void sumAll() {
        long sum = 0;
        for (long i : list) {
            sum += i;
        }
        System.out.println("Sum of all elements: " + sum);
    }

    void average() {
        BigDecimal sum = BigDecimal.ZERO;
        for (long i : list) {
            sum = sum.add(BigDecimal.valueOf(i));
        }
        System.out.println("Average of all elements: " + sum.divide(BigDecimal.valueOf(list.size())));
    }
}
