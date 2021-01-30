import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InterruptedIOException;

public class UDPEchoClientTimeout {
    public static final int TIMEOUT = 3000;
    public static final int MAXTRIES = 5;
    public static void main(String[] args)throws IOException{
        if ((args.length < 2)||(args.length > 3)){
            throw new IllegalArgumentException("Parameters(s):<Server> <Word> [<Port>]"); 
        }
        InetAddress serveAddress = InetAddress.getByName(args[0]);
        byte[] bytesToSend = args[1].getBytes();
        int serverPort = (args.length == 3) ? Integer.parseInt(args[2]):7;
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT);
        DatagramPacket sendPacket = new DatagramPacket(bytesToSend,bytesToSend.length,serveAddress,serverPort);
        DatagramPacket recievePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);
        int tries = 0;
        boolean recieveResponse = false;
        do{
            socket.send(sendPacket);
            try{
                socket.receive(recievePacket);
                if(!recievePacket.getAddress().equals(serveAddress)){
                    throw new IOException("Recieve packet from an unknow source");
                }
                recieveResponse = true;
            }catch(InterruptedIOException e){
                tries += 1;
                System.out.println("Timed Out, "+(MAXTRIES-tries)+" more tries.....");
            }
        }while((!recieveResponse)&&(tries<MAXTRIES));
        if(recieveResponse){
            System.out.println("Recieved: "+new String(recievePacket.getData()));
        }else{
            System.out.println("No response --- giving  up");
        }
        socket.close();
    }
}