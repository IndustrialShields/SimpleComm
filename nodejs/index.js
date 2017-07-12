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

exports.send = (stream, data, dest, type, callback) => {
	if (typeof dest == 'function') {
		return exports.send(stream, data, 0, 0, dest);
	}
	if (typeof type == 'function') {
		return exports.send(stream, data, dest, 0, type);
	}

	// Allocate Buffer: SYN + LEN + DST + SRC + TYP + DAT + CRC
	const dat = Buffer.from(data);
	const hdr = Buffer.from([dest, exports.address, type]);
	const pkt = Buffer.concat([hdr, dat], hdr.length + dat.length);
	const crc = calcCRC(pkt);
	const tlen = pkt.length + 1;
	const buff = Buffer.concat([
		Buffer.from([SYN, tlen]),
		pkt,
		Buffer.from([crc]),
	], tlen + 2);

	stream.write(buff, callback);
};

exports.fromBuffer = (buff) => {
	if (buff.length < 2) {
		// Incomplete buffer
		console.error("incomplete buffer");
		return null;
	}
	if (buff.length != buff[1] + 2) {
		// Invalid length
		console.error("invalid length");
		return null;
	}
	const pkt = buff.slice(2, buff.length - 1);
	const crc = calcCRC(pkt);
	if (crc != buff[buff.length - 1]) {
		// Invalid CRC
		return null;
	}

	return {
		destination: pkt[0],
		source: pkt[1],
		type: pkt[2],
		data: pkt.slice(3),
	};
};
