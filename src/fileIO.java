import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class fileIO {

    static List<String> fileIn(String dirPath) {
        File dir = new File(dirPath);
        List<String> filePathList = new LinkedList<>();

        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();

            if (files != null) {
                int fileCount = 0;
                for (File file : files) {
                    if (file.isFile()) {
                        fileCount++;
                        filePathList.add(file.getPath());
                    }
                }
                System.out.println("res 폴더 안의 총 파일 개수: " + fileCount + "개\n");
            }
        } else {
            System.out.println("디렉토리가 존재하지 않습니다: " + dirPath);
        }

        return filePathList;
    }

    static CalFile[] fileReadIn(List<String> paths) {
        CalFile[] calFiles = new CalFile[paths.size()]; // <- 이 배열에 txt파일 내용을 저장합니다.

        for (int i = 0; i < paths.size(); i++) {
            try {
                File file = new File(paths.get(i));
                Scanner scanner = new Scanner(file);

                if (scanner.hasNextInt()) {
                    // 2. 첫 줄의 연, 월, 일, 행개수(txtRow) 순서대로 읽어온다
                    int year = scanner.nextInt();
                    int month = scanner.nextInt();
                    int day = scanner.nextInt();
                    int txtRow = scanner.nextInt();

                    // 3. CalFile 객체 생성 (전달받은 txtRow 크기만큼 배열 할당)
                    calFiles[i] = new CalFile(year, month, day, txtRow);

                    // 4. DoList이중배열에 플래너에 출력될 데이터를 채웁니다.
                    for (int j = 0; j < txtRow; j++) {
                        for (int k = 0; k < 4; k++) {
                            if (scanner.hasNext()) {
                                calFiles[i].DoList[j][k] = scanner.next();
                            }
                        }
                    }
                }
                scanner.close();

            } catch (FileNotFoundException e) {
                System.out.println("파일을 찾을 수 없습니다: " + paths.get(i));
            }
        }
        return calFiles;
    }

    static void main() {
        CalFile[] calFiles = fileReadIn(fileIn("res"));

        // 테스트 출력입니다.
        for (CalFile cf : calFiles) {
            if (cf != null) {
                System.out.println("=== " + cf.year + "년 " + cf.month + "월 " + cf.day + "일 ===");
                for (int j = 0; j < cf.DoList.length; j++) {
                    System.out.println(cf.DoList[j][0] + " | " +
                            cf.DoList[j][1] + " | " +
                            cf.DoList[j][2] + " | " +
                            cf.DoList[j][3]);
                }
                System.out.println();
            }
        }
    }
}