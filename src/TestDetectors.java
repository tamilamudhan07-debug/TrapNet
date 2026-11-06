import detector.DetectionEngine;
import model.AttackAttempt;

public class TestDetectors {
    public static void main(String[] args) throws Exception {
        DetectionEngine engine = new DetectionEngine();

        String[] guesses = {"admin123", "password", "123456", "admin@123", "qwerty", "letmein"};
        for (String guess : guesses) {
            DetectionEngine.Result r = engine.process(new AttackAttempt("10.0.0.7", "admin", guess));
            System.out.println(guess + " -> " + r);
        }

        DetectionEngine.Result r2 = engine.process(new AttackAttempt("10.0.0.8", "' OR 1=1 --", "x"));
        System.out.println("SQL injection -> " + r2);
    }
}