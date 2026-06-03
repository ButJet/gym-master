public class TimeOfDay implements Comparable<TimeOfDay> {
    private int hours;
    private int minutes;

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
}
