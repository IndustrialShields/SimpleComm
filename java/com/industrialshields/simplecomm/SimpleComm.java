package com.industrialshields.simplecomm;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

// PACKET FORMAT:
//  _____________________________________________________________________
// |         |         |                                                 |
// |         |         |                       PKT                       |
// |_________|_________|_________________________________________________|
// |         |         |                             |         |         |
// |   SYN   |   LEN   |            HDR              |   DAT   |   CRC   |
// |_________|_________|_____________________________|_________|_________|
// |         |         |         |         |         |         |         |
// | SYN (1) | LEN (1) | DST (1) | SRC (1) | TYP (1) |   DAT   | CRC (1) |
// |_________|_________|_________|_________|_________|_________|_________|
//

public class SimpleComm {
	private static final byte SYN = 0x02;

	private static byte address = 0;

	private SimpleComm() {
	
	}

	public static void setAddress(byte address) {
		SimpleComm.address = address;
	}

	public static final byte getAddress() {
		return SimpleComm.address;
	}

	public static boolean send(OutputStream stream, SimplePacket packet) {
		packet.setSource(address);

		ByteBuffer dat = packet.getBuffer();
		dat.flip();
		int len = dat.limit();

		ByteBuffer buffer = ByteBuffer.allocate(258)
			.put(SYN)
			.put((byte) (len + 4))
			.put(packet.getDestination())
			.put(packet.getSource())
			.put(packet.getType())
			.put(dat.array(), 0, len);
		byte crc = SimpleComm.calcCRC(buffer.array(), 2, len + 3);
		buffer.put(crc);
		buffer.flip();

		int tlen = buffer.remaining();
		WritableByteChannel channel = Channels.newChannel(stream);

		boolean ret = false;
		try {
			ret = (channel.write(buffer) == tlen);
		} catch (Exception e) {
			ret = false;
		}
		return ret;
	}

	public static boolean send(OutputStream stream, SimplePacket packet, byte destination) {
		packet.setDestination(destination);
		return send(stream, packet);
	}

	public static boolean send(OutputStream stream, SimplePacket packet, byte destination, byte type) {
		packet.setType(type);
		return send(stream, packet, destination);
	}

	public static SimplePacket receive(InputStream stream) {
		ReadableByteChannel channel = Channels.newChannel(stream);
		ByteBuffer buffer = ByteBuffer.allocate(258);

		try {
			while (true) {
				buffer.clear();
				buffer.limit(1);
				if (channel.read(buffer) <= 0) {
					break;
				}

				buffer.rewind();
				byte in = buffer.get();
				if (in != SYN) {
					continue;
				}

				buffer.clear();
				buffer.limit(1);
				if (channel.read(buffer) <= 0) {
					continue;
				}

				buffer.rewind();
				byte tlen = buffer.get();

				if (tlen == 0) {
					continue;
				}

				buffer.clear();
				buffer.limit(tlen);
				if (channel.read(buffer) < tlen) {
					continue;
				}

				buffer.position(buffer.limit() - 1);
				byte crc = buffer.get();

				buffer.rewind();
				if (calcCRC(buffer.array(), 0, tlen - 1) != crc) {
					continue;
				}

				byte destination = buffer.get();
				if (destination != SimpleComm.getAddress()) {
					continue;
				}

				SimplePacket ret = new SimplePacket()
					.setDestination(destination)
					.setSource(buffer.get())
					.setType(buffer.get());

				ByteBuffer dat = ret.getBuffer();
				dat.put(buffer.array(), 3, tlen - 4);
				dat.flip();

				return ret;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private static byte calcCRC(byte[] buff, int offset, int length) {
		byte ret = 0;
		for (int i = 0; i < length; ++i) {
			ret += buff[i + offset];
		}
		return ret;
	}
}
