import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class EchoProtocol implements Runnable{
    private static final int BUFFERSIZE = 32;
    private Socket clientSocket;
    private Logger logger;
    public EchoProtocol(Socket cliSocket, Logger logger){
        this.clientSocket = clientSocket;
        this.logger = logger;
    }
    public static void handleEchoClient(Socket clientSocket,Logger logger){
        try{
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            int recieveMsgSize;
            int totalByteEchoed = 0;
            byte[] echoBuffer = new byte[BUFFERSIZE];
            while((recieveMsgSize = in.read(echoBuffer)) != -1){
                out.write(echoBuffer,0,recieveMsgSize);
                totalByteEchoed += recieveMsgSize;
            }
            logger.info("Client "+clientSocket.getRemoteSocketAddress()+", echoed "+totalByteEchoed+" bytes.");
        }catch(IOException ex){
            logger.log(Level.WARNING,"Exception n echo protocol", ex);
        }finally{
            try{
                clientSocket.close();
            }catch(IOException e){}
        }
    }
    public void run(){
        handleEchoClient(clientSocket, logger);
    }
}
