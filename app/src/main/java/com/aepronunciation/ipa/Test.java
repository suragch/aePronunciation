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
    private String wrong;

    // constructor
    public Test() {
        ;
    }

    // getters
    public long getId() {
        return id;
    }

    public String getUserName() {
        return username;
    }

    public long getDate() {
        return date;
    }

    public long getTimeLength() {
        return timelength;
    }

    public SoundMode getMode() {
        return mode;
    }

    public int getScore() {
        return score;
    }

    public String getCorrectAnswers() {
        return correctAnswers;
    }

    public String getUserAnswers() {
        return userAnswers;
    }

    public String getWrong() {
        return wrong;
    }

    // setters
    public void setId(long id) {
        this.id = id;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setTimeLength (long timelength) {
        this.timelength = timelength;
    }

    public void setMode(SoundMode mode) {
        this.mode = mode;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCorrectAnswers(String correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setUserAnswers(String userAnswers) {
        this.userAnswers = userAnswers;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }

}