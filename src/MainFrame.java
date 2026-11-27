import com.sun.jdi.Value;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainFrame extends JFrame {
    Container frame = getContentPane();
    private final CardLayout cardLayout = new CardLayout();
    private final static fileIO IO = new fileIO();

    public MainFrame(String title) {
        super(title);
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(cardLayout);
        initLayout();
        this.setVisible(true);
    }

    private void initLayout() {
        initFirstPanel();
        createPanel();
        modifyPanel();
        searchPanel();
        deletePanel();
        dayViewPanel(new CalFile(2026,10,10,""), 3);
        showPanel("dayViewPanel");
    }

    private void deletePanel() {
        //첼맥
        JPanel panel = new JPanel();
        //필요한 컴포넌트 선언 및 사용
        //CalendarManager로 날짜 검증
        //이벤트 처리
        //확인 클릭 -> CalFile 객체 생성(DoList == null) -> dayViewPanel(CalFile,1)호출
        //취소 클릭 -> showPanel(frame, "firstPanel) 호출

        frame.add(panel, "deletePanel");
    }

    private void showPanel(String panelName) {
        cardLayout.show(frame, panelName);
    }



    private void dayViewPanel(CalFile file, int flag) {
        //장상욱
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));

        JLabel titleLabel = new JLabel("study planner");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel dataLabel = new JLabel(file.year + "년 " + file.month + "월 " + file.day+"일");
        dataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel1.add(titleLabel, "North");
        panel1.add(dataLabel, "Center");

        JLabel contentLabel = new JLabel("기록 : " + file.content);
        String[] tableTitle = {"No", "과목", "공부시간", "상태"};
        DefaultTableModel model = new DefaultTableModel(tableTitle, 0);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        JTable table = new JTable(model);
        table.setDefaultRenderer(Object.class, renderer);

        switch (flag){
            case 0 -> {
                if(model.getRowCount()>0){
                    model = new DefaultTableModel(tableTitle, 0);
                    table.setModel(model);
                    //모델을 바꿔줄 수 있다.
                }
                file.DoList.add(new Record(1, "2", 3, "true"));
                for(Record record:file.DoList){
                    model.addRow(new String[]{String.valueOf(record.id), record.title, String.valueOf(record.miniute), record.check });
                }

                JButton btn1 = new JButton("확인");
                btn1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        IO.writeRecords(file);
                        JOptionPane.showMessageDialog(panel,"기록이 생성되었습니다.");
                        showPanel("firstPanel");
                    }
                });
                JButton btn2 = new JButton("취소");
                btn2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showPanel("firstPanel");
                    }
                });
                panel2.add(btn1);
                panel2.add(btn2);
            }
            case 1 -> {
                if(model.getRowCount()>0){
                    model = new DefaultTableModel(tableTitle, 0);
                    table.setModel(model);
                    //모델을 바꿔줄 수 있다.
                }
                CalFile result = IO.fileRead(file.year, file.month, file.day);
                if(result==null){
                    JOptionPane.showMessageDialog(panel,"삭제할 파일이 없습니다.");
                    showPanel("firstPanel");
                    return;
                }
                for(Record record:result.DoList){
                    model.addRow(new String[]{String.valueOf(record.id), record.title, String.valueOf(record.miniute), record.check });
                }
                JButton btn1 = new JButton("전체 삭제");
                btn1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        IO.deleteFile(result.year, result.month, result.day);
                        JOptionPane.showMessageDialog(panel,"삭제되었습니다");
                        showPanel("firstPanel");
                    }
                });
                JButton btn2 = new JButton("일부 삭제");

                btn2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        panel2.removeAll();
                        panel2.setLayout(new FlowLayout());
                        JLabel lable = new JLabel("삭제할 과목 번호");
                        JTextField text = new JTextField(5);
                        JButton btn = new JButton("확인");
                        panel2.add(lable);
                        panel2.add(text);
                        panel2.add(btn);
                        panel2.revalidate();
                        panel2.repaint();
                        btn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(result.count == 0){
                                    JOptionPane.showMessageDialog(panel,"삭제할 과목 기록이 없습니다, 파일부터 삭제해주세요");
                                    showPanel("firstPanel");

                                }
                                int[] validId = new int[result.DoList.size()];
                                for(int i=0;i<validId.length;i++){
                                    validId[i] = result.DoList.get(i).id;
                                }
                                int flag = 0;
                                for(int i=0; i<validId.length;i++){
                                    if(Integer.parseInt(text.getText()) == validId[i]){
                                        flag = 1;
                                    }

                                }
                                if(flag==1){
                                    List<Record> tempList = result.DoList;
                                    int tempindex = Integer.parseInt(text.getText());
                                    IO.deleterow(result.year, result.month, result.day, tempindex);
                                    showPanel("firstPanel");
                                    return;
                                }else {
                                    JOptionPane.showMessageDialog(panel,"올바르지 않은 과목 번호입니다.");
                                    showPanel("firstPanel");
                                    return;
                                }
                            }
                        });
                    }
                });
                panel2.add(btn1);
                panel2.add(btn2);
            }
            case 2 -> {
                if(model.getRowCount()>0){
                    model = new DefaultTableModel(tableTitle, 0);
                    table.setModel(model);
                    //모델을 바꿔줄 수 있다.
                }
                CalFile searchFile = IO.fileRead(file.year, file.month, file.day);
                if(searchFile==null){
                    JOptionPane.showMessageDialog(panel,"조회할 파일이 없습니다.");
                    showPanel("firstPanel");
                    return;
                }
                for(Record record:searchFile.DoList){
                    model.addRow(new String[]{String.valueOf(record.id), record.title, String.valueOf(record.miniute), record.check });
                }
                JButton btn = new JButton("돌아가기");
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showPanel("firstPanel");
                    }
                });
                panel2.add(btn);
            }
            case 3 -> {
                if(model.getRowCount()>0){
                    model = new DefaultTableModel(tableTitle, 0);
                    table.setModel(model);
                }
                CalFile searchFile = IO.fileRead(file.year, file.month, file.day);
                if(searchFile==null){
                    JOptionPane.showMessageDialog(panel,"수정할 파일이 없습니다.");
                    showPanel("firstPanel");
                    return;
                }
                for(Record record:searchFile.DoList){
                    model.addRow(new String[]{String.valueOf(record.id), record.title, String.valueOf(record.miniute), record.check });
                }
                JButton btn = new JButton("일정 기록 수정");
                JButton btn2 = new JButton("과목 수정");
                panel2.add(btn);
                panel2.add(btn2);
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        panel2.removeAll();
                        panel2.setLayout(new FlowLayout());
                        JLabel label = new JLabel("수정할 일정 기록 입력");
                        JTextField text = new JTextField(20);
                        JButton btn3 = new JButton("확인");
                        panel2.add(label);
                        panel2.add(text);
                        panel2.add(btn3);
                        panel2.revalidate();
                        panel2.repaint();
                        btn3.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                searchFile.content = text.getText();
                                IO.writeRecords(searchFile);
                                JOptionPane.showMessageDialog(panel,"일정 기록을 수정했습니다.");
                                showPanel("firstPanel");
                                return;
                            }
                        });

                    }
                });
                btn2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        panel2.removeAll();
                        panel2.setLayout(new FlowLayout());
                        for(Record record : searchFile.DoList){
                            JButton btn = new JButton(record.id + record.title);
                            panel2.add(btn);
                            btn.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    panel2.removeAll();
                                    panel2.setLayout(new FlowLayout());
                                    JLabel title = new JLabel("과목");
                                    JTextField titleText = new JTextField(5);
                                    JLabel time = new JLabel("시간");
                                    JTextField timeText = new JTextField(5);
                                    JLabel status = new JLabel("상태");
                                    JTextField statusText = new JTextField(5);
                                    JButton btn = new JButton("확인");
                                    panel2.add(title);
                                    panel2.add(titleText);
                                    panel2.add(time);
                                    panel2.add(timeText);
                                    panel2.add(status);
                                    panel2.add(statusText);
                                    panel2.add(btn);
                                    panel2.revalidate();
                                    panel2.repaint();
                                    btn.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            record.title = titleText.getText();
                                            //if(timeText.getText().)
                                            try {
                                                record.miniute = Integer.parseInt(timeText.getText().trim());
                                            } catch (NumberFormatException a) {
                                                JOptionPane.showMessageDialog(null, "숫자만 입력해주세요.");
                                                showPanel("firstPanel");
                                                return;
                                            }
                                            record.check = statusText.getText();
                                            if(record.check == "Y" || record.check == "N"){
                                                JOptionPane.showMessageDialog(null, "Y 또는 N만 입력해주세요");
                                                showPanel("firstPanel");
                                                return;
                                            }
                                            IO.deleterow(searchFile.year, searchFile.month, searchFile.day, record.id);
                                            IO.writeRecords(searchFile);
                                            JOptionPane.showMessageDialog(panel,"과목을 수정했습니다.");
                                            showPanel("firstPanel");

                                        }
                                    });


                                }
                            });
                        }
                        panel2.revalidate();
                        panel2.repaint();
                    }
                });

            }
        }
        panel.add(panel1, "North");
        panel.add(contentLabel, "West");
        panel.add(new JScrollPane(table));
        panel.add(panel2, "South");
        frame.add(panel, "dayViewPanel");
    }

    private void searchPanel() {
        //첼맥
        JPanel panel = new JPanel();
        //필요한 컴포넌트 선언 및 사용
        //CalendarManager로 날짜 검증
        //이벤트 처리
        //확인 클릭 -> CalFile 객체 생성(DoList == null) -> dayViewPanel(CalFile,2)호출
        //취소 클릭 -> showPanel(frame, "firstPanel) 호출

        frame.add(panel, "searchPanel");
    }

    private void modifyPanel() {
        //첼맥
        JPanel panel = new JPanel();
        //필요한 컴포넌트 선언 및 사용
        //CalendarManager로 날짜 검증
        //이벤트 처리
        //확인 클릭 -> CalFile 객체 생성(DoList == null) -> dayViewPanel(CalFile,3)호출
        //취소 클릭 -> showPanel(frame, "firstPanel) 호출

        frame.add(panel, "modifyPanel");
    }

    private void createPanel() {
        //윤퓨
        JPanel panel = new JPanel();


        frame.add(panel, "createPanel");
        //필요한 컴포넌트 선언 및 사용
        //CalendarManager로 날짜 검증
        //이벤트 처리
        //날짜랑 record 받아서 Calfile 객체 생성 (record 받을 때 count++ 해줘야 합니다)
        //확인 클릭 -> Calfile 객체 생성 후 -> dayViewPanel(Calfile,0) 호출
        //취소 클릭 -> showPanel(frame, "firstPanel) 호출
    }

    private void initFirstPanel() {
        //강윤호
        JPanel panel = new JPanel();

        frame.add(panel, "firstPanel");
        //첫번째 화면 panel
        //내부에서 필요한 컴포넌트 생성 후 frame에 붙이기
//        JPanel panel = new JPanel();
//        JButton btn = new JButton("hel");
//        panel.add(btn);
//        frame.add(panel);
        //이거처럼 하면 됩니다
        //일정 생성 -> showPanel(frame,"createPanel")
        //일정 삭제 -> showPanel(frame, "deletePanel")
        //일정 수정 -> showPanel(frame, "modifyPanel")
        //일정 조회 -> showPanel(frame, "searchPanel")
    }



    static void main() {
        new MainFrame("team_1");
    }
}
