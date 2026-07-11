package com.example.privacyawarepermissionsystem;

/**
 * PrivacyRiskAnalyzer analyzes application permissions
 * and generates privacy-related information.
 */
public class PrivacyRiskAnalyzer {

    /**
     * Calculate privacy score.
     * The score starts at 100 and decreases
     * according to sensitive permissions.
     */
    public static int calculatePrivacyScore(String permissions) {

        if (permissions == null) {
            return 100;
        }

        int score = 100;

        if (permissions.contains("CAMERA")) {
            score -= 20;
        }

        if (permissions.contains("ACCESS_FINE_LOCATION")) {
            score -= 25;
        }

        if (permissions.contains("ACCESS_COARSE_LOCATION")) {
            score -= 15;
        }

        if (permissions.contains("READ_CONTACTS")) {
            score -= 20;
        }

        if (permissions.contains("WRITE_CONTACTS")) {
            score -= 20;
        }

        if (permissions.contains("SMS")) {
            score -= 30;
        }

        if (permissions.contains("PHONE")) {
            score -= 20;
        }

        if (permissions.contains("RECORD_AUDIO")) {
            score -= 20;
        }

        if (permissions.contains("STORAGE")) {
            score -= 10;
        }

        if (permissions.contains("CALENDAR")) {
            score -= 10;
        }

        if (score < 0) {
            score = 0;
        }

        return score;

    }

    /**
     * Convert score into risk level.
     */
    public static String calculateRiskLevel(int score) {

        if (score >= 80) {
            return "Low";
        }

        if (score >= 50) {
            return "Medium";
        }

        return "High";

    }

    /**
     * Count sensitive permissions.
     */
    public static int countSensitivePermissions(String permissions) {

        if (permissions == null) {
            return 0;
        }

        int count = 0;

        String[] keywords = {

                "CAMERA",
                "LOCATION",
                "CONTACTS",
                "SMS",
                "PHONE",
                "RECORD_AUDIO",
                "STORAGE",
                "CALENDAR"

        };

        for (String keyword : keywords) {

            if (permissions.contains(keyword)) {

                count++;

            }

        }

        return count;

    }

    /**
     * Generate privacy summary.
     */
    public static String generateSummary(int score) {

        if (score >= 80) {

            return "This application requests very few sensitive permissions.";

        }

        if (score >= 50) {

            return "This application requests several sensitive permissions.";

        }

        return "This application requests many sensitive permissions and should be reviewed carefully.";

    }

}