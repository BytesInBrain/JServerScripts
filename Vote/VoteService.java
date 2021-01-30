package Vote;
import java.util.HashMap;
import java.util.Map;
public class VoteService {
    private Map<Integer,Long> results = new HashMap<Integer,Long>();
    public VoteMessage handleRequest(VoteMessage message){
        if(message.isResponse()){
            return message;
        }
        message.setResponse(true);
        int candidate = message.getCandidateID();
        Long count = results.get(candidate);
        if(count == null){
            count = 0L;
        }
        if(!message.isInquiry()){
            results.put(candidate,++count);
        }
        message.setVoteCount(count);
        return message;
    }    
}
