import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import java.util.*;


public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine booleanSearchEngine = new BooleanSearchEngine(new File("pdfs"));
        final int PORT = 8989;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT + "...");
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                    String searchWord = in.readLine().toLowerCase();
                    List<PageEntry> pageEntryList = booleanSearchEngine.search(searchWord);
                    out.println(converterJson(pageEntryList));
                }
            }
        }
    }

    public static String converterJson(List<PageEntry> pageList) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(pageList);
    }
}