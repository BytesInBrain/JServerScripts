package Vote;
import java.io.IOException;
public interface VoteMessageCoder {
    byte[] toWire(VoteMessage message) throws IOException;
    VoteMessage fromWire(byte[] input) throws IOException;
}
