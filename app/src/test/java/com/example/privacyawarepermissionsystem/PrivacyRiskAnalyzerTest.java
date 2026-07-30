package com.example.privacyawarepermissionsystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PrivacyRiskAnalyzerTest {

    @Test
    public void nullPermissionsReturnPerfectScore() {
        assertEquals(
                100,
                PrivacyRiskAnalyzer
                        .calculatePrivacyScore(null));
    }

    @Test
    public void cameraPermissionProducesLowRiskBoundary() {
        int score =
                PrivacyRiskAnalyzer
                        .calculatePrivacyScore(
                                "android.permission.CAMERA");

        assertEquals(80, score);
        assertEquals(
                "Low",
                PrivacyRiskAnalyzer
                        .calculateRiskLevel(score));
    }

    @Test
    public void fineLocationProducesMediumRisk() {
        int score =
                PrivacyRiskAnalyzer
                        .calculatePrivacyScore(
                                "android.permission.ACCESS_FINE_LOCATION");

        assertEquals(75, score);
        assertEquals(
                "Medium",
                PrivacyRiskAnalyzer
                        .calculateRiskLevel(score));
    }

    @Test
    public void manySensitivePermissionsCannotGoBelowZero() {
        String permissions =
                "CAMERA\n" +
                "ACCESS_FINE_LOCATION\n" +
                "ACCESS_COARSE_LOCATION\n" +
                "READ_CONTACTS\n" +
                "WRITE_CONTACTS\n" +
                "READ_SMS\n" +
                "READ_PHONE_STATE\n" +
                "RECORD_AUDIO\n" +
                "READ_EXTERNAL_STORAGE\n" +
                "READ_CALENDAR\n";

        int score =
                PrivacyRiskAnalyzer
                        .calculatePrivacyScore(
                                permissions);

        assertEquals(0, score);
        assertEquals(
                "High",
                PrivacyRiskAnalyzer
                        .calculateRiskLevel(score));
    }

    @Test
    public void categoryCounterAvoidsDuplicateCategories() {
        String permissions =
                "ACCESS_FINE_LOCATION\n" +
                "ACCESS_COARSE_LOCATION\n" +
                "READ_CONTACTS\n" +
                "WRITE_CONTACTS\n" +
                "CAMERA\n";

        assertEquals(
                3,
                PrivacyRiskAnalyzer
                        .countSensitivePermissions(
                                permissions));
    }

    @Test
    public void summaryMatchesRiskRange() {
        assertTrue(
                PrivacyRiskAnalyzer
                        .generateSummary(90)
                        .contains("very few"));

        assertTrue(
                PrivacyRiskAnalyzer
                        .generateSummary(60)
                        .contains("several"));

        assertTrue(
                PrivacyRiskAnalyzer
                        .generateSummary(30)
                        .contains("many"));
    }
}
