public class CalFile {
    int year,month,day;
    Object[][] DoList;

    public CalFile(int year, int month, int day, int txtRow){
        this.year = year;
        this.month = month;
        this.day = day;
        this.DoList = new Object[txtRow][4];
    }

}
