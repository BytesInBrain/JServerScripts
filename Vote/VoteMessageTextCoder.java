package Vote;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class VoteMessageTextCoder implements VoteMessageCoder{
    /*
    * Wire Format "VOTEPROTO" <"v"|"i"> [<RESPFLAG>] <CANDIDATE> [<VOTECNT>]
    * Charset is fixed by the wire format.
    */

    public static final String MAGIC = "Voting";
    public static final String VOTESTR = "v";
    public static final String INQSTR = "i";
    public static final String RESPONSESTR = "R";

    public static final String CHARSETNAME = "US-ASCII";
    public static final String DELIMSTR = " ";
    public static final int MAX_WIRE_LENGTH = 2000;
    public byte[] toWire(VoteMessage message) throws IOException{
        String msgString = MAGIC + DELIMSTR + (message.isInquiry() ? INQSTR : VOTESTR)
        + DELIMSTR + (message.isResponse() ? RESPONSESTR + DELIMSTR : "")
        + Integer.toString(message.getCandidateID()) + DELIMSTR
        + Long.toString(message.getVoteCount());
        byte data[] = msgString.getBytes(CHARSETNAME);
        return data;
    }
    public VoteMessage fromWire(byte[] message) throws IOException{
        ByteArrayInputStream messageStream = new ByteArrayInputStream(message);
        Scanner s = new Scanner(new InputStreamReader(messageStream,CHARSETNAME));
        boolean isInquiry;
        boolean isResponse;
        int candidateID;
        long voteCount;
        String token;
        try {
            token = s.next();
            if (!token.equals(MAGIC)) {
                throw new IOException("Bad magic string: " + token);
            }
            token = s.next();
            if (token.equals(VOTESTR)) {
                isInquiry = false;
            } else if (!token.equals(INQSTR)) {
                throw new IOException("Bad vote/inq indicator: " + token);
            } else {
                isInquiry = true;
            }
            token = s.next();
            if (token.equals(RESPONSESTR)) {
                isResponse = true;
                token = s.next();
            } else {
                isResponse = false;
            }
            // Current token is candidateID
            // Note: isResponse now valid
            candidateID = Integer.parseInt(token);
            if (isResponse) {
                token = s.next();
                voteCount = Long.parseLong(token);
            } else {
                voteCount = 0;
            }
        } catch (IOException ioe) {
            throw new IOException("Parse error...");
        }
        return new VoteMessage(isResponse, isInquiry, candidateID, voteCount);
    }
    }

}
