package avanzadas.herramientas.sales_partner.Reportes;

public class Meses {

    private int day;
    private int month;
    private int year;
    private String monthString;

    public Meses(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public Meses(String monthString) {
        this.monthString = monthString;
    }

    public String getMonthString() {
        return monthString;
    }

    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
