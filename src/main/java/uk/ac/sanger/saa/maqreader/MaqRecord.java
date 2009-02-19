package uk.ac.sanger.saa.maqreader;

import java.util.Set;

public class MaqRecord {

 public static final int MAX_READLEN=63;
	 
	public final short mapQ;
	 
	 public final short size;
	 
	 //if PAIRFLAG_SW|PAIRFLAG_PAIRED
	 public short indelLength;
	 public short indelPosition;
	 public short mateMapQ;
	 
	 public short seMapQ;
	 public short finalMapQ;
	 public short lowerMapQ;
	 
	 public final short info1A;
	 public final short info1B;
	 public final short info2;
	 
	 public final int dist;
	 
	 public final long pos;
	 
	 public String chromosome;
	 
	 public final boolean forward;
	 
	 public final Set<MaqFlags> flags;
	 public final String name;
		
	 
	 
	public MaqRecord(Maqmap1_t maqMap1t, String[] refNames) {
		flags = MaqFlags.getFlags(maqMap1t.flag.get());
		name = maqMap1t.name.get();
		size = maqMap1t.size.get();
	
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
		pos = ((maqMap1t.pos.get()>>1)+1);
		forward = (((int)maqMap1t.pos.get() & 1)==0);
		dist = maqMap1t.dist.get();
		mapQ = maqMap1t.map_qual.get();
	}
	@Override
	public String toString(){
		
		return name + " " + chromosome + " " + pos + " " + forward+ " " + dist + " " +  flags;
	}

}
