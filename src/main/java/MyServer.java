import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MyServer {
    public static void main(String[] args) {

        HashMap<String,String> veriTabani = new HashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(6666)
        ) {

            while (true){
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket,veriTabani);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }


        } catch (IOException e) {
            System.out.println("Error:" + e);
        }
    }
}