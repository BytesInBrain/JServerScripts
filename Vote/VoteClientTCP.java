package Vote;
import java.io.OutputStream;
import java.net.Socket;

import javax.naming.ldap.SortKey;

public class VoteClientTCP {
    public static final int CANDIDATEID = 888;
    public static void main(String[] args) throws Exception{
        if(args.length != 2){
            throw new IllegalArgumentException("Parameter(s): <Server> <Port>");
        }
        String destinationAddress = args[0];
        int destinationPort = Integer.parseInt(args[1]);
        Socket socket = new Socket(destinationAddress,destinationPort);
        OutputStream out = socket.getOutputStream();
        VoteMessageCoder coder = new VoteMessageCoder();
    }
}
