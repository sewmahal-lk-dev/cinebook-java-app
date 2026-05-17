import java.util.List;

public class Main {
    public static void main(String[] args) {
        MovieDAO movieDAO = new MovieDAO();

        System.out.println("--- CineBook System Setup Testing ---");


        Movie movie1 = new Movie(0, "Spiderman", "Action");
        Movie movie2 = new Movie(0, "Avatar", "Sci-Fi");

        movieDAO.addMovie(movie1);
        movieDAO.addMovie(movie2);

        System.out.println("\n--- දැනට Database එකේ තියෙන Movies ---");

        List<Movie> movieList = movieDAO.getAllMovies();
        for (Movie m : movieList) {
            System.out.println("ID: " + m.getId() + " | Title: " + m.getTitle() + " | Genre: " + m.getGenre());
        }
    }
}