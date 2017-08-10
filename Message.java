import java.util.ArrayList;

public class Message {
    // the message is a list of objects
    public ArrayList<Object> data;
    
    public Message(int code)
    {
        data = new ArrayList<>();
        data.add(code);
    }
    
    public Message(int code, int id, int pin)
    {
        data = new ArrayList<>();
        data.add(code);
        data.add(id);
        data.add(pin);
    }
    
    public Message(int code, int account, float balance)
    {
        data = new ArrayList<>();
        data.add(code);
        data.add(account);
        data.add(balance);
    }
    
    public Message(int code, int id, int pin, float amount)
    {
        data = new ArrayList<>();
        data.add(code);
        data.add(id);
        data.add(pin);
        data.add(amount);
    }
    
    public Message(ArrayList<Object> data)
    {
        this.data = data;
    }
}
