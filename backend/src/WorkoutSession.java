import java.util.ArrayList;
import java.util.List;

public class WorkoutSession{
    private String date;
    private List<ExerciseEntry> exercises;

    public WorkoutSession(String date){
        this.date=date;
        this.exercises=new ArrayList<>();

    }
    
    public void addExercise(ExcerciseEntry e){
        exercises.add(e);   
    }

    public String getDate(){
        return date;
    }

    public List<ExerciseEntry> getExercises(){
        return exercises;
    }
    
    }