package model;

public class Round {
    private int id;
    private int roundNum;
    private int seasonId;

    public Round() {}

    public Round(int id, int roundNum, int seasonId) {
        this.id = id;
        this.roundNum = roundNum;
        this.seasonId = seasonId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRoundNum() { return roundNum; }
    public void setRoundNum(int roundNum) { this.roundNum = roundNum; }

    public int getSeasonId() { return seasonId; }
    public void setSeasonId(int seasonId) { this.seasonId = seasonId; }

    @Override
    public String toString() {
        return "Round " + roundNum;
    }
}
