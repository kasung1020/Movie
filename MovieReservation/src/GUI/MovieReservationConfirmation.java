package GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.*;
import java.util.*;
import DB.UserInfoInputScreenDB;

public class MovieReservationConfirmation extends JFrame {
    public MovieReservationConfirmation(String userName, String[][] reservationData) {
        setTitle("예매내역 확인");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 창 닫기 이벤트 처리
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"예", "처음으로"};
                int option = JOptionPane.showOptionDialog(
                    null,
                    "프로그램을 종료하시겠습니까?",
                    "종료 확인",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
                );

                if (option == 0) {
                    System.exit(0);
                } else if (option == 1) {
                    SwingUtilities.invokeLater(() -> new main().main(null));
                    dispose();
                }
            }
        });

        // 메인 패널
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // 상단 패널: 로고와 버튼
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        // POLY CINEMA 제목 (중앙 정렬)
        JLabel logoLabel = new JLabel("POLY CINEMA", JLabel.CENTER);
        logoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 70, 10, 0));
        topPanel.add(logoLabel, BorderLayout.CENTER);

        // 뒤로가기 버튼 추가
        JButton backButton = new JButton("뒤로가기");
        backButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14)); // 폰트 설정
        backButton.setPreferredSize(new Dimension(100, 30)); // 버튼 크기 설정
        backButton.addActionListener(e -> {
            // 뒤로가기 버튼 클릭 시 처음 화면으로 돌아가기
            Object[] options = {"예", "아니오"};
            int option = JOptionPane.showOptionDialog(
                null, // 부모 컴포넌트
                "처음 화면으로 돌아가시겠습니까?", // 메시지
                "뒤로가기 확인", // 제목
                JOptionPane.DEFAULT_OPTION, // 버튼 옵션
                JOptionPane.QUESTION_MESSAGE, // 질문 아이콘
                null, // 아이콘
                options, // 버튼 텍스트
                options[0] // 기본 버튼
            );

            if (option == 0) { // "예" 선택 시
                // 처음 화면으로 돌아가기 (main 화면을 띄우기)
                SwingUtilities.invokeLater(() -> {
                    new main().main(null);
                });
                dispose(); // 현재 창 닫기
            }
        });

        // 뒤로가기 버튼을 위한 패널
        JPanel backButtonPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT); // 오른쪽 정렬
        flowLayout.setHgap(30); // 오른쪽 여백을 30px로 설정
        flowLayout.setVgap(20); // 세로 여백을 20px로 설정 (상단 여백)
        backButtonPanel.setLayout(flowLayout); // FlowLayout을 backButtonPanel에 적용
        backButtonPanel.add(backButton);
        backButtonPanel.setBackground(Color.WHITE); // 배경색 흰색으로 설정
        topPanel.add(backButtonPanel, BorderLayout.EAST); // 상단 패널에 버튼 추가

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 중단 패널: 사용자 예매 현황
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(Color.WHITE);

        // 사용자 예매 현황 문구
        JLabel userInfoLabel = new JLabel("'" + userName + "' 님의 예매 현황 (" + reservationData.length + "건)");
        userInfoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        userInfoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        userInfoLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        middlePanel.add(userInfoLabel, BorderLayout.NORTH);

        // 테이블 생성
        String[] columnNames = {"영화", "영화관", "상영시간", "상영관", "좌석번호", "상영 날짜", "     "};
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // "예매 취소" 버튼만 편집 가능
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 6 ? JButton.class : String.class; // "예매 취소" 열은 JButton

            }
        };

        for (String col : columnNames) {
            tableModel.addColumn(col);
        }

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        table.setBackground(Color.WHITE);  // 테이블 배경색 흰색으로 설정
        table.getTableHeader().setReorderingAllowed(false); // 헤더 이동 비활성화
        table.setShowGrid(false); // 테이블 경계선 제거
        table.setIntercellSpacing(new Dimension(0, 0)); // 셀 간격 제거

        // 테이블 헤더 커스터마이징
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("맑은 고딕", Font.BOLD, 14));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBackground(Color.WHITE);
                label.setOpaque(true);
                return label;
            }
        });

        // 첫 번째 열(영화, 영화관)을 좌측 정렬
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.LEFT); // 좌측 정렬
                return label;
            }
        });

        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.LEFT); // 좌측 정렬
                return label;
            }
        });

     // 테이블 렌더러 및 에디터 설정 (버튼 활성화)
        table.setDefaultRenderer(JButton.class, new ButtonRenderer());
        table.setDefaultEditor(JButton.class, new ButtonEditor(new JCheckBox()));

     // 현재 날짜 및 시간을 가져오기
        Date currentDateTime = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // 데이터 추가
        for (int i = 0; i < reservationData.length; i++) {
            String[] data = reservationData[i];
            JButton cancelButton = new JButton("예매 취소");
            
            try {
                // 상영 날짜와 시간을 결합해 파싱
                String screeningDateTimeStr = data[5] + " " + data[2]; // 예: "2024-12-03 12:00"
                Date screeningDateTime = dateTimeFormat.parse(screeningDateTimeStr);

                // 상영 시간이 현재 시간보다 이전이면 버튼 비활성화
                if (screeningDateTime.before(currentDateTime)) {
                    cancelButton.setEnabled(false); // 비활성화
                } else {
                	// 예매 취소 버튼 클릭 시
                	cancelButton.setEnabled(true);   // 활성화
                	cancelButton.addActionListener(e -> {
                	    int viewRowIndex = table.getSelectedRow(); // 뷰 인덱스 가져오기
                	    if (viewRowIndex == -1) {
                	        JOptionPane.showMessageDialog(null, "취소할 예매를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                	        return;
                	    }
                	    // 모델 인덱스 가져오기 (뷰 인덱스를 모델 인덱스로 변환)
                	    int modelRowIndex = table.convertRowIndexToModel(viewRowIndex);
                	    
                	    // 확인 대화 상자
                	    int option = JOptionPane.showOptionDialog(
                	        null,
                	        "예매를 취소하시겠습니까?",
                	        "예매 취소 확인",
                	        JOptionPane.YES_NO_OPTION,
                	        JOptionPane.QUESTION_MESSAGE,
                	        null,
                	        new Object[]{"예", "아니오"},
                	        "아니오"
                	    );
                	    if (option == JOptionPane.YES_OPTION) {
                	        // 삭제 작업
                	        String movieName = (String) tableModel.getValueAt(modelRowIndex, 0);  // 영화 이름
                	        String area = (String) tableModel.getValueAt(modelRowIndex, 1);      // 상영관 지역
                	        String showtime = (String) tableModel.getValueAt(modelRowIndex, 2);  // 상영 시간
                	        String theater = (String) tableModel.getValueAt(modelRowIndex, 3);   // 상영관 번호
                	        String seats = (String) tableModel.getValueAt(modelRowIndex, 4);     // 좌석 정보
                	        String screenDay = (String) tableModel.getValueAt(modelRowIndex, 5); // 상영 날짜

                	        UserInfoInputScreenDB dbHandler = new UserInfoInputScreenDB();
                	        boolean isDeleted = dbHandler.cancelReservation(userName, movieName, area, showtime, theater, screenDay, seats);


                	        if (isDeleted) {
                	            JOptionPane.showMessageDialog(null, "예매가 성공적으로 취소되었습니다.", "취소 완료", JOptionPane.INFORMATION_MESSAGE);
                	            // 모델에서 행 삭제
                	            tableModel.removeRow(modelRowIndex);
                	            // 행 삭제 후 선택 상태 초기화
                	            table.clearSelection(); // 선택 해제

                	            // 삭제된 데이터를 로그로 출력 (디버깅용)
                	            System.out.println("userName: " + userName);
                	            System.out.println("movieName: " + movieName);
                	            System.out.println("area: " + area);
                	            System.out.println("showtime: " + showtime);
                	            System.out.println("theater: " + theater);
                	            System.out.println("screenDay: " + screenDay);
                	            System.out.println("seats: " + seats);
                	        } else {
                	            JOptionPane.showMessageDialog(null, "예매 취소에 실패했습니다. 다시 시도해주세요.", "취소 실패", JOptionPane.ERROR_MESSAGE);
                	        }
                	    }
                	});


                }
                tableModel.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4], data[5], cancelButton});
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 스크롤 패널에 테이블 추가
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);  // 스크롤 영역 배경색 흰색으로 설정
        middlePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(middlePanel, BorderLayout.CENTER);

        // 하단 패널: 예매 정보
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        JLabel footerLabel = new JLabel("※ 예매 취소는 상영 날짜가 지나지 않은 예매만 가능합니다.");
        footerLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        bottomPanel.add(footerLabel);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[][] reservationData = {
                {"영화1", "상영관1", "12:00", "A관", "A1", "2024-12-01"},
                {"영화2", "상영관2", "12:00", "B관", "B1", "2024-12-02"},
                {"영화3", "상영관3", "12:00", "C관", "C1", "2024-12-03"}
            };
            new MovieReservationConfirmation("홍길동", reservationData).setVisible(true);
        });
    }
}

//버튼 렌더러 클래스 수정
class ButtonRenderer extends JButton implements TableCellRenderer {
 public ButtonRenderer() {
     setOpaque(true);  // 버튼의 배경을 투명하게 설정
 }

 @Override
 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
     if (value instanceof JButton) {
         JButton button = (JButton) value;
         button.setText("예매 취소");  // 강제로 텍스트 설정
         return button;
     }
     return this;
 }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private boolean isEditing; // 편집 상태를 추적

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        isEditing = false;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof JButton) {
            JButton cancelButton = (JButton) value;
            cancelButton.addActionListener(e -> {
                if (!isEditing) {
                    // 편집이 시작되지 않은 상태에서 버튼을 클릭하면 편집을 시작
                    isEditing = true;
                    stopCellEditing(); // 셀 편집 종료
                } else {
                    // 편집이 끝난 후 버튼을 다시 클릭하여 취소 다이얼로그 처리
                    stopCellEditing(); // 셀 편집 종료
                }
            });
            return cancelButton;
        }
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button; // 버튼 객체를 반환
    }

    @Override
    public boolean stopCellEditing() {
        isEditing = false; // 편집 상태를 리셋
        return super.stopCellEditing();
    }
}

