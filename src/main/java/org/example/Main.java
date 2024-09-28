package org.example;

import java.io.*;
import java.util.*;

import static org.example.bean.Calculate.calculateAnswer;
import static org.example.bean.FileUtil.checkAnswers;
import static org.example.bean.FileUtil.writeToFile;
import static org.example.tool.ArithmeticGenerator.generateExpression;

public class Main {
    // 生成题目
    public static void generateExercises(int n, int range) throws IOException {
        Set<String> exercises = new HashSet<>();
        List<String> answers = new ArrayList<>();

        Random rand = new Random();

        while (exercises.size() < n) {
            String exercise = generateExpression(rand, range,3);
            String answer = calculateAnswer(exercise);

            if (!exercises.contains(exercise)) {
                exercises.add(exercise);
                answers.add(answer);
            }
        }

        writeToFile("Exercises.txt", exercises);
        writeToFile("Answers.txt", answers);
    }


    // 主方法
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            if (args[0].equals("-n")) {
                int n = Integer.parseInt(args[1]);
                int range = Integer.parseInt(args[3]);
                generateExercises(n, range);
            } else if (args[0].equals("-e")) {
                checkAnswers(args[1], args[3]);
            } else {
                printUsage();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
        }
    }

    // 打印帮助信息
    private static void printUsage() {
        System.out.println("Usage: java -jar main.jar -n <num> -r <range>");
        System.out.println("       java -jar main.jar -e <exercisefile> -a <answerfile>");
    }
}