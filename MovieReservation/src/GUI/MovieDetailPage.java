package GUI;

import DB.MovieDetailPageDB;
import DB.MovieDetailPageDB.Movie;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import DB.conn;

public class MovieDetailPage extends JFrame {

    private String moviename; // 무비선택시 받을 변수
    
    public MovieDetailPage(int movieId) {
        setTitle("영화 상세 정보");

        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // DB에서 영화 정보 가져오기
        Movie movie = MovieDetailPageDB.getMovieDetails(movieId); // movieId를 전달

        if (movie == null) {
            JOptionPane.showMessageDialog(this, "영화 정보를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 영화 상세 정보
        String movieTitle = movie.getName();
        String formatTitle = movieTitle.length() > 16 ? "<html>" + movieTitle.substring(0, 18) + "<br>" + movieTitle.substring(18) + "</html>" : movieTitle;
        String director = "감독: " + movie.getSupervision();
        String actorsList = String.join(", ", MovieDetailPageDB.getActorsByMovieId(movieId));
        String actors = "배우: " + (actorsList.length() > 15 ? actorsList.substring(0, 15) + "..." : actorsList);
        String genre = "장르: " + movie.getGenre();

        int rating = movie.getAgeLimit(); // 연령
        String country = movie.getCountry();
        String runtime = "상영시간: " + movie.getRunTime().toString(); // 'mm:ss' 형식
        String mDate = "개봉일: " + movie.getStartDay().toString();
     // 줄거리
        String plot = movie.getDetail().replace("+", "\n"); // +를 줄바꿈으로 변경


        // 상단 POLY CINEMA 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("POLY CINEMA", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 120, 10, 0));

        JButton backButton = new JButton("뒤로가기");
        backButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            dispose(); // 현재 창을 닫기
            // MovieReservationSystem 화면을 바로 띄우기
            new MovieReservationSystem().setVisible(true);
        });

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 15));
        backButtonPanel.add(backButton);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(backButtonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        
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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 포스터와 영화 정보가 포함된 상단 패널
        JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 중앙 정렬, 간격 설정

        // 포스터 추가
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(150, 200));
        conn connt = new conn();
        String imagePath = connt.picUrl() + movie.getPicture(); //경로 수정하기
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            try {
                Image img = ImageIO.read(imageFile).getScaledInstance(150, 200, Image.SCALE_SMOOTH);
                posterLabel.setIcon(new ImageIcon(img));
            } catch (IOException e) {
                posterLabel.setText("포스터 없음");
            }
        } else {
            posterLabel.setText("포스터 없음");
        }
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        upperPanel.add(posterLabel);

        // 배우 정보를 JLabel로 표시
        JLabel actorsLabel = new JLabel(actors);
//        actorsLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        actorsLabel.setToolTipText("배우: " + actorsList); // 마우스를 올리면 전체 배우 이름이 보이도록 설정
        // 영화 정보 추가
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(new JLabel("<html><h2>" + formatTitle + "</h2></html>"));
        infoPanel.add(new JLabel(director));
        infoPanel.add(actorsLabel);
        infoPanel.add(new JLabel(genre));

        if(rating != 0) {
        	infoPanel.add(new JLabel("기본정보: " + rating + "세 관람가, " + country));
        } else {
        	infoPanel.add(new JLabel("기본정보: 전체관람가, " + country));
        }
        infoPanel.add(new JLabel(runtime));
        infoPanel.add(new JLabel(mDate));
        

        // 예매하기 버튼 클릭 시 TicketingView로 이동
        JButton reserveButton = new JButton("예매하기");
        reserveButton.addActionListener(e -> {
            // "예매하기" 버튼 클릭 시 해당 영화 이름을 전역 변수에 저장
            moviename = movieTitle; // movieTitle을 moviename에 저장
        	MovieReservationSystem movieSet = new MovieReservationSystem();
        	movieSet.setmovieId(movieId); 
            // TicketingView 창 열기
            TicketingView ticketingView = new TicketingView(); // 영화 이름 전달
            ticketingView.setVisible(true);

            // 현재 창 닫기 (필요시 주석 해제)
            dispose();
        });

        infoPanel.add(Box.createVerticalStrut(20)); // 버튼과 간격 추가
        infoPanel.add(reserveButton);
        upperPanel.add(infoPanel); // 포스터 옆에 영화 정보 추가

        // 줄거리 패널 추가
        JPanel plotPanel = new JPanel(new BorderLayout());
        plotPanel.setBorder(BorderFactory.createTitledBorder("줄거리"));
        plotPanel.setPreferredSize(new Dimension(300, 600)); // 크기 조정
        plotPanel.setMaximumSize(new Dimension(400, 600));

        JTextArea plotTextArea = new JTextArea(plot);
        plotTextArea.setWrapStyleWord(true);
        plotTextArea.setLineWrap(true);
        plotTextArea.setOpaque(false);
        plotTextArea.setEditable(false);
        plotTextArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(plotTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        plotPanel.add(scrollPane, BorderLayout.CENTER);

        // plotPanel을 중앙에 배치하기 위한 wrapper 패널
        JPanel plotWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        plotWrapperPanel.add(plotPanel);

        // 메인 패널에 요소 추가
        mainPanel.add(upperPanel); // 상단: 포스터와 영화 정보
        mainPanel.add(Box.createVerticalStrut(20)); // 간격 추가
        mainPanel.add(plotPanel); // 하단: 줄거리
        mainPanel.add(Box.createVerticalGlue()); // 남은 공간을 채움

        add(mainPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int testMovieId = 7; // 테스트용 영화 ID
            new MovieDetailPage(testMovieId).setVisible(true);
        });
    }
}
