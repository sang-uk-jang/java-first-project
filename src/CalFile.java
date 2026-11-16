import java.util.ArrayList;
import java.util.List;

public class CalFile {
    int year,month,day,count;
    List<Record> DoList;
    String content; //일정 기록

    //CalFile 하나가 하루의 기록

    public CalFile(int year, int month, int day, String content){
        this.year = year;
        this.month = month;
        this.day = day;
        this.content = content;
        this.count =1;
        this.DoList = new ArrayList<>();
    }

    public void setContent(String content) {
        this.content = content;
    }
}
