package test;

import com.industrialshields.simplecomm.*;

import java.io.File;
import java.io.FileOutputStream;

class WriteFileTest {
	public static void main(String[] args) {
		SimpleComm.setAddress((byte) 0x01);

		String filename = "test.hex";
		if (args.length > 0) {
			filename = args[0];
		}

		try {
			File file = new File(filename);
			FileOutputStream out = new FileOutputStream(file);

			if (!file.exists()) {
				file.createNewFile();
			}

			SimplePacket tx = new SimplePacket();
			tx.getBuffer()
				.putInt(0x12345678)
				.put((byte) 0xab);
			SimpleComm.send(out, tx, (byte) 0x02, (byte) 0xee);

			out.flush();
			out.close();
		} catch (Exception e) {
			System.out.println("Exception");
		}
	}
}
