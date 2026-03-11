public class ExerciseEntry {
    private String name;
    private int sets;
    private int reps;
    private double weight;

    // constructor
    public ExerciseEntry(String name, int sets, int reps, double weight) {
        this.name = name;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    // getters
    public String getName() {
        return name;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public double getVolume() {
        return sets * reps * weight;
    }

    // setters
    public void setName(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name;
    }

    public void setSets(int sets) {
        if (sets < 0)
            throw new IllegalArgumentException("Sets cannot be negative");
        this.sets = sets;
    }

    public void setReps(int reps) {
        if (reps < 0)
            throw new IllegalArgumentException("Reps cannot be negative");
        this.reps = reps;
    }

    public void setWeight(double weight) {
        if (weight < 0)
            throw new IllegalArgumentException("Weight cannot be negative");
        this.weight = weight;
    }

    public String toString() {
        return name + ": " + sets + " sets x" + reps + " reps @" + weight + " lbs.";
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ExerciseEntry))
            return false;
        ExerciseEntry e = (ExerciseEntry) obj;
        return name.equalsIgnoreCase(e.name);

    }

    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}