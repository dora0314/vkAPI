package Main;

public class AccesClosedException extends Exception {
    public AccesClosedException()
    {
        super("Access is closed");
    }
}
