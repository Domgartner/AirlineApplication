package ProjectCode;

public class Date {
    private int day;
    private int month;
    private int year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getDate() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }
}
