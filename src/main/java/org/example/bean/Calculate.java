package org.example.bean;

import lombok.Data;

import java.util.Stack;

import static org.example.bean.Fraction.*;

@Data
public class Calculate{
    public static String calculateAnswer(String expression) {
        // 移除空格
        expression = expression.replace(" ", "");

        // 操作数栈和运算符栈
        Stack<Fraction> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        // 当前数字或分数
        StringBuilder currentNum = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '/' || ch == '\'') {
                // 数字或分数的一部分
                currentNum.append(ch);
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                // 处理当前的数字或分数
                if (currentNum.length() > 0) {
                    operandStack.push(parseFraction(currentNum.toString()));
                    currentNum.setLength(0);
                }

                // 处理乘除的优先级高于加减
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(ch)) {
                    char op = operatorStack.pop();
                    Fraction second = operandStack.pop();
                    Fraction first = operandStack.pop();
                    operandStack.push(applyOperator(first, second, op));
                }

                operatorStack.push(ch);
            } else if (ch == '(') {
                operatorStack.push(ch);
            } else if (ch == ')') {
                if (currentNum.length() > 0) {
                    operandStack.push(parseFraction(currentNum.toString()));
                    currentNum.setLength(0);
                }
                // 计算直到匹配到'('
                while (operatorStack.peek() != '(') {
                    char op = operatorStack.pop();
                    Fraction second = operandStack.pop();
                    Fraction first = operandStack.pop();
                    operandStack.push(applyOperator(first, second, op));
                }
                operatorStack.pop(); // 移除 '('
            }
        }

        // 最后一个数字
        if (currentNum.length() > 0) {
            operandStack.push(parseFraction(currentNum.toString()));
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

}
