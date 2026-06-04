import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Timetable {

    // ИСПРАВЛЕНО по 3-й правке: второй уровень — это HashMap для O(1) доступа по времени
    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new HashMap<>()); // Инициализируем обычным HashMap
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (trainingSession == null) return;
        
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }

        daySchedule.get(time).add(trainingSession);
    }

    // ИСПРАВЛЕНО по 2-й и 3-й правке: создаем TreeMap на лету для сортировки "всего дня"
    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        
        // Перекладываем в TreeMap, чтобы вернуть упорядоченную структуру за O(1) от исходной карты дня
        return new TreeMap<>(daySchedule);
    }

    // ИСПРАВЛЕНО по 3-й правке: теперь этот метод работает за чистые O(1) через HashMap!
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        // Быстрый доступ O(1) благодаря HashMap
        return daySchedule.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounters = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            // Используем наш метод, чтобы получить отсортированную карту дня
            TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = getTrainingSessionsForDay(day);
            
            for (List<TrainingSession> sessionsAtTime : daySchedule.values()) {
                for (TrainingSession session : sessionsAtTime) {
                    Coach coach = session.getCoach();
                    if (coach != null) {
                        coachCounters.put(coach, coachCounters.getOrDefault(coach, 0) + 1);
                    }
                }
            }
        }

        List<CounterOfTrainings> workloadList = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounters.entrySet()) {
            workloadList.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(workloadList);
        return workloadList;
    }
}
