package org.example.bean;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.example.bean.Calculate.calculateAnswer;

public class FileUtil {
    // 写入文件
    public static void writeToFile(String filename, Collection<String> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // 检查答案
    public static void checkAnswers(String exerciseFile, String answerFile) throws IOException {
        List<String> exercises = readFromFile(exerciseFile);
        List<String> answers = readFromFile(answerFile);

        List<Integer> correct = new ArrayList<>();
        List<Integer> wrong = new ArrayList<>();

        for (int i = 0; i < exercises.size(); i++) {
            String correctAnswer = calculateAnswer(exercises.get(i));
            if (correctAnswer.equals(answers.get(i))) {
                correct.add(i + 1);
            } else {
                wrong.add(i + 1);
            }
        }

        writeGrade(correct, wrong);
    }

    // 读取文件
    private static List<String> readFromFile(String filename) throws  IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    // 写入成绩文件
    private static void writeGrade(List<Integer> correct, List<Integer> wrong) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Grade.txt"))) {
            writer.write("Correct: " + correct.size() + " " + correct.toString());
            writer.newLine();
            writer.write("Wrong: " + wrong.size() + " " + wrong.toString());
            writer.newLine();
        }
    }
}
