package uk.ac.sanger.saa.maqreader;

import java.util.Set;

public class MaqRecord {

 public static final int MAX_READLEN=63;
	 
	 final char[] sequence;
	 final byte[] quality;
	 
	 short mapQ;
	 
	 final short size;
	 
	 //if PAIRFLAG_SW|PAIRFLAG_PAIRED
	 short indelLength;
	 short indelPosition;
	 short mateMapQ;
	 
	 short seMapQ;
	 short finalMapQ;
	 short lowerMapQ;
	 
	 final short info1A;
	 final short info1B;
	 final short info2;
	 
	 int dist;
	 
	 long pos;
	 
	 String chromosome;
	 
	 final Set<MaqFlags> flags;
	 final String name;
	 private final static char[] acgt = {'A','C','G','T'};
		
	public MaqRecord(Maqmap1_t maqMap1t, String[] refNames) {
		flags = MaqFlags.getFlags(maqMap1t.flag.get());
		name = maqMap1t.name.get();
		size = maqMap1t.size.get();
		sequence = new char[size];
		
		for (int c = 0; c < size; c++){
			 short i = maqMap1t.seq[c].get();
			 if (i==0){
				 sequence[c]='N';
			 }else {
				 sequence[c]=(acgt[i>>6]);
			 }
		 }
		
		quality = new byte[size];
		for (int c = 0; c < size; c++){
			 short i = maqMap1t.seq[c].get();
			 quality[c]=(byte) (i & 0x3f);
		}
		
		if (flags.contains(MaqFlags.PAIRFLAG_SW)||flags.contains(MaqFlags.PAIRFLAG_PAIRED)){
			indelLength=maqMap1t.mapQ.get();
			indelPosition=maqMap1t.map_qual.get();
			mateMapQ=maqMap1t.alt_qual.get();
		}else {
			seMapQ = maqMap1t.mapQ.get();
			finalMapQ = maqMap1t.map_qual.get();
			lowerMapQ = maqMap1t.alt_qual.get();
		}
		
		info1A=(short) (maqMap1t.info1.get() & 0xf);
		info1B=(short)((maqMap1t.info1.get() & ~0xf)<<4);
		info2 = maqMap1t.info2.get();
		chromosome = refNames[(int) maqMap1t.seqid.get()];
	}

}
