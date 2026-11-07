import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final ConcurrentHashMap<String, String> veriTabani;

    public ClientHandler(Socket clientSocket, ConcurrentHashMap<String, String> veriTabani) {
        this.clientSocket = clientSocket;
        this.veriTabani = veriTabani;
    }

    public void run() {
        try (this.clientSocket;
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


        ) {
            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Okunan Satır: " + clientMessage);
                if (clientMessage.startsWith("*")) {
                    int elementNumber = Integer.parseInt(clientMessage.substring(1));
                    String[] parcalar = new String[elementNumber];
                    for (int i = 0; i < elementNumber; i++) {
                        in.readLine();
                        parcalar[i] = in.readLine();
                    }
                    try {
                        if (parcalar[0].equalsIgnoreCase("COMMAND")) {
                            out.print("+OK\r\n");

                        } else if (parcalar[0].equalsIgnoreCase("Set")) {
                            if (parcalar.length < 3) {
                                throw new InvalidCommandException("Set komutu icin eksik arguman!");
                            } else {
                                veriTabani.put(parcalar[1], parcalar[2]);
                                out.print("+Kaydedildi.\r\n");

                            }

                        } else if (parcalar[0].equalsIgnoreCase("Get")) {
                            if (parcalar.length < 2) {
                                throw new InvalidCommandException("Get komutu icin eksik arguman!");
                            }
                            else {
                                String sonuc = veriTabani.get(parcalar[1]);
                                if (sonuc == null) {
                                    out.print("$-1\r\n");
                                } else {
                                    out.print("$" + sonuc.length() + "\r\n" + sonuc + "\r\n");
                                }
                            }
                        } else if (parcalar[0].equalsIgnoreCase("PING")) {
                            out.print("+PONG\r\n");

                        } else if (parcalar[0].equalsIgnoreCase("DEL")) {
                            if (parcalar.length < 2) {
                                throw new InvalidCommandException("Del komutu icin eksik arguman!");
                            } else {
                                String silinen = veriTabani.remove(parcalar[1]);
                                if (silinen != null) {
                                    out.print(":1\r\n");
                                } else {
                                    out.print(":0\r\n");
                                }

                            }
                        }
                        else if (parcalar[0].equalsIgnoreCase("INCR")){
                            if(parcalar.length<2){
                                throw new InvalidCommandException("INCR komutu icin eksik arguman");
                            }
                            else{
                                String new_value = veriTabani.compute(parcalar[1],(anahtar,eskiDeger)->{
                                    if (eskiDeger == null){
                                        return "1";
                                    }
                                    else {
                                        Integer int_eski_deger = Integer.parseInt(eskiDeger);
                                        int_eski_deger++;
                                        return String.valueOf(int_eski_deger);
                                    }
                                } );
                                out.print(":" + new_value + "\r\n");
                            }
                        }
                        else {
                            throw new InvalidCommandException("Bilinmeyen komut" + parcalar[0]);
                        }
                        out.flush();
                    } catch (InvalidCommandException e) {
                        out.print("-ERR " + e.getMessage() + "\r\n");
                        out.flush();
                    }
                    catch (NumberFormatException e){
                        out.print("-ERR deger integer degil veya out of range\r\n");
                        out.flush();
                    }

                }
            }


        } catch (IOException exception) {
            System.err.println(Thread.currentThread().getName() + "bağlantı hatası." + exception.getMessage());
        }
    }

}