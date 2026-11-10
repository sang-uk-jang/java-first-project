import java.time.YearMonth;
import java.time.LocalDate;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UI {
    public UI() {}
    public void showMenu(){
        int year = YearMonth.now().getYear();
        int month = YearMonth.now().getMonthValue();
        int daysInMonth = YearMonth.now().lengthOfMonth();
        LocalDate firstDay = YearMonth.now().atDay(1);
        int startDay = firstDay.getDayOfWeek().getValue() % 7;

        System.out.println("=".repeat(30));
        System.out.printf(" ".repeat(10) + "%d년 %d월\n", year, month);
        System.out.println("=".repeat(30));

        System.out.println(" Sun Mon Tue Wed Thu Fri Sat");

        for (int i = 0; i < startDay; i++) {
            System.out.print("    ");
        }

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = YearMonth.now().atDay(day);

            System.out.printf("%4d", day);

            if ((startDay + day) % 7 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("메뉴 : 1. 일정 생성, 2. 일정 삭제, 3. 일정 수정, 4. 일정 조회");
        System.out.print("입력 : ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice){
            case 1 -> insert();
            case 2 -> delete();
            case 3 -> modify();
            case 4 -> search();
        }
        //다음 함수 호출
    }
    public void printDailyView(int year, int month, int day) {


        System.out.println();
        System.out.println("===========================================");
        System.out.println("               STUDY PLANNER");
        System.out.println("===========================================");

        System.out.printf("             " +"%d년 %d월 %d일\n", year, month, day);

        System.out.println("-------------------------------------------");
        System.out.println("  [일정 기록]");
        System.out.println();
        System.out.println("  No.  과목          공부 시간      상태");
        System.out.println("-------------------------------------------");
    }

    private void search() {
    }

    private void modify() {
        
    }

    private void delete() {
        
    }

    private void insert() {
        
    }
}
