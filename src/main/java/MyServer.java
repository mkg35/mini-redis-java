import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6666);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            System.out.println("Server başlatılıyor.");

            System.out.println("Server Başlatıldı.Port:6666");

            System.out.println("Client Kabul Edildi.");

            String istemciMesaji;
            while((istemciMesaji = in.readLine()) != null){
                System.out.println(istemciMesaji);
                out.println(istemciMesaji);
            }

        }
        catch (IOException e){
            System.out.println("Error:" + e);
        }
    }
}
