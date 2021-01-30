import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.SocketAddress;

public class TCPEchoServer
{
    private static final int BUFSIZE = 32;
    public static void main(String[] args) throws IOException{
        if(args.length != 1)
            throw new IllegalArgumentException("Parameters:<Port>");
        int serverPort = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(serverPort);
        int recieveMessageSize;
        byte[] recieveBuffer = new byte[BUFSIZE];
        while(true){
            Socket clientSocket = serverSocket.accept();
            SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
            System.out.println("Hanling client at "+ clientAddress);
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            while((recieveMessageSize = in.read(recieveBuffer)) != -1){
                out.write(recieveBuffer,0,recieveMessageSize);
            }
            clientSocket.close();
        }
    }
}
