package uk.ac.sanger.saa.maqreader;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MaqFlagsTest {

	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void test(){
		assertTrue(MaqFlags.getFlags(0x01).contains(MaqFlags.PAIRFLAG_FF));
		assertEquals(1,MaqFlags.getFlags(0x01).size());
		
		assertEquals(3,MaqFlags.getFlags(0x01+0x02+0x10).size());
		assertEquals(0,MaqFlags.getFlags(0x00).size());
		
	}

}
