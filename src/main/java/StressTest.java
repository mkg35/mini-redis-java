import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StressTest {
    public static void main(String[] args) throws InterruptedException{
        //100 kişilik bir saldırı timi
        ExecutorService saldiriTimi = Executors.newFixedThreadPool(100);
        int toplamIstek = 1000;

        System.out.println("Saldiri basliyor"+ toplamIstek+"adet INCR komutu atilacak.");

        for (int i=0;i<toplamIstek;i++){
            saldiriTimi.execute(() -> {
                try (Socket socket = new Socket("localhost",6666);
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true))
                {
                  //RESP formatinda INCR bilet komutunu gonder
                  out.print("*2\r\n$4\r\nINCR\r\n$5\r\nbilet\r\n");
                  out.flush();
                } catch (Exception e){
                    System.out.println("Baglanti koptu!");
                }
            });
        }
        saldiriTimi.shutdown();
        saldiriTimi.awaitTermination(1,TimeUnit.MINUTES);
        System.out.println("Saldiri bitti! Simdi redis-cli ile 'GET bilet' yazip sonuca bak!");
    }
}
