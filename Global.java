
import java.util.ArrayList;

public class Global {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // accounts:        
        
        CheckingAccount c1, c2, c3, c4, c5;
        c1 = new CheckingAccount();
        c2 = new CheckingAccount();
        c3 = new CheckingAccount();
        c4 = new CheckingAccount();
        c5 = new CheckingAccount();
        
        
        c1.open(1000);
        c1.credit(200);
        c2.open(1001);
        c2.credit(200);
        c3.open(1002);
        c3.credit(300);
        c4.open(1003);
        c4.credit(400);
        c5.open(1004);
        c5.credit(500);
        
        ArrayList<CheckingAccount> accounts = new ArrayList<>();
        accounts.add(c1);
        accounts.add(c2);
        accounts.add(c3);
        accounts.add(c4);
        accounts.add(c5);
        
        // debit cards:
        
        DebitCard d1, d2, d3, d4, d5;
        
        d1 = new DebitCard(3001, 3001, 1000, 200);
        d2 = new DebitCard(3002, 3002, 1001, 250);
        d3 = new DebitCard(3003, 3003, 1002, 0);
        d4 = new DebitCard(3004, 3004, 1003, 100);
        d5 = new DebitCard(3005, 3005, 1004, 150);
        
        ArrayList<DebitCard> cards = new ArrayList<>();
        cards.add(d1);
        cards.add(d2);
        cards.add(d3);
        cards.add(d4);
        cards.add(d5);        
        
        /*
        CheckBalanceTransaction t1 = new CheckBalanceTransaction();
        WithdrawalTransaction t2 = new WithdrawalTransaction();
        
        
        float b;
        
        b = t1.checkBalance(666,777,accounts, cards);
        b = t1.checkBalance(3001,3001,accounts, cards);
        
        System.out.println(b);
        
        b = t2.withDrawFunds(3001, 3001, 100, accounts, cards);
        System.out.println(b);
        
        b = t2.withDrawFunds(3001, 3001, 1, accounts, cards);
        System.out.println(b);
        
        b = t2.withDrawFunds(3003, 3003, 100, accounts, cards);
        System.out.println(b);
        */
        SynchronousConnector conn = new SynchronousConnector();
        HeartBeatConnector hbConn = new HeartBeatConnector();
                
        Server reg = new Server(conn, hbConn, accounts, cards, false);
        Server fault = new Server(conn, hbConn, accounts, cards, true);

        ErrorProcessingUnit errorHandler = new ErrorProcessingUnit(reg, fault);
        
        Client c = new Client(conn, reg, fault);
        
        Monitor m = new Monitor(hbConn, errorHandler);
        
        c.ep = errorHandler;
        errorHandler.m = m;
        
        c.start();
        reg.start();
        m.start();
        //fault.start();
        
    }
    
}
