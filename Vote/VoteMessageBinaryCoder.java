package Vote;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VoteMessageBinaryCoder implements VoteMessageCoder {
    public static final int MIN_WIRE_LENGTH = 4;
    public static final int MAX_WIRE_LENGTH = 16;
    public static final int MAGIC = 0x5400;
    public static final int MAGIC_MASK = 0xfc00;
    public static final int MAGIC_SHIFT = 8;
    public static final int RESPONSE_FLAG = 0x0200;
    public static final int INQUIRE_FLAG = 0x0100;

    public byte[] toWire(VoteMessage message) throws IOException{
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(message);
        DataOutputStream out = new DataOutputStream(byteStream);

        short magicAndFlags = MAGIC;
        if(message.isInquiry()){
            magicAndFlags |= INQUIRE_FLAG;
        }
        if(message.isResponse()){
            magicAndFlags |= RESPONSE_FLAG;
        }
        out.writeShort(magicAndFlags);
        out.writeShort((short)message.getCandidateID());
        if(message.isResponse()){
            out.writeLong(message.getVoteCount());
        }
        out.flush();
        byte[] data = byteStream.toByteArray();
        return data;
    }

    public  VoteMessage fromWire(byte[] input) throws IOException{
        if(input.length < MIN_WIRE_LENGTH){
            throw new IOException("Runt message");
        }
        ByteArrayInputStream bs = new ByteArrayInputStream(input);
        DataInputStream in = new DataInputStream(bs);
        int magic = in.readShort();
        if((magic & MAGIC_MASK) != MAGIC){
            throw new IOException("Bad Magic #: " +((magic & MAGIC_MASK) >> MAGIC_SHIFT));
        }
        boolean resp = ((magic & RESPONSE_FLAG)!=0);
        boolean inq = ((magic & INQUIRE_FLAG)!=0);
        int candidateID = in.readShort();
        if(candidateID < 0 || candidateID > 1000){
            throw new IOException("Bad candiadate ID: " + candidateID);
        }
        long count = 0;
        if(resp){
            count = in.readLong();
            if(count < 0 ){
                throw new IOException("Bad vote count: "+ count);
            }
        }
        return new VoteMessage(resp, inq, candidateID,count);
    }
}
