public class ErrorProcessingUnit {
    
    public ErrorProcessingUnit(Server main, Server backup)
    {
        this.main = main;
        this.backup = backup;
    }
    
    Server main, backup; 
    Monitor m;
    
    // handle server faults
    public void fault()
    {
        main.kill();
        backup.start();
    }
    
    public void endAll()
    {
        main.kill();
        backup.kill();
        m.kill();
    }
}
