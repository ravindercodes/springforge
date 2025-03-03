package com.ravindercodes.util;

import com.ravindercodes.constant.CommonConstants;

import java.util.Random;

public class UsernameUtility {

    private static final Random random = new Random();

    public static String generateRandomUsername() {
        StringBuilder username = new StringBuilder(CommonConstants.USERNAME_LENGTH);

        for (int i = 0; i < CommonConstants.USERNAME_LENGTH; i++) {
            int index = random.nextInt(CommonConstants.CHARACTERS.length());
            username.append(CommonConstants.CHARACTERS.charAt(index));
        }

        return username.toString();
    }

}
