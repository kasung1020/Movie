package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import GUI.MovieReservationSystem;

public class main {
    public static void main(String[] args) {
        showMainWindow(); // 메인 화면 표시 메서드 호출
    }
    
    public static void showMainWindow() {
    	MovieReservationSystem movieSet = new MovieReservationSystem();
    	movieSet.setmovieId(0);      
        JFrame frame = new JFrame("POLY CINEMA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450); // 프레임 크기 설정
        frame.setLocationRelativeTo(null); // 화면 중앙 배치

        // JPanel 생성 및 레이아웃 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Y축 방향으로 배치
        panel.setBackground(Color.WHITE); // 배경을 흰색으로 설정

        // 로고 이미지 로드 및 크기 조정
        ImageIcon originalLogoIcon = new ImageIcon("bin/image/logo.png"); // 로고 파일 경로 입력
        Image resizedLogoImage = originalLogoIcon.getImage().getScaledInstance(470, 150, Image.SCALE_SMOOTH); // 로고 크기 조정
        ImageIcon resizedLogoIcon = new ImageIcon(resizedLogoImage);

        // 로고 라벨 생성
        JLabel logoLabel = new JLabel("", JLabel.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setIcon(resizedLogoIcon); // 크기 조정된 로고 삽입
        logoLabel.setHorizontalTextPosition(JLabel.CENTER); // 텍스트 위치 설정
        logoLabel.setVerticalTextPosition(JLabel.BOTTOM); // 텍스트 위치 설정
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 수평 중앙 정렬
        
        // 로고 위쪽 여백 추가 (20px)
        panel.add(Box.createVerticalStrut(30));

        // 버튼 패널 생성 (FlowLayout 사용)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 버튼 중앙 배치, 가로 및 세로 여백 설정
        buttonPanel.setBackground(Color.WHITE); // 버튼 패널 배경을 흰색으로 설정

        // 버튼 생성
        JButton listButton = new JButton("영화 목록");
        JButton bookButton = new JButton("티켓 예매");
        JButton checkButton = new JButton("예매내역 확인");

        // 버튼 크기 설정 (너비와 높이를 지정)
        listButton.setPreferredSize(new Dimension(150, 50));
        bookButton.setPreferredSize(new Dimension(150, 50));
        checkButton.setPreferredSize(new Dimension(150, 50));

        // "목록" 버튼 액션 리스너 추가
        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // MovieReservationSystem 창 열기
                SwingUtilities.invokeLater(() -> {
                    new MovieReservationSystem().setVisible(true);
                });
                frame.dispose(); // 현재 창 닫기
            }
        });

        // "티켓 예매" 버튼 액션 리스너 (임시 액션)
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TicketingView 창 열기
                SwingUtilities.invokeLater(() -> {
                    new TicketingView().setVisible(true); // TicketingView 창 표시
                });
                frame.dispose(); // 현재 창 닫기
            }
        });

        // "예매내역 확인" 버튼 액션 리스너 추가
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 개인정보 입력 화면으로 이동
                SwingUtilities.invokeLater(() -> {
                    new UserInfoInputScreen().setVisible(true); // 개인정보 입력 화면 호출
                });
                frame.dispose(); // 현재 창 닫기
            }
        });


        // 버튼 추가
        buttonPanel.add(listButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(checkButton);

        // 패널에 로고와 버튼 패널 추가
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(40)); // 로고와 버튼 사이 간격 추가
        panel.add(buttonPanel);

        // 프레임에 패널 추가
        frame.add(panel);

        // 프레임 표시
        frame.setVisible(true);
    }
}
