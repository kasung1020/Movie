package DB;

import javax.swing.*;
import java.sql.*;

public class SeatViewDB {
	public static Connection connection = null;

	public static void main(String[] args) {
		conn conn = new conn();
		connection = conn.connect();
		selectMovies();
		
		
	}
    // 배우 목록 조회
    public static void selectMovies() {
    	conn conn = new conn();
		connection = conn.connect();
		
        String query = "SELECT * FROM actor"; 
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int movieId = rs.getInt("m_id"); 
                String actor = rs.getString("a_name"); 
                System.out.println("Movie ID: " + movieId + "  배우이름 : " + actor);
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
