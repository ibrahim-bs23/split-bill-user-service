package com.brainstation23.skeleton.common.utils;

/**
 * @author Abdur Rahim Nishad
 * @since 1.0.0
 */

import lombok.experimental.UtilityClass;

import java.util.Random;


@UtilityClass
public class RandomGenerator {

    private static final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARACTERS = "@!&";
    private static final String NUMBERS = "0123456789";

    /*public String generateRandomPassword() {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder()
                .withinRange(64, 90)
                .withinRange(97,122)
                .withinRange(37,38)
                .withinRange(42,42)
                .build();
        return pwdGenerator.generate(10);
    }*/

    public String generateRandomPassword() {
        return generatePassword(10);
    }

    public String generatePassword(int length) {
        String combinedCharacters = UPPERCASE_CHARACTERS + LOWERCASE_CHARACTERS + SPECIAL_CHARACTERS + NUMBERS;
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));

        for (int i = 2; i < length; i++) {
            int index = random.nextInt(combinedCharacters.length());
            password.append(combinedCharacters.charAt(index));
        }

        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[index];
            passwordArray[index] = temp;
        }

        return new String(passwordArray);
    }
}
