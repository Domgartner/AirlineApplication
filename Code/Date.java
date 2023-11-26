package Code;

public class Date {
    private String day;
    private String month;
    private String year;

    public Date(String d, String m, String y) {
        day = d;
        month = m;
        year = y;
    }

    public String getFormattedDate() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
