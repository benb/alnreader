package uk.ac.sanger.saa.maqreader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javolution.io.Struct;


public class MaqmapReader {
	
	private final ReadableByteChannel in;
	private final Maqmap1_t maqMap1t;
	
	private final int format;
	private final int refnum;
	private final int nref;
	
	private final String[] refNames;
	public MaqmapReader(File f) throws FileNotFoundException, IOException{
		InputStream st = new GZIPInputStream(new FileInputStream(f));
		in = Channels.newChannel(st);
		Maqmap_tA maqMapA = new Maqmap_tA();
		
		read(maqMapA);
		format = maqMapA.format.get();
		refnum = maqMapA.format.get();
		nref = maqMapA.n_ref.get();
		

		Maqmap_tB maqMapB = new Maqmap_tB();
		List<String> refList = new ArrayList<String>(); 
		for (int i=0; i < nref; i++){
			read(maqMapB);
			long nameLen = maqMapB.len.get();
			
			Maqmap_tC maqMapC = new Maqmap_tC(nameLen);
			read(maqMapC);
			refList.add(maqMapC.name.get());
		}
		refNames = refList.toArray(new String[refList.size()]);
		

		
		//need to skip 64 bits
		ByteBuffer junk = ByteBuffer.allocate(128);
		while (junk.hasRemaining()){
			in.read(junk);
		}
		

		maqMap1t = new Maqmap1_t();
		
	}
	
	public MaqRecord getRecord() throws IOException{
		while (true){
			if (!read(maqMap1t)){
				return null;
			}
			
			return new MaqRecord(maqMap1t,refNames);
		}

	}
	
	
	private boolean read(Struct str) throws IOException{
		str.getByteBuffer().clear();
		while(str.getByteBuffer().hasRemaining()){
			if ((in.read(str.getByteBuffer()))==-1){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String args[]) throws FileNotFoundException, IOException{
		
		// Backward compatability thing:
		MaqmapReader reader = new MaqmapReader(new File(args[0]));
		MaqRecord record = null;
		
		System.out.println(reader.nref);
		while ((record = reader.getRecord())!=null){
			
			System.out.println(record);
		}
		
	}
}
