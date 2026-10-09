import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable{

    private final Socket clientSocket;
    private final HashMap<String,String> veriTabani;

    public ClientHandler(Socket clientSocket, HashMap<String,String> veriTabani){
        this.clientSocket = clientSocket;
        this.veriTabani = veriTabani;
    }
    public void run(){
        try(this.clientSocket;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        ){String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Okunan Satır: " + clientMessage);
                if (clientMessage.startsWith("*")) {
                    int elementNumber = Integer.parseInt(clientMessage.substring(1));
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



        }
        catch (IOException exception){
            System.err.println(Thread.currentThread().getName() +"bağlantı hatası." + exception.getMessage());
        }
    }

}
