import com.sun.jdi.Value;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.YearMonth;
import java.util.List;
import java.time.LocalDate;


public class MainFrame extends JFrame {
    Container frame = getContentPane();
    private final CardLayout cardLayout = new CardLayout();
    private final static fileIO IO = new fileIO();
    private YearMonth currentYearMonth = YearMonth.now(); // 달력 갱신 매서드

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
        dayViewPanel(new CalFile(2026,10,10,"수학"), 2);
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
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // 1. 상단: 연/월 헤더 및 이전/다음 달 이동 버튼
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton prevBtn = new JButton("<");
        JButton nextBtn = new JButton(">");
        JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        topPanel.add(prevBtn, BorderLayout.WEST);
        topPanel.add(monthLabel, BorderLayout.CENTER);
        topPanel.add(nextBtn, BorderLayout.EAST);

        // 2. 중앙: 요일 헤더 + 날짜 그리드
        JPanel centerPanel = new JPanel(new BorderLayout(0, 6));

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        JPanel weekHeaderPanel = new JPanel(new GridLayout(1, 7, 4, 4));
        for (int i = 0; i < daysOfWeek.length; i++) {
            JLabel dayLbl = new JLabel(daysOfWeek[i], SwingConstants.CENTER);
            dayLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            if (i == 0) {
                dayLbl.setForeground(Color.RED);
            } else if (i == 6) {
                dayLbl.setForeground(Color.BLUE);
            }
            weekHeaderPanel.add(dayLbl);
        }

        JPanel calendarGridPanel = new JPanel(new GridLayout(0, 7, 4, 4));

        centerPanel.add(weekHeaderPanel, BorderLayout.NORTH);
        centerPanel.add(calendarGridPanel, BorderLayout.CENTER);

        // 3. 하단: 메뉴 버튼 (1. 일정 생성, 2. 일정 삭제, 3. 일정 수정, 4. 일정 조회)
        JPanel menuPanel = new JPanel(new GridLayout(1, 4, 6, 0));
        JButton createBtn = new JButton("일정 생성");
        JButton deleteBtn = new JButton("일정 삭제");
        JButton modifyBtn = new JButton("일정 수정");
        JButton searchBtn = new JButton("일정 조회");

        createBtn.addActionListener(e -> showPanel("createPanel"));
        deleteBtn.addActionListener(e -> showPanel("deletePanel"));
        modifyBtn.addActionListener(e -> showPanel("modifyPanel"));
        searchBtn.addActionListener(e -> showPanel("searchPanel"));

        menuPanel.add(createBtn);
        menuPanel.add(deleteBtn);
        menuPanel.add(modifyBtn);
        menuPanel.add(searchBtn);

        // 달력 초기 렌더링 및 월 이동 이벤트
        Runnable renderCalendar = () -> updateCalendarGrid(panel, monthLabel, calendarGridPanel);
        prevBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            renderCalendar.run();
        });
        nextBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            renderCalendar.run();
        });

        renderCalendar.run();

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(menuPanel, BorderLayout.SOUTH);

        frame.add(panel, "firstPanel");
    }

    private void updateCalendarGrid(JPanel parentPanel, JLabel monthLabel, JPanel calendarGridPanel) {
        calendarGridPanel.removeAll();

        int year = currentYearMonth.getYear();
        int month = currentYearMonth.getMonthValue();
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int startDay = currentYearMonth.atDay(1).getDayOfWeek().getValue() % 7; // Sun = 0

        monthLabel.setText(year + "년 " + month + "월");

        LocalDate today = LocalDate.now();

        // 1일 이전 빈 칸 채우기
        for (int i = 0; i < startDay; i++) {
            calendarGridPanel.add(new JLabel(""));
        }

        // 날짜 버튼 생성
        for (int day = 1; day <= daysInMonth; day++) {
            final int selectedDay = day;
            boolean hasSchedule = hasScheduleFile(year, month, day);

            JButton dayBtn = new JButton(hasSchedule ? day + " •" : String.valueOf(day));
            dayBtn.setMargin(new Insets(2, 2, 2, 2));
            dayBtn.setFocusPainted(false);
            dayBtn.setOpaque(true);

            int colIndex = (startDay + day - 1) % 7;
            Color dayTextColor = (colIndex == 0) ? Color.RED : (colIndex == 6) ? Color.BLUE : Color.BLACK;
            dayBtn.setForeground(dayTextColor);

            boolean isToday = (year == today.getYear() && month == today.getMonthValue() && day == today.getDayOfMonth());

            // 우선순위: 일정 있음 + 오늘 > 일정 있음 > 오늘 > 기본
            if (hasSchedule && isToday) {
                dayBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
                dayBtn.setBackground(new Color(204, 245, 220));
                dayBtn.setBorder(BorderFactory.createLineBorder(new Color(25, 135, 84), 2));
            } else if (hasSchedule) {
                dayBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
                dayBtn.setBackground(new Color(232, 248, 238));
                dayBtn.setBorder(BorderFactory.createLineBorder(new Color(64, 165, 115), 1));
            } else if (isToday) {
                dayBtn.setBackground(new Color(220, 235, 255));
                dayBtn.setBorder(BorderFactory.createLineBorder(new Color(60, 120, 215), 2));
            }

            // 날짜 클릭 시 저장된 일정이 있으면 일간 조회 화면으로 이동
            dayBtn.addActionListener(e -> {
                CalFile found = IO.fileRead(year, month, selectedDay);
                if (found != null) {
                    dayViewPanel(found, 2);
                    showPanel("dayViewPanel");
                } else {
                    JOptionPane.showMessageDialog(parentPanel,
                            year + "년 " + month + "월 " + selectedDay + "일에 저장된 일정이 없습니다.");
                }
            });

            calendarGridPanel.add(dayBtn);
        }

        // 마지막 주 빈 칸 채우기
        int totalCells = startDay + daysInMonth;
        int remainder = totalCells % 7;
        if (remainder != 0) {
            for (int i = remainder; i < 7; i++) {
                calendarGridPanel.add(new JLabel(""));
            }
        }

        calendarGridPanel.revalidate();
        calendarGridPanel.repaint();
    }

    // 해당 날짜의 일정 txt 파일 존재 여부 확인 ("res/연도 월 일.txt")
    private boolean hasScheduleFile(int year, int month, int day) {
        java.io.File file = new java.io.File("res/" + year + " " + month + " " + day + ".txt");
        return file.exists() && file.isFile();
    }



    static void main() {
        new MainFrame("team_1");
    }
}
