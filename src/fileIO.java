import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class fileIO {

    public static boolean deleterow(int year, int month, int day, int id) {
        //id로 row찾아서 삭제(구현해야함)
        return true;
    }

    public boolean writeRecords(CalFile dayFile){
        //txt에 작성하는 로직 필요.(구현해야함)
        return true;
    }

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

    //날짜로 파일찾아서 CalFile 반환하기(구현해야함)
    static CalFile fileRead(int year, int month, int day) {
        CalFile readFile = null;
            try {
                File file = new File("./res/" + year + month + day);
                Scanner scanner = new Scanner(file);

                if (scanner.hasNextInt()) {
                    scanner.nextLine();
                    //content읽어오기, 아래는 임시로 만든 것, while문으로 받아오기(쓰기한 형식에 따라 구현은 달라짐)
                    String content = scanner.nextLine();

                    readFile = new CalFile(year, month, day, content);

                    //한행 읽어오기(아래는 임시로 만든것 구현은 해야함)
                    Record row = new Record(1, "title", 20, "true");

                    //DoList에 넣기
                    readFile.DoList.add(row);

                }
                scanner.close();

            } catch (FileNotFoundException e) {
            }
        return readFile;
    }

    public void deleteFile(int year, int month, int day) {
        //file 삭제 구현
    }

    public Record findRecord(int year, int month, int day, int id) {
        //파일에서 Record 찾기 구현
        return null;
    }
}