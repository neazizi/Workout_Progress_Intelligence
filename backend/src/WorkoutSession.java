import java.util.ArrayList;
import java.util.List;

public class WorkoutSession {
    private String date;
    private List<ExerciseEntry> exercises;

    public WorkoutSession(String date) {
        this.date = date;
        this.exercises = new ArrayList<>();

    }

    public void addExercise(ExerciseEntry e) {
        ExerciseEntry existing = getExerciseByName(e.getName());
        if (existing == null)
            exercises.add(e);
        else
            System.out.println(e.getName() + " already exists in this session.");
    }

    public boolean removeExercise(String name) {
        ExerciseEntry e = getExerciseByName(name);
        if (e != null) {
            exercises.remove(e);
            return true;
        }
        return false;

    }

    public String getDate() {
        return date;
    }

    public List<ExerciseEntry> getExercises() {
        return exercises;
    }

    public ExerciseEntry getExerciseByName(String name) {
        String normalized = name.trim().toLowerCase();
        for (ExerciseEntry e : exercises) {
            if (e.getName().trim().toLowerCase().equals(normalized)) {
                return e;
            }
        }
        return null;

    }

    public double getTotalVolume() {
        double total = 0;
        for (ExerciseEntry e : exercises)
            total += e.getVolume();
        return total;

    }

    public boolean hasExercise(String name) {
        return getExerciseByName(name) != null;
    }
}