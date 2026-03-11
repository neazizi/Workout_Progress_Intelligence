import java.io.FileWriter;
import java.io.IOException;

public class FileStorage {

    private static final String FILE_NAME = "workouts.csv";

    public static void saveSession(WorkoutSession session) {

        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {

            for (ExerciseEntry e : session.getExercises()) {

                double volume = e.getSets() * e.getReps() * e.getWeight();

                writer.write(
                        session.getDate() + "," +
                                e.getName() + "," +
                                e.getSets() + "," +
                                e.getReps() + "," +
                                e.getWeight() + "," +
                                volume + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error saving workout: " + e.getMessage());
        }
    }
}
