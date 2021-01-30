import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class TCPEchoClient {
    public static void main(String[] args) throws IOException{
        if((args.length<2)||(args.length>3))
            throw new IllegalArgumentException("Parameter(s):<Server> <word> [<port>]");
        String server = args[0];
        byte[] data = args[1].getBytes();
        int serverPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;
        Socket socket = new Socket(server,serverPort);
        System.out.println("Connected to server.......sending eco string");
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        out.write(data);
        int totalBytesRecieved = 0;
        int bytesRecieved;
        while(totalBytesRecieved < data.length){
            if((bytesRecieved = in.read(data,totalBytesRecieved, data.length-totalBytesRecieved)) == -1)
                throw new SocketException("Connection Closed Prematurely");
            totalBytesRecieved += bytesRecieved;
            System.out.println("Recieved: "+ new String(data));
            socket.close();
        }
    }
}
