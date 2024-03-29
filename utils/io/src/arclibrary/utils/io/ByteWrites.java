package arclibrary.utils.io;

import arc.util.io.*;

import java.io.*;

/**
 * Uses to write something to clear bytes
 * */
public class ByteWrites extends Writes{
    public final ReusableByteOutStream r = new ReusableByteOutStream(8192);

    public ByteWrites(){
        super(null);
        output = new DataOutputStream(r);
    }
    public void reset(){
        r.reset();
    }

    public byte[] getBytes(){
        return  r.toByteArray();
    }
}
