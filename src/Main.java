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
        System.out.println("🚀 CineBook Backend Server එක Port 8080 එකේ සුපිරියටම Start වුණා!");
        server.start();
    }

    static class MovieHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            MovieDAO movieDAO = new MovieDAO();


            if ("GET".equals(exchange.getRequestMethod())) {
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

            else if ("POST".equals(exchange.getRequestMethod())) {

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
                    exchange.sendResponseHeaders(400, 0); // Bad Request
                }
            } else if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
            }
        }
    }
}