import java.util.LinkedList;

public class SynchronousConnector {
    
    boolean messageBufferFull;
    boolean responseBufferFull;
    
    LinkedList<Message> messageBuffer;
    LinkedList<Message> responseBuffer;
    
    int messageBufferMax;  // max capacity of message buffer
    int responseBufferMax; // max capacity of response buffer
    
    int messageBufferCount; // current size of message buffer
    int responseBufferCount; // current size of response buffer
    
    public synchronized boolean isMessage()
    {
        return messageBufferCount > 0;
    }
    
    public SynchronousConnector()
    {
        messageBuffer = new LinkedList<>();
        responseBuffer = new LinkedList<>();
        
        messageBufferFull = false;
        messageBufferMax = 1;
        messageBufferCount = 0;
        
        responseBufferFull = false;
        responseBufferMax = 1;
        responseBufferCount = 0;
    }
    
    public synchronized void send(Message m)
    {
        while(messageBufferCount >= messageBufferMax)
        {
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
