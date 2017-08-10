
import java.util.ArrayList;

public class Server extends Thread{
    
    public Server(SynchronousConnector conn, HeartBeatConnector hbConn, ArrayList<CheckingAccount> accounts, ArrayList<DebitCard> cards, boolean faultTolerant)
    {
        this.conn = conn;
        this.hbConn = hbConn;
        this.accounts = accounts;
        this.cards = cards;
        stillAlive = true;
        this.faultTolerant = faultTolerant;
    }
    
    SynchronousConnector conn;
    HeartBeatConnector hbConn;
    ArrayList<CheckingAccount> accounts;
    ArrayList<DebitCard> cards;
    
    void processMessage()
    {
            Message petition = conn.receive();
            int type = (int) petition.data.get(0);
            
            if (type == 1) // check balance
            {
                int cardId = (int) petition.data.get(1);
                int PIN = (int) petition.data.get(2);
                
                if(!faultTolerant && cardId == 3005) // fail when using card 3005
                {
                    stillAlive = false;
                    conn.replay(new Message(2)); // send message 2 (no server)
                    return;
                }
                
                float balance = checkBalanceTransaction.checkBalance(cardId, PIN, accounts, cards);
                if(((Float)balance).isNaN())
                {
                    conn.replay(new Message(0)); // there was en error
                }
                else
                {
                    conn.replay(new Message(1, checkBalanceTransaction.account, balance));
                }
            }
            else if(type == 2) // withdrawal
            {
                int cardId = (int) petition.data.get(1);
                int PIN = (int) petition.data.get(2);
                float amount = (float) petition.data.get(3);
                
                if(!faultTolerant && cardId == 3005) // fail when using card 3005
                {
                    stillAlive = false;
                    conn.replay(new Message(2)); // send message 2 (no server)
                    return;
                }               
                                
                float balance = withdrawalTransaction.withDrawFunds(cardId, PIN, amount, accounts, cards);
                if(((Float)balance).isNaN())
                {
                    conn.replay(new Message(0)); // there was en error
                }
                else
                {
                    conn.replay(new Message(1, withdrawalTransaction.account, balance));
                }                                 
            }
    }
    
    CheckBalanceTransaction checkBalanceTransaction;
    WithdrawalTransaction withdrawalTransaction;
    
    boolean stillAlive;
    boolean faultTolerant;
    
    void processHeartBeatMessage()
    {
        hbConn.receive();
        if(stillAlive)
        {
            hbConn.replay(new Message(1)); // we are alive!
        }
        else
        {
            hbConn.replay(new Message(0)); // we are not alive!
        }
    }
    
    void kill()
    {
        keepGoing = false;
    }
    
    boolean keepGoing = true;
        
    public void run()
    {
        checkBalanceTransaction = new CheckBalanceTransaction();
        withdrawalTransaction = new WithdrawalTransaction();
        
        while(keepGoing)
        {
            if(conn.isMessage())
                processMessage();
            else if(hbConn.isMessage())
                processHeartBeatMessage();

        }
    }
}
