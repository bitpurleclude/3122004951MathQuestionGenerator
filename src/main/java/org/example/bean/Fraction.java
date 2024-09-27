package org.example.bean;

import lombok.Data;

@Data
public class Fraction {
    // 定义分数类
        int numerator;   // 分子
        int denominator; // 分母

        public Fraction(int numerator, int denominator) {
            if (denominator == 0) {
                throw new ArithmeticException("Denominator cannot be zero");
            }
            int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
            this.numerator = numerator / gcd;
            this.denominator = denominator / gcd;
            if (this.denominator < 0) {
                this.numerator = -this.numerator;
                this.denominator = -this.denominator;
            }
        }

        // 最大公约数
        private int gcd(int a, int b) {
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }

        // 分数加法
        public Fraction add(Fraction other) {
            int num = this.numerator * other.denominator + other.numerator * this.denominator;
            int denom = this.denominator * other.denominator;
            return new Fraction(num, denom);
        }

        // 分数减法
        public Fraction subtract(Fraction other) {
            int num = this.numerator * other.denominator - other.numerator * this.denominator;
            int denom = this.denominator * other.denominator;
            return new Fraction(num, denom);
        }

        // 分数乘法
        public Fraction multiply(Fraction other) {
            return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
        }

        // 分数除法
        public Fraction divide(Fraction other) {
            return new Fraction(this.numerator * other.denominator, this.denominator * other.numerator);
        }

        @Override
        public String toString() {
            // 输出带分数的形式
            if (Math.abs(this.numerator) >= this.denominator) {
                int whole = this.numerator / this.denominator;
                int remainder = Math.abs(this.numerator % this.denominator);
                if (remainder == 0) {
                    return String.valueOf(whole); // 纯整数
                } else {
                    return whole + "'" + remainder + "/" + this.denominator; // 带分数
                }
            } else {
                return this.numerator + "/" + this.denominator; // 真分数
            }
        }
    // 分数类，用于解析和比较操作数
        public int compareTo(Fraction other) {
            return Integer.compare(this.numerator * other.denominator, other.numerator * this.denominator);
        }

    // 解析分数（支持带分数）
    public static Fraction parseFraction(String num) {
        if (num.contains("'")) {
            // 处理带分数的形式，如 2'3/5
            String[] parts = num.split("'");
            int whole = Integer.parseInt(parts[0]);
            String[] fractionParts = parts[1].split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);
            if (whole < 0) numerator = -numerator; // 处理负数的带分数
            return new Fraction(whole * denominator + numerator, denominator);
        } else if (num.contains("/")) {
            // 处理真分数，如 3/5
            String[] fractionParts = num.split("/");
            return new Fraction(Integer.parseInt(fractionParts[0]), Integer.parseInt(fractionParts[1]));
        } else {
            // 处理整数
            return new Fraction(Integer.parseInt(num), 1);
        }
    }

    // 运算符优先级
    static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    // 应用运算符
    static Fraction applyOperator(Fraction first, Fraction second, char operator) {
        switch (operator) {
            case '+':
                return first.add(second);
            case '-':
                return first.subtract(second);
            case '*':
                return first.multiply(second);
            case '/':
                return first.divide(second);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
