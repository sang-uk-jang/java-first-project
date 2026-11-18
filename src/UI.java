import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;

public class UI {
    private final Scanner scanner;
    public final StudyPlanner SP;

    public UI() {
        scanner = new Scanner(System.in);
        SP = new StudyPlanner(scanner, this);
    }
    public void showMenu(){
        while(true){
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
            System.out.println("메뉴 : 1. 일정 생성, 2. 일정 삭제, 3. 일정 수정, 4. 일정 조회, 5. 종료");
            int choice = readMenuChoice();
            switch (choice){
                case 1 -> SP.addRecord();
                case 2 -> SP.deleteRecord();
                case 3 -> SP.updateRecord();
                case 4 -> SP.findByDate();
                case 5 -> {
                    return;
                }
            }
            //다음 함수 호출
        }

    }

    private int readMenuChoice() {
        while (true) {
            System.out.print("입력 : ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= 5) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("1~5 사이의 숫자를 입력해주세요.");
        }
    }

    public void printDailyView(CalFile dayfile) {
        //임시코드
        if(dayfile == null){
            System.out.println("해당 날짜의 일정이 없습니다.");
            return;
        }
        System.out.println();
        System.out.println("===========================================");
        System.out.println("               STUDY PLANNER");
        System.out.println("===========================================");

        System.out.printf("             " +"%d년 %d월 %d일\n", dayfile.year, dayfile.month, dayfile.day);

        System.out.println("-------------------------------------------");
        System.out.println("  [일정 기록]");
        System.out.println("   " + dayfile.content);
        System.out.println();
        System.out.println("No. \t과목    \t공부 시간    \t상태");
        System.out.println("-------------------------------------------");
        for(Record record : dayfile.DoList){
            System.out.println(record);
        }
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
