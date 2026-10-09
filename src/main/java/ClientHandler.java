import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    public void run(){
        try(this.clientSocket;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        ){String clientMessage;
            while ((clientMessage=in
                    .readLine())!=null){
                System.out.println(Thread.currentThread().getName() +"threadi şunu söyledi:"+ clientMessage);
                out.println("Sunucu mesajı aldı: " + clientMessage);
            }

        }
        catch (IOException exception){
            System.err.println(Thread.currentThread().getName() +"bağlantı hatası." + exception.getMessage());
        }
    }

}
