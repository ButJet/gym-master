import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        this.timetable = new HashMap<>();
        // Заранее наполняем карту пустыми TreeMap для каждого дня недели
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    // Добавление новой тренировки в расписание
    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        // Если этого времени еще нет в TreeMap, создаем для него новый список
        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }

        // Добавляем тренировку в список для этого времени
        daySchedule.get(time).add(trainingSession);
    }

    // Возвращает все тренировки за день, упорядоченные по времени начала
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> allSessionsForDay = new ArrayList<>();
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        // Используем метод navigableKeySet() для последовательного обхода по времени от утра к вечеру
        for (TimeOfDay time : daySchedule.navigableKeySet()) {
            allSessionsForDay.addAll(daySchedule.get(time));
        }
        return allSessionsForDay;
    }

    // Возвращает все тренировки, начинающиеся в конкретное время конкретного дня
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        // Безопасное извлечение: если на это время ничего нет, возвращаем пустой список
        return daySchedule.getOrDefault(timeOfDay, new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounters = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            List<TrainingSession> daySessions = getTrainingSessionsForDay(day);
            for (TrainingSession session : daySessions) {
                Coach coach = session.getCoach();

                coachCounters.put(coach, coachCounters.getOrDefault(coach, 0) + 1);
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
