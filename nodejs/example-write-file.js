var fs = require('fs');
var simplecomm = require('./index.js');

simplecomm.address = 12;

var file = fs.createWriteStream('./example-write-file.bin');
simplecomm.send(file, [0x01, 0x02, 0x03, 0x04], 0, 10, () => {
	file.end();
});
