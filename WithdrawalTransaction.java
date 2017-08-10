
import java.util.ArrayList;

public class WithdrawalTransaction {
    
    public int account;
    
    synchronized public float withDrawFunds(int cardId, int PIN, float amount, 
            ArrayList<CheckingAccount> checkingAccountList,
            ArrayList<DebitCard> debitCardList)
    {
        for(DebitCard dc : debitCardList)
        {
            if(dc.cardId == cardId && dc.PIN == PIN)
            {
                for(CheckingAccount ca : checkingAccountList)
                {
                    if(ca.getAccountNumber() == dc.accountNo)
                    {
                        if(dc.checkDailyDebitLimit(amount)) // we can withdraw this amount from the debit card
                        {
                            if(ca.readBalance() >= amount)
                            {
                                dc.updateDailyDebitTotal(amount);
                                ca.debit(amount);
                                account = ca.getAccountNumber();
                                return ca.balance;
                            }
                            else
                            {
                                System.out.println("Not enough balance");
                                return Float.NaN;
                            }
                        }
                        else
                        {
                            System.out.println("Daily limit reached");
                            return Float.NaN;
                        }
                    }
                }
            }
        }
        System.out.println("Invalid CardId or PIN");     
        return Float.NaN;
    }
}
