package org.example.bean;

import lombok.Data;

@Data
public class Fraction implements Comparable<Fraction> {
    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Denominator cannot be zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
        simplify();
    }

    private void simplify() {
        int gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
    }

    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public Fraction add(Fraction other) {
        int newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction subtract(Fraction other) {
        int newNumerator = this.numerator * other.denominator - other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    public Fraction divide(Fraction other) {
        return new Fraction(this.numerator * other.denominator, this.denominator * other.numerator);
    }

    // 将假分数转化为真分数或带分数
    public String toProperFractionOrMixed() {
        if (Math.abs(numerator) < denominator) {
            // 真分数，直接返回
            return numerator + "/" + denominator;
        } else {
            // 带分数的情况
            int whole = numerator / denominator;
            int remainder = Math.abs(numerator % denominator);
            if (remainder == 0) {
                // 整数情况
                return String.valueOf(whole);
            } else {
                // 带分数情况
                return whole + "'" + remainder + "/" + denominator;
            }
        }
    }

    @Override
    public String toString() {
        return toProperFractionOrMixed(); // 自动将假分数转换为真分数或带分数输出
    }

    @Override
    public int compareTo(Fraction other) {
        return Integer.compare(this.numerator * other.denominator, other.numerator * this.denominator);
    }
}
