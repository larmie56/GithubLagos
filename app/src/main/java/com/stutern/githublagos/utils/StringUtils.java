package com.stutern.githublagos.utils;

public class StringUtils {

    public static String formatGitHubUrl(String login) {
        String formattedUrl;
        formattedUrl = "https://github.com/" + login;
        return formattedUrl;
    }
}
