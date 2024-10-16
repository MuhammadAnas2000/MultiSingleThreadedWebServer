import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public long performIntensiveComputation() {
        long sum = 0;
        for (long i = 1; i <= 1000000000L; i++) {
            sum += i;
        }
        return sum;
    }
    public void run(){
        int port = 8010;
        try {
            ServerSocket socket = new ServerSocket(port);
            socket.setSoTimeout(10000);
            while(true){
                System.out.println("Server is listening on port "+port);
                Socket acceptedConnection = socket.accept();
                System.out.println("Connection accepted from client "+acceptedConnection.getRemoteSocketAddress());
                PrintWriter toClient = new PrintWriter(acceptedConnection.getOutputStream());
                //BufferedReader fromClient = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()));
                //String clientMessage = fromClient.readLine();
               // System.out.println("Message from client: " + clientMessage);
                long sum = performIntensiveComputation();
                toClient.println("Computed Sum: " + sum);
                toClient.close();
                acceptedConnection.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args){
        Server server = new Server();
        server.run();
    }
}
