public class Record {
    int id;
    String title;
    int miniute;
    String check;

    public Record(int id, String title, int miniute, String check) {
        this.id = id;
        this.title = title;
        this.miniute = miniute;
        this.check = check;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMiniute(int miniute) {
        this.miniute = miniute;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    @Override
    public String toString() {
        String str = "  ";
        str += this.id + " " + this.title + " " + this.miniute + " " + this.check;
        return str;
    }
}
