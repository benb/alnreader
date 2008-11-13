package uk.ac.sanger.saa.maqreader;


import java.nio.ByteOrder;

import javolution.io.Struct;
import javolution.io.Struct.UTF8String;

public class Maqmap_tB extends Struct {
	
	public Unsigned32 len = new Unsigned32();
	
    public ByteOrder byteOrder() {
        // use hardware byte order.
        return ByteOrder.nativeOrder();
   }

}
