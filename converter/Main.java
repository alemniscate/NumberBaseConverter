package converter;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            String input = scanner.nextLine();
            if ("/exit".equals(input)) {
                break;
            }
            int[] ints = Arrays.stream(input.split("\\s+")).mapToInt(Integer::parseInt).toArray();
            int sourceBase = ints[0];
            int targetBase = ints[1];
            while (true) {
                System.out.println("Enter number in base " + sourceBase + " to convert to base " + targetBase + " (To go back type /back)");
                String source = scanner.nextLine();
                if ("/back".equals(source)) {
                    break;
                }
                String[] strs = source.split("\\.");
                String integerPart = strs[0];
                String fractionalPart = "";
                if (strs.length > 1) {
                    fractionalPart = strs[1]; 
                }
                String result = convert(integerPart, sourceBase, targetBase);
                String resultFractional = convertFractional(fractionalPart, sourceBase, targetBase);
                System.out.println("Conversion result: " + result + resultFractional);
            }
        }
        scanner.close();
    }

    static String convertFractional(String source, int sourceBase, int targetBase) {
        if ("".equals(source)) {
            return "";
        }
        String source2 = (source + "00000").substring(0, 5);
        BigInteger decimal = convertTo(source2, sourceBase);
        BigDecimal decimal2 = new BigDecimal(decimal);
        decimal2 = decimal2.divide((new BigDecimal(sourceBase)).pow(5), 6, RoundingMode.HALF_UP);
        String str = convertFromFractional(decimal2, targetBase);
        str = str + "00000";
        return "." + str.substring(0, 5);
    }

    static String convertFromFractional(BigDecimal decimal, int radix) {
        BigDecimal q = decimal;
        BigDecimal radixB = new BigDecimal("" + radix);
        String result = "";
        for (int i = 0; i < 5 && q.compareTo(BigDecimal.ZERO) != 0; i++) {
            int r = q.multiply(radixB).intValue();
            result += convertDigit(r);
            q = q.multiply(radixB);
            q = q.subtract(new BigDecimal("" + r));
        }
        return result;
    }

    static String convert(String source, int sourceBase, int targetBase) {
        BigInteger decimal = convertTo(source, sourceBase);
        return convertFrom(decimal, targetBase);
    }

    static String convertFrom(BigInteger decimal, int radix) {
        BigInteger q = decimal;
        BigInteger radixB = new BigInteger("" + radix);
        String result = "";
        while (q.compareTo(radixB) >= 0) {
            int r = q.remainder(radixB).intValue();
            result = convertDigit(r) + result;
            q = q.divide(radixB);
        }
        return convertDigit(q.intValue()) + result;
    }

    static String convertDigit(int d) {
        if (d <= 9) {
            return "" + d;
        } else {
            return "" + (char) ('a' + d - 10);
        }
    }

    static BigInteger convertTo(String source, int radix) {
        BigInteger result = new BigInteger("0");
        BigInteger radixB = new BigInteger("" + radix);
        for (int i = 0; i < source.length(); i++) {
            result = result.multiply(radixB);
            char d = source.charAt(i);
            if (d >= '0' && d <= '9') {
                result = result.add(new BigInteger("" + (d - '0')));
            } else if (d >= 'A' && d <= 'Z') {
                result = result.add(new BigInteger("" + (d - 'A' + 10)));
            } else {
                result = result.add(new BigInteger("" + (d - 'a' + 10)));
            }
        }
        return result;
    }
}