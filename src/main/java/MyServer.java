import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;

public class MyServer {
    public static void main(String[] args) {

        ConcurrentHashMap<String,CacheEntry> veriTabani = new ConcurrentHashMap<>();
        // 6666: Port numarası
        // 500: İşletim sistemindeki bekleme odası (Backlog) kapasitesi!
        try (ServerSocket serverSocket = new ServerSocket(6666,500)
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