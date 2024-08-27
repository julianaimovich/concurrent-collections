import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    static String letters = "abc";
    static int length = 100_000;
    static int textsCount = 10000;
    static char a = 'a';
    static char b = 'b';
    static char c = 'c';
    static ArrayBlockingQueue<String> firstQueue = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> secondQueue = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> thirdQueue = new ArrayBlockingQueue<>(100);


    public static void main(String[] args) {

        new Thread(() -> {
            for (int i = 0; i < textsCount; i++) {
                try {
                    firstQueue.put(generateText(letters, length));
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < textsCount; i++) {
                try {
                    secondQueue.put(generateText(letters, length));
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < textsCount; i++) {
                try {
                    thirdQueue.put(generateText(letters, length));
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> getResults(a, firstQueue)).start();
        new Thread(() -> getResults(b, secondQueue)).start();
        new Thread(() -> getResults(c, thirdQueue)).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void getResults(char letter, ArrayBlockingQueue<String> queue) {
        List<Integer> letterCount = new ArrayList<>();

        for (int i = 0; i < textsCount; i++) {
            try {
                String text = queue.take();
                int repeatCount = (int) text.chars().filter(ch -> ch == letter).count();
                letterCount.add(repeatCount);
            } catch (InterruptedException e) {
                return;
            }
        }

        System.out.println("Максимальное количество символов '" + letter + "' в тексте: " + Collections.max(letterCount));
    }
}