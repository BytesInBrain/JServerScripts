import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class TCPEchoServerPool {
    public static void main(String[] args) throws IOException{
        if(args.length != 2){
            throw new IllegalArgumentException("Parameter(s): <Port> <Threads>");
        }
        int echoServerPort = Integer.parseInt(args[0]);
        int threadPoolSize = Integer.parseInt(args[1]);
        final ServerSocket serverSocket = new ServerSocket(echoServerPort);
        final Logger logger = Logger.getLogger("practical");
        for(int i = 0;i < threadPoolSize;i++){
            Thread thread = new Thread(){
                public void run(){
                    while(true){
                        try{
                            Socket clientSocket = serverSocket.accept();
                            EchoProtocol.handleEchoClient(clientSocket, logger);
                        }catch(IOException ex){
                            logger.log(Level.WARNING,"Client accept failed",ex);
                        }
                    }
                }
            };
            thread.start();
            logger.info("Created and Started Thread = "+ thread.getName());
        }
    }
}
