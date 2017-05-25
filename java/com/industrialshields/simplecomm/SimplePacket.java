package com.industrialshields.simplecomm;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SimplePacket {
	private byte source;
	private byte destination;
	private byte type;
	private ByteBuffer buffer;

	public SimplePacket() {
		this.source = SimpleComm.getAddress();
		this.destination = 0;
		this.type = 0;
		this.buffer = ByteBuffer.allocate(250).order(ByteOrder.LITTLE_ENDIAN);
	}

	public ByteBuffer getBuffer() {
		return this.buffer;
	}

	public byte getSource() {
		return this.source;
	}

	public SimplePacket setSource(byte source) {
		this.source = source;
		return this;
	}

	public byte getDestination() {
		return this.destination;
	}

	public SimplePacket setDestination(byte destination) {
		this.destination = destination;
		return this;
	}

	public byte getType() {
		return this.type;
	}

	public SimplePacket setType(byte type) {
		this.type = type;
		return this;
	}
}
