import java.io.*;
import java.net.*;


public class Client {
    public static void main(String[] args) {
        try (Socket clientSocket = new Socket("localhost", 8989);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String searchQuery = "бизнес";

            out.println(searchQuery);
            while (true) {
                String s = in.readLine();
                if (s == null) {
                    break;
                }
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}