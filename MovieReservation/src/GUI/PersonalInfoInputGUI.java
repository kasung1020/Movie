package GUI;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import GUI.TicketingView;
import GUI.TicketingView.tickObject;
import GUI.SeatView;
import DB.PersonalInfoInputGUIDB;
import DB.MovieReservationSystemDB.Movie;
import DB.TicketingViewDB;
import DB.TicketingViewDB.RMovie;

public class PersonalInfoInputGUI extends JFrame {
	
	public class userInfo {
		public String u_name;
		public String phone;
		public String birth;

		public userInfo(String u_name, String phone, String birth) {
			this.u_name = u_name;
			this.phone = phone;
			this.birth = birth;
		}
	}
	private static userInfo userinfo;
	//유저정보 가져오기
	public static userInfo getuserinfo() {
		return userinfo;
	}
	//여기 관련 내용을 Database에서 꺼내게 바꾼다. 
	private ArrayList<RMovie> RMovies;
	
	public PersonalInfoInputGUI() {
		buildGUI();
	}
	
    public void buildGUI() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("src/GUI/favicon.png");
		setIconImage(img);
		
        // 창 설정
        setTitle("개인정보 입력");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // 전체 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        add(mainPanel);

        // 제목 레이블
        JLabel titleLabel = new JLabel("POLY CINEMA", JLabel.CENTER);
        titleLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
	    titleLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
        titleLabel.setBounds(100, 10, 200, 30);
        mainPanel.add(titleLabel);
		
        // 이름 입력
        JLabel nameLabel = new JLabel("이름", JLabel.RIGHT);
        nameLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
        nameLabel.setBounds(50, 60, 60, 20);
        mainPanel.add(nameLabel);

		// 이름 필드 입력
        JTextField nameField = new JTextField();
        nameField.setBounds(120, 60, 200, 20);
        mainPanel.add(nameField);

        // 전화번호 입력
        JLabel phoneLabel = new JLabel("전화번호", JLabel.RIGHT); 
        phoneLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
        phoneLabel.setBounds(50, 100, 60, 20);
        mainPanel.add(phoneLabel);
        
        JTextField phoneField1 = createLimitedTextField(3);
        phoneField1.setBounds(120, 100, 50, 20);
        mainPanel.add(phoneField1);

        JLabel dash1 = new JLabel("-");
        dash1.setBounds(175, 100, 10, 20);
        mainPanel.add(dash1);

        JTextField phoneField2 = createLimitedTextField(4);
        phoneField2.setBounds(190, 100, 50, 20);
        mainPanel.add(phoneField2);

        JLabel dash2 = new JLabel("-");
        dash2.setBounds(245, 100, 10, 20);
        mainPanel.add(dash2);

        JTextField phoneField3 = createLimitedTextField(4);
        phoneField3.setBounds(260, 100, 60, 20);
        mainPanel.add(phoneField3);

        // 생년월일 입력
        JLabel birthLabel = new JLabel("생년월일", JLabel.RIGHT);
        birthLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
        birthLabel.setBounds(50, 140, 60, 20);
        mainPanel.add(birthLabel);
		
        JTextField birthField1 = createLimitedTextField(4);
        birthField1.setBounds(120, 140, 50, 20);
        mainPanel.add(birthField1);

        JLabel slash1 = new JLabel("/");
        slash1.setBounds(175, 140, 10, 20);
        mainPanel.add(slash1);

        JTextField birthField2 = createLimitedTextField(2);
        birthField2.setBounds(190, 140, 40, 20);
        mainPanel.add(birthField2);

        JLabel slash2 = new JLabel("/");
        slash2.setBounds(235, 140, 10, 20);
        mainPanel.add(slash2);

        JTextField birthField3 = createLimitedTextField(2);
        birthField3.setBounds(245, 140, 50, 20);
        mainPanel.add(birthField3);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(80, 190, 250, 50);
        buttonPanel.setLayout(new FlowLayout());
        mainPanel.add(buttonPanel);

        // 뒤로가기 버튼
        JButton backButton = new JButton("뒤로가기");
        backButton.setPreferredSize(new Dimension(90, 30));
        buttonPanel.add(backButton);
        backButton.addActionListener(e -> {
            // TicketingView에서 tickObject 가져오기
            TicketingView.tickObject tick = TicketingView.getTobj();

            // SeatView로 돌아가기 (데이터 전달)
            SwingUtilities.invokeLater(() -> {
                new SeatView(tick.area, tick.date, tick.u_num, tick.price,tick.time,"").setVisible(true);
            });
            
            // 현재 창 닫기
            dispose();

        });

		buttonPanel.add(Box.createHorizontalStrut(15));
	    
        // 확인 버튼
        JButton confirmButton = new JButton("확인");
        confirmButton.setPreferredSize(new Dimension(90, 30));
        confirmButton.setEnabled(false); // 초기 상태 비활성화
        buttonPanel.add(confirmButton);

        // 입력 필드의 값 변화를 감지하는 DocumentListener 추가
        DocumentListener fieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateConfirmButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateConfirmButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateConfirmButtonState();
            }

            private void updateConfirmButtonState() {
                boolean isNameFilled = !nameField.getText().trim().isEmpty();
                boolean isPhoneFilled = phoneField1.getText().length() == 3 &&
                                        phoneField2.getText().length() == 4 &&
                                        phoneField3.getText().length() == 4;
                boolean isBirthFilled = birthField1.getText().length() == 4 &&
                                        birthField2.getText().length() == 2 &&
                                        birthField3.getText().length() == 2;
                confirmButton.setEnabled(isNameFilled && isPhoneFilled && isBirthFilled);
            }
        };
        
        // 각 필드에 DocumentListener 추가
        nameField.getDocument().addDocumentListener(fieldListener);
        phoneField1.getDocument().addDocumentListener(fieldListener);
        phoneField2.getDocument().addDocumentListener(fieldListener);
        phoneField3.getDocument().addDocumentListener(fieldListener);
        birthField1.getDocument().addDocumentListener(fieldListener);
        birthField2.getDocument().addDocumentListener(fieldListener);
        birthField3.getDocument().addDocumentListener(fieldListener);
        
        // 액션 리스너 추가 (필요 시)
        backButton.addActionListener(e -> {
            System.out.println("뒤로가기 버튼 클릭");
            dispose(); // 현재 창 닫기
        });
        
        int selectedMovieIndex = TicketingView.movielist.getSelectedIndex(); // 사용자가 선택한 영화의 인덱스
        if (selectedMovieIndex == -1) {
            JOptionPane.showMessageDialog(this, "영화를 선택해주세요!", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        TicketingViewDB.RMovie selectedMovie = TicketingView.RMovies.get(selectedMovieIndex);
        int mLimits = selectedMovie.getMovieLimits();
        
        // 확인 버튼을 눌렀을 때 나오는 다이얼로그 창 코드
        confirmButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField1.getText() + "-" + phoneField2.getText() + "-" + phoneField3.getText();
            String birthYear = birthField1.getText();
            String birthMonth = birthField2.getText();
            String birthDay = birthField3.getText();

            System.out.println("이름: " + name);
            System.out.println("전화번호: " + phone);
            System.out.println("생년월일: " + birthYear);
       
            // 생년월일 검증 및 나이 계산
            try {
                // 생년월일을 기반으로 나이 계산
                int birthYearInt = Integer.parseInt(birthYear);
                int birthMonthInt = Integer.parseInt(birthMonth);
                int birthDayInt = Integer.parseInt(birthDay);

                int currentYear = java.time.Year.now().getValue();
                java.time.LocalDate today = java.time.LocalDate.now();
                java.time.LocalDate birthDate = java.time.LocalDate.of(birthYearInt, birthMonthInt, birthDayInt);

                int age = currentYear - birthYearInt;
                if (today.isBefore(birthDate.withYear(currentYear))) {
                    age--; // 생일이 지나지 않은 경우 나이를 -1
                }
                
                // TicketingView의 tickObject에서 예매 정보 가져오기
                TicketingView.tickObject ticketInfo = TicketingView.getTobj();
                int adultCount = ticketInfo.adultCount;
                int teenagerCount = ticketInfo.teenagerCount;   
                
                // 나이에 따른 성인/청소년 검증
                boolean isAdult = age >= 19;
                boolean isTeenager = age >= 1 && age <= 18;
                
                if ((isAdult && adultCount == 0) || (isTeenager && teenagerCount == 0)) {
                    JOptionPane.showMessageDialog(
                        this,
                        "선택된 인원 정보와 생년월일이 일치하지 않습니다.",
                        "오류",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                
                if (age < mLimits) {
                    // 나이가 미성년자일 경우 예매 실패 다이얼로그
                    String movieRatingMessage = "";
                    if (mLimits == 12) {
                        movieRatingMessage = "<span style='color: #FFCC00;'>[12세 이상 관람가]</span>";
                    } else if (mLimits == 15) {
                        movieRatingMessage = "<span style='color: #FF8C00;'>[15세 이상 관람가]</span>";
                    } else if (mLimits == 19) {
                        movieRatingMessage = "<span style='color: #B22222;'>[청소년 관람불가]</span>";
                    }
                    
                    JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>"
                            + "<b>예매 실패!</b><br><br>본 영화는 " + movieRatingMessage + " 영화입니다.<br>"
                            + "(단, 만 " + mLimits + "세 미만 고객은 만 19세 이상<br>성인 보호자 동반 시 관람 가능하므로<br>"
                            + "보호자께서 예매하실 수 있습니다.)"
                            + "</div></html>");
                    
                    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    JOptionPane optionPane = new JOptionPane(
                            messageLabel,
                            JOptionPane.PLAIN_MESSAGE
                    );
	                
                    JDialog dialog = optionPane.createDialog(this, "경고");
                    dialog.setIconImage(img); // 아이콘 설정
	                
                    // 다이얼로그 OK 버튼 클릭 시 main.java로 돌아가기
                    JButton okButton = new JButton("OK");
                    okButton.addActionListener(e1 -> {
                        dialog.dispose(); // 다이얼로그 닫기
                        main.showMainWindow(); // main 클래스를 다시 실행하여 메인 화면으로 돌아가기
                        dispose(); // 현재 창 닫기
                    });
                    optionPane.setOptions(new Object[] {okButton}); // OK 버튼을 옵션으로 추가   

                    dialog.setVisible(true);
            	} else {
                    // 예매 성공 다이얼로그
            		String birth = birthYear  + "-" + birthMonth  + "-" + birthDay;
            		userinfo = new userInfo(name, phoneField1.getText() + phoneField2.getText() + phoneField3.getText(), birth);
                	PersonalInfoInputGUIDB.insertReservation();
                	
                    JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>"
                        + "<b>예매 성공!</b><br><br>즐거운 관람 되세요."
                        + "</div></html>");
                    messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    JOptionPane optionPane = new JOptionPane(
                            messageLabel,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    JDialog dialog = optionPane.createDialog(this, "알림");
                    dialog.setIconImage(img); // 아이콘 설정
                    
//	                // 다이얼로그 OK 버튼 클릭 시 main.java로 돌아가기
	                JButton okButton = new JButton("OK");
	                okButton.addActionListener(e1 -> {
	                    dialog.dispose(); // 다이얼로그 닫기
	                    main.showMainWindow(); // main 클래스를 다시 실행하여 메인 화면으로 돌아가기
	                    dispose(); // 현재 창 닫기
	                });
	                optionPane.setOptions(new Object[] {okButton}); // OK 버튼을 옵션으로 추가   
	                
                    dialog.setVisible(true);
                	
                    
                }
            } catch (Exception ex) {
                // 잘못된 생년월일 입력 시 경고 메시지
                JOptionPane.showMessageDialog(
                        this,
                        "올바른 생년월일을 입력해주세요 (예: 2000/01/01)",
                        "입력 오류",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
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
    }
    
    
    
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

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= limit) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        });
        return textField;
    }
    

    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(() -> new PersonalInfoInputGUI().setVisible(true));
    }
}
