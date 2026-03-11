import java.io.*;
import java.util.*;

public class FileDatabaseManager {

    private static final String FILE_PATH = "../../data/sessions.csv"; // adjust path if needed

    // Save a workout session
    public static void saveSession(WorkoutSession session) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (ExerciseEntry e : session.getExercises()) {
                // Format: date,name,sets,reps,weight
                writer.write(session.getDate() + "," + e.getName() + "," + e.getSets() + "," + e.getReps() + ","
                        + e.getWeight());
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Load all workout sessions
    public static List<WorkoutSession> loadAllSessions() {
        List<WorkoutSession> sessions = new ArrayList<>();
        Map<String, WorkoutSession> map = new HashMap<>();

        File file = new File(FILE_PATH);
        if (!file.exists())
            return sessions; // no file yet

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 5)
                    continue;

                String date = parts[0];
                String name = parts[1];
                int sets = Integer.parseInt(parts[2]);
                int reps = Integer.parseInt(parts[3]);
                double weight = Double.parseDouble(parts[4]);

                WorkoutSession session;
                if (map.containsKey(date)) {
                    session = map.get(date);
                } else {
                    session = new WorkoutSession(date);
                    map.put(date, session);
                    sessions.add(session);
                }
                session.addExercise(new ExerciseEntry(name, sets, reps, weight));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return sessions;
    }
}
