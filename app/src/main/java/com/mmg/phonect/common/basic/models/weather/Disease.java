package com.mmg.phonect.common.basic.models.weather;

public class Disease {
    private final String diseaseName;
    private final String diseaseInfo;
    private final int score;


    public Disease(String diseaseName, String diseaseInfo, int score) {
        this.diseaseName = diseaseName;
        this.diseaseInfo = diseaseInfo;
        this.score = score;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getDiseaseInfo() {
        return diseaseInfo;
    }

    public int getScore() {
        return score;
    }
}
