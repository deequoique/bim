package edu.hitsz.bim.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    public static BigDecimal fromStr(String stringValue) {
        return new BigDecimal(stringValue);
    }

    public static String toStr(BigDecimal decimalValue) {
        return decimalValue.toPlainString();
    }
    public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
        return value1.add(value2);
    }

    public static BigDecimal add(String valueStr1, String valueStr2) {
        return fromStr(valueStr1).add(fromStr(valueStr2));
    }

    public static BigDecimal sub(String valueStr1, String valueStr2) {
        return fromStr(valueStr1).subtract(fromStr(valueStr2));
    }
    public static BigDecimal sub(BigDecimal value1, BigDecimal value2) {
        return value1.subtract(value2);
    }
    public static BigDecimal divide(String valueStr1, String valueStr2){
        return fromStr(valueStr1).divide(fromStr(valueStr2));
    }
    public static BigDecimal divide(BigDecimal value1, BigDecimal value2) {
        return value1.divide(value2);
    }

    public static BigDecimal multiply(String valueStr1, String valueStr2){
        return fromStr(valueStr1).multiply(fromStr(valueStr2));
    }
    public static BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
        return value1.multiply(value2);
    }

    /**
     * compare bigDecimal
     * @param str1
     * @param str2
     * @return
     *     -1 str1<str2
     *     0  str1=str2
     *     1  str1>str2
     */
    public static int compareTo(String str1, String str2) {
        return fromStr(str1).compareTo(fromStr(str2));
    }
    public static int compareTo(BigDecimal str1, BigDecimal str2) {
        return str1.compareTo(str2);
    }
    public static boolean ifGreater(String str1, String str2) {
        return compareTo(str1, str2) == 1;
    }
    public static boolean ifGreater(BigDecimal str1, BigDecimal str2) {
        return compareTo(str1, str2) == 1;
    }

    public static boolean ifLess(String str1, String str2) {
        return compareTo(str1, str2) == -1;
    }
    public static boolean ifLess(BigDecimal str1, BigDecimal str2) {
        return compareTo(str1, str2) == -1;
    }

    public static boolean ifEqual(String str1, String str2) {
        return compareTo(str1, str2) == 0;
    }
    public static boolean ifEqual(BigDecimal str1, BigDecimal str2) {
        return compareTo(str1, str2) == 0;
    }
}
