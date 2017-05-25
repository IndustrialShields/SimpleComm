package test;

import com.industrialshields.simplecomm.*;

import java.io.File;
import java.io.FileInputStream;

class ReadFileTest {
	public static void main(String[] args) {
		SimpleComm.setAddress((byte) 0x02);

		String filename = "test.hex";
		if (args.length > 0) {
			filename = args[0];
		}

		try {
			File file = new File(filename);
			FileInputStream in = new FileInputStream(file);

			SimplePacket rx = SimpleComm.receive(in);
			if (rx != null) {
				System.out.println("destination: " + String.format("%02x", rx.getDestination()));
				System.out.println("source: " + String.format("%02x", rx.getSource()));
				System.out.println("type: " + String.format("%02x", rx.getType()));
				System.out.println("data:");
				System.out.println("  " + String.format("%08x", rx.getBuffer().getInt()));
				System.out.println("  " + String.format("%02x", rx.getBuffer().get()));
			}

			in.close();
		} catch (Exception e) {
			System.out.println("Exception");
		}
	}
}
