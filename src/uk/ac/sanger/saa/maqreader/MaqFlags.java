package uk.ac.sanger.saa.maqreader;

import java.util.EnumSet;
import java.util.Set;

import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;

public enum MaqFlags {

	PAIRFLAG_FF      (0x01),
	PAIRFLAG_FR      (0x02),
	PAIRFLAG_RF      (0x04),
	PAIRFLAG_RR      (0x08),
	PAIRFLAG_PAIRED  (0x10),
	PAIRFLAG_DIFFCHR (0x20),
	PAIRFLAG_NOMATCH (0x40),
	PAIRFLAG_SW      (0x80);
	final int bitmask;
	private MaqFlags(int hexCode){
		bitmask = hexCode;
	}
	public static MaqFlags valueOf(int hexCode){
		return null;
		
	}
	public static Set<MaqFlags> getFlags(int i){

		Set<MaqFlags> set = EnumSet.noneOf(MaqFlags.class);
		for (MaqFlags flag:MaqFlags.values()){
			if ((i & flag.bitmask)!=0){
				set.add(flag);
			}
		}
		return set;
	}
}
