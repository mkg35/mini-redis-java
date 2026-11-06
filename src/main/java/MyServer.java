import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyServer {
    public static void main(String[] args) {

        HashMap<String,String> veriTabani = new HashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(6666)
        ) {
            ExecutorService threadPool = Executors.newFixedThreadPool(100);
            while (true){
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket,veriTabani);
                threadPool.execute(clientHandler);
            }


        } catch (IOException e) {
            System.out.println("Error:" + e);
        }
    }
}