const SYN = 0x02;

function calcCRC(buff) {
	var ret = 0;
	for (const b of buff) {
		ret += b;
	}
	return ret & 0xff;
}

var exports = module.exports = {};

exports.address = 0;

exports.toBuffer = (data, dest, type) => {
	if (typeof type == "undefined") {
		type = 0;
	}
	if (typeof dest == "undefined") {
		dest = 0;
	}

	// Allocate Buffer: SYN + LEN + DST + SRC + TYP + DAT + CRC
	const dat = Buffer.from(data);
	const hdr = Buffer.from([dest, exports.address, type]);
	const pkt = Buffer.concat([hdr, dat], hdr.length + dat.length);
	const crc = calcCRC(pkt);
	const tlen = pkt.length + 1;

	return  Buffer.concat([
			Buffer.from([SYN, tlen]),
			pkt,
			Buffer.from([crc]),
	], tlen + 2);
};

exports.fromBuffer = (buff) => {
	var packets = [];
	while (buff.length > 2) {
		if (buff[0] != SYN) {
			// Unsynchronized
			buff = buff.slice(1);
			continue;
		}

		if (buff[1] > 132) { // 128 (Max DAT length) + 4 (DST + SRC + TYP + CRC)
			// Invalid length
			buff = buff.slice(1);
			continue;
		}

		if (buff[1] + 2 > buff.length) {
			// Too small buffer
			break;
		}

		const pkt = buff.slice(2, buff[1] + 2);
		const crc = calcCRC(pkt.slice(0, pkt.length - 1));
		if (pkt[pkt.length - 1] != crc) {
			// Invalid CRC
			buff = buff.slice(1);
			continue;
		}

		buff = buff.slice(pkt.length + 2);

		packets.push({
			destination: pkt[0],
			source: pkt[1],
			type: pkt[2],
			data: pkt.slice(3, pkt.length - 1),
		});
	}

	return packets;
};

exports.send = (stream, data, dest, type, callback) => {
	if (typeof dest == 'function') {
		return exports.send(stream, data, 0, 0, dest);
	}
	if (typeof type == 'function') {
		return exports.send(stream, data, dest, 0, type);
	}

	const buff = exports.toBuffer(data, dest, type);
	stream.write(buff, callback);
};
