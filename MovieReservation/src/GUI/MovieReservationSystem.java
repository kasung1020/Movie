package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import DB.MovieReservationSystemDB;
import DB.MovieReservationSystemDB.Movie;

public class MovieReservationSystem extends JFrame {
    private ArrayList<Movie> movieList; // DB에서 불러온 영화 데이터를 저장
    
    private static int movieId; //무비선택시 받을 변수
    public static int getmovieId() {
    	return movieId;
    }
    public static void setmovieId(int setmovieId) {
    	movieId = setmovieId; 
    }

    public MovieReservationSystem() {
        setTitle("영화 목록");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // 상단 패널 (제목 및 뒤로가기 버튼)
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("POLY CINEMA", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("뒤로가기");
        backButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(100, 30));
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

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButtonPanel.add(backButton);
        topPanel.add(backButtonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 영화 목록 패널
        JPanel movieListPanel = new JPanel();
        movieListPanel.setLayout(new GridLayout(3, 4, 10, 10)); // 3행 4열, 간격 10px

        // 데이터베이스에서 영화 목록 불러오기
        movieList = MovieReservationSystemDB.loadMoviesFromDatabase();

        // 영화 데이터를 기반으로 영화 카드 생성
        for (Movie movie : movieList) {
            JPanel movieCard = createMovieCard(movie);
            movieListPanel.add(movieCard);
        }

        // 영화 목록을 가운데 정렬하기 위한 부모 패널
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 가로, 세로 여백 20px
        centerPanel.add(movieListPanel);

        // 스크롤 가능한 패널로 추가
        add(new JScrollPane(centerPanel), BorderLayout.CENTER);

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

    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS)); // 세로로 요소 배치
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 외부 여백 추가

        // 포스터 이미지 로드
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(150, 200));

        String imagePath = movie.getMoviePicture();
        if (imagePath != null) {
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
        } else {
            posterLabel.setText("포스터 없음");
        }

        // 포스터 클릭 시 상세 정보 페이지로 이동
        posterLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 영화 ID만 전달
                int movieId = movie.getMovieId(); // 영화 ID 추출
                new MovieDetailPage(movieId).setVisible(true); // 영화 아이디를 넘겨 MovieDetailPage로 이동
                dispose(); // 현재 창 닫기
            }
        });

        // 영화 제목
        String movieTitle = movie.getMovieName();
        if (movieTitle.length() > 10) {
            movieTitle = movieTitle.substring(0, 10) + "...";
        }
        JLabel titleLabel = new JLabel(movieTitle);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        // 영화 제목 클릭 시 상세 정보 페이지로 이동
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 영화 ID만 전달
                int movieId = movie.getMovieId(); // 영화 ID 추출
                new MovieDetailPage(movieId).setVisible(true); // 영화 아이디를 넘겨 MovieDetailPage로 이동
                dispose(); // 현재 창 닫기
            }
        });

        // 예매율과 개봉일 패널 (한 줄, 글씨 크기 축소)
        JLabel infoLabel = new JLabel("예매율: " + movie.getMovieId() + "위  |  개봉일: " + movie.getMovieStartDay());
        infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 9));

     // 예매하기 버튼
        JButton reserveButton = new JButton("예매하기");
        reserveButton.addActionListener(e -> {
            // "예매하기" 버튼 클릭 시 해당 영화 이름을 전역 변수에 저장
            movieId = movie.getMovieId();

            // TicketingView 창 열기
            TicketingView ticketingView = new TicketingView();
            ticketingView.setVisible(true);

            // 현재 창 닫기 (필요시 주석 해제)
            dispose();
        });

        // 좌측 정렬 설정
        posterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        reserveButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 요소 추가
        card.add(posterLabel);
        card.add(Box.createVerticalStrut(10)); // 요소 간의 간격
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(infoLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(reserveButton);

        return card;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieReservationSystem().setVisible(true));
    }
}

