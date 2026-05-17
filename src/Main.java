import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MovieDAO movieDAO = new MovieDAO();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("====================================");
        System.out.println("   🎬 CINEBOOK MANAGEMENT SYSTEM 🎬   ");
        System.out.println("====================================");

        while (running) {
            System.out.println("\n👉 කරන්න ඕන දේ තෝරන්න:");
            System.out.println("1. අලුත් Movie එකක් ඇතුළත් කරන්න (Add Movie)");
            System.out.println("2. ඔක්කොම Movies ටික බලන්න (View Movies)");
            System.out.println("3. System එකෙන් ඉවත් වෙන්න (Exit)");
            System.out.print("ඔයාගේ අංකය (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Buffer එක clear කරන්න

            switch (choice) {
                case 1:
                    System.out.print("\nEnter Movie Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Movie Genre: ");
                    String genre = scanner.nextLine();

                    Movie newMovie = new Movie(0, title, genre);
                    movieDAO.addMovie(newMovie);
                    break;

                case 2:
                    System.out.println("\n--- 🎬 දැනට තියෙන චිත්‍රපට ලැයිස්තුව ---");
                    List<Movie> movieList = movieDAO.getAllMovies();

                    if (movieList.isEmpty()) {
                        System.out.println("තවම කිසිම Movie එකක් ඇතුළත් කර නැත!");
                    } else {
                        for (Movie m : movieList) {
                            System.out.println("🆔 ID: " + m.getId() + " | 🎥 Title: " + m.getTitle() + " | 🏷️ Genre: " + m.getGenre());
                        }
                    }
                    System.out.println("---------------------------------------");
                    break;

                case 3:
                    running = false;
                    System.out.println("\n👋 CineBook පාවිච්චි කළාට ස්තූතියි! සුබ දවසක්!");
                    break;

                default:
                    System.out.println("❌ වැරදි අංකයක්! කරුණාකර 1, 2 හෝ 3 තෝරන්න.");
            }
        }
        scanner.close();
    }
}