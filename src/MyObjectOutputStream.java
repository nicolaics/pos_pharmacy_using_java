import java.io.*;

/* OutputStream class for appending files */
public class MyObjectOutputStream extends ObjectOutputStream{

    MyObjectOutputStream() throws IOException, SecurityException {
        super();
    }
    
    MyObjectOutputStream(OutputStream o) throws IOException{
        super(o);
    }
    
    @Override
    protected void writeStreamHeader() throws IOException {
    }
}
