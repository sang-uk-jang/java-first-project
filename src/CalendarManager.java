import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class CalendarManager {
    private static final DateTimeFormatter TIME_FORMAT =  DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public boolean isValidYear(int year) {
        return year >= 2020 && year <= 2030;
    }

    public boolean isLeapYear(int year) {
        if (!isValidYear(year)) {
            return false;
        }
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }

    public boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }

    public int getDaysInMonth(int year, int month) {
        if (!isValidYear(year) || !isValidMonth(month)) {
            return -1;
        }
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public boolean isValidDay(int year, int month, int day) {
        if (!isValidYear(year) || !isValidMonth(month)) {
            return false;
        }
        int maxDay = getDaysInMonth(year, month);

        return day >= 1 && day <= maxDay;
    }

    public boolean isValidDate(int year, int month, int day) {
        return isValidYear(year) &&
                isValidMonth(month) &&
                isValidDay(year, month, day);
    }

    public boolean isValidDate(String date) {
        //날짜 검증 테스트 필요(여러번 해보면서 함수가 제대로 동작하는지 확인해주세요)
        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("uuuu-MM-dd")
                            .withResolverStyle(ResolverStyle.STRICT);

            LocalDate parsedDate = LocalDate.parse(date, formatter);

            return isValidDate(
                    parsedDate.getYear(),
                    parsedDate.getMonthValue(),
                    parsedDate.getDayOfMonth());

        } catch (Exception e) {
            return false;
        }
    }
    public boolean isValidTime (String time){
        try{
            LocalTime.parse(time, TIME_FORMAT);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public int getFirstDayOfMonth(int year, int month){
        if(!isValidYear(year) || !isValidMonth(month)) {
            return -1;
        }

        DayOfWeek firstDay =
                YearMonth.of(year, month).atDay(1).getDayOfWeek();

        return firstDay.getValue() % 7;

    }

    public boolean isValidDateTime(String date, String time){
        return isValidDate(date) && isValidTime(time);
    }
}
