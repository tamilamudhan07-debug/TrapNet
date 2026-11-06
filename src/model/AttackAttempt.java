package model;

import java.time.LocalDateTime;

public class AttackAttempt {
    private final String ipAddress;
    private final String usernameTried;
    private final String passwordTried;
    private final LocalDateTime attemptTime;

    public AttackAttempt(String ipAddress, String usernameTried, String passwordTried) {
        this.ipAddress = ipAddress;
        this.usernameTried = usernameTried;
        this.passwordTried = passwordTried;
        this.attemptTime = LocalDateTime.now();
    }

    public String getIpAddress() { return ipAddress; }
    public String getUsernameTried() { return usernameTried; }
    public String getPasswordTried() { return passwordTried; }
    public LocalDateTime getAttemptTime() { return attemptTime; }
}