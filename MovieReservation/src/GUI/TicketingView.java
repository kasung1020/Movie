package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.toedter.calendar.JCalendar;

import GUI.MovieReservationSystem;
import DB.TicketingViewDB;
import DB.MovieReservationSystemDB.Movie;
import DB.TicketingViewDB.RMovie;

public class TicketingView extends JFrame implements ActionListener,ListSelectionListener, ItemListener{
	
	public class tickObject {
		public int m_id;
		public String area;
		public String date;
		public String time;
		public String theater;
		public int u_num;
	    public int adultCount;   // 성인 수
	    public int teenagerCount; // 청소년 수
		public int price;
		
		public tickObject(int m_id, String area, String date, String time, String theater, int u_num, int adultCount, int teenagerCount, int price) {
			this.m_id = m_id;
			this.area = area;
			this.date = date;
			this.time = time;
			this.theater = theater;
			this.u_num = u_num;
	        this.adultCount = adultCount;
	        this.teenagerCount = teenagerCount;
			this.price = price;
		}
	}
	
	private static tickObject tobj;
	private String moviename;
	private String picturePath;

	public TicketingView(String moviename) {
	    this.moviename = moviename; // 영화 제목 저장
	    
	}
    public static tickObject getTobj() {
        return tobj;
    }
    
    //tickObject 초기화용 메서드
    public void setTickObject(int movieId, String area, String date, String time, String theater, int userCount, int adultCount, int teenagerCount, int price) {
        tobj = new tickObject(movieId, area, date, time, theater, userCount, adultCount, teenagerCount, price);
        // 디버깅용 출력
      //  System.out.println("영화 ID: " + movieId + ", 지역: " + area + ", 날짜: " + date + ", 시간: " + time);
        System.out.println("영화 ID: " + movieId + ", 지역: " + area + ", 날짜: " + date + ", 시간: " + time);
        System.out.println("성인: " + adultCount + "명, 청소년: " + teenagerCount + "명");
    }
	
	private static final long serialVersionUID = 1L;
	
	private Calendar today = Calendar.getInstance();
	
	//좌석 선택
	private SeatView seatView; 
	
	// GUI
	private JPanel mainPanel = new JPanel();
	private JPanel mNorthPanel = new JPanel();
	private JPanel mCenterPanel = new JPanel();
	private JPanel mEastPanel = new JPanel();
	//내가 추가한 상단 패널
	private JPanel mTopPanel = new JPanel();
	
	//포스터 동적으로 넣기. 
	private ImgPanel imgPanel;
	private String movieImgSrc;
	
	// 이거는 DB에서 사진 불러왔을때 쓸 것!!!!!!!!!!
	class ImgPanel extends JPanel {
	    private ImageIcon icon;
	    private Image img;

	    public ImgPanel() {
	        setImage(null); 
	    }

	    // 이미지 경로를 받아 이미지를 갱신
	    public void setImage(String imagePath) {
	        if (imagePath != null && !imagePath.isEmpty()) {
	            icon = new ImageIcon(imagePath);
	            img = icon.getImage();
	        } else {
	            img = null; 
	        }
	        repaint();
	    }

	    //기본배경
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
	
	private JPanel mCenterMoviePanel = new JPanel();
	private JPanel mEastSouthPanel = new JPanel();
	private JPanel mEastSouthCenterPanel = new JPanel();
	private JPanel mCenterTheatherPanel = new JPanel();
	private JPanel mCenterTimePanel = new JPanel();
	private JPanel mCenterCalendarPanel = new JPanel();
	private JPanel mCenerButtomPanel = new JPanel();
	private JPanel roomPanel = new JPanel();
	
	private JLabel theatherLabel = new JLabel("-");
	private JLabel dayLabel = new JLabel("-");
	private JLabel personLabel = new JLabel("-");
	private JLabel costLabel = new JLabel("-");
	private JLabel choiceMovieLabel = new JLabel("< 영화 선택 >");
		
	private JLabel choiceTheaterLabel = new JLabel("< 극장 선택 >");
	private JLabel choiceCalendarLabel = new JLabel("< 날짜 선택 >");

	private JCalendar calendar = new JCalendar();
	private ButtonGroup adultCount = new ButtonGroup();
	private ButtonGroup teenagerCount = new ButtonGroup();

	private JRadioButton one = new JRadioButton("1");
	private JRadioButton two = new JRadioButton("2");
	private JRadioButton three = new JRadioButton("3");
	private JRadioButton four = new JRadioButton("4");
	private JRadioButton five = new JRadioButton("5");
	private JRadioButton zero = new JRadioButton("0");
	
	private JRadioButton oneT = new JRadioButton("1");
	private JRadioButton twoT = new JRadioButton("2");
	private JRadioButton threeT = new JRadioButton("3");
	private JRadioButton fourT = new JRadioButton("4");
	private JRadioButton fiveT = new JRadioButton("5");
	private JRadioButton zeroT = new JRadioButton("0");
	
	private JButton ticketingButton = new JButton("좌석선택 >");
	private JButton ticketbackButton = new JButton("뒤로가기");
	
	//여기 관련 내용을 Database에서 꺼내게 바꾼다. 
	public static ArrayList<RMovie> RMovies;
	
	private String[] theathers = { "광주 금남로", "광주 상무", "광주 용봉", "광주 첨단", "광주 충장로", "광주 터미널", "광주 하남"};
	private String[] movieTimes = { "10:00", "11:15", "13:05", "14:20", "15:35", "16:10", "19:15", "21:45", "22:20" };
	private String[] movieRooms = { "2관", "1관", "2관", "1관", "4관", "2관", "2관", "7관", "2관", "3관", "5관" };

	public static JList<String> movielist = new JList<>();
	private JList<String> theatherlist = new JList<>(theathers);
	private JList<String> movietimelist = new JList<>();

	//check 관련
	private String movieName ="";
	private String theatherName ="";
	private String checkDay; 
	private String dbDay; 
	private int roomNumber;  //관정보  
	private String time; //회차 정보 
	
	//성인, 청소년
	private static final int ADULTCOST = 10000;
	private static final int TEENAGECOST = 8000;
	
	// 가격
	private int totalCost = 0;
	private int adultCost = 0;
	private int teenagerCost = 0;
	private int person = 0;
	
	public void setEvent() {
		movielist.addListSelectionListener(this);
		theatherlist.addListSelectionListener(this);
		movietimelist.addListSelectionListener(this);
		ticketingButton.addActionListener(this);
		one.addItemListener(this);
		two.addItemListener(this);
		three.addItemListener(this);
		four.addItemListener(this);
		five.addItemListener(this);
		oneT.addItemListener(this);
		twoT.addItemListener(this);
		threeT.addItemListener(this);
		fourT.addItemListener(this);
		fiveT.addItemListener(this);	
	}
	
    // 값 반환 확인 및 모든 값이 들어 가 있으면 좌석 선택 버튼 활성화 하는 메서드
    public void check() {	
        System.out.println("\n\n=================== 예약 확인 ===================");
        System.out.println("adultCost: " + adultCost);
        System.out.println("teenagerCost: " + teenagerCost);
        System.out.println("person: " + person);
        System.out.println("movieName: " + movieName);
        System.out.println("theatherName: " + theatherName);
        System.out.println("checkDay: " + checkDay);
        System.out.println("roomNumber: " + roomNumber);
        System.out.println("time: " + time);
        
        if (person != 0 && movieName != null && !movieName.isEmpty() &&
                theatherName != null && !theatherName.isEmpty() && 
                time != null && !time.isEmpty()) {
            ticketingButton.setEnabled(true);
            //System.out.println("Button enabled");
        } else {
            ticketingButton.setEnabled(false);
            //System.out.println("Button disabled");
        }
    }
    
    //모든 메서드 실행
	public TicketingView() {
		DBMovieInfoGet();
		buildGUI();
		setEvent();
	}
	
	//DB에서 정보 가져오기
	public void DBMovieInfoGet() {
		//영화 이름
		RMovies = TicketingViewDB.loadMoviesFromDatabase();
		
		ArrayList<String> movies = new ArrayList<>();
	    for (RMovie movie : RMovies) {
	        movies.add(movie.getMovieName());
	    }
        
        String[] updatedMovies = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            updatedMovies[i] = movies.get(i);
        }
        movielist = new JList<>(updatedMovies);
        
        //영화 상영 시간 목록
		
	}
	
	//좌석선택 버튼 눌렀을때 실행되는 메서드
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ticketingButton) {
	        // 영화 선택 인덱스 확인
	        int selectedMovieIndex = movielist.getSelectedIndex();

	        //tickObject 초기화용 
	        if (selectedMovieIndex >= 0) {
	            TicketingViewDB.RMovie selectedMovie = TicketingView.RMovies.get(selectedMovieIndex);
	            this.picturePath = selectedMovie.getMoviePicture(); // 이미지 경로 설정
	            
	            // 영화 등급 확인
	            int movieLimits = selectedMovie.getMovieLimits(); // 영화 등급 가져오기
	            int teenagerCount = getTeenagerCount(); // 선택된 청소년 인원 수 확인

	            // 청소년 관람 불가 영화인데 청소년 인원이 포함된 경우 경고 메시지 출력
	            if (movieLimits == 19 && teenagerCount > 0) {
	                JOptionPane.showMessageDialog(
	                    null,
	                    "이 영화는 청소년 관람 불가입니다. 청소년이 포함된 예매는 진행할 수 없습니다.",
	                    "경고",
	                    JOptionPane.WARNING_MESSAGE
	                );
	                
	                // 청소년 버튼 상태를 초기화 (0명으로 설정)
	                resetTeenagerButtons();
	                return; // 메서드 종료, 다음 단계로 이동하지 않음
	            }
	            
	            // 사용자 선택 값 가져오기
	            String area = this.theatherName;   // 극장 이름
	            String date = this.dbDay;       // 날짜
	            String time = this.time;           // 시간
	            String theater = movieRooms[selectedMovieIndex];  // 상영관
	            int userCount = this.person;       // 인원 수
	            int adultCount = getAdultCount();
	            int price = this.totalCost;        // 가격
	           
	            // tickObject 초기화
	            setTickObject(
	                selectedMovie.getMovieId(),
	                area,
	                date,
	                time,
	                theater,
	                userCount,
	                adultCount,
	                teenagerCount,
	                price
	            );
	            
	            
	        } else {
	            return;  // 영화가 선택되지 않았다면 메서드 종료
	        }
	        
	        String theaterName = this.theatherName;  // 예매된 극장 이름
	        String day = this.dbDay;              // 예매된 날짜
	        int person = this.person;                // 예매된 
	        int cost = this.totalCost;               // 계산된 요금 
	        String showtime = this.time;
	        
			seatView = new SeatView(theaterName, day, person, cost, showtime, this.picturePath); 
			this.dispose();
		}
	}

	// 청소년 버튼 상태를 초기화하는 메서드
	private void resetTeenagerButtons() {
	    oneT.setSelected(false);
	    twoT.setSelected(false);
	    threeT.setSelected(false);
	    fourT.setSelected(false);
	    fiveT.setSelected(false);
	    zeroT.setSelected(true);
	    
	    // 청소년 요금 및 인원 초기화
	    teenagerCost = 0;
	    person -= getTeenagerCount();
	    check();
	}
	
	//성인, 청소년 요금 계산 메서드
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		
	    // adult
	    if (source == one) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            adultCost += ADULTCOST;
	            person += 1;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            adultCost -= ADULTCOST;
	            person -= 1;
	        }
	    } else if (source == two) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            adultCost += ADULTCOST * 2;
	            person += 2;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            adultCost -= ADULTCOST * 2;
	            person -= 2;
	        }
	    } else if (source == three) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            adultCost += ADULTCOST * 3;
	            person += 3;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            adultCost -= ADULTCOST * 3;
	            person -= 3;
	        }
	    } else if (source == four) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            adultCost += ADULTCOST * 4;
	            person += 4;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            adultCost -= ADULTCOST * 4;
	            person -= 4;
	        }
	    } else if (source == five) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            adultCost += ADULTCOST * 5;
	            person += 5;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            adultCost -= ADULTCOST * 5;
	            person -= 5;
	        }
	    }

	    // teenage
	    if (source == oneT) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            teenagerCost += TEENAGECOST;
	            person += 1;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            teenagerCost -= TEENAGECOST;
	            person -= 1;
	        }
	    } else if (source == twoT) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            teenagerCost += TEENAGECOST * 2;
	            person += 2;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            teenagerCost -= TEENAGECOST * 2;
	            person -= 2;
	        }
	    } else if (source == threeT) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            teenagerCost += TEENAGECOST * 3;
	            person += 3;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            teenagerCost -= TEENAGECOST * 3;
	            person -= 3;
	        }
	    } else if (source == fourT) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            teenagerCost += TEENAGECOST * 4;
	            person += 4;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            teenagerCost -= TEENAGECOST * 4;
	            person -= 4;
	        }
	    } else if (source == fiveT) {
	        if (e.getStateChange() == ItemEvent.SELECTED) {
	            teenagerCost += TEENAGECOST * 5;
	            person += 5;
	        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
	            teenagerCost -= TEENAGECOST * 5;
	            person -= 5;
	        }
	    }
		
		totalCost = adultCost + teenagerCost;
		String totalCostStr = Integer.toString(totalCost);
		String personStr = Integer.toString(person);
		
		costLabel.setText(totalCostStr +" 원");
		//성인, 청소년 따로 적게 하는 코드
		personLabel.setText("성인: " + getAdultCount() + "명, 청소년: " + getTeenagerCount() + "명");
		personLabel.setText(personStr + " 명");
		
		check();
	}
	
	// 성인 인원 반환 메서드
	private int getAdultCount() {
	    return adultCost / ADULTCOST;
	}

	// 청소년 인원 반환 메서드
	private int getTeenagerCount() {
	    return teenagerCost / TEENAGECOST;
	}
	
	// 상영 시간 및 상영관 리스트 생성 메서드
	private String[] generateMovieTimeAndRoomList() {
	    String[] timeAndRoomList = new String[movieTimes.length];
	    for (int i = 0; i < movieTimes.length; i++) {
	        timeAndRoomList[i] = movieTimes[i] + ", " + movieRooms[i];
	    }
	    return timeAndRoomList;
	}
	
	// 영화 DB 가져와서 선택했을때 이미지 동적으로 가져오기, 시간과 상영관 출력 메서드
//	@Override
	public void valueChanged(ListSelectionEvent e) {
	    if (e.getSource() == movielist && movielist.getSelectedValue() != null) {
	        movieName = movielist.getSelectedValue().trim();

	        for (RMovie movie : RMovies) {
	            if (movie.getMovieName().equals(movieName)) {
	                String picturePath = movie.getMoviePicture();
	                // UI 업데이트 (예: 포스터 업데이트)
	                imgPanel.setImage(picturePath);
	                break;
	            }
	        }
	        movietimelist.setListData(generateMovieTimeAndRoomList());
	    }

	    if (e.getSource() == theatherlist && theatherlist.getSelectedValue() != null) {
	        theatherName = theatherlist.getSelectedValue().trim();
	        theatherLabel.setText(theatherName);
	    }

	    if (e.getSource() == movietimelist && movietimelist.getSelectedValue() != null) {
	        String selectedTimeAndRoom = movietimelist.getSelectedValue();
	        String[] parts = selectedTimeAndRoom.split(",");

	        if (parts.length == 2) {
	            time = parts[0].trim();
	            roomNumber = Integer.parseInt(parts[1].replaceAll("[^0-9]", "").trim());
	        }
	    }

	    check();
	}	
    
	//GUI 디자인 메서드
	public void buildGUI() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image img = toolkit.getImage("src/GUI/favicon.png");
		setIconImage(img);     
		
		setTitle("티켓 예매");
		setSize(900, 652);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null); // 화면 가운데 출력시키기
		setResizable(false);
		setVisible(true);
		getContentPane().setLayout(null);
		mainPanel.setBounds(0, 0, 884, 623);
//		mainPanel.setBackground(Color.LIGHT_GRAY);
//		mainPanel.setOpaque(true);
		
		// main
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);		
		
		//상단패널 폴리시네마 및 뒤로가기 버튼 둘 곳
		mTopPanel.setBounds(0, 0, 884, 50);
		mainPanel.add(mTopPanel);
		mTopPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 50, 7)); // 오른쪽 정렬 및 버튼과 오른쪽 경계 간의 20px 여백
		
		 // "POLY CINEMA" 텍스트 추가
	    JLabel titleLabel = new JLabel("POLY CINEMA", JLabel.CENTER);
	    titleLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
	    titleLabel.setForeground(Color.BLACK); // 텍스트 색상 설정
	    mTopPanel.add(titleLabel);  // 화면 중앙에 배치
		
	    // "뒤로가기" 버튼 추가
	    JButton backButton = new JButton("뒤로가기");
	    backButton.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
	    backButton.setPreferredSize(new Dimension(115, 37)); // 버튼의 너비와 높이 설정
	    backButton.addActionListener(e -> {
	    	Object[] options = {"예", "아니오"};
            int option = JOptionPane.showOptionDialog(
                null,
                "처음 화면으로 돌아가시겠습니까?",
                "뒤로가기 확인",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            if (option == 0) {
                SwingUtilities.invokeLater(() -> new main().main(null));
                dispose();
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
	    
	    // titleLabel 와 backButton 의 중간 여백 추가하기위한 박스
	    mTopPanel.add(Box.createHorizontalStrut(100));
	    mTopPanel.add(backButton); // 화면 왼쪽에 배치
	    
		//오른쪽 하단 극장,날짜,,금액 (시안색)
		mEastPanel.setBounds(678, 58, 199, 585);
		mainPanel.add(mEastPanel);
		mEastPanel.setLayout(null);
//		mEastPanel.setBackground(Color.CYAN);
//		mEastPanel.setOpaque(true);
		
		//이미지패널 박스 
		imgPanel = new ImgPanel();
		imgPanel.setBounds(0, 20, 188, 363);
		mEastPanel.add(imgPanel);
		imgPanel.setBackground(Color.WHITE);
		imgPanel.setOpaque(true);
		
		//오른쪽 하단 극장,날짜,,금액 (노랑색)
		mEastSouthPanel.setBounds(0, 380, 188, 155);
		mEastPanel.add(mEastSouthPanel);
		mEastSouthPanel.setLayout(new BorderLayout(0, 0));
//		mEastSouthPanel.setBackground(Color.YELLOW);
//		mEastSouthPanel.setOpaque(true);
		
		// mEastSouthPanel의 여백 설정
		JLabel blank_5 = new JLabel(" ");
		JLabel blank_6 = new JLabel(" ");
		JLabel blank_7 = new JLabel(" ");
		JLabel blank_8 = new JLabel(" ");
		mEastSouthPanel.add(blank_5, BorderLayout.NORTH);
		mEastSouthPanel.add(blank_6, BorderLayout.SOUTH);
		mEastSouthPanel.add(blank_7, BorderLayout.WEST);
		mEastSouthPanel.add(blank_8, BorderLayout.EAST);

		mEastSouthPanel.add(mEastSouthCenterPanel, BorderLayout.CENTER);
		mEastSouthCenterPanel.setLayout(new GridLayout(4, 2));

		JLabel lblNewLabel_1_1 = new JLabel("\uADF9\uC7A5 :");
		JLabel lblNewLabel_5 = new JLabel("\uC778\uC6D0 :");
		JLabel lblNewLabel_7 = new JLabel("금액 :");
		
		lblNewLabel_1_1.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
		mEastSouthCenterPanel.add(lblNewLabel_1_1);
		
		theatherLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
		mEastSouthCenterPanel.add(theatherLabel);
		
		JLabel lblNewLabel_3_1 = new JLabel("\uB0A0\uC9DC :");
		lblNewLabel_3_1.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
		mEastSouthCenterPanel.add(lblNewLabel_3_1);
		dayLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
		mEastSouthCenterPanel.add(dayLabel);
		
		lblNewLabel_5.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
		mEastSouthCenterPanel.add(lblNewLabel_5);
		personLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
		mEastSouthCenterPanel.add(personLabel);
		
		lblNewLabel_7.setFont(new Font("휴먼엑스포", Font.BOLD, 14));
		mEastSouthCenterPanel.add(lblNewLabel_7);
		costLabel.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
		mEastSouthCenterPanel.add(costLabel);
		
		// 빈 셀 추가
		JPanel ticketingButtonPanel = new JPanel();
		ticketingButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // 버튼 중앙 정렬
		ticketingButtonPanel.setOpaque(false); // 배경 투명

		ticketingButton.setPreferredSize(new Dimension(110, 30)); // 버튼 크기
		ticketingButton.setFont(new Font("휴먼엑스포", Font.BOLD, 13));
		ticketingButtonPanel.add(ticketingButton);

		// ticketingButtonPanel을 mEastSouthPanel의 아래쪽에 배치
		mEastSouthPanel.add(ticketingButtonPanel, BorderLayout.SOUTH);
		
		//왼쪽 패널 부분 영역
		mCenterPanel.setBounds(0, 58, 666, 585);
//		mCenterPanel.setBackground(Color.PINK);
//		mCenterPanel.setOpaque(true);
		
		// 모든 창 패널 추가
		mainPanel.add(mCenterPanel);
		mCenterPanel.setLayout(null);
		
		// 영화 선택 밖에 박스 (초록색)
		mCenterMoviePanel.setBounds(8, 10, 140, 325);
		mCenterMoviePanel.add(movielist);
//		mCenterMoviePanel.setBackground(Color.GREEN);
//		mCenterMoviePanel.setOpaque(true);
		
		// 영화 선택
		mCenterMoviePanel.setLayout(null);
		mCenterPanel.add(mCenterMoviePanel);
		
		//영화선택 안에 박스 (빨간색)
		movielist.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		movielist.setBounds(15, 35, 122, 281);
//		movielist.setBackground(Color.RED);
//		movielist.setOpaque(true);
				
		//영화선택 위에 팻말 박스 (청록색)
		choiceMovieLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		choiceMovieLabel.setBounds(29, 10, 86, 15);
		mCenterMoviePanel.add(choiceMovieLabel);
//		choiceMovieLabel.setBackground(Color.CYAN);
//		choiceMovieLabel.setOpaque(true);
		
		//극장선택 밖에 박스 (마젠타)
		mCenterTheatherPanel.setBounds(152, 10, 135, 325);
		mCenterTheatherPanel.setLayout(null);
		mCenterTheatherPanel.add(theatherlist);
		mCenterPanel.add(mCenterTheatherPanel);
//		mCenterTheatherPanel.setBackground(Color.MAGENTA);
//		mCenterTheatherPanel.setOpaque(true);
		
		//극장선택 팻말 박스 (블루)
		choiceTheaterLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		choiceTheaterLabel.setBounds(35, 10, 80, 15);
		mCenterTheatherPanel.add(choiceTheaterLabel);
//		choiceTheaterLabel.setBackground(Color.BLUE);
//		choiceTheaterLabel.setOpaque(true);
		
		//극장선택 안에 박스 (노란색)
		theatherlist.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		theatherlist.setBounds(12, 35, 122, 280);
//		theatherlist.setBackground(Color.YELLOW);
//		theatherlist.setOpaque(true);
		
		//상영시간표 밖에 박스
		mCenterTimePanel.setBounds(502, 10, 155, 325);
		mCenterTimePanel.setLayout(null);
		mCenterTimePanel.add(movietimelist);
		mCenterPanel.add(mCenterTimePanel);
		
		//상영시간표 팻말 박스
		JLabel timeTableLabel = new JLabel("< 상영 시간표 >");
		timeTableLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		timeTableLabel.setBounds(32, 10, 97, 15);
		mCenterTimePanel.add(timeTableLabel);
//		timeTableLabel.setBackground(new Color(100, 100, 128));
//		timeTableLabel.setOpaque(true);

		//상영시간표 안에 박스 (노란색)
		movietimelist.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		movietimelist.setBounds(17, 35, 122, 280);
		MovieReservationSystem getMovieName = new MovieReservationSystem();

        for (RMovie movie : RMovies) {
    		if(getMovieName.getmovieId() == movie.getMovieId()) {
    			movieName = movie.getMovieName();
    			movielist.setSelectedIndex(movie.getMovieId()-1);
    			imgPanel.setImage(movie.getMoviePicture());
    			movietimelist.setListData(generateMovieTimeAndRoomList());
    		}
        }
		
//		movietimelist.setBackground(Color.YELLOW);
//		movietimelist.setOpaque(true);
		
		//캘린더 박스 조정 (연보라색)
		mCenterCalendarPanel.setBounds(292, 10, 201, 325);
		mCenterCalendarPanel.setLayout(null);
		mCenterPanel.add(mCenterCalendarPanel);
//		mCenterCalendarPanel.setBackground(new Color(230, 230, 250));
//		mCenterCalendarPanel.setOpaque(true);
		
		//날짜 선택 팻말 위치 조정 (민트색)
		choiceCalendarLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 13));
		choiceCalendarLabel.setBounds(72, 10, 86, 15);
		mCenterCalendarPanel.add(choiceCalendarLabel);
//		choiceCalendarLabel.setBackground(new Color(152, 255, 152));
//		choiceCalendarLabel.setOpaque(true);
		
		//캘린더 위치 조정
		calendar.setBounds(12, 35, 185, 280);
		mCenterCalendarPanel.add(calendar);
//		calendar.setBackground(new Color(25, 25, 112));
//		calendar.setOpaque(true);
		
		//하단 박스 위치 조정 (오렌지색)
		mCenerButtomPanel.setBounds(0, 345, 573, 199);
		mCenerButtomPanel.setLayout(null);
//		mCenerButtomPanel.setBackground(Color.ORANGE);
//		mCenerButtomPanel.setOpaque(true);
		
		mCenterPanel.add(mCenerButtomPanel);

		adultCount.add(one);
		adultCount.add(two);
		adultCount.add(three);
		adultCount.add(four);
		adultCount.add(five);
		adultCount.add(zero);
		teenagerCount.add(oneT);
		teenagerCount.add(twoT);
		teenagerCount.add(threeT);
		teenagerCount.add(fourT);
		teenagerCount.add(fiveT);
		teenagerCount.add(zeroT);
		one.setFont(new Font("Dialog", Font.BOLD, 9));
		one.setBounds(22, 72, 31, 29);
		mCenerButtomPanel.add(one);
		two.setFont(new Font("Dialog", Font.BOLD, 9));
		two.setBounds(57, 72, 31, 29);
		mCenerButtomPanel.add(two);
		three.setFont(new Font("Dialog", Font.BOLD, 9));
		three.setBounds(92, 72, 31, 29);
		mCenerButtomPanel.add(three);
		four.setFont(new Font("Dialog", Font.BOLD, 9));
		four.setBounds(127, 72, 31, 29);
		mCenerButtomPanel.add(four);
		five.setFont(new Font("Dialog", Font.BOLD, 9));
		five.setBounds(162, 72, 31, 29);
		mCenerButtomPanel.add(five);
		zero.setFont(new Font("Dialog", Font.BOLD, 9));
		zero.setBounds(197, 72, 31, 29);
		mCenerButtomPanel.add(zero);
		
		oneT.setFont(new Font("Dialog", Font.BOLD, 9));
		oneT.setBounds(22, 137, 31, 29);
		mCenerButtomPanel.add(oneT);
		twoT.setFont(new Font("굴림", Font.BOLD, 9));
		twoT.setBounds(57, 137, 31, 29);
		mCenerButtomPanel.add(twoT);
		threeT.setFont(new Font("굴림", Font.BOLD, 9));
		threeT.setBounds(92, 137, 31, 29);
		mCenerButtomPanel.add(threeT);
		fourT.setFont(new Font("굴림", Font.BOLD, 9));
		fourT.setBounds(127, 137, 31, 29);
		mCenerButtomPanel.add(fourT);
		fiveT.setFont(new Font("굴림", Font.BOLD, 9));
		fiveT.setBounds(162, 137, 31, 29);
		mCenerButtomPanel.add(fiveT);
		zeroT.setFont(new Font("Dialog", Font.BOLD, 9));
		zeroT.setBounds(197, 137, 31, 29);
		mCenerButtomPanel.add(zeroT);

		// 인원상태 팻말 위치 (산호색)
		JLabel headCountLabel = new JLabel(" < 인원 선택 > ");
		headCountLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 15));
		headCountLabel.setBounds(22, 10, 121, 29);
		mCenerButtomPanel.add(headCountLabel);
//		headCountLabel.setBackground(new Color(255, 127, 80));
//		headCountLabel.setOpaque(true);

		// 성인 팻말 박스 (연어색)
		JLabel adultLabel = new JLabel("성인 (10,000 원)");
		adultLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 12));
		adultLabel.setBounds(22, 54, 121, 15);
		mCenerButtomPanel.add(adultLabel);

		JLabel teenagerLabel = new JLabel("청소년 (8,000 원)");
		teenagerLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 12));
		teenagerLabel.setBounds(22, 116, 121, 15);
		mCenerButtomPanel.add(teenagerLabel);
		
		if (checkDay == null) {
		    int year = today.get(Calendar.YEAR);
		    int month = today.get(Calendar.MONTH) + 1;
		    int day = today.get(Calendar.DAY_OF_MONTH);
		    dbDay = Integer.toString(year) + "-" + Integer.toString(month) +"-" + Integer.toString(day);
		    checkDay = Integer.toString(year) + "년 " + Integer.toString(month) +"월 " + Integer.toString(day) +"일";
		    dayLabel.setText(checkDay); // 레이블에 기본 날짜 표시
		}
			
		ticketingButton.setEnabled(false);

		calendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				final Calendar c = (Calendar) e.getNewValue();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH)+1;
				int day = c.get(Calendar.DAY_OF_MONTH);

				dbDay = Integer.toString(year) + "-" + Integer.toString(month) +"-" + Integer.toString(day);
				checkDay = Integer.toString(year) + "년 " + Integer.toString(month) +"월 " + Integer.toString(day) +"일";
				dayLabel.setText(checkDay);
			}
		});
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}

    public static void main(String[] args) {
                new TicketingView();
        }
   
}