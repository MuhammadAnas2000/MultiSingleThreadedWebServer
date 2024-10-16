import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    public long performIntensiveComputation() {
        long sum = 0;
        for (long i = 1; i <= 1000000000L; i++) {
            sum += i;
        }
        return sum;
    }
    public Consumer<Socket> getConsumer(){
        return(clientSocket)->{
            long sum = performIntensiveComputation();
            try {
                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());
                //toClient.println("Hello from the sever Anas");
                toClient.println("Computed Sum: " + sum);
                toClient.flush();
                toClient.close();
                clientSocket.close();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
    public static void main(String[] args) {
        int port = 8010;
        Server server = new Server();
        try {
            ServerSocket serverSocket= new ServerSocket(port);
            serverSocket.setSoTimeout(10000);
            while(true){
                System.out.println("Server is listening on port "+port);
                Socket acceptedsocket = serverSocket.accept();
                System.out.println("Connection accepted from client "+acceptedsocket.getRemoteSocketAddress());
                Thread thread= new Thread(()->server.getConsumer().accept(acceptedsocket));
                thread.start();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
