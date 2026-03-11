import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Analytics {
    public static double calculateTotalVolume(WorkoutSession session) {
        double total = 0.0;
        for (ExerciseEntry e : session.getExercises()) {
            total += e.getVolume();
        }
        return total;
    }

    public static void printSessionSummary(WorkoutSession session) {
        System.out.println("Workout Date: " + session.getDate());
        for (ExerciseEntry e : session.getExercises()) {
            System.out.println(e);
        }
        System.out.println("Total Volume: " + calculateTotalVolume(session));

    }

    private static double calculateExerciseVolume(ExerciseEntry e) {
        return e.getVolume();
    }

    public static Map<String, Boolean> findPlateaus(List<WorkoutSession> sessions) {

        Map<String, Boolean> result = new HashMap<>();
        if (sessions.size() < 2)
            return result;
        List<WorkoutSession> sortedSessions = new ArrayList<>(sessions);
        sortedSessions.sort((s1, s2) -> LocalDate.parse(s1.getDate()).compareTo(LocalDate.parse(s2.getDate())));
        WorkoutSession last = sortedSessions.get(sortedSessions.size() - 1);
        WorkoutSession prev = sortedSessions.get(sortedSessions.size() - 2);

        for (ExerciseEntry ePrev : prev.getExercises()) {
            String name = ePrev.getName().trim().toLowerCase();
            ExerciseEntry eLast = last.getExerciseByName(name);

            if (eLast != null) { // Only compare if exercise exists in the last session
                boolean plateau = eLast.getVolume() <= ePrev.getVolume();
                result.put(name, plateau);
            }
        }
        return result;
    }

    public static Map<String, Double> analyzeProgress(List<WorkoutSession> sessions) {
        Map<String, Double> result = new HashMap<>();
        if (sessions.size() < 2)
            return result;

        List<WorkoutSession> sorted = new ArrayList<>(sessions);
        sorted.sort((s1, s2) -> LocalDate.parse(s1.getDate()).compareTo(LocalDate.parse(s2.getDate())));

        WorkoutSession last = sessions.get(sessions.size() - 1);
        WorkoutSession prev = sessions.get(sessions.size() - 2);

        for (ExerciseEntry ePrev : prev.getExercises()) {
            String name = ePrev.getName().trim().toLowerCase();
            ExerciseEntry eLast = last.getExerciseByName(name);

            if (eLast != null) {
                double diff = eLast.getVolume() - ePrev.getVolume();
                result.put(name, diff);
            }
        }
        return result;
    }

    public static Map<String, Double> recommendedNextWeight(List<WorkoutSession> sessions) {
        Map<String, Double> nextWeights = new HashMap<>();
        if (sessions.size() < 2)
            return nextWeights;

        List<WorkoutSession> sorted = new ArrayList<>(sessions);
        sorted.sort((s1, s2) -> LocalDate.parse(s1.getDate()).compareTo(LocalDate.parse(s2.getDate())));

        WorkoutSession last = sessions.get(sessions.size() - 1);
        WorkoutSession prev = sessions.get(sessions.size() - 2);

        for (ExerciseEntry ePrev : prev.getExercises()) {
            String name = ePrev.getName().trim().toLowerCase();
            ExerciseEntry eLast = last.getExerciseByName(name);

            if (eLast != null) {
                double newWeight = eLast.getWeight();
                if (eLast.getVolume() > ePrev.getVolume()) {
                    newWeight += 2.5; // small increment if progressed
                } else if (eLast.getVolume() < ePrev.getVolume()) {
                    newWeight -= 2.5; // small decrease if regressed
                }
                nextWeights.put(name, newWeight);
            }
        }
        return nextWeights;
    }

    public static Map<Integer, Double> totalWeeklyVolume(List<WorkoutSession> sessions) {
        Map<Integer, Double> weeklyVolume = new HashMap<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (WorkoutSession session : sessions) {
            LocalDate date = LocalDate.parse(session.getDate());
            int weekNumber = date.get(weekFields.weekOfWeekBasedYear());

            double volume = calculateTotalVolume(session);
            weeklyVolume.put(weekNumber, weeklyVolume.getOrDefault(weekNumber, 0.0) + volume);

        }
        return weeklyVolume;
    }

    public static Map<String, String> compareTwoSessions(WorkoutSession s1, WorkoutSession s2) {
        Map<String, String> comparison = new HashMap<>();
        for (ExerciseEntry e1 : s1.getExercises()) {
            String name = e1.getName();
            ExerciseEntry e2 = s2.getExerciseByName(name);
            if (e2 == null) {
                comparison.put(name, "not performed in session 2");
                continue;
            }
            double diff = e2.getVolume() - e1.getVolume();
            if (diff > 0) {
                comparison.put(name, "improved by " + diff + " total volume");
            } else if (diff < 0) {
                comparison.put(name, "regressed by " + (-diff) + " total volume");
            } else {
                comparison.put(name, "no change in total volume");
            }

        }
        return comparison;

    }

    public static double averageWeight(String exerciseName, List<WorkoutSession> sessions) {
        double totalWeight = 0.0;
        int count = 0;

        for (WorkoutSession session : sessions) {
            for (ExerciseEntry e : session.getExercises()) {
                if (e.getName().equalsIgnoreCase(exerciseName)) {
                    totalWeight += e.getWeight();
                    count++;
                }
            }
        }
        return (count > 0) ? totalWeight / count : 0.0;

    }

    public static double maxWeight(String exerciseName, List<WorkoutSession> sessions) {
        double max = 0.0;

        for (WorkoutSession session : sessions) {
            for (ExerciseEntry e : session.getExercises()) {
                if (e.getName().equalsIgnoreCase(exerciseName)) {
                    if (e.getWeight() > max) {
                        max = e.getWeight();
                    }

                }
            }
        }
        return max;

    }

    public static WorkoutSession leastIntenseSession(List<WorkoutSession> sessions) {
        if (sessions.isEmpty())
            return null;
        WorkoutSession least = sessions.get(0);
        double minVolume = calculateTotalVolume(least);
        for (WorkoutSession session : sessions) {
            double volume = calculateTotalVolume(session);
            if (volume < minVolume) {
                minVolume = volume;
                least = session;
            }

        }
        return least;
    }

    public static List<Double> exerciseVolumeTrend(String exerciseName, List<WorkoutSession> sessions) {
        List<Double> trend = new ArrayList<>();

        for (WorkoutSession session : sessions) {
            ExerciseEntry e = session.getExerciseByName(exerciseName);
            if (e != null) {
                trend.add(e.getVolume());
            } else {
                trend.add(0.0);
            }
        }
        return trend;
    }

    public static Map<Integer, String> weeklyProgressSummary(List<WorkoutSession> sessions) {
        Map<Integer, Double> weeklyVolume = totalWeeklyVolume(sessions);
        Map<Integer, String> summary = new HashMap();
        List<Integer> weeks = new ArrayList<>(weeklyVolume.keySet());
        Collections.sort(weeks);

        double preVolume = -1;

        for (int week : weeks) {
            double currentVolume = weeklyVolume.get(week);
            if (preVolume < 0) {
                summary.put(week, "No previous week to compare.");
            } else if (currentVolume > preVolume) {
                summary.put(week, "Increased volume by " + (currentVolume - preVolume));
            } else if (currentVolume < preVolume) {
                summary.put(week, "Decreased volume by " + (preVolume - currentVolume));
            } else {
                summary.put(week, "No change in volume.");
            }
            preVolume = currentVolume;
        }
        return summary;

    }
}