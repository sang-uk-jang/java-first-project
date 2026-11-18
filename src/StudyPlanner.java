import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudyPlanner {

    public StudyPlanner() {}
    public static Scanner scan = new Scanner(System.in);
    public CalendarManager CM = new CalendarManager();
    public fileIO IO = new fileIO();
    public static UI ui = new UI();

    public void addRecord() {
        //scan.nextLine();
        int year = 0;
        int month = 0;
        int day = 0;
        boolean check=false;
        while(!check){
            System.out.print("일정을 생성할 날짜를 입력하세요(예시 2026-11-16) : ");
            String input = scan.next();
            check = CM.isValidDate(input);
            if(!check){
                continue;
            }
            String[] result = input.split("-");
            year = Integer.parseInt(result[0]);
            month = Integer.parseInt(result[1]);
            day = Integer.parseInt(result[2]);
        }
        System.out.print("일정 기록을 입력하세요 : ");
        String content = scan.nextLine();
//        while(scan.hasNext()){
//            content += scan.nextLine();
//            content += "\n";
//        }
        //기록이 여러줄일 경우 입력받는 방법 생각해보기

        CalFile c1 = new CalFile(year, month, day, content);

        while(true){
            scan.nextLine();
            System.out.print("과목을 입력하세요 : ");
            String title = scan.nextLine();
            System.out.print("공부 시간을 입력하세요(분) : ");
            int minute = scan.nextInt();
            System.out.print("목표를 달성했는지 Y/N로 입력해주세요 : ");
            String goal = scan.next();
            System.out.print("계속 작성은 Y, 작성 중단은 N을 입력해주세요 : ");
            String addCheck = scan.next();
            Record record = new Record(c1.count++, title, minute, goal);
            c1.DoList.add(record);
            if(addCheck.equals("N")){
                break;
            }
        }
        boolean writeCheck = IO.writeRecords(c1);
        if(writeCheck){
            System.out.println("저장 완료");
        }else {
            System.out.println("저장 실패, 다시 입력해주세요");
        }
    }

    public void findByDate() {
        int year = 0;
        int month = 0;
        int day = 0;
        boolean check=false;
        while(check){
            System.out.print("일정을 확인할 날짜를 입력하세요(예시 2026-11-16) : ");
            scan = new Scanner(System.in);
            String input = scan.next();
            check = CM.isValidDate(input);
            if(!check){
                continue;
            }
            String[] result = input.split("-");
            year = Integer.parseInt(result[0]);
            month = Integer.parseInt(result[1]);
            day = Integer.parseInt(result[2]);
        }
        CalFile findCalFile = fileIO.fileRead(year, month, day);
        ui.printDailyView(findCalFile);
    }

    public void updateRecord() {
        int year = 0;
        int month = 0;
        int day = 0;
        String content = null;
        boolean check=false;
        while(!check){
            System.out.print("일정을 수정할 날짜를 입력하세요(예시 2026-11-16) : ");
            scan = new Scanner(System.in);
            String input = scan.next();
            check = CM.isValidDate(input);
            if(!check){
                continue;
            }
            String[] result = input.split("-");
            year = Integer.parseInt(result[0]);
            month = Integer.parseInt(result[1]);
            day = Integer.parseInt(result[2]);
        }


        CalFile c1 = fileIO.fileRead(year,month,day);
        if(c1 == null){
            System.out.println("해당 날짜의 일정이 없습니다.");
            return;
        }
        ui.printDailyView(c1);
        System.out.print("수정할 부분을 입력하세요(0 : 일정 기록, 숫자 : 과목, 공부시간, 상태 변경) : ");
        int choice = scan.nextInt();
        if(choice == 0){
            System.out.print("수정할 일정 기록을 입력하세요 : ");
            while(scan.hasNext()){
                content += scan.nextLine();
                content += "\n";
            }
            c1.setContent(content);
        }
        else {
            Record modifyRecord = c1.DoList.get(choice);
            System.out.print("과목을 입력하세요 : ");
            String title = scan.nextLine();
            System.out.print("공부 시간을 입력하세요 : ");
            int minute = scan.nextInt();
            System.out.print("목표를 달성했는지 Y/N로 입력해주세요 : ");
            String goal = scan.next();
            modifyRecord.setTitle(title);
            modifyRecord.setMiniute(minute);
            modifyRecord.setCheck(goal);
            c1.DoList.set(choice, modifyRecord);
            ui.printDailyView(c1);
            System.out.print("이렇게 수정하시겠습니까?(Y/N) : ");
            String modifyInput = scan.next();
            if(modifyInput.equals("Y")){
                boolean writeCheck = IO.writeRecords(c1);
                if(writeCheck){
                    System.out.println("저장 성공");
                }else {
                    System.out.println("수정에 실패했습니다. 다시 수정해주세요.");
                }
            }else {
                System.out.println("수정을 취소합니다.");
            }
        }
    }

    public void deleteRecord() {
        int year = 0;
        int month = 0;
        int day = 0;
        String content = null;
        boolean check = false;
        while (check) {
            System.out.print("일정을 삭제할 날짜를 입력하세요(예시 2026-11-16) : ");
            scan = new Scanner(System.in);
            String input = scan.next();
            check = CM.isValidDate(input);
            if (!check) {
                continue;
            }
            String[] result = input.split("-");
            year = Integer.parseInt(result[0]);
            month = Integer.parseInt(result[1]);
            day = Integer.parseInt(result[2]);
        }

        System.out.print("전체를 삭제할지 일부만 삭제할지 선택하세요(전체/일부) : ");
        String userInput = scan.next();
        if (userInput.equals("전체")) {
            ui.printDailyView(IO.fileRead(year, month, day));
            System.out.print("진짜 삭제 하시겠습니까??(Y/N) : ");
            String lastInput = scan.next();
            if (lastInput.equals("Y")) {
                IO.deleteFile(year, month, day);
            } else
                System.out.println("삭제를 취소합니다.");
        } else {
            ui.printDailyView(IO.fileRead(year, month, day));
            System.out.print("삭제할 과목의 아이디를 입력해주세요 : ");
            int id = scan.nextInt();
            Record deleteRecord = IO.findRecord(year, month, day, id);
            deleteRecord.toString();
            System.out.print("위의 과목을 삭제하겠습니까?(Y/N) : ");
            String rowInput = scan.next();
            if (rowInput.equals("Y")) {
                boolean deleteCheck = fileIO.deleterow(year, month, day, id);
                if (deleteCheck) {
                    System.out.println("삭제 성공");
                } else {
                    System.out.println("삭제 실패, 삭제를 다시 해주세요.");
                }
            } else {
                System.out.println("삭제를 취소합니다.");
            }
        }
    }
}
