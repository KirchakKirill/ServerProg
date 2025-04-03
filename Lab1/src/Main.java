import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    private static final int PORT = 8089;
    private static final String LOG_FILE = "server_log.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();


            String[] requestParts = requestLine.split(" ");


            String path = requestParts[1].substring(1);
            File file = new File(path);

            if (!file.exists() || file.isDirectory()) {
                sendErrorResponse(out, 404, "Not Found");
                logRequest(clientSocket, path, 404);
                return;
            }


            String contentType = getContentType(file);
            sendFileResponse(out, file, contentType);
            logRequest(clientSocket, path, 200);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getContentType(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".html")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "application/octet-stream";
        }
    }

    private static void sendFileResponse(OutputStream out, File file, String contentType) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + fileContent.length + "\r\n" +
                "\r\n";
        out.write(response.getBytes());
        out.write(fileContent);
    }

    private static void sendErrorResponse(OutputStream out, int statusCode, String statusMessage) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + statusMessage.length() + "\r\n" +
                "\r\n" +
                statusMessage;
        out.write(response.getBytes());
    }

    private static void logRequest(Socket clientSocket, String path, int statusCode) {
        String clientIp = clientSocket.getInetAddress().getHostAddress();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String logEntry = date + "\n" + clientIp + "\n" + "http://localhost:"+ PORT+"/"+ path + "\n" + statusCode + "\n\n";

        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.print(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}