public class ExerciseEntry{
    private String name;
    private int sets;
    private int reps;
    private double weight;
    //constructor
    public ExerciseEntry(String name, int sets, int reps, double weight){
        this.name=name;
        this.sets=sets;
        this.reps=reps;
        this.weight=weight;
    }
    //getters
    public String getName(){
        return name;
    }
    public int getSets(){
        return sets;
    }
    public int getReps(){
        return reps;
    }
    public double getWeight(){
        return weight;
    }

    //setters
    public void setName(String name){
        this.name=name;
    }
    public void setSets(int sets){
        this.sets=sets;
    }
    public void setReps(int reps){
        this.reps=reps;
    }
    public void setWeight(double weight){
        this.weight=weight;
    }

    public String toString(){
        return name +": " +sets+ " sets x" +reps+ " reps @" +weight+ " lbs.";
    }
}