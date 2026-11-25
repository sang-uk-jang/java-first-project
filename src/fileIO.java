import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class fileIO {

    // 1. 특정 날짜 파일에서 id로 row(Record)를 찾아 삭제 후 파일 다시 쓰기
    public boolean deleterow(int year, int month, int day, int id) {
        CalFile calFile = fileRead(year, month, day);
        if (calFile == null || calFile.DoList == null) {
            return false;
        }

        boolean removed = calFile.DoList.removeIf(record -> record.id == id);
        calFile.count--;
        if (removed) {
            // 개수가 변경되었으므로 파일 전체를 갱신
            return new fileIO().writeRecords(calFile);
        }
        return false;
    }

    // 2. CalFile 객체를 받아 txt 파일에 포맷에 맞춰 작성
    public boolean writeRecords(CalFile dayFile) {
        String dirPath = "res";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs(); // res 폴더가 없으면 생성
        }

        // 파일 이름 형식: "연도 월 일.txt" (예: "res/2026 11 12.txt")
        String fileName = dirPath + "/" + dayFile.year + " " + dayFile.month + " " + dayFile.day + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // 첫 번째 줄: 연도 월 일 개수
            int count = (dayFile.DoList != null) ? dayFile.DoList.size() : 0;
            writer.println(dayFile.year + " " + dayFile.month + " " + dayFile.day + " " + count);

            // 두 번째 줄: content (띄어쓰기가 있어도 그대로 한 줄로 저장)
            writer.println(dayFile.content != null ? dayFile.content : "");

            // 세 번째 줄 이후: Record 목록 작성 (id | title | minute | check 형식)
            if (dayFile.DoList != null) {
                for (Record r : dayFile.DoList) {
                    writer.println(r.id + " | " + r.title + " | " + r.miniute + " | " + r.check);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. 날짜로 파일 찾아서 CalFile 반환하기 (띄어쓰기 포함된 content와 " | " 구분자 안전 처리)
    public CalFile fileRead(int year, int month, int day) {
        CalFile readFile = null;
        try {
            File file = new File("res/" + year + " " + month + " " + day + ".txt");
            Scanner scanner = new Scanner(file);

            if (scanner.hasNextInt()) {
                int fYear = scanner.nextInt();
                int fMonth = scanner.nextInt();
                int fDay = scanner.nextInt();
                int count = scanner.nextInt();

                // 버퍼에 남아있는 줄바꿈 문자 제거
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                // 두 번째 줄: content 읽어오기 (띄어쓰기 포함 전체 라인 보존)
                String content = "";
                if (scanner.hasNextLine()) {
                    content = scanner.nextLine();
                }

                readFile = new CalFile(fYear, fMonth, fDay, content);
                readFile.count = count;

                // 세 번째 줄 이후: Record 목록 읽어오기 (" | " 기준 분리)
                for (int i = 0; i < count && scanner.hasNextLine(); i++) {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty()) {
                        i--; // 빈 줄인 경우 다시 반복
                        continue;
                    }

                    // " | " 기호를 앞뒤 공백 무시하고 분리
                    String[] parts = line.split("\\s*\\|\\s*");
                    if (parts.length >= 4) {
                        int id = Integer.parseInt(parts[0]);
                        String title = parts[1];
                        int minute = Integer.parseInt(parts[2]);
                        String check = parts[3];

                        Record row = new Record(id, title, minute, check);
                        readFile.DoList.add(row);
                    }
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("해당 날짜의 파일이 존재하지 않습니다.");
        }
        return readFile;
    }

    // 4. 파일 삭제 구현
    public void deleteFile(int year, int month, int day) {
        File file = new File("res/" + year + " " + month + " " + day + ".txt");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("파일이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("파일 삭제에 실패했습니다.");
            }
        } else {
            System.out.println("삭제할 파일이 존재하지 않습니다.");
        }
    }

    // 5. 파일에서 특정 Record 찾기 구현
    public Record findRecord(int year, int month, int day, int id) {
        CalFile calFile = fileRead(year, month, day);
        if (calFile != null && calFile.DoList != null) {
            for (Record r : calFile.DoList) {
                if (r.id == id) {
                    return r;
                }
            }
        }
        return null;
    }
}