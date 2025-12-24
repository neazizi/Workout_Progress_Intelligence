import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Analytics{
    public static double calculateTotalVolume(WorkoutSession session){
        double total=0.0;
        for(ExerciseEntry e: session.getExercises()){
            total+=e.getReps() * e.getSets() * e.getWeight();
        }
        return total;
    }
    public static void printSessionSummary(WorkoutSession session){
        System.out.println("Workout Date: "+session.getDate());
        for(ExerciseEntry e: session.getExercises()){
            System.out.println(e);
        }
        System.out.println("Total Volume: "+ calculateTotalVolume(session));

    }
    public static Map<String, Boolean> findPlateaus(List<WorkoutSession> sessions){
        Map<String, Double> lastWeight=new HashMap<>();
        Map<String, Boolean> plateau=new HashMap<>();
        for(WorkoutSession session: sessions){
            for(ExerciseEntry e: session.getExercises()){
                String name=e.getName();
                double weight=e.getWeight();
                if(lastWeight.containsKey(name)){
                    if(weight<=lastWeight.get(name)){
                        plateau.put(name, true);
                    } else{
                        plateau.put(name, false);
                        lastWeight.put(name, weight);
                    }
                }
                else{
                    lasWeight.put(name, weight);
                    plateau.put(name, false);
                }


            }
        }
        return plateau;
    }



}