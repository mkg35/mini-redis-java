import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MyServer {
    public static void main(String[] args) {

        HashMap<String,String> veriTabani = new HashMap<>();

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

                String[] parcalar = istemciMesaji.split(" ");
                if (parcalar[0].equalsIgnoreCase("Set")){
                    veriTabani.put(parcalar[1],parcalar[2]);
                    out.println("Kaydedildi.");
                }
                else if (parcalar[0].equalsIgnoreCase("Get")){
                    String sonuc = veriTabani.get(parcalar[1]);
                    out.println(sonuc);
                }
                else {
                    out.println("Geçersiz komut girildi.");
                }
            }

        }
        catch (IOException e){
            System.out.println("Error:" + e);
        }
    }
}
