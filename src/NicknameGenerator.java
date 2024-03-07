import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NicknameGenerator {

    private static final AtomicInteger countLength3 = new AtomicInteger(0);
    private static final AtomicInteger countLength4 = new AtomicInteger(0);
    private static final AtomicInteger countLength5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindromeChecker = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread sameLetterChecker = new Thread(() -> {
            for (String text : texts) {
                if (isMadeOfSameLetters(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread ascendingOrderChecker = new Thread(() -> {
            for (String text : texts) {
                if (isInAscendingOrder(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        palindromeChecker.start();
        sameLetterChecker.start();
        ascendingOrderChecker.start();

        palindromeChecker.join();
        sameLetterChecker.join();
        ascendingOrderChecker.join();

        System.out.println("Красивых слов с длиной 3: " + countLength3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + countLength4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + countLength5.get() + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static void incrementCounter(int length) {
        switch (length) {
            case 3:
                countLength3.incrementAndGet();
                break;
            case 4:
                countLength4.incrementAndGet();
                break;
            case 5:
                countLength5.incrementAndGet();
                break;
        }
    }

    private static boolean isPalindrome(String text) {
        return new StringBuilder(text).reverse().toString().equals(text);
    }

    private static boolean isMadeOfSameLetters(String text) {
        return text.chars().distinct().count() == 1;
    }

    private static boolean isInAscendingOrder(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) >= text.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
