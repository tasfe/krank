package org.crank.core;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Formatter;

public class CrankException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Throwable wrappedException;

    public CrankException() {
        super();
    }

    public CrankException( String message, Throwable wrappedException ) {
        super(message);
        this.wrappedException = wrappedException;
    }

    public CrankException( String message ) {
        super( message );
    }

    public CrankException( String message, Object... args ) {
        super(  (new Formatter()).format(message, args).toString() );
    }
    public CrankException( Throwable wrappedException, String message, Object... args) {
        super(  (new Formatter()).format(message, args).toString() );
        this.wrappedException = wrappedException;
    }

    public CrankException( Throwable wrappedExeption ) {
        super(  );
        this.wrappedException = wrappedExeption;
    }


    @Override
    public void printStackTrace() {
        System.err.println(this.getMessage());
        System.err.println("---------------- ROOT CAUSE -----------------------");
        if (wrappedException!=null) {
            wrappedException.printStackTrace();
        }
        super.printStackTrace();
    }

    @Override
    public void printStackTrace( PrintStream stream ) {
        stream.println(this.getMessage());
        stream.println("---------------- ROOT CAUSE -----------------------");

        if (wrappedException!=null) {
            wrappedException.printStackTrace(stream);
        }
        super.printStackTrace( stream );
    }

    @Override
    public void printStackTrace( PrintWriter writer ) {
        writer.println(this.getMessage());
        writer.println("---------------- ROOT CAUSE -----------------------");
        if (wrappedException!=null) {
            wrappedException.printStackTrace(writer);        
        }
        super.printStackTrace( writer );
    }    

}
