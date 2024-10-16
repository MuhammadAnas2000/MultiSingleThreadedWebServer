import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadPool;
    public Server(int poolSize){
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    private long performIntensiveComputation(){
        long sum =0;
        for(long i=0;i< 1000000000L; i++){
            sum+=i;
        }
        return sum;
    }
    public void performIntensiveComputationSum(Socket clientSocket) {
        long sum = performIntensiveComputation();
        try(PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(),true)) {
            //toClient.println("Hellofrom Server"+clientSocket.getInetAddress());
            toClient.println("Computed Sum: " + sum);
            toClient.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args){
        int port = 8010;
        int poolSize = 800;
        Server server = new Server(poolSize);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(80000);
            System.out.println("Server is listening on port no "+ port);
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from client "+clientSocket.getRemoteSocketAddress());
                server.threadPool.execute(()->server.performIntensiveComputationSum(clientSocket));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
