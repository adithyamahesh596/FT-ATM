public class Monitor extends Thread{
    
    HeartBeatConnector hbConn;
    ErrorProcessingUnit errorHandler;
    
    public Monitor(HeartBeatConnector hbConn, ErrorProcessingUnit errorHandler)
    {
        this.hbConn = hbConn;  
        this.errorHandler = errorHandler;
    }
    
    public void kill()
    {
        keepGoing = false;
        hbConn.end();
    }
    
    boolean keepGoing = true;
    
    public void run()
    {
        while(keepGoing)
        {
            hbConn.send(null); // check if server is up
            
            if(!hbConn.isMessage()) continue;
            
            Message m = hbConn.receiveReplay();
            
           // System.out.println((int)m.data.get(0));
                        
            if((int)m.data.get(0) == 0)  // server is down
            {
                hbConn.send(null); // check a second time
                m = hbConn.receiveReplay();
                                
                if((int)m.data.get(0) == 0)  // server is down
                {
                    hbConn.send(null); // check a third time
                    m = hbConn.receiveReplay();

                    if((int)m.data.get(0) == 0)  // server is down
                    {
                        System.out.println("Count 3 times. Server does not respond!");
                        errorHandler.fault();
                    }
                }                
            }            
        }
    }
}
