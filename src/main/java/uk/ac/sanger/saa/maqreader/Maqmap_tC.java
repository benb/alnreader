package uk.ac.sanger.saa.maqreader;


import java.nio.ByteOrder;

import javolution.io.Struct;
import javolution.io.Struct.UTF8String;

public class Maqmap_tC extends Struct {
	
	public final UTF8String name;
	
	public Maqmap_tC(long l){
		 name = new UTF8String((int)l);
	}
    public ByteOrder byteOrder() {
        // use hardware byte order.
        return ByteOrder.nativeOrder();
   }

}
