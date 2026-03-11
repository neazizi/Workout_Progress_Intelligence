import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<WorkoutSession> allSessions = FileDatabaseManager.loadAllSessions();
        allSessions = sortedByDate(allSessions);

        if (allSessions == null) {
            allSessions = new ArrayList<>();
        }

        System.out.println("=== Workout Progress Intelligence ===");

        // read workout date
        String date = readDate(sc);
        WorkoutSession session = new WorkoutSession(date);

        // read exercises
        boolean moreExercises = true;
        while (moreExercises) {

            String name = readExerciseName(sc);
            int sets = readPositiveInt(sc, "Enter sets: ");
            int reps = readPositiveInt(sc, "Enter reps: ");
            double weight = readPositiveDouble(sc, "Enter weight: ");

            session.addExercise(new ExerciseEntry(name, sets, reps, weight));

            System.out.print("Add another exercise? (yes/no): ");
            moreExercises = sc.nextLine().trim().equalsIgnoreCase("yes");
        }

        // save session to file
        FileDatabaseManager.saveSession(session);
        allSessions.add(session);
        allSessions = sortedByDate(allSessions);

        System.out.println("\nWorkout saved successfully!\n");

        // print all sessions
        for (WorkoutSession s : allSessions) {
            Analytics.printSessionSummary(s);
            System.out.println("-----------------------");
        }

        // plateau analysis
        Map<String, Boolean> plateauResults = Analytics.findPlateaus(allSessions);
        System.out.println("Plateau Analysis:");
        for (String exercise : plateauResults.keySet()) {
            System.out.println(exercise + ": " +
                    (plateauResults.get(exercise) ? "Plateauing" : "Progressing"));
        }

        // progress analysis
        Map<String, Double> progressResults = Analytics.analyzeProgress(allSessions);
        System.out.println("\nProgress Analysis:");
        for (String exercise : progressResults.keySet()) {
            double score = progressResults.get(exercise);
            if (score > 0)
                System.out.println(exercise + ": Progressing");
            else if (score < 0)
                System.out.println(exercise + ": Regressing");
            else
                System.out.println(exercise + ": Stagnant");
        }

        // recommended next weights
        Map<String, Double> nextWeights = Analytics.recommendedNextWeight(allSessions);
        System.out.println("\nRecommended Next Weights:");
        for (String exercise : nextWeights.keySet()) {
            System.out.println(exercise + ": " + nextWeights.get(exercise) + " lbs");
        }

        // compare last two sessions
        if (allSessions.size() >= 2) {
            int size = allSessions.size();
            Map<String, String> comparison = Analytics.compareTwoSessions(allSessions.get(size - 2),
                    allSessions.get(size - 1));

            System.out.println("\nComparison Between Last Two Sessions:");
            for (String exercise : comparison.keySet()) {
                System.out.println(exercise + ": " + comparison.get(exercise));
            }
        }

        sc.close();
    }

    // ===== INPUT VALIDATION METHODS =====

    public static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                sc.nextLine();
                if (value > 0)
                    return value;
            } else {
                sc.nextLine();
            }
            System.out.println("Invalid input. Enter a positive number.");
        }
    }

    public static double readPositiveDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                double value = sc.nextDouble();
                sc.nextLine();
                if (value >= 0)
                    return value;
            } else {
                sc.nextLine();
            }
            System.out.println("Invalid input. Enter a non-negative number.");
        }
    }

    public static String readExerciseName(Scanner sc) {
        while (true) {
            System.out.print("Enter exercise name: ");
            String name = sc.nextLine().trim().toLowerCase();
            if (!name.isEmpty())
                return name;
            System.out.println("Exercise name cannot be empty.");
        }
    }

    public static String readDate(Scanner sc) {
        while (true) {
            System.out.print("Enter workout date (YYYY-MM-DD): ");
            String date = sc.nextLine().trim();
            if (date.matches("\\d{4}-\\d{2}-\\d{2}"))
                return date;
            System.out.println("Invalid date format.");
        }
    }

    public static List<WorkoutSession> sortedByDate(List<WorkoutSession> sessions) {
        sessions.sort((s1, s2) -> {
            LocalDate dateA = LocalDate.parse(s1.getDate().trim());
            LocalDate dateB = LocalDate.parse(s2.getDate().trim());
            return dateA.compareTo(dateB);
        });
        return sessions;
    }

}
