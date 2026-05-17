import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/movies", new MovieHandler());
        server.setExecutor(null);
        System.out.println("🚀 CineBook Premium Backend is running on Port 8080!");
        server.start();
    }

    static class MovieHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // CORS Settings
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            MovieDAO movieDAO = new MovieDAO();
            String method = exchange.getRequestMethod();

            // 1. GET Request - Get all movies
            if ("GET".equals(method)) {
                List<Movie> movieList = movieDAO.getAllMovies();
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < movieList.size(); i++) {
                    Movie m = movieList.get(i);
                    json.append(String.format("{\"id\":%d, \"title\":\"%s\", \"genre\":\"%s\"}",
                            m.getId(), m.getTitle(), m.getGenre()));
                    if (i < movieList.size() - 1) json.append(",");
                }
                json.append("]");

                byte[] response = json.toString().getBytes();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            }
            // 2. POST Request - Add a new movie
            else if ("POST".equals(method)) {
                java.io.InputStream is = exchange.getRequestBody();
                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                String body = s.hasNext() ? s.next() : "";

                try {
                    String title = body.split("\"title\":\"")[1].split("\"")[0];
                    String genre = body.split("\"genre\":\"")[1].split("\"")[0];

                    Movie newMovie = new Movie(0, title, genre);
                    movieDAO.addMovie(newMovie);

                    String response = "{\"message\":\"Success\"}";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (Exception e) {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
            // 3. DELETE Request - Remove a movie
            else if ("DELETE".equals(method)) {
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.split("=")[1]);
                    movieDAO.deleteMovie(id);

                    String response = "{\"message\":\"Deleted\"}";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } else {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
            else if ("OPTIONS".equals(method)) {
                exchange.sendResponseHeaders(204, -1);
            }
        }
    }
}