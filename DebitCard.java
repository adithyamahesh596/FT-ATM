public class DebitCard {
    int cardId;
    int PIN;
    int accountNo;
    float dailyDebitTotal;  
    
    public DebitCard(int cardId, int PIN, int accoutNo, float dailyDebitTotal)
    {
        this.cardId = cardId;
        this.PIN = PIN;
        this.accountNo = accoutNo;
        this.dailyDebitTotal = dailyDebitTotal;
    }
    
    public int validatePIN(int cardId, int PIN)
    {
        if(cardId == this.cardId && PIN == this.PIN)
            return accountNo;
        
        return 0;
    }
    
    public void updateDailyDebitTotal(float amount)
    {
        dailyDebitTotal += amount;
    }
    
    public boolean checkDailyDebitLimit(float amount)
    {
        return dailyDebitTotal + amount <= 300;
    }
}
