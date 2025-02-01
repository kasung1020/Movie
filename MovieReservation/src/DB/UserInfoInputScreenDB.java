package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import GUI.MovieReservationConfirmation;
import java.util.ArrayList;
import java.util.List;

public class UserInfoInputScreenDB {

    private Connection connection;

    public UserInfoInputScreenDB() {
        // DB 연결
        connection = DBconnect.connect();
    }

    /**
     * 데이터베이스에서 입력된 정보와 일치하는 사용자의 예약 정보를 가져오는 메서드
     */
    public void searchUserInfo(String name, String phone, String birthDate, JFrame currentFrame) {
        // 수정된 SQL 쿼리
        String sql = "SELECT u.u_name, m.m_name, r.area, DATE_FORMAT(r.showtime, '%H:%i') AS showtime, r.theater, GROUP_CONCAT(r.r_seat ORDER BY r.r_seat SEPARATOR ',') AS seats, r.screenDay FROM user u JOIN reservation r ON r.u_id = u.u_id "
                   + "JOIN movie m ON r.m_id = m.m_id WHERE u.u_name = ? AND u.phone = ? AND u.birth = ? GROUP BY u.u_name, m.m_name, r.area, r.theater, r.showtime, r.screenDay ORDER BY r.screenDay";

        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            // 매개변수 설정
            pst.setString(1, name);
            pst.setString(2, phone.replace("-", "")); // "-" 제거
            pst.setString(3, birthDate);

            ResultSet rs = pst.executeQuery();

            // 결과 데이터를 저장할 리스트
            List<String[]> reservationDataList = new ArrayList<>();

            // 조회 결과를 리스트에 추가
            while (rs.next()) {
                String[] data = new String[7]; // 7개 열 데이터
                data[0] = rs.getString("m_name");      // 영화 이름
                data[1] = rs.getString("area");        // 상영관 지역
                data[2] = rs.getString("showtime");    // 상영 시간
                data[3] = rs.getString("theater");     // 상영관 번호
                data[4] = rs.getString("seats");       // 예약된 좌석들 (콤마로 구분된 좌석 번호)
                data[5] = rs.getString("screenDay");   // 상영 날짜
                reservationDataList.add(data);
            }
            // 예약 정보가 없으면 경고 메시지 출력
            if (reservationDataList.isEmpty()) {
                JOptionPane.showMessageDialog(currentFrame, "일치하는 예매 정보가 없습니다.", "정보 없음", JOptionPane.WARNING_MESSAGE);
            } else {
                // 예약 정보가 있으면 다음 화면으로 이동
                String[][] reservationData = new String[reservationDataList.size()][6];
                reservationDataList.toArray(reservationData);
                SwingUtilities.invokeLater(() -> new MovieReservationConfirmation(name, reservationData).setVisible(true));
                currentFrame.dispose(); // 현재 창 닫기
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(currentFrame, "데이터베이스 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    public boolean cancelReservation(String userName, String movieName, String area, String showtime, String theater, String screenDay, String seats) {
    	String deleteSql = "DELETE r FROM reservation r " +
                "JOIN user u ON r.u_id = u.u_id " +
                "JOIN movie m ON r.m_id = m.m_id " +
                "WHERE u.u_name = ? AND m.m_name = ? AND r.area = ? AND r.theater = ? " +
                "AND r.showtime = ? AND r.screenDay = ? " +
                "AND FIND_IN_SET(r.r_seat, ?)";


        try (PreparedStatement pst = connection.prepareStatement(deleteSql)) {
        	pst.setString(1, userName);
        	pst.setString(2, movieName);
        	pst.setString(3, area);
        	pst.setString(4, theater);
        	pst.setString(5, showtime); // 상영 시간 추가
        	pst.setString(6, screenDay);
        	pst.setString(7, seats);    // 좌석 정보


            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0; // 삭제 성공 여부 반환
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // 삭제 실패
        }
    }


}
