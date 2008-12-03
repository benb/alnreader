package uk.ac.sanger.saa.maqreader;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import javolution.io.Struct;
import javolution.io.Struct.UTF8String;
import javolution.text.TextBuilder;

/**
 * Implements the maqmap1_t struct for reading maq files
 * http://maq.sourceforge.net/maqmap_format.shtml
 * @author bpb
 *
 */
public class Maqmap1_t extends Struct {
	
	//could be 128
	 
	 public static final int MAX_NAMELEN=36;
	 public final Unsigned8[] seq;
	 public final Signed8 mapQ;
	 //uint8_t seq[MAX_READLEN]; /* the last base is the single-end mapping quality. */
	 public final Unsigned8 size;
	 public final Unsigned8 map_qual;
	 public final Unsigned8 info1;
	 public final Unsigned8 info2;
	 public final Unsigned8 c0;
	 public final Unsigned8 c1;
	 
	 public final Unsigned8 flag;
	 public final Unsigned8 alt_qual;
	 
	 public final Unsigned32 seqid;
	 public final Unsigned32 pos;
	 public final Signed32 dist; 
	 
	 public final UTF8String name;
	 
	 private Maqmap1_t(int MAX_READLEN){
		 super();
		 seq = array(new Unsigned8[MAX_READLEN]);
		 mapQ = new Signed8();
		 size = new Unsigned8();
		 map_qual = new Unsigned8();
		 info1 = new Unsigned8();
		 info2 = new Unsigned8();
		 c0 = new Unsigned8();
		 c1 = new Unsigned8();
		 flag = new Unsigned8();
		 alt_qual = new Unsigned8();
		 seqid = new Unsigned32();
		 pos = new Unsigned32();
		 dist = new Signed32(); 
		 
		 name = new UTF8String(MAX_NAMELEN);
	 }
	 
	 public static Maqmap1_t factory(boolean newFormat){
		 if (newFormat){
			 return new Maqmap1_t(127);
		 }else {
			 return new Maqmap1_t(63);
		 }
	 }
	 
	 
	 
	 public ByteOrder byteOrder() {
	        // use hardware byte order.
	        return ByteOrder.nativeOrder();
	   }
	 
	 
	public void dump(){
		
		 
		 System.out.println("Size " + size.get());
		 System.out.println("MapQ " + map_qual.get());
		 System.out.println("Info1a " + (info1.get() & 0xf));
		 System.out.println("Info1b " + (info1.get() & ~0xf));
		 System.out.println("Info2 " + info2.get());
		 System.out.println("C0 " + c0.get());
		 System.out.println("C1 " + c1.get());
		 System.out.println("Flag " + flag.get());
		 System.out.println("Altqual " + alt_qual.get());
		 System.out.println("SEQID " + seqid.get() );
		 System.out.println("pos " + ((pos.get()>>1) + 1));
		 if (((int)pos.get() & 1)>0){
			 System.out.println("-");
		 }else {
			 System.out.println("+");
		 }
		 String[] acgt = {"A","C","G","T"};
		 for (int c = 0; c < size.get(); c++){
			 short i = seq[c].get();
			 if (i==0){
				 System.out.print("N");
			 }else {
				 System.out.print(acgt[i>>6]);
			 }
		 }
		 System.out.println();
		 for (int c = 0; c < size.get(); c++){
			 short i = seq[c].get();
			 System.out.print((char) (33+(i & 0x3f)));
		 }
		 System.out.println(); 
		 System.out.println("dist " + dist.get());
		 

		 
		 
		 System.out.println("name (" + name.get() + ")");
		 
		 System.out.println((int)seqid.get());
	
	 }

}
