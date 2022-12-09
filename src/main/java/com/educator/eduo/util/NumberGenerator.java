package com.educator.eduo.util;

import java.util.Random;

public class NumberGenerator {

    public static String generateRandomUniqueNumber(int length) {
        return generateRandomNumber(length, true);
    }

    public static String generateRandomNumberWithDuplicated(int length) {
        return generateRandomNumber(length, false);
    }

    public static String generateRandomNumber(int length, boolean unique) {
        if (length <= 0) {
            throw new IllegalArgumentException("1 이상의 길이를 입력하세요.");
        }

        Random random = new Random();
        StringBuilder generated = new StringBuilder();

        while (generated.length() < length) {
            String num = String.valueOf(random.nextInt(10));

            if (unique && contains(generated, num)) {
                continue;
            }

            generated.append(num);
        }

        return generated.toString();
    }

    private static boolean contains(StringBuilder stringBuilder, String target) {
        return stringBuilder.indexOf(target) > -1;
    }

}
