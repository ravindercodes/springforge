package com.ravindercodes.util;

public class GitHubOAuthUtility {

    public static String generateEmail(String username) {
        return username.toLowerCase().concat("@github.com");
    }

}
