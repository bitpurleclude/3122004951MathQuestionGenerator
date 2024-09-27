package org.example.tool;


import org.example.bean.Fraction;

import java.util.Random;
import java.util.Stack;

public class ArithmeticGenerator {
    // 随机生成合法的四则运算表达式
    public static String generateExpression(Random rand, int range, int maxDepth) {
        if (maxDepth == 0) {
            return generateOperand(rand, range); // 到达最大嵌套深度，生成操作数
        }

        // 随机决定是否添加括号
        boolean useParentheses = rand.nextBoolean();

        String left = generateExpression(rand, range, maxDepth - 1);  // 左子表达式
        String right = generateExpression(rand, range, maxDepth - 1); // 右子表达式
        String operator = generateOperator(rand); // 随机选择运算符

        String expression;

        if (useParentheses) {
            // 使用括号包裹表达式
            expression = "( " + left + " " + operator + " " + right + " )";
        } else {
            // 不使用括号
            expression = left + " " + operator + " " + right;
        }

        // 确保合法性：不产生负数或不合法的除法结果
        if (!isValidExpression(expression)) {
            return generateExpression(rand, range, maxDepth);  // 如果不合法，递归重新生成
        }

        return expression;
    }

    private static String generateOperand(Random rand, int range) {
        // 随机生成自然数、真分数、带分数
        int type = rand.nextInt(3);  // 0: 自然数, 1: 真分数, 2: 带分数

        switch (type) {
            case 0:
                return String.valueOf(rand.nextInt(range));  // 自然数
            case 1:
                return generateProperFraction(rand, range);  // 真分数
            case 2:
                return generateMixedFraction(rand, range);  // 带分数
            default:
                throw new IllegalArgumentException("Unknown operand type");
        }
    }
    private static String generateProperFraction(Random rand, int range) {
        int numerator = rand.nextInt(range - 1) + 1; // 分子，确保 > 0
        int denominator;
        do {
            denominator = rand.nextInt(range - 1) + 2; // 分母，确保分母大于分子
        } while (denominator <= numerator);
        return numerator + "/" + denominator;
    }

    // 随机生成运算符
    private static String generateOperator(Random rand) {
        String[] operators = {"+", "-", "*", "/"};
        return operators[rand.nextInt(operators.length)];
    }
    private static String generateMixedFraction(Random rand, int range) {
        int whole = rand.nextInt(range);  // 整数部分
        int numerator;
        int denominator;

        // 确保分数部分是真分数，即分子 < 分母
        do {
            numerator = rand.nextInt(range - 1) + 1; // 分子，确保 > 0
            denominator = rand.nextInt(range - 1) + 2; // 分母，确保分母大于分子
        } while (denominator <= numerator);

        return whole + "'" + numerator + "/" + denominator;
    }
    private static boolean isValidExpression(String expression) {
        try {
            // 计算表达式结果并检查是否有效
            Fraction result = evaluateExpression(expression);

            // 如果计算结果是负数，则不合法
            if (result.getNumerator() < 0) {
                return false;
            }

            return true;
        } catch (ArithmeticException e) {
            // 捕获非法除法（除以零等）的异常
            return false;
        }
    }

    // 用于解析和计算表达式
    private static Fraction evaluateExpression(String expression) throws ArithmeticException {
        Stack<Fraction> operands = new Stack<>();
        Stack<String> operators = new Stack<>();

        // 将表达式按空格拆分，便于逐个解析
        String[] tokens = expression.split(" ");
        int i = 0;

        while (i < tokens.length) {
            String token = tokens[i];

            if (isOperator(token)) {
                // 如果是运算符，压入运算符栈
                while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
                    Fraction right = operands.pop();
                    Fraction left = operands.pop();
                    String operator = operators.pop();
                    operands.push(applyOperator(left, right, operator));
                }
                operators.push(token);
            } else if (token.equals("(")) {
                // 遇到左括号时，递归处理括号内的子表达式
                int j = findClosingParenthesis(tokens, i); // 找到对应的右括号
                String subExpression = String.join(" ", java.util.Arrays.copyOfRange(tokens, i + 1, j)); // 获取括号内的子表达式
                operands.push(evaluateExpression(subExpression)); // 递归计算子表达式的值
                i = j; // 跳过括号内的部分
            } else if (token.equals(")")) {
                // 右括号在此不做处理（在遇到左括号时已经处理过）
                throw new IllegalArgumentException("Mismatched parentheses in expression.");
            } else {
                // 处理操作数，将其转换为分数对象并压入操作数栈
                operands.push(parseFraction(token)); // 只对非括号的部分进行操作数解析
            }
            i++;
        }

        // 完成所有运算
        while (!operators.isEmpty()) {
            Fraction right = operands.pop();
            Fraction left = operands.pop();
            String operator = operators.pop();
            operands.push(applyOperator(left, right, operator));
        }

        // 最终栈中应该只剩下一个结果
        return operands.pop();
    }

    // 找到与当前左括号匹配的右括号的位置
    private static int findClosingParenthesis(String[] tokens, int start) {
        int count = 0;
        for (int i = start; i < tokens.length; i++) {
            if (tokens[i].equals("(")) {
                count++;
            } else if (tokens[i].equals(")")) {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        throw new IllegalArgumentException("Mismatched parentheses in expression.");
    }

    // 检查是否是运算符
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    // 检查运算符优先级
    private static boolean hasPrecedence(String op1, String op2) {
        if (op2.equals("(") || op2.equals(")")) {
            return false;
        }
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) {
            return false;
        }
        return true;
    }

    // 执行四则运算并返回结果
    private static Fraction applyOperator(Fraction left, Fraction right, String operator) {
        switch (operator) {
            case "+":
                return left.add(right);
            case "-":
                // 检查减法结果是否为负数
                if (left.compareTo(right) < 0) {
                    throw new ArithmeticException("Negative result in subtraction");
                }
                return left.subtract(right);
            case "*":
                return left.multiply(right);
            case "/":
                // 检查除法是否合法，确保除数不为零
                if (right.getNumerator() == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return left.divide(right);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }

    // 解析操作数为Fraction对象（自然数、真分数、带分数）
    private static Fraction parseFraction(String operand) {
        operand = operand.trim(); // 去除首尾空格

        // 处理带分数，如 2'3/4
        if (operand.contains("'")) {
            String[] parts = operand.split("'");
            int whole = Integer.parseInt(parts[0]);
            String[] fractionParts = parts[1].split("/");
            int numerator = Integer.parseInt(fractionParts[0]);
            int denominator = Integer.parseInt(fractionParts[1]);
            return new Fraction(whole * denominator + numerator, denominator);
        }

        // 处理真分数，如 3/5
        if (operand.contains("/")) {
            String[] fractionParts = operand.split("/");
            return new Fraction(Integer.parseInt(fractionParts[0]), Integer.parseInt(fractionParts[1]));
        }

        // 处理自然数
        try {
            return new Fraction(Integer.parseInt(operand), 1);
        } catch (NumberFormatException e) {
            // 处理非数字字符串（例如，传入整个表达式而不是单一数字）
            throw new IllegalArgumentException("Invalid operand: " + operand);
        }
    }

}
