package DB;

import java.sql.*;
import java.util.Vector;

import DB.TicketingViewDB.RMovie;
import GUI.SeatView;
import GUI.TicketingView;
import GUI.TicketingView.tickObject;
import GUI.PersonalInfoInputGUI;
import GUI.PersonalInfoInputGUI.userInfo;;

public class PersonalInfoInputGUIDB {
	
	public static Connection connection = null;

	public static void main(String[] args){
		conn conn = new conn();
		connection = conn.connect();
		//insertReservation();
	}
    // 예약정보 넣기
    public static void insertReservation() {
    	
    	conn conn = new conn();
		connection = conn.connect();
		//예매정보 가져오기
		tickObject tick = TicketingView.getTobj();
    	Vector<String> seatNum = SeatView.getseatNum();
    	userInfo userinfo = PersonalInfoInputGUI.getuserinfo();
    	java.sql.Time sqlTime  = java.sql.Time.valueOf(tick.time + ":00");
    	
    	int userid = 0;
    	
    	//이미 있는 유저인가 확인
    	String select = "select u_id from user where u_name = ? and phone = ? and birth = ?";
    	try (PreparedStatement u_existence = connection.prepareStatement(select)) {
    		u_existence.setString(1, userinfo.u_name);
    		u_existence.setString(2, userinfo.phone);
    		u_existence.setDate(3, Date.valueOf(userinfo.birth));
            try (ResultSet rs = u_existence.executeQuery()) {
            	//신규가입자
            	if (!rs.isBeforeFirst()) { 
                	//유저정보 넣기
                	String query = "insert into user (u_name, phone, birth) VALUES (?, ?, ?)";
                	try (PreparedStatement u_insert = connection.prepareStatement(query)) {
                		u_insert.setString(1, userinfo.u_name);
                		u_insert.setString(2, userinfo.phone);
                		u_insert.setDate(3, Date.valueOf(userinfo.birth));
                	    int rowsInserted = u_insert.executeUpdate();
                	    if (rowsInserted > 0) {
                	    	//유저정보 가져오기
                	    	query = "select u_id from user where u_name = ? and phone = ? and birth = ?";
                	    	try (PreparedStatement u_infoget = connection.prepareStatement(query)) {
                	    		u_infoget.setString(1, userinfo.u_name);
                	    		u_infoget.setString(2, userinfo.phone);
                	    		u_infoget.setDate(3, Date.valueOf(userinfo.birth));
                	            try (ResultSet rs1 = u_infoget.executeQuery()) {
                	                while (rs1.next()) {
                	                	userid = rs1.getInt("u_id");
                	                }
                	                //예약 정보 넣기
                	                query = "insert into reservation(m_id, u_id, theater, showtime, screenDay, r_seat, area) VALUES (?, ?, ?, ?, ?, ?, ?)";

                	                try (PreparedStatement reservation = connection.prepareStatement(query)) {
                	                	for (int i = 0; i < seatNum.size(); i++) {
                	                		reservation.setInt(1, tick.m_id);
                	                		reservation.setInt(2, userid);
                	                		reservation.setString(3, tick.theater);
                	                		reservation.setTime(4, sqlTime);
                	                		reservation.setDate(5, Date.valueOf(tick.date));
                	                		reservation.setString(6, seatNum.get(i));
                	                		reservation.setString(7, tick.area);
                	                		reservation.addBatch();
                	                	}
                	                	reservation.executeBatch();
                	                }
                	            }
                	        }
                	        
                	        
                	    }
                	}
                } else {
                	//아이디 있음
                    while (rs.next()) {
                        userid = rs.getInt("u_id");
                    }
	                //예약 정보 넣기
	                String query = "insert into reservation(m_id, u_id, theater, showtime, screenDay, r_seat, area) VALUES (?, ?, ?, ?, ?, ?, ?)";

	                try (PreparedStatement reservation = connection.prepareStatement(query)) {
	                	for (int i = 0; i < seatNum.size(); i++) {
	                		reservation.setInt(1, tick.m_id);
	                		reservation.setInt(2, userid);
	                		reservation.setString(3, tick.theater);
	                		reservation.setTime(4, sqlTime);
	                		reservation.setDate(5, Date.valueOf(tick.date));
	                		reservation.setString(6, seatNum.get(i));
	                		reservation.setString(7, tick.area);
	                		reservation.addBatch();
	                	}
	                	reservation.executeBatch();
	                }
                }

            }
        } catch (SQLException e) {
            System.err.println("SQL 실행 중 에러 발생: " + e.getMessage());
            e.printStackTrace();
        }
        

    }
}
