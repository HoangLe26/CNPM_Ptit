package model;

public class MatchDetail {
    private int matchId;
    private String opponentName;
    private float result;
    private int eloChange;

    public MatchDetail() {}

    public MatchDetail(int matchId, String opponentName, float result, int eloChange) {
        this.matchId = matchId;
        this.opponentName = opponentName;
        this.result = result;
        this.eloChange = eloChange;
    }

    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public String getOpponentName() { return opponentName; }
    public void setOpponentName(String opponentName) { this.opponentName = opponentName; }

    public float getResult() { return result; }
    public void setResult(float result) { this.result = result; }

    public int getEloChange() { return eloChange; }
    public void setEloChange(int eloChange) { this.eloChange = eloChange; }
}
