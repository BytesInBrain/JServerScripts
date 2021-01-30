package Thread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPEchoServerThread {
    public static void main(String[] args) throws IOException{
        if(args.length != 1){
            throw new IllegalArgumentException("Parameters(s): <Port>");
        }
        int echoServerPort = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(echoServerPort);
        Logger logger = Logger.getLogger("practical");
        while(true){
            Socket clientSocket = serverSocket.accept();
            Thread thread = new Thread(new EchoProtocol(clientSocket,logger));
            thread.start();
            logger.info("Created and started Thread "+thread.getName());
        }
    }
}
