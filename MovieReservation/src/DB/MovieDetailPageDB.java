package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailPageDB {

    // 내부 클래스 (Movie DTO)
    public static class Movie {
        private int id;
        private String name;
        private String detail;
        private java.sql.Time runTime;
        private int ageLimit;
        private String supervision;
        private String genre;
        private String country;
        private int price;
        private java.sql.Date startDay;
        private java.sql.Date endDay;
        private String picture;

        // Constructor
        public Movie(int id, String name, String detail, java.sql.Time runTime, int ageLimit, String supervision,
                     String genre, String country, int price, java.sql.Date startDay, java.sql.Date endDay, String picture) {
            this.id = id;
            this.name = name;
            this.detail = detail;
            this.runTime = runTime;
            this.ageLimit = ageLimit;
            this.supervision = supervision;
            this.genre = genre;
            this.country = country;
            this.price = price;
            this.startDay = startDay;
            this.endDay = endDay;
            this.picture = picture;
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDetail() {
            return detail;
        }

        public java.sql.Time getRunTime() {
            return runTime;
        }

        public int getAgeLimit() {
            return ageLimit;
        }

        public String getSupervision() {
            return supervision;
        }

        public String getGenre() {
            return genre;
        }

        public String getCountry() {
            return country;
        }

        public int getPrice() {
            return price;
        }

        public java.sql.Date getStartDay() {
            return startDay;
        }

        public java.sql.Date getEndDay() {
            return endDay;
        }

        public String getPicture() {
            return picture;
        }
    }

    // 영화 정보를 가져오는 메서드
    public static Movie getMovieDetails(int movieId) {
        Connection conn = DBconnect.connect();
        Movie movie = null;

        String movieQuery = "SELECT * FROM movie WHERE m_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(movieQuery)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                movie = new Movie(
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
                    rs.getString("m_picture")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBconnect.close();
        }
        return movie;
    }

    // 배우 목록을 가져오는 메서드
    public static List<String> getActorsByMovieId(int movieId) {
        Connection conn = DBconnect.connect();
        List<String> actors = new ArrayList<>();

        String actorQuery = "SELECT a_name FROM actor WHERE m_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(actorQuery)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                actors.add(rs.getString("a_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBconnect.close();
        }
        return actors;
    }

    // 테스트용 메인 메서드 (필요하면 삭제 가능)
    public static void main(String[] args) {
        int testMovieId = 7; // 테스트용 영화 ID

        // 영화 정보 가져오기
        Movie movie = getMovieDetails(testMovieId);
        if (movie != null) {
            System.out.println("영화 제목: " + movie.getName());
            System.out.println("영화 장르: " + movie.getGenre());
            System.out.println("영화 감독: " + movie.getSupervision());
            System.out.println("상세 정보: " + movie.getDetail());
            System.out.println(movie.getPicture());
        } else {
            System.out.println("영화 정보를 찾을 수 없습니다.");
        }

        // 배우 정보 가져오기
        List<String> actors = getActorsByMovieId(testMovieId);
        System.out.println("배우 목록:");
        for (String actor : actors) {
            System.out.println(actor);
        }
    }
}
