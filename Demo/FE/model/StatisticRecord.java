package model;

public class StatisticRecord {
    private int playerId;
    private String code;
    private String name;
    private String yob;
    private String nationality;
    private int oldElo;
    private int newElo;
    private int eloChange;

    public StatisticRecord() {}

    public StatisticRecord(int playerId, String code, String name, String yob,
                           String nationality, int oldElo, int newElo, int eloChange) {
        this.playerId = playerId;
        this.code = code;
        this.name = name;
        this.yob = yob;
        this.nationality = nationality;
        this.oldElo = oldElo;
        this.newElo = newElo;
        this.eloChange = eloChange;
    }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getYob() { return yob; }
    public void setYob(String yob) { this.yob = yob; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public int getOldElo() { return oldElo; }
    public void setOldElo(int oldElo) { this.oldElo = oldElo; }

    public int getNewElo() { return newElo; }
    public void setNewElo(int newElo) { this.newElo = newElo; }

    public int getEloChange() { return eloChange; }
    public void setEloChange(int eloChange) { this.eloChange = eloChange; }
}
