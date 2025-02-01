package GUI;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import DB.UserInfoInputScreenDB;

public class UserInfoInputScreen extends JFrame {
    private JTextField nameField;
    private JTextField phoneField1;
    private JTextField phoneField2;
    private JTextField phoneField3;
    private JTextField birthYearField;
    private JTextField birthMonthField;
    private JTextField birthDayField;
    private JButton confirmButton;

    public UserInfoInputScreen() {
        setTitle("개인정보 입력");
        setSize(400, 300);
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
        JPanel panel = new JPanel();
        panel.setLayout(null);  // 절대 위치 레이아웃
        panel.setBackground(UIManager.getColor("Panel.background")); // 기본 배경색

        // 제목 레이블
        JLabel titleLabel = new JLabel("POLY CINEMA", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 폰트 설정
        titleLabel.setBounds(100, 10, 200, 30); // 위치 설정
        panel.add(titleLabel);

        // 이름 입력
        JLabel nameLabel = new JLabel("이름", JLabel.RIGHT);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        nameLabel.setBounds(50, 60, 60, 20); // 위치 설정
        panel.add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(120, 60, 200, 20); // 위치 설정
        panel.add(nameField);

        // 전화번호 입력
        JLabel phoneLabel = new JLabel("전화번호", JLabel.RIGHT);
        phoneLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        phoneLabel.setBounds(50, 100, 60, 20); // 위치 설정
        panel.add(phoneLabel);

        phoneField1 = createLimitedTextField(3);
        phoneField1.setBounds(120, 100, 50, 20);
        panel.add(phoneField1);

        JLabel dash1 = new JLabel("-");
        dash1.setBounds(175, 100, 10, 20);
        panel.add(dash1);

        phoneField2 = createLimitedTextField(4);
        phoneField2.setBounds(190, 100, 50, 20);
        panel.add(phoneField2);

        JLabel dash2 = new JLabel("-");
        dash2.setBounds(245, 100, 10, 20);
        panel.add(dash2);

        phoneField3 = createLimitedTextField(4);
        phoneField3.setBounds(260, 100, 60, 20);
        panel.add(phoneField3);

        // 생년월일 입력
        JLabel birthLabel = new JLabel("생년월일", JLabel.RIGHT);
        birthLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        birthLabel.setBounds(50, 140, 60, 20); // 위치 설정
        panel.add(birthLabel);

        birthYearField = createLimitedTextField(4);
        birthYearField.setBounds(120, 140, 50, 20);
        panel.add(birthYearField);

        JLabel slash1 = new JLabel("/");
        slash1.setBounds(175, 140, 10, 20);
        panel.add(slash1);

        birthMonthField = createLimitedTextField(2);
        birthMonthField.setBounds(190, 140, 40, 20);
        panel.add(birthMonthField);

        JLabel slash2 = new JLabel("/");
        slash2.setBounds(235, 140, 10, 20);
        panel.add(slash2);

        birthDayField = createLimitedTextField(2);
        birthDayField.setBounds(245, 140, 50, 20);
        panel.add(birthDayField);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(80, 190, 250, 50);
        buttonPanel.setLayout(new FlowLayout());
        panel.add(buttonPanel);

        // 뒤로가기 버튼
        JButton backButton = new JButton("뒤로가기");
        backButton.setPreferredSize(new java.awt.Dimension(90, 30));
        buttonPanel.add(backButton);

        buttonPanel.add(Box.createHorizontalStrut(15));

        // 확인 버튼
        confirmButton = new JButton("확인");
        confirmButton.setPreferredSize(new java.awt.Dimension(90, 30));
        confirmButton.setEnabled(false); // 초기 상태 비활성화
        buttonPanel.add(confirmButton);

        // 뒤로가기 버튼 동작
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new main().main(null)); // main 화면으로 이동
            dispose(); // 현재 창 닫기
        });

        // 확인 버튼 동작
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String phone = phoneField1.getText().trim() + "-" + phoneField2.getText().trim() + "-" + phoneField3.getText().trim();
                String birthDate = birthYearField.getText().trim() + "/" + birthMonthField.getText().trim() + "/" + birthDayField.getText().trim();

                if (name.isEmpty() || phone.contains("--") || birthDate.contains("//")) {
                    JOptionPane.showMessageDialog(null, "모든 정보를 정확히 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 데이터베이스에서 확인 (DB 연결 및 조회는 UserInfoInputScreenDB로 이동)
                SwingUtilities.invokeLater(() -> new UserInfoInputScreenDB().searchUserInfo(name, phone, birthDate, UserInfoInputScreen.this));
            }
        });

        // 각 필드에 DocumentListener 추가 (모든 입력이 완료되었는지 확인)
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkFields();
            }
        };

        nameField.getDocument().addDocumentListener(documentListener);
        phoneField1.getDocument().addDocumentListener(documentListener);
        phoneField2.getDocument().addDocumentListener(documentListener);
        phoneField3.getDocument().addDocumentListener(documentListener);
        birthYearField.getDocument().addDocumentListener(documentListener);
        birthMonthField.getDocument().addDocumentListener(documentListener);
        birthDayField.getDocument().addDocumentListener(documentListener);

        // 프레임에 패널 추가
        add(panel);
    }

    // 모든 필드가 채워졌는지 확인
    private void checkFields() {
        boolean isFilled = !nameField.getText().trim().isEmpty()
                && !phoneField1.getText().trim().isEmpty()
                && !phoneField2.getText().trim().isEmpty()
                && !phoneField3.getText().trim().isEmpty()
                && !birthYearField.getText().trim().isEmpty()
                && !birthMonthField.getText().trim().isEmpty()
                && !birthDayField.getText().trim().isEmpty();

        confirmButton.setEnabled(isFilled); // 모든 필드가 채워졌으면 확인 버튼 활성화
    }

    /**
     * 중앙 정렬된 텍스트 필드를 생성하는 헬퍼 메서드
     */
    private JTextField createCenteredTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트 설정
        return textField;
    }

    /**
     * 텍스트 필드의 길이를 제한하는 메서드
     */
    private JTextField createLimitedTextField(int limit) {
        JTextField textField = new JTextField();
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (fb.getDocument().getLength() + text.length() - length <= limit) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        textField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        return textField;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserInfoInputScreen().setVisible(true));
    }
}
