package uk.ac.sanger.saa.maqreader;


import java.nio.ByteOrder;

import javolution.io.Struct;
import javolution.io.Struct.UTF8String;

public class Maqmap_tA extends Struct {
	
	
	 public final Signed32 format = new Signed32();
	 public final Signed32 n_ref = new Signed32();
	    public ByteOrder byteOrder() {
	        // use hardware byte order.
	        return ByteOrder.nativeOrder();
	   }

	
}
