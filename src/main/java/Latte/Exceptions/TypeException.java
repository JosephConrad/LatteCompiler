package Latte.Exceptions;

/**
 * Created by konrad on 10/02/15.
 */
public class TypeException extends Exception
{
    //Parameterless Constructor
    public TypeException(String functionName, String message) {
        super("TYPE ERROR:\n\t" + "At function "+ functionName +":\n\t\t"+ message);

    }

    //Constructor that accepts a message
    public TypeException(String message)
    {
        super("\nTYPE ERROR:\n\t" + message);
    }
}