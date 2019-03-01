package io.ejat.zos3270.internal.datastream;

import java.nio.ByteBuffer;

import io.ejat.zos3270.internal.terminal.Screen;

public class QueryReplyUsableArea extends QueryReply {
	
	private static final byte USUABLE_AREA = (byte)0x81;
	
	private final int cellX;
	private final int cellY;
	
	private static final byte   UNITS = 0x01;
	private static final byte[] XR    = new byte[] {0x00, 0x0a, 0x02, (byte) 0xe5};
	private static final byte[] YR    = new byte[] {0x00, 0x02, 0x00, (byte) 0x6f};
	private static final byte   AW    = 0x09;
	private static final byte   AH    = 0x0c;
	
	
	public QueryReplyUsableArea(Screen screen) {
		this.cellX = screen.getNoOfColumns();
		this.cellY = screen.getNoOfRows();
	}

	@Override
	public byte[] toByte() {
		ByteBuffer buffer = ByteBuffer.allocate(23);
		buffer.putShort((short) 23);
		buffer.put(QueryReply.QUERY_REPLY);
		buffer.put(USUABLE_AREA);
		buffer.put((byte)0x01);  //*** 12/14 bit addressing allowed
		buffer.put((byte)0x00);  //*** Variable cells nosupported, matrix chars, cell units
		buffer.putShort((short)cellX);
		buffer.putShort((short)cellY);
		buffer.put(UNITS);
		buffer.put(XR);
		buffer.put(YR);
		buffer.put(AW);
		buffer.put(AH);
		buffer.putShort((short)(cellX * cellY));
		return buffer.array();
	}
	
	@Override
	public byte getID() {
		return USUABLE_AREA;
	}
	
}
