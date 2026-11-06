package model;

public class Attacker {
    private int attackerId;
    private String ipAddress;
    private int totalAttempts;
    private boolean blocked;

    public Attacker(int attackerId, String ipAddress, int totalAttempts, boolean blocked) {
        this.attackerId = attackerId;
        this.ipAddress = ipAddress;
        this.totalAttempts = totalAttempts;
        this.blocked = blocked;
    }

    public int getAttackerId() { return attackerId; }
    public String getIpAddress() { return ipAddress; }
    public int getTotalAttempts() { return totalAttempts; }
    public boolean isBlocked() { return blocked; }
}