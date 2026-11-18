import java.util.Scanner;

public class StudyPlanner {

    private final Scanner scan;
    public final CalendarManager CM = new CalendarManager();
    public final fileIO IO = new fileIO();
    private final UI ui;

    public StudyPlanner(Scanner scan, UI ui) {
        this.scan = scan;
        this.ui = ui;
    }

    public void addRecord() {
        int year = 0;
        int month = 0;
        int day = 0;
        boolean check = false;

        while (!check) {
            System.out.print("일정을 생성할 날짜를 입력하세요(예시 2026-11-16) : ");
            String input = scan.nextLine().trim(); // nextLine()으로 통일
            check = CM.isValidDate(input);
            if (!check) {
                System.out.println("올바르지 않은 날짜 형식입니다. 다시 입력해주세요.");
                continue;
            }
            String[] result = input.split("-");
            year = Integer.parseInt(result[0]);
            month = Integer.parseInt(result[1]);
            day = Integer.parseInt(result[2]);
        }

        System.out.print("일정 기록을 입력하세요 : ");
        String content = scan.nextLine(); // 이제 정상적으로 입력됨

        CalFile c1 = new CalFile(year, month, day, content);

        while (true) {
            System.out.print("과목을 입력하세요 : ");
            String title = scan.nextLine();

            System.out.print("공부 시간을 입력하세요(분) : ");
            int minute = Integer.parseInt(scan.nextLine().trim()); // 버퍼 문제 방지를 위해 nextLine 후 파싱

            System.out.print("목표를 달성했는지 Y/N로 입력해주세요 : ");
            String goal = scan.nextLine().trim();

            System.out.print("계속 작성은 Y, 작성 중단은 N을 입력해주세요 : ");
            String addCheck = scan.nextLine().trim();

            Record record = new Record(c1.count++, title, minute, goal);
            c1.DoList.add(record);

            if (addCheck.equalsIgnoreCase("N")) {
                break;
            }
        }

        boolean writeCheck = IO.writeRecords(c1);
        if (writeCheck) {
            System.out.println("저장 완료");
        } else {
            System.out.println("저장 실패, 다시 입력해주세요");
        }
    }

    public void findByDate() {
        int year = 0;
        int month = 0;
        int day = 0;
        boolean check=false;
        while(!check){
            System.out.print("일정을 확인할 날짜를 입력하세요(예시 2026-11-16) : ");
            String input = scan.nextLine().trim();
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
        if(findCalFile == null){
            return;
        }
        ui.printDailyView(findCalFile);
    }

    public void updateRecord() {
        int year = 0;
        int month = 0;
        int day = 0;
        boolean check=false;
        while(!check){
            System.out.print("일정을 수정할 날짜를 입력하세요(예시 2026-11-16) : ");
            String input = scan.nextLine().trim();
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
        int choice = Integer.parseInt(scan.nextLine().trim());
        if(choice == 0){
            System.out.print("수정할 일정 기록을 입력하세요 : ");
            c1.setContent(scan.nextLine());
        }
        else {
            int index = choice - 1;
            if (index < 0 || index >= c1.DoList.size()) {
                System.out.println("해당 일정을 찾을 수 없습니다.");
                return;
            }
            Record modifyRecord = c1.DoList.get(index);
            System.out.print("과목을 입력하세요 : ");
            String title = scan.nextLine();
            System.out.print("공부 시간을 입력하세요 : ");
            int minute = Integer.parseInt(scan.nextLine().trim());
            System.out.print("목표를 달성했는지 Y/N로 입력해주세요 : ");
            String goal = scan.nextLine().trim();
            modifyRecord.setTitle(title);
            modifyRecord.setMiniute(minute);
            modifyRecord.setCheck(goal);
            c1.DoList.set(index, modifyRecord);
        }

        ui.printDailyView(c1);
        System.out.print("이렇게 수정하시겠습니까?(Y/N) : ");
        String modifyInput = scan.nextLine().trim();
        if(modifyInput.equalsIgnoreCase("Y")){
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

    public void deleteRecord() {
        int year = 0;
        int month = 0;
        int day = 0;
        //String content = null;
        boolean check = false;
        CalFile dayFile = null;
        while (!check) {
            System.out.print("일정을 삭제할 날짜를 입력하세요(예시 2026-11-16) : ");
            //scan = new Scanner(System.in);
            String input = scan.nextLine().trim();
            check = CM.isValidDate(input);
            if (!check) {
                System.out.println("짤못된 날짜입니다.다시 입력하세요. ");
                continue;
            }
            String[] result = input.split("-");
            year = Integer.parseInt(result[0]);
            month = Integer.parseInt(result[1]);
            day = Integer.parseInt(result[2]);

            dayFile = fileIO.fileRead(year,month,day);
            if(dayFile == null){
                System.out.println("해당날짜에 저장된 일정이 없습니다");
                check = false;
                continue;
            }

        }

        System.out.print("전체를 삭제할지 일부만 삭제할지 선택하세요(All/Selected) : ");

        String userInput = scan.nextLine().trim();
        if (userInput.equals("All")) {
            //ui.printDailyView(IO.fileRead(year, month, day));
//             dayFile = fileIO.fileRead(year,month,day);
//            if(dayFile == null){
//                System.out.println("해당날짜에 저장된 일정이 없습니다");
//                return;
//            }
            ui.printDailyView(dayFile);
            System.out.print("진짜 삭제 하시겠습니까??(Y/N) : ");
            String lastInput = scan.nextLine().trim();
            if (lastInput.equalsIgnoreCase("Y")) {
                IO.deleteFile(year, month, day);
                System.out.println("삭제되었습니다.");
            } else
                System.out.println("삭제를 취소합니다.");
        } else if (userInput.equals("Selected")){
//            dayFile = fileIO.fileRead(year,month,day);
//            if(dayFile == null) {
//                System.out.println("해당날짜에 저장된 일정이 없습니다");
//                return;
//            }
            ui.printDailyView(dayFile);
            //ui.printDailyView(IO.fileRead(year, month, day));
            System.out.print("삭제할 과목의 아이디를 입력해주세요 : ");
            int id = Integer.parseInt(scan.nextLine().trim());
            Record deleteRecord = IO.findRecord(year, month, day, id);
            if(deleteRecord == null) {
                System.out.println("해당 일정을 찾을 수 없습니다.");
                return;
            }
            System.out.println(deleteRecord);
            //deleteRecord.toString();
            System.out.print("위의 과목을 삭제하겠습니까?(Y/N) : ");
            String rowInput = scan.nextLine().trim();
            if (rowInput.equalsIgnoreCase("Y")) {
                boolean deleteCheck = fileIO.deleterow(year, month, day, id);
                if (deleteCheck) {
                    System.out.println("삭제 성공");
                } else {
                    System.out.println("삭제 실패, 삭제를 다시 해주세요.");
                }
            } else {
                System.out.println("삭제를 취소합니다.");
            }
            }else {
            System.out.println("잘못된 선택입니다.");
        }
    }
}
