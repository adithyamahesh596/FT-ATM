
import java.util.LinkedList;

public class HeartBeatConnector {
    
    boolean messageBufferFull;
    
    LinkedList<Message> messageBuffer;
    
    int messageBufferMax;  // max capacity of message buffer    
    int messageBufferCount; // current size of message buffer
    
    boolean responseBufferFull;
    
    LinkedList<Message> responseBuffer;
    int responseBufferMax; // max capacity of response buffer    
    int responseBufferCount; // current size of response buffer   
    
    public synchronized boolean isMessage()
    {
        return messageBufferCount > 0;
    }
    
    public HeartBeatConnector()
    {
        messageBuffer = new LinkedList<>();
        messageBufferFull = false;
        messageBufferMax = 1;
        messageBufferCount = 0;
        
        responseBuffer = new LinkedList<>();
        responseBufferFull = false;
        responseBufferMax = 1;
        responseBufferCount = 0;
    }
    
    public boolean endAll = false;
    
    public synchronized void end()
    {
        endAll = true;
        try{
        notifyAll();
        }catch(Exception e)
        {
        }
    }
    
    public synchronized void send(Message m)
    {
        while(messageBufferCount >= messageBufferMax)
        {
            if(endAll) 
            {
                return;
            }
            try{
                wait();
            }catch(InterruptedException e)
            {
                
            }
        }
        messageBufferCount++;
        messageBuffer.add(m);
        notifyAll();
    }
    
    public synchronized Message receive()
    {
        while(messageBufferCount == 0) // empty
        {
            try{
                wait();
            }catch(InterruptedException e)
            {
                
            } 
        }
        Message m = messageBuffer.pollFirst();
        messageBufferCount--;
        notifyAll();
        return m;
    } 

    public synchronized void replay(Message m)
    {
        while(responseBufferCount >= responseBufferMax)
        {
            try{
                wait();
            }catch(InterruptedException e)
            {
                
            }
        }
        responseBufferCount++;
        responseBuffer.add(m);
        notifyAll();
    }
    
    public synchronized Message receiveReplay()
    {
        while(responseBufferCount == 0) // empty
        {
            try{
                wait();
            }catch(InterruptedException e)
            {
                
            } 
        }
        Message r = responseBuffer.pollFirst();
        responseBufferCount--;
        notifyAll();
        return r;
    }    
    
}
