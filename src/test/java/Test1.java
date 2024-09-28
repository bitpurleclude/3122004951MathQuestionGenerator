import org.example.Main;
import org.example.bean.Calculate;
import org.example.bean.FileUtil;
import org.example.bean.Fraction;
import org.example.tool.ArithmeticGenerator;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.example.bean.Calculate.calculateAnswer;
import static org.example.tool.ArithmeticGenerator.generateExpression;
import static org.junit.jupiter.api.Assertions.*;

public class Test1 {

    @Test
    public void testCalculateAnswer() {// 测试计算表达式
        String expression = "1 + 2 * 3";
        String result = Calculate.calculateAnswer(expression);
        System.out.println("预期结果: 7, 实际结果: " + result);
        assertEquals("7", result, "计算表达式结果失败");
    }

    @Test
    public void testCalculateAnswerWithParentheses() {// 测试计算带括号的表达式
        String expression = "( 1 + 2 ) * 3";
        String result = Calculate.calculateAnswer(expression);
        System.out.println("预期结果: 9, 实际结果: " + result);
        assertEquals("9", result, "计算带括号的表达式结果失败");
    }

    @Test
    public void testCalculateAnswerWithMixedFractions() {// 测试计算带分数的表达式
        String expression = "1'1/2 + 2'1/3";
        String result = Calculate.calculateAnswer(expression);
        System.out.println("预期结果: 3'5/6, 实际结果: " + result);
        assertEquals("3'5/6", result, "计算带分数的表达式结果失败");
    }

    @Test
    public void testToProperFractionOrMixed() {// 测试转换为真分数或带分数
        Fraction f = new Fraction(7, 3);
        String result = f.toString();
        System.out.println("预期结果: 2'1/3, 实际结果: " + result);
        assertEquals("2'1/3", result, "带分数转换错误");
    }

    @Test
    public void testCompareTo() {// 测试分数比较
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        int comparisonResult = f1.compareTo(f2);
        System.out.println("预期结果: > 0, 实际结果: " + comparisonResult);
        assertTrue(comparisonResult > 0, "分数比较错误");
    }

    @Test
    public void testGenerateExpression() { // 测试生成随机数学表达式
        Random rand = new Random();
        int range = 10;
        int complexity = 3;
        String expression = ArithmeticGenerator.generateExpression(rand, range, complexity);
        System.out.println("生成的表达式: " + expression);
        assertNotNull(expression, "生成的表达式不应为空");
        assertFalse(expression.isEmpty(), "生成的表达式不应为空字符串");
    }

    @Test
    public void testGenerateExpressionWithDifferentRange() { // 测试不同范围的表达式生成
        Random rand = new Random();
        int range = 20;
        int complexity = 3;
        String expression = ArithmeticGenerator.generateExpression(rand, range, complexity);
        System.out.println("生成的表达式 (范围20): " + expression);
        assertNotNull(expression, "生成的表达式不应为空");
        assertFalse(expression.isEmpty(), "生成的表达式不应为空字符串");
    }

    @Test
    public void testGenerateExpressionWithDifferentComplexity() { // 测试不同复杂度的表达式生成
        Random rand = new Random();
        int range = 10;
        int complexity = 5;
        String expression = ArithmeticGenerator.generateExpression(rand, range, complexity);
        System.out.println("生成的表达式 (复杂度5): " + expression);
        assertNotNull(expression, "生成的表达式不应为空");
        assertFalse(expression.isEmpty(), "生成的表达式不应为空字符串");
    }

    @Test
    public void testGenerateExercises() throws IOException { // 测试生成题目
        int n = 5;
        int range = 10;
        Main.generateExercises(n, range);

        List<String> exercises = Files.readAllLines(Paths.get("Exercises.txt"));
        List<String> answers = Files.readAllLines(Paths.get("Answers.txt"));

        System.out.println("生成的题目数量: " + exercises.size());
        System.out.println("生成的答案数量: " + answers.size());

        assertEquals(n, exercises.size(), "生成的题目数量错误");
        assertEquals(n, answers.size(), "生成的答案数量错误");
    }

    @Test
    public void testCheckAnswers() throws IOException { // 测试检查答案
        String exerciseFile = "testExercises.txt";
        String answerFile = "testAnswers.txt";
        List<String> exercises = List.of("1 + 1", "2 * 2", "3 - 1");
        List<String> answers = List.of("2", "4", "2");
        Files.write(Paths.get(exerciseFile), exercises);
        Files.write(Paths.get(answerFile), answers);

        Main.main(new String[]{"-e", exerciseFile, "-a", answerFile});

        List<String> gradeData = Files.readAllLines(Paths.get("Grade.txt"));
        System.out.println("预期结果: [Correct: 3 [1, 2, 3], Wrong: 0 []], 实际结果: " + gradeData);
        assertTrue(gradeData.contains("Correct: 3 [1, 2, 3]"), "检查答案结果错误");
        assertTrue(gradeData.contains("Wrong: 0 []"), "检查答案结果错误");
    }

}
