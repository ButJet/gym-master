import java.util.Objects;

public class TimeOfDay implements Comparable<TimeOfDay> {
    private final int hours; 
    private final int minutes;

    public TimeOfDay(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() { return hours; }
    public int getMinutes() { return minutes; }

    @Override
    public int compareTo(TimeOfDay other) {
        int thisTotalMinutes = this.hours * 60 + this.minutes;
        int otherTotalMinutes = other.getHours() * 60 + other.getMinutes();
        return Integer.compare(thisTotalMinutes, otherTotalMinutes);
    }

    // ИСПРАВЛЕНИЕ: Добавлен метод equals для корректной работы HashMap
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeOfDay timeOfDay = (TimeOfDay) o;
        return hours == timeOfDay.hours && minutes == timeOfDay.minutes;
    }

    // ИСПРАВЛЕНИЕ: Добавлен метод hashCode для корректной работы HashMap
    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }
}
