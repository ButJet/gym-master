import java.util.*;

public class Timetable {
    // Вложенная структура: День недели -> (Время -> Список занятий)
    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> dailyMap = timetable.computeIfAbsent(day, k -> new TreeMap<>());
        List<TrainingSession> sessions = dailyMap.computeIfAbsent(time, k -> new ArrayList<>());

        sessions.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> allDaySessions = new ArrayList<>();
        Map<TimeOfDay, List<TrainingSession>> dailyMap = timetable.get(dayOfWeek);

        if (dailyMap != null) {
            TreeMap<TimeOfDay, List<TrainingSession>> sortedDailyMap = (TreeMap<TimeOfDay, List<TrainingSession>>) dailyMap;
            NavigableSet<TimeOfDay> sortedTimes = sortedDailyMap.navigableKeySet();

            for (TimeOfDay time : sortedTimes) {
                List<TrainingSession> sessions = sortedDailyMap.get(time);
                if (sessions != null) {
                    allDaySessions.addAll(sessions);
                }
            }
        }
        return allDaySessions;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> dailyMap = timetable.get(dayOfWeek);
        if (dailyMap != null) {
            List<TrainingSession> sessions = dailyMap.get(timeOfDay);
            if (sessions != null) {
                return sessions;
            }
        }
        return new ArrayList<>();
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounters = new HashMap<>();

        for (Map<TimeOfDay, List<TrainingSession>> dailyMap : timetable.values()) {
            for (List<TrainingSession> sessions : dailyMap.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCounters.put(coach, coachCounters.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> resultList = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounters.entrySet()) {
            resultList.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(resultList);
        return resultList;
    }
}
// проверка расписания
