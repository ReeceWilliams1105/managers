package io.ejat.zos3270.internal.datastream;

import io.ejat.zos3270.spi.NetworkException;

public class StructuredField {
	
	public static final byte SF_READ_PARTITION = 0x01;

	public static StructuredField getStructuredField(byte[] sfData) throws NetworkException {
		switch(sfData[0]) {
		case SF_READ_PARTITION:
			return new StructuredFieldReadPartition(sfData);
		default:
			throw new NetworkException("Unknown Structured Field = " + sfData[0]);
		}
	}

}
