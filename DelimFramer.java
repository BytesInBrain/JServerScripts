import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DelimFramer {
    private InputStream in;
    private static final byte DELIMITER = "\n";

    public DelimFramer(InputStream in){
        this.in = in;
    }

    public void frameMsg(byte[]  message,OutputStream out) throws IOException{
        for(byte b : message){
            if(b == DELIMITER){
                throw new IOException("Message contains a Delimiter");
            }
        }
        out.write(message);
        out.write(DELIMITER);
        out.flush();
    }

    public byte[] nextMsg() throws IOException{
        ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
        int nextByte;
        while((nextByte = in.read()) != DELIMITER){
            if(nextByte == -1){
                if(messageBuffer.size() == 0){
                    return null;
                }else{
                    throw new EOFException("Non-empty message wothout Delimiter");
                }
            }
            messageBuffer.write(nextByte);
        }
        return messageBuffer.toByteArray();
    }
}
