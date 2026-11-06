# TrapNet

**A Honeypot-Based Intrusion Detection and Attack Analysis System**

TrapNet is a decoy admin login that attracts attackers, records everything they try, detects brute-force and SQL injection attempts, and blocks the attacker automatically. A separate secure admin dashboard shows what happened.

## Features

- **Decoy admin login** that captures attacker input (IP, username, password, timestamp) without revealing detection
- **Rule-based detection** (OOP detector classes):
  - Brute force: 5 or more attempts within 60 seconds
  - SQL injection: pattern matching (for example `' OR 1=1 --`)
- **Automatic IP blocking**, and retries from blocked IPs are still logged
- **Normalized MySQL database** with triggers that automate attempt counters and blocking
- **Secure admin login** with salted SHA-256 password hashing and lockout after 3 failed attempts
- **Admin dashboard** with live stats (total attempts, attackers seen, IPs blocked, alerts raised) and tabs for top attackers, most-tried passwords and recent alerts
- **Attack simulator** ("Simulate attack" button) that generates random IPs with both attack types

## Screenshots

| Admin login | Top attackers |
|---|---|
| ![Admin login](screenshots/WhatsApp%20Image%202026-11-03%20at%206.48.09%20PM.jpeg) | ![Top attackers](screenshots/WhatsApp%20Image%202026-11-03%20at%206.49.02%20PM.jpeg) |

| Most-tried passwords | Recent alerts |
|---|---|
| ![Most-tried passwords](screenshots/WhatsApp%20Image%202026-11-03%20at%206.49.19%20PM.jpeg) | ![Recent alerts](screenshots/WhatsApp%20Image%202026-11-03%20at%206.49.49%20PM.jpeg) |

## Tech Stack

Java 21, Swing with FlatLaf (GUI), JDBC, MySQL 8, MySQL Connector/J (jar in `lib`), IntelliJ IDEA

## How to Run

1. Install Java 21 and MySQL 8.
2. Run `Schema.sql` in MySQL Workbench to create the `trapnet` database, tables, views and triggers.
3. In `DBConnection.java`, set your own MySQL URL, username and password (replace `YOUR_PASSWORD`).
4. Open the project in IntelliJ IDEA and add the jars in `lib` (MySQL Connector/J and FlatLaf) as libraries if they aren't picked up.
5. Open `SetupAdmin.java`, replace `YOUR_PASSWORD` with a password of your choice, and run it once to create the admin account (username: `admin`).
6. Run `Main` to start the honeypot (the decoy login attackers see).
7. Run `AdminMain` to start the secure admin login and dashboard. Use the "Simulate attack" button to generate sample attacks.

## Other Files

`TestBlock`, `TestConnection`, `TestDAO` and `TestDetectors` are small programs for checking blocking, the database connection, the DAO layer and the detectors.

## Scope and Limitations

- Runs on a single machine, so attacker IPs are simulated
- No live network traffic capture and no machine learning
- Detects two attack types: brute force and SQL injection
