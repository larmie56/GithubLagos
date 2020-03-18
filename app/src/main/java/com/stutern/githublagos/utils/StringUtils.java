package com.stutern.githublagos.utils;

public class StringUtils {

    public static String formatGitHubUrl(String login) {
        String formattedUrl;
        formattedUrl = "https://github.com/" + login;
        return formattedUrl;
    }

    public static String buildUrl(String login) {
        String url;
        url = "https://api.github.com/users/" + login;
        return url;
    }

    public static String appendFollowing(int i) {
        String following;
        following = "Following: " + i;
        return following;
    }

    public static String appendFollowers(int i) {
        String followers;
        followers = "Followers: " + i;
        return followers;
    }
}
