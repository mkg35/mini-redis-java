import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final ConcurrentHashMap<String, CacheEntry> veriTabani;

    public ClientHandler(Socket clientSocket, ConcurrentHashMap<String, CacheEntry> veriTabani) {
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
                                CacheEntry value = new CacheEntry(parcalar[2],null );
                                veriTabani.put(parcalar[1],value);
                                out.print("+Kaydedildi.\r\n");
                            }

                        } else if (parcalar[0].equalsIgnoreCase("Get")) {
                            if (parcalar.length < 2) {
                                throw new InvalidCommandException("Get komutu icin eksik arguman!");
                            }
                            else {
                                CacheEntry sonuc = veriTabani.get(parcalar[1]);
                                if (sonuc == null){
                                    out.print("$-1\r\n");
                                }
                                else {
                                    if(sonuc.isExpired()){
                                        veriTabani.remove(parcalar[1]);
                                        out.print("$-1\r\n");
                                    }
                                    else {
                                        String value = sonuc.getValue();
                                        out.print("$" + value.length() + "\r\n" + value + "\r\n");
                                    }
                                }

                            }
                        } else if (parcalar[0].equalsIgnoreCase("PING")) {
                            out.print("+PONG\r\n");

                        } else if (parcalar[0].equalsIgnoreCase("DEL")) {
                            if (parcalar.length < 2) {
                                throw new InvalidCommandException("Del komutu icin eksik arguman!");
                            } else {
                                CacheEntry silinen = veriTabani.remove(parcalar[1]);
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
                                CacheEntry new_value = veriTabani.compute(parcalar[1],(anahtar,eskiDeger)->{
                                    if (eskiDeger == null || eskiDeger.isExpired()){
                                        return new CacheEntry("1",null);
                                    }
                                    else {
                                        Integer int_eski_deger = Integer.parseInt(eskiDeger.getValue());
                                        int_eski_deger++;
                                        return new CacheEntry(String.valueOf(int_eski_deger), eskiDeger.getExpireAt());
                                    }
                                } );
                                out.print(":" + new_value.getValue() + "\r\n");
                            }
                        }
                        else if (parcalar[0].equalsIgnoreCase("EXPIRE")){
                            if(parcalar.length<3){
                                throw new InvalidCommandException("EXPIRE komutu icin eksik arguman");
                            }
                            else{
                                CacheEntry value = veriTabani.get(parcalar[1]);
                                if (value == null || value.isExpired()){
                                    out.print(":0\r\n");
                                    veriTabani.remove(parcalar[1]);
                                }
                                else {
                                    Integer time_to_add = Integer.parseInt(parcalar[2]);
                                    Long new_time = System.currentTimeMillis() + (time_to_add * 1000L);
                                    CacheEntry new_cache_entry = new CacheEntry(value.getValue(), new_time);
                                    veriTabani.put(parcalar[1],new_cache_entry);
                                    out.print(":1\r\n");
                                }
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