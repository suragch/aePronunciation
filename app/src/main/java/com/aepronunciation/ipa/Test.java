package com.aepronunciation.ipa;


public class Test {

    private long id;
    private String username;
    private long date;
    private long timelength;
    private SoundMode mode;
    private int score;
    private String correctAnswers;
    private String userAnswers;
    //private String wrong;

    // constructor
    public Test() {}

    // getters
    long getId() {
        return id;
    }

    String getUserName() {
        return username;
    }

    long getDate() {
        return date;
    }

    long getTimeLength() {
        return timelength;
    }

    SoundMode getMode() {
        return mode;
    }

    int getScore() {
        return score;
    }

    String getCorrectAnswers() {
        return correctAnswers;
    }

    String getUserAnswers() {
        return userAnswers;
    }

    // setters
    void setId(long id) {
        this.id = id;
    }

    void setUserName(String username) {
        this.username = username;
    }

    void setDate(long date) {
        this.date = date;
    }

    void setTimeLength (long timelength) {
        this.timelength = timelength;
    }

    void setMode(SoundMode mode) {
        this.mode = mode;
    }

    void setScore(int score) {
        this.score = score;
    }

    void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    void setUserAnswers(String userAnswers) {
        this.userAnswers = userAnswers;
    }

}