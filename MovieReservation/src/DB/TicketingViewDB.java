package DB;

import java.sql.*;
import java.util.ArrayList;

public class TicketingViewDB {
	// 영화 정보를 담을 Movie 클래스 (내부 클래스)
    public static class RMovie {
        private int movieId;
        private String movieName;
        private int movieLimits;
        private int moviePrice;
        private java.sql.Date movieStartDay;
        private java.sql.Date movieEndDay;
        private String moviePicture;

        public RMovie(int movieId, String movieName, int movieLimits, int moviePrice,
                     java.sql.Date movieStartDay, java.sql.Date movieEndDay, String moviePicture) {
            this.movieId = movieId;
            this.movieName = movieName;
            this.movieLimits = movieLimits;
            this.moviePrice = moviePrice;
            this.movieStartDay = movieStartDay;
            this.movieEndDay = movieEndDay;
            this.moviePicture = moviePicture;
        }

        // Getter 메서드들
        public int getMovieId() { return movieId; }
        public String getMovieName() { return movieName; }
        public int getMovieLimits() { return movieLimits; }
        public int getMoviePrice() { return moviePrice; }
        public java.sql.Date getMovieStartDay() { return movieStartDay; }
        public java.sql.Date getMovieEndDay() { return movieEndDay; }
        public String getMoviePicture() { return moviePicture; }
    }

    // 영화 데이터를 데이터베이스에서 불러오는 메서드
    public static ArrayList<RMovie> loadMoviesFromDatabase() {
        ArrayList<RMovie> movieList = new ArrayList<>();
        conn connt = new conn();
        Connection conn = DBconnect.connect();
        String query = "SELECT m_id, m_name, m_limits, m_price, m_start_day, m_end_day, m_picture FROM movie";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
            	RMovie movie = new RMovie(
                    rs.getInt("m_id"),
                    rs.getString("m_name"),
                    rs.getInt("m_limits"),
                    rs.getInt("m_price"),
                    rs.getDate("m_start_day"),
                    rs.getDate("m_end_day"),
                    connt.picUrl()+rs.getString("m_picture")
                );
                movieList.add(movie);
            }

        } catch (SQLException e) {
            System.err.println("영화 데이터를 불러오는 데 실패했습니다: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBconnect.close();
        }
        return movieList;
    }

}
