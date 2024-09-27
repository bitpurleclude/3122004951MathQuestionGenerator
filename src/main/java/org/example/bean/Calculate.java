package org.example.bean;

import java.util.Stack;


public class Calculate {

    public static String calculateAnswer(String expression) {
        // 移除多余的空格并将表达式按空格拆分
        String[] tokens = expression.trim().split(" ");

        // 操作数栈和运算符栈
        Stack<Fraction> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        // 遍历每一个分割后的 token
        for (String token : tokens) {
            // 忽略空的 token
            if (token.isEmpty()) {
                continue;
            }

            // 检查是否为运算符且前后有空格
            if (isOperator(token)) {
                // 处理乘除的优先级高于加减
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(token.charAt(0))) {
                    char op = operatorStack.pop();
                    Fraction second = operandStack.pop();
                    Fraction first = operandStack.pop();
                    operandStack.push(applyOperator(first, second, op));
                }
                operatorStack.push(token.charAt(0));
            } else if (token.equals("(")) {
                // 遇到左括号，压入运算符栈
                operatorStack.push('(');
            } else if (token.equals(")")) {
                // 计算直到匹配到'('
                while (operatorStack.peek() != '(') {
                    char op = operatorStack.pop();
                    Fraction second = operandStack.pop();
                    Fraction first = operandStack.pop();
                    operandStack.push(applyOperator(first, second, op));
                }
                operatorStack.pop(); // 移除 '('
            } else {
                // 如果不是运算符，就是数字或分数，将其压入操作数栈
                operandStack.push(parseFraction(token));
            }
        }

        // 处理剩余的操作符
        while (!operatorStack.isEmpty()) {
            char op = operatorStack.pop();
            Fraction second = operandStack.pop();
            Fraction first = operandStack.pop();
            operandStack.push(applyOperator(first, second, op));
        }

        // 最终结果是栈顶的分数
        return operandStack.pop().toString();
    }
    // 判断是否为运算符，且前后有空格时才当作运算符
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    // 解析分数或整数的函数，包括带分数
    private static Fraction parseFraction(String num) {
        num = num.trim();

        if (num.isEmpty()) {
            throw new NumberFormatException("Empty string for fraction");
        }

        if (num.contains("'")) {
            // 带分数，如 7'5/9
            String[] parts = num.split("'");
            int whole = Integer.parseInt(parts[0]); // 整数部分
            String[] fractionParts = parts[1].split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);
            // 带分数等于整数部分 + 真分数
            return new Fraction(whole * denominator + numerator, denominator);
        } else if (num.contains("/")) {
            // 真分数，如 5/9
            String[] fractionParts = num.split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);
            return new Fraction(numerator, denominator);
        } else {
            // 处理整数
            return new Fraction(Integer.parseInt(num), 1);
        }
    }

    // 运算符优先级
    private static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    // 应用操作符
    private static Fraction applyOperator(Fraction first, Fraction second, char op) {
        switch (op) {
            case '+':
                return first.add(second);
            case '-':
                return first.subtract(second);
            case '*':
                return first.multiply(second);
            case '/':
                return first.divide(second);
        }
        throw new IllegalArgumentException("Unknown operator: " + op);
    }

}
