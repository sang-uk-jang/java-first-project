public class TestMain {
     static void main(){

        CalendarManager cal = new CalendarManager();
        System.out.println(cal.isValidDate("2024-02-29"));
         System.out.println(cal.isValidDate("2025-02-29"));
         System.out.println(cal.isValidDate("2026-04-31"));
         System.out.println(cal.isValidDate("2026-01-31"));
         System.out.println(cal.isValidDate("2026-13-01"));
         System.out.println(cal.isValidDate("2019-12-01"));
         System.out.println(cal.isValidDate("2031-01-01"));


    }
}
