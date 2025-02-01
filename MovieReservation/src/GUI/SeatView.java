package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;

import DB.conn;
import DB.TicketingViewDB.RMovie;
import GUI.TicketingView;
import GUI.TicketingView.tickObject;

public class SeatView extends JFrame implements ActionListener, ItemListener {
	private static final int TOTAL_SEATS = 255; // 전체 좌석 수
	private int reservedCount = 0; // 예약된 좌석 수
	
	private ArrayList<String> reservedSeat = new ArrayList<String>();
	
	private String movieImgSrc;
	private int person = 0;// 좌석에 등록할 인원수.
	private int personCheck = 0; // 좌석 등록할 인원수 check용
	
	// 행, 열
    private JCheckBox[][] seats = new JCheckBox[15][15];
    private static int test = 0;
    public static int gettest() {
    	return test;
    }
    private static Vector<String> seatsNumber = new Vector<String>(); // 여기에 check한 좌석이 저장된다.
    public static Vector<String> getseatNum() {
        return seatsNumber;
    }
    private Vector<JLabel> seatDetails = new Vector<>();
    
    private JPanel mainPanel = new JPanel();
	private ImgPanel imgPanel;
	
	private JPanel titlePanel = new JPanel();
	private JPanel leftPanel = new JPanel();
	private JPanel seatPanel = new JPanel();
	private JPanel panel_1 = new JPanel();
	private JPanel panel_2 = new JPanel();
	private JPanel leftBottomPanel = new JPanel();
	private JLabel lblNewLabel_1_1 = new JLabel("극장 :");
	private JLabel lblNewLabel_3_1 = new JLabel("날짜 :");
    private JLabel lblNewLabel_5 = new JLabel("인원:");
    private JLabel lblNewLabel_7 = new JLabel("요금:");

	private JLabel theatherLabel = new JLabel("-");
	private JLabel dayLabel = new JLabel("-");
	private JLabel personLabel = new JLabel("-");
	private JLabel costLabel = new JLabel("-");
	 
    private JLabel checkSeatLabel = new JLabel("좌석 확인");
    private JButton ticketingButton = new JButton("예매하기");
	private JButton reCheckSeatButton = new JButton("다시 선택");
    private JLabel seatInfoLabel = new JLabel("선택 좌석:"); 
    private JLabel seatInfoDetailLabel = new JLabel(TOTAL_SEATS + " / " + TOTAL_SEATS);

    // 이미지 패널 클래스
    class ImgPanel extends JPanel {
        private ImageIcon icon;
        private Image img;

        public ImgPanel() {
            setImage(null);
        }

        public void setImage(String imagePath) {
            if (imagePath != null && !imagePath.isEmpty()) {
                icon = new ImageIcon(imagePath);
                img = icon.getImage();
            } else {
                img = null;
            }
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            Dimension d = getSize();
            if (img != null) {
                g.drawImage(img, 0, 0, d.width, d.height, null);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, d.width, d.height);
            }
        }
    }
	
    public SeatView(String theaterName, String day, int person, int cost, String showtime, String movieImgSrc) {
    	this.person = person;
    	this.movieImgSrc = movieImgSrc; // 포스터 이미지 경로 저장
    	
        setTitle("좌석 선택");
        setSize(818, 632);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(null);
        
        // 데이터 적용
        theatherLabel.setText(theaterName);
        dayLabel.setText(day);
        personLabel.setText(person + "명");
        costLabel.setText(cost + "원");
        
		//이미지 판넬이 들어감
		imgPanel = new ImgPanel();
		imgPanel.setBounds(0, 0, 180, 383);
		leftPanel.add(imgPanel);
		imgPanel.setLayout(null);
		imgPanel.setBackground(Color.WHITE);
		imgPanel.setOpaque(true);
		
		imgPanel.setImage(movieImgSrc);
		
		setPerson();
		buildGUI();
		setEvent();
	    
	    // 좌석 번호를 업데이트하는 메서드(두개)
	    updateSeatDetails();  // 계속 최신화 (선택된 좌석 번호 반영)
	    resetSeats(); // 리셋하여 좌석 초기화 하는 메서드
	    
	    // 예약된 좌석 가져오기
		loadReservedSeatsFromDB(theaterName, day, showtime); // DB 데이터 로드 및 UI 반영
		
        setVisible(true);
    }

	public void setPerson() {
		for (int i = 0; i < person; i++) {
			JLabel jLabel = new JLabel("좌석 " + (i + 1));
			jLabel.setBounds(11, (35 + (i * 25)), 57, 15);
			jLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 10));
			jLabel.setForeground(Color.GRAY);

			// Vector에 넣어서 관리가능하도록 한다.
			seatDetails.add(jLabel);
			panel_1.add(jLabel);
		}
	}

	// 좌석 초기화 메서드 추가
	private void resetSeats() {
	    // 모든 체크박스를 초기화
	    for (int j = 0; j < 15; j++) {
	        for (int i = 0; i < 15; i++) {
	            seats[j][i].setSelected(false);  // 선택 해제
	            seats[j][i].setEnabled(true);    // 다시 활성화
	        }
	    }

	    // 선택된 좌석 목록 초기화
	    seatsNumber.clear();
	    reservedSeat.clear();

	    // 좌석 정보 라벨 초기화
	    for (int i = 0; i < seatDetails.size(); i++) {
	        seatDetails.get(i).setText("좌석 " + (i + 1));  // 초기 상태로 설정
	    }

	    // 관련 상태 변수 초기화
	    personCheck = 0;
	    ticketingButton.setEnabled(false);
	    reCheckSeatButton.setEnabled(false); 
	}
	
	//다시선택 좌석 초기화 메서드
	private void reCheckSeats() {
	    // 모든 체크박스를 초기화
	    for (int j = 0; j < 15; j++) {
	        for (int i = 0; i < 15; i++) {
	            String currentSeat = seats[j][i].getText().replace(",", "");
	            // 예약된 좌석은 항상 비활성화 상태 유지
	            if (reservedSeat.contains(currentSeat)) {
	                seats[j][i].setSelected(false);  // 선택 해제
	                seats[j][i].setEnabled(false);  // 비활성화 유지
	            } else {
	                seats[j][i].setSelected(false);  // 선택 해제
	                seats[j][i].setEnabled(true);    // 활성화
	            }
	        }
	    }

	    // 선택된 좌석 목록 초기화
	    seatsNumber.clear();
	    System.out.print("예약되어있던 좌석: " + reservedSeat);

	    // 좌석 정보 라벨 초기화
	    for (int i = 0; i < seatDetails.size(); i++) {
	        seatDetails.get(i).setText("좌석 " + (i + 1));  // 초기 상태로 설정
	    }

	    // 관련 상태 변수 초기화
	    personCheck = 0;
	    ticketingButton.setEnabled(false);
	    reCheckSeatButton.setEnabled(true); 
	}
	
	//버튼을 눌렀을때 액션 메서드
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
				// 좌석 다시선택 버튼을 눌렀을때
				if (e.getSource() == reCheckSeatButton) {
					
			        // 다시선택 좌석 초기화 메서드
			        reCheckSeats();
			        personCheck = 0; // 초기화된 상태에서 인원수 확인 리셋
			        reCheckSeatButton.setEnabled(false); // 다시 선택 버튼 비활성화
			        ticketingButton.setEnabled(false);  // "예매하기" 버튼 비활성화
				}

				//예약버튼 클릭
				if (e.getSource() == ticketingButton) {
					// 좌석 예매 하기 메서드
					reserve();
				}
	}
	
	// 좌석을 완벽히 선택하면 실행되는 메서드
	public void reserve() {
	    // 좌석 번호들을 저장할 변수
	    String seatNumberCheck = "";

	    List<String> hardcodedReservedSeats = new ArrayList<>();

	    // 선택된 좌석을 예약된 좌석 목록에 추가
	    for (int i = 0; i < person; i++) {
	        String seat = seatsNumber.get(i);  // 선택한 좌석 번호
	        if (!hardcodedReservedSeats.contains(seat)) {
	            // 예약 좌석에 포함되지 않으면 예약된 것으로 처리
	            hardcodedReservedSeats.add(seat);
	        }

	        // 좌석 예약된 정보를 label에 반영
	        seatNumberCheck += (i == person - 1) ? seatsNumber.get(i) : seatsNumber.get(i) + "/";  // 마지막 좌석은 "/" 없이 추가

	        // 좌석 선택 후 체크박스 비활성화
	        int row = seat.charAt(0) - 'A';  // 행 값 (A부터 시작)
	        int col = Integer.parseInt(seat.substring(1)) - 1;  // 열 값 (1부터 시작)
	        seats[row][col].setEnabled(false);  // 선택한 좌석 비활성화
	    }

	    //좌석 업데이트 (여기서는 선택 좌석 인원 수 체크 하기 위한 용도)
        updateSeatDetails();

	    // 티켓 예매 후 확인 화면으로 이동
	    dispose();  // 현재 창 닫기
	    PersonalInfoInputGUI personalInfoInputGUI = new PersonalInfoInputGUI();  // 다음 화면 열기
	    personalInfoInputGUI.setVisible(true);
	}

	// reservation 테이블에서 데이터를 가져오는 메서드
	private void loadReservedSeatsFromDB(String area, String showDate, String showtime) {
	    String query = "SELECT r_seat FROM reservation WHERE area = ? AND screenDay = ? AND showtime = ?";
	    List<String> reservedSeatsFromDB = new ArrayList<>();

	    // conn 객체를 사용하여 연결 설정
	    conn conn = new conn();
	    Connection connection = conn.connect();

	    if (connection == null) {
	        return;
	    }

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, area);
	        stmt.setString(2, showDate);
	        stmt.setString(3, showtime); // 추가된 조건: 상영 시간

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                String seat = rs.getString("r_seat");
	                reservedSeatsFromDB.add(seat);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        conn.close(); // 연결 닫기
	    }

	    // 예약된 좌석 정보 확인
	    System.out.println("총 예약된 좌석 수: " + reservedSeatsFromDB.size());

	    // 예약된 좌석 정보를 처리
	    reservedSeat.clear();
	    reservedSeat.addAll(reservedSeatsFromDB);

	    // GUI에 예약 좌석 표시
	    for (String seat : reservedSeatsFromDB) {
	        int row = seat.charAt(0) - 'A'; // 좌석 행
	        int col = Integer.parseInt(seat.substring(1)) - 1; // 좌석 열

	        seats[row][col].setEnabled(false);  // 비활성화
	    }
	    
	    updateSeatDetails();
	}

    
	// 좌석 선택 시 좌석 번호를 업데이트하는 메서드 추가
	private void updateSeatDetails() {
        int selectedCount = seatsNumber.size();
        int remainingSeats = TOTAL_SEATS - reservedSeat.size() - selectedCount;
		
        // 좌석 정보 라벨 업데이트
        seatInfoDetailLabel.setText(remainingSeats + " / " + TOTAL_SEATS);
        
	    for (int i = 0; i < seatDetails.size(); i++) {
	        if (i < seatsNumber.size()) {
	        	seatDetails.get(i).setFont(new Font("Monospaced", Font.BOLD, 12));
	        	String spaces = "     "; // 원하는 만큼의 공백
	        	seatDetails.get(i).setText("좌석 " + (i + 1) + spaces + seatsNumber.get(i));

	        } else {
	            // 선택되지 않은 경우 초기 상태로 표시
	            seatDetails.get(i).setText("좌석 " + (i + 1));
	        }
	    }
	    // 좌석 선택이 완료되면 "다시 선택" 버튼 활성화
	    reCheckSeatButton.setEnabled(seatsNumber.size() == person);
	}

	
//	 모든 좌석 못누르게 하기.
	public void seatAlldisabledchecked() {
		for (int j = 0; j < 15; j++) {
			for (int i = 0; i < 15; i++) {
				seats[j][i].setEnabled(false);
			}
		}
		ticketingButton.setEnabled(true);
	}
	
	// 모든 좌석 누를 수있게 변경.
	public void seatAllchecked() {
		for (int j = 0; j < 15; j++) {
			for (int i = 0; i < 15; i++) {
				seats[j][i].setEnabled(true);
			}
		}
	}
	
	class SeatChangeListener implements ItemListener {

	    private Vector<String> vector = new Vector<>();
	    private Vector<JCheckBox> seatDetails = new Vector<>();
	    private int personCheck = 0;
	    private javax.swing.JButton reCheckSeatButton;
	    private java.util.List<javax.swing.JButton> seatButtons = new java.util.ArrayList<>();

	    // seatAlldisabledchecked() 메서드
	    private void seatAlldisabledchecked() {
	        // 예시로 버튼 비활성화 처리
	        reCheckSeatButton.setEnabled(false);
	    }

	    // 좌석 선택/해제 시 동작하는 메서드
	    @Override
	    public void itemStateChanged(ItemEvent e) {
	    	
			System.out.print("사람 수 : "+person+"\n\n");
			
	        JCheckBox source = (JCheckBox) e.getSource();
	        String seat = source.getText().replace(",", ""); // 좌석 번호 (예: "A1")
	        
	        // 체크박스가 체크된 경우
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            // 좌석 번호를 vector에 추가
	            if (!seatsNumber.contains(seat)) {
	                seatsNumber.add(seat);
	            } else {
	                seatsNumber.remove(seat);
	            }
	            
	            System.out.println(seatsNumber); // 디버깅: 선택된 좌석 리스트 출력
	            seatButtons.add(reCheckSeatButton);
	            
	            // 모든 좌석이 선택되면 "예매하기" 버튼 활성화 and 예약 사람수만큼 선택시 다른버튼 비활성화
	            if (seatsNumber.size() == person) {
	            	for (int i = 0; i < 15; i++) {
	        			for (int j = 0; j < 15; j++) {
	        				//좌석이 하나였을때
	        				if(seatsNumber.size() == 1) {
	        					if (seats[i][j].getText().replace(",", "").equals(seatsNumber.get(0)) == false) {
		        					seats[i][j].setEnabled(false);
		        				}
	        				}else {
	        					boolean isEnabled = false;  

	        					for (int iget = 0; iget < seatsNumber.size(); iget++) {
	        					    if (seats[i][j].getText().replace(",", "").equals(seatsNumber.get(iget))) {
	        					        isEnabled = true;
	        					        break; 
	        					    }
	        					}

	        					seats[i][j].setEnabled(isEnabled);
	        				}
	        				
	        				
	        			}
	        		}
	                System.out.println("All seats selected, enabling ticketing button.");
	                ticketingButton.setEnabled(true);  // "예매하기" 버튼 활성화
	            }
	            
	        } else { // 체크박스가 해제된 경우
	            // 좌석 번호를 vector에서 제거
	            if (seatsNumber.contains(seat)) {
	                seatsNumber.remove(seat);
	            }
	            System.out.println("Seats selected after deselection: " + seatsNumber); // 디버깅: 취소된 후 좌석 리스트 출력

	            // 좌석이 취소되면 "예매하기" 버튼 비활성화
	            if (seatsNumber.size() < person) {
	                ticketingButton.setEnabled(false);  // "예매하기" 버튼 비활성화
	            	for (int i = 0; i < 15; i++) {
	        			for (int j = 0; j < 15; j++) {
	                        String currentSeat = seats[i][j].getText().replace(",", "");
	                        // 예약된 좌석은 항상 비활성화 유지
	                        if (reservedSeat.contains(currentSeat)) {
	                            seats[i][j].setEnabled(false);
	                        } else {
	                            seats[i][j].setEnabled(true);  // 나머지 좌석 활성화
	                        }
	        				
	        			}
	        		}
	            }
	        }
	        
	        updateSeatDetails();
	    }
	}


	public void setEvent() {
	    for (int j = 0; j < 15; j++) {
	        for (int i = 0; i < 15; i++) {
	            seats[j][i].addItemListener(new SeatChangeListener());
	        }
	    }
	    reCheckSeatButton.addActionListener(this);
	    ticketingButton.addActionListener(this);
	    
	    // 예매 버튼 초기 상태 비활성화
	    ticketingButton.setEnabled(false);  // 처음에 예매 버튼 비활성화
	}
	
    public void buildGUI() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("src/GUI/favicon.png");
		setIconImage(img);
		
        //상단 패널 박스 (시안색)
        titlePanel.setBounds(0, 0, 812, 50);
        getContentPane().add(titlePanel);
		titlePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 50, 7));
		
		 // "POLY CINEMA" 텍스트 추가
	    JLabel titleLabel = new JLabel("POLY CINEMA", JLabel.CENTER);
	    titleLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
	    titleLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
	    titlePanel.add(titleLabel);  // 화면 중앙에 배치
		
	    // "뒤로가기" 버튼 추가
	    JButton backButton = new JButton("뒤로가기");
	    backButton.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
	    backButton.setPreferredSize(new Dimension(115, 37)); // 버튼의 너비와 높이 설정
	    backButton.addActionListener(e -> {
	        // 좌석 초기화
	        resetSeats();

            // 현재 창 닫기
            dispose();
            
            // TicketingView 창 호출
            SwingUtilities.invokeLater(() -> {
                new TicketingView(); // TicketingView 새 창 생성
            });
	    });
	    
	    // 뒤로가기 주변에 border 안 생기게 하기
	    backButton.setFocusable(false);
	    
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
	    
	    // titleLabel 와 backButton 의 중간 여백 추가하기위한 박스
	    titlePanel.add(Box.createHorizontalStrut(80));
	    titlePanel.add(backButton); // 화면 왼쪽에 배치
	    
        //오른쪽 패널
        leftPanel.setBounds(608, 52, 192, 531);
        getContentPane().add(leftPanel);
        leftPanel.setLayout(null);
        	
		//오른쪽 패널 전체 (노랑색)
        leftBottomPanel.setBounds(0, 373, 180, 168);
        leftPanel.add(leftBottomPanel);
        leftBottomPanel.setLayout(null);

		//극장 글자
        lblNewLabel_1_1.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
        lblNewLabel_1_1.setBounds(12, 10, 63, 25);
        leftBottomPanel.add(lblNewLabel_1_1);

		//날짜 글자
        lblNewLabel_3_1.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
        lblNewLabel_3_1.setBounds(12, 35, 63, 25);
        leftBottomPanel.add(lblNewLabel_3_1);

        //인원 글자
        lblNewLabel_5.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
        lblNewLabel_5.setBounds(12, 60, 63, 25);
        leftBottomPanel.add(lblNewLabel_5);

		//요금 글자
        lblNewLabel_7.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
        lblNewLabel_7.setBounds(12, 85, 63, 25);
        leftBottomPanel.add(lblNewLabel_7);

        //예매 글자
        ticketingButton.setFont(new Font("휴먼엑스포", Font.BOLD, 13));
        ticketingButton.setBounds(47, 114, 94, 30);
        leftBottomPanel.add(ticketingButton);

		//극장에 들어갈 변수
        theatherLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
        theatherLabel.setBounds(87, 10, 85, 25);
        leftBottomPanel.add(theatherLabel);

        dayLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
        dayLabel.setBounds(87, 34, 90, 25);
        leftBottomPanel.add(dayLabel);

        personLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
        personLabel.setBounds(87, 60, 85, 25);
        leftBottomPanel.add(personLabel);

        costLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
        costLabel.setBounds(87, 85, 85, 25);
        leftBottomPanel.add(costLabel);

        //좌석 패널
        seatPanel.setBounds(10, 41, 598, 532);
        getContentPane().add(seatPanel);
        seatPanel.setLayout(null);
        
        //좌석확인 패널
        panel_1.setBackground(Color.WHITE);
        panel_1.setBounds(465, 10, 127, 480);
        seatPanel.add(panel_1);
        panel_1.setLayout(null);
     
        // 좌석 확인 레이블 초기화 및 배치
        for (int i = 0; i < seatDetails.size(); i++) {
            JLabel seatLabel = seatDetails.get(i);
            seatLabel.setBounds(14, 40 + i * 25, 110, 20); // x, y, width, height
            panel_1.add(seatLabel);
        }
        
        
        //좌석 확인 팻말 
        checkSeatLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 13));
        checkSeatLabel.setBounds(32, 10, 60, 15);
        panel_1.add(checkSeatLabel);

        //다시선택 버튼
        reCheckSeatButton.setBounds(17, 447, 97, 23);
        panel_1.add(reCheckSeatButton);

        //좌석확인 밖에 패널
        panel_2.setBackground(Color.WHITE);
        panel_2.setBounds(12, 10, 447, 480);
        seatPanel.add(panel_2);
        panel_2.setLayout(null);
        
        //스크린 팻말
        JLabel screenLabel = new JLabel("\t\t\t\t\t\t\t\tScreen");
        screenLabel.setBackground(Color.WHITE);
        screenLabel.setForeground(Color.BLACK);
        screenLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
        screenLabel.setBounds(198, 10, 76, 15);
        panel_2.add(screenLabel);
        
        //선택좌석 밑에 작은 팻말
        seatInfoLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 12));
        seatInfoLabel.setBounds(186, 427, 59, 21);
        panel_2.add(seatInfoLabel);
        
        //선택좌석 밑에 작은 팻말 그 밑에 더 작은 팻말
        seatInfoDetailLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 12));
        seatInfoDetailLabel.setBounds(186, 449, 68, 21);
        panel_2.add(seatInfoDetailLabel);
        
        reCheckSeatButton.setEnabled(false);
        ticketingButton.setEnabled(false);
        repaint();

        // 열
        for (int i = 0; i < 15; i++) {
            JLabel seatColLabel = new JLabel();
            char input = (char) ('A' + i);
            seatColLabel.setText(Character.toString(input));
            seatColLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
            seatColLabel.setBounds(25, (72 + (i * 20)), 22, 15);
            panel_2.add(seatColLabel);
        }

        // 행
        for (int i = 0; i < 15; i++) {
            JLabel seatRowLabel = new JLabel();
            String input = Integer.toString(i + 1);
            seatRowLabel.setText(input);
            seatRowLabel.setBounds(61 + (i * 22), 55, 22, 15);
            seatRowLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
            panel_2.add(seatRowLabel);
        }
        
		// 좌석 checkBox
		for (int j = 0; j < 15; j++) {
			for (int i = 0; i < 15; i++) {
				JCheckBox chkBox = new JCheckBox("");
				chkBox.setBackground(Color.WHITE);
				chkBox.setForeground(Color.WHITE);
				chkBox.setBounds(61 + (i * 22), 72 + (j * 20), 22, 15);
				seats[j][i] = chkBox; // 좌석을 seats배열에 넣어줌. 2차원배열로 생성.
				char input = (char) (j + 65);
				chkBox.setText(input + "," + Integer.toString(i + 1)); // 좌석의 값들을 얻을 수 잇음.
				panel_2.add(chkBox);
			}
		}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	new SeatView("", "", 0, 0,"","").setVisible(true);
        });
    }

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
}