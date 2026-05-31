import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(singleTrainingSession, mondaySessions.get(0));

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0)); // 13:00
        assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1)); // 20:00

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> mondayAt13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, mondayAt13.size());
        assertEquals(singleTrainingSession, mondayAt13.get(0));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> mondayAt14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(mondayAt14.isEmpty());
    }

    // НОВЫЙ ТЕСТ 1: Проверка базовой сортировки тренеров по убыванию нагрузки
    @Test
    void testGetCountByCoachesDifferentCounts() {
        Timetable timetable = new Timetable();

        Coach coachIvanov = new Coach("Иванов", "Иван", "Иванович");
        Coach coachPetrov = new Coach("Петров", "Петр", "Петрович");

        Group group = new Group("Акробатика", Age.CHILD, 60);

        // Петров — 2 тренировки (должен быть на 1-м месте)
        timetable.addNewTrainingSession(new TrainingSession(group, coachPetrov, DayOfWeek.MONDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachPetrov, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));

        // Иванов — 1 тренировка (должен быть на 2-м месте)
        timetable.addNewTrainingSession(new TrainingSession(group, coachIvanov, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());

        // 1-е место: Петров (2 занятия)
        assertEquals(coachPetrov, result.get(0).getCoach());
        assertEquals(2, result.get(0).getCount());

        // 2-е место: Иванов (1 занятие)
        assertEquals(coachIvanov, result.get(1).getCoach());
        assertEquals(1, result.get(1).getCount());
    }

    // НОВЫЙ ТЕСТ 2: Ситуация, когда у тренеров одинаковое количество тренировок
    @Test
    void testGetCountByCoachesEqualCounts() {
        Timetable timetable = new Timetable();

        Coach coachIvanov = new Coach("Иванов", "Иван", "Иванович");
        Coach coachPetrov = new Coach("Петров", "Петр", "Петрович");

        Group group = new Group("Растяжка", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coachIvanov, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachPetrov, DayOfWeek.TUESDAY, new TimeOfDay(19, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getCount());
        assertEquals(1, result.get(1).getCount());
    }

    // НОВЫЙ ТЕСТ 3: Поведение метода при полностью пустом расписании
    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Список должен быть пуст, ошибок компиляции или падений быть не должно
        assertTrue(result.isEmpty());
    }
}
