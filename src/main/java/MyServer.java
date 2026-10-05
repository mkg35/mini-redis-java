import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MyServer {
    public static void main(String[] args) {

        HashMap<String, String> veriTabani = new HashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(6666);
             Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            System.out.println("Server başlatılıyor.");

            System.out.println("Server Başlatıldı.Port:6666");

            System.out.println("Client Kabul Edildi.");

            String istemciMesaji;
            while ((istemciMesaji = in.readLine()) != null) {
                System.out.println("Okunan Satır: " + istemciMesaji);
                if (istemciMesaji.startsWith("*")) {
                    int elementNumber = Integer.parseInt(istemciMesaji.substring(1));
                    String[] parcalar = new String[elementNumber];
                    for (int i = 0; i < elementNumber; i++) {
                        in.readLine();
                        parcalar[i] = in.readLine();
                    }


                    if (parcalar[0].equalsIgnoreCase("COMMAND")) {
                        out.print("+OK\r\n");
                        out.flush();
                    }
                    else if (parcalar[0].equalsIgnoreCase("Set")) {
                        veriTabani.put(parcalar[1], parcalar[2]);
                        out.print("+Kaydedildi.\r\n");
                        out.flush();
                    } else if (parcalar[0].equalsIgnoreCase("Get")) {
                        String sonuc = veriTabani.get(parcalar[1]);
                        out.print("+" + sonuc + "\r\n");
                        out.flush();
                    } else if (parcalar[0].equalsIgnoreCase("PING")) {
                        out.print("+PONG\r\n");
                        out.flush();
                    } else {
                        out.print("+Geçersiz komut girildi.\r\n");
                        out.flush();
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error:" + e);
        }
    }
}
