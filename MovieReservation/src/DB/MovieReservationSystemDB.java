package DB;

import java.sql.*;
import java.util.ArrayList;

public class MovieReservationSystemDB {
    // 영화 정보를 담을 Movie 클래스 (내부 클래스)
    public static class Movie {
        private int movieId;
        private String movieName;
        private String movieDetail;
        private java.sql.Time movieRunTime;
        private int movieLimits;
        private String movieSupervision;
        private String movieGenre;
        private String movieCountry;
        private int moviePrice;
        private java.sql.Date movieStartDay;
        private java.sql.Date movieEndDay;
        private String moviePicture;

        public Movie(int movieId, String movieName, String movieDetail, java.sql.Time movieRunTime, int movieLimits,
                     String movieSupervision, String movieGenre, String movieCountry, int moviePrice,
                     java.sql.Date movieStartDay, java.sql.Date movieEndDay, String moviePicture) {
            this.movieId = movieId;
            this.movieName = movieName;
            this.movieDetail = movieDetail;
            this.movieRunTime = movieRunTime;
            this.movieLimits = movieLimits;
            this.movieSupervision = movieSupervision;
            this.movieGenre = movieGenre;
            this.movieCountry = movieCountry;
            this.moviePrice = moviePrice;
            this.movieStartDay = movieStartDay;
            this.movieEndDay = movieEndDay;
            this.moviePicture = moviePicture;
        }

        // Getter 메서드들
        public int getMovieId() { return movieId; }
        public String getMovieName() { return movieName; }
        public String getMovieDetail() { return movieDetail; }
        public java.sql.Time getMovieRunTime() { return movieRunTime; }
        public int getMovieLimits() { return movieLimits; }
        public String getMovieSupervision() { return movieSupervision; }
        public String getMovieGenre() { return movieGenre; }
        public String getMovieCountry() { return movieCountry; }
        public int getMoviePrice() { return moviePrice; }
        public java.sql.Date getMovieStartDay() { return movieStartDay; }
        public java.sql.Date getMovieEndDay() { return movieEndDay; }
        public String getMoviePicture() { return moviePicture; }
    }

    // 영화 데이터를 데이터베이스에서 불러오는 메서드
    public static ArrayList<Movie> loadMoviesFromDatabase() {
    	conn connt = new conn();
        ArrayList<Movie> movieList = new ArrayList<>();
        Connection conn = DBconnect.connect();
        String query = "SELECT * FROM movie";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("m_id"),
                    rs.getString("m_name"),
                    rs.getString("m_detail"),
                    rs.getTime("m_run_time"),
                    rs.getInt("m_limits"),
                    rs.getString("m_supervision"),
                    rs.getString("m_genre"),
                    rs.getString("m_country"),
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
