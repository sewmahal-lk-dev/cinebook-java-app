import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    // Movie එකක් ඇතුළත් කිරීම (Create)
    public void addMovie(Movie movie) {
        String sql = "INSERT INTO movies (title, genre) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.executeUpdate();
            System.out.println("✔ Movie එක සාර්ථකව Database එකට වැටුණා!");

        } catch (SQLException e) {
            System.out.println("❌ Error: Movie එක දාන්න බැරි වුණා: " + e.getMessage());
        }
    }


    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre")
                );
                movies.add(movie);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: Movies ටික ගන්න බැරි වුණා: " + e.getMessage());
        }
        return movies;
    }
}