import org.junit.jupiter.api.Test;


import java.util.Random;

import static org.example.bean.Calculate.calculateAnswer;
import static org.example.tool.ArithmeticGenerator.generateExpression;

public class Test1 {
    @Test
    public void test1() {
        // 示例表达式
        String expression = "(1/2 + 2'3/4) * (3+1)";
        String answer = calculateAnswer(expression);
        System.out.println("表达式: " + expression);
        System.out.println("答案: " + answer);
    }
    @Test
    public void test2() {
//        Random rand = new Random();
//        int range = 10; // 范围
//        String expression = generateExpression(rand, range,1);
//        System.out.println("生成的表达式: " + expression);
        for (int i = 0; i < 100; i++) {
            Random rand = new Random();
            int range = 10; // 范围
            String expression = generateExpression(rand, range,2);
            System.out.println("生成的表达式: " + expression);
        }
    }
}
