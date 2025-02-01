package DB;

import javax.swing.*;
import java.sql.*;

public class conn {
	public static String picUrl() {
		return "C:\\Users\\sung\\Documents\\MovieReservation\\MovieReservation\\";
	}
    public static final String databaseDriver = "com.mysql.cj.jdbc.Driver";
    public static final String databaseUrl = "jdbc:mysql://localhost:3306/movie?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF8"; //DB이름 입력구간
    public static final String databaseUser = "root";
    public static final String databasePassword = "";
    public static Connection connection = null;

    public static void main(String[] args) {
        connect();
        close();
    }

    public static Connection connect() { 
        try {
            Class.forName(databaseDriver);
            connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
            if (connection != null) {
            	System.out.println("Connection Succeed");
            }
            else {
            	System.out.println("Connection Failed");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "데이터베이스가 연결되지 않았습니다", "경고!!", JOptionPane.WARNING_MESSAGE);
            System.err.println("Connection Error! : " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void close() { 
        try {
            if (connection != null) {
                System.out.println("Connection Close");
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Connection Closing Failed! : " + e.getMessage());
            e.printStackTrace();
        }
    }


}
