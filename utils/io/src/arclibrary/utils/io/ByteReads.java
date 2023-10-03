package arclibrary.utils.io;

import arc.util.io.*;

import java.io.*;
/**
 * Uses to read something from clear bytes
 * */
public class ByteReads extends Reads{
    public final ReusableByteInStream r = new ReusableByteInStream();

    public ByteReads(){
        super(null);
        input = new DataInputStream(r);
    }

    public ByteReads(byte[] bytes){
        this();
        setBytes(bytes);
    }

    public static ReusableByteInStream setBytes(Reads reads, byte[] bytes){
        if(reads instanceof ByteReads){
            ByteReads byteReads = (ByteReads)reads;
            byteReads.setBytes(bytes);
            return byteReads.r;
        }
        ReusableByteInStream reusableByteInStream = new ReusableByteInStream();
        reads.input = new DataInputStream(reusableByteInStream);
        reusableByteInStream.setBytes(bytes);
        return reusableByteInStream;
    }

    public void setBytes(byte[] bytes){
        r.setBytes(bytes);
    }
}
