
import java.util.Scanner;

public class Client extends Thread{
    
    public Client(SynchronousConnector sc, Server regular, Server faultTolerantServer)
    {
        conn = sc;
        this.regularServer = regular;
        this.faultTolerantServer = faultTolerantServer;
    }
    
    SynchronousConnector conn;
    Server regularServer;
    Server faultTolerantServer;   
    public ErrorProcessingUnit ep;
    
    public void run()
    {
        while(true)
        {
            int option = display();
            if (option == 1)
                check();
            else if (option == 2)
                withdraw();
            
            System.out.println("**************************************************************");
            System.out.println("Would you like to do other transactions (press 1) or exit (press 2)");
            System.out.println("**************************************************************");
            Scanner sc = new Scanner(System.in);
            option = sc.nextInt();
            if(option == 2) break;            
        }
        ep.endAll();
    }
    
    void check()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("input msg.CardId: ");
        int cardId = sc.nextInt();
        System.out.print("input msg.PIN: ");
        int PIN = sc.nextInt();
        
        while(true)
        {
            conn.send(new Message(1, cardId, PIN));
            Message m = conn.receiveReplay();

            if((int)m.data.get(0)==1) // all ok
            {
                int account = (int)m.data.get(1); // account
                float balance = (float)m.data.get(2); // balance
                System.out.println("**************************ATM Client**************************");
                System.out.println("Customer notification: Balance account for Account # " + account + " is: " + balance);
                System.out.println("**************************************************************"); 
                break;
            }
            else if((int)m.data.get(0)==0) // some error
            {
                break;
            }
            // else there is an error... simply try again!
        }
        

    }
    
    void withdraw()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("input msg.CardId: ");
        int cardId = sc.nextInt();
        System.out.print("input msg.PIN: ");
        int PIN = sc.nextInt();
        System.out.print("input msg.amount: ");
        float amount = sc.nextFloat();
        
        while(true)
        {
            conn.send(new Message(2, cardId, PIN, amount));
            Message m = conn.receiveReplay();
            if((int)m.data.get(0)==1) // all ok
            {
                int account = (int)m.data.get(1); // account
                float balance = (float)m.data.get(2); // balance
                System.out.println("**************************ATM Client**************************");
                System.out.println("Customer notification: Balance account for Account # " + account + " is: " + balance);
                System.out.println("**************************************************************"); 
                break;
            }    
            else if((int)m.data.get(0)==0) // some error
            {
                break;
            }
            
        }        
        
    }
    
    int display()
    {
        System.out.println("+#######################################################+");
        System.out.println("|   ***                                           ***   |");
        System.out.println("|   ***            WELCOME TO MSU BANK            ***   |");
        System.out.println("|   ***                                           ***   |");
        System.out.println("+#######################################################+");
        System.out.println("|   ***          Choose your Transaction          ***   |");
        System.out.println("|   ***   Please press the corresponding number   ***   |");
        System.out.println("|   ***                                           ***   |");
        System.out.println("|   ***1.   Check Balance                         ***   |");
        System.out.println("|   ***2.   Withdraw                              ***   |");
        System.out.println("+#######################################################+");
        System.out.println("");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }
}
