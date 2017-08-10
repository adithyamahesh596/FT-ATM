public class Account {
    int accountNumber;
    float balance;
    
    public Account()
    {
        balance = 0.f;
    }
    
    public void open(int accountNumber)
    {
        this.accountNumber = accountNumber;
    }
    
    public float readBalance()
    {
        return balance;
    }
    
    public void credit(float amount)
    {
        balance += amount;
    }
    
    public void debit(float amount)
    {
        balance -= amount;
    }
    
    int getAccountNumber()
    {
        return accountNumber;
    }        
}
