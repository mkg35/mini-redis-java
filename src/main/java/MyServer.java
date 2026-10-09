import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(6666)
        ) {

            while (true){
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }


        } catch (IOException e) {
            System.out.println("Error:" + e);
        }
    }
}
