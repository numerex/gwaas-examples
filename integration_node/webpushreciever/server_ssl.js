var express = require('express');
var https = require('https');
var http = require('http');
var fs = require('fs');

var options = {
key: fs.readFileSync('privatekey.pem'),
	cert: fs.readFileSync('certificate.pem')
};

var app = express();
var auth = express.basicAuth(function(user, pass) {
	return (user == "andrewisthecoolest" && pass == "andrewisthecoolest");
}, 'Authentication required');

app.use(express.logger());
app.use(express.bodyParser());

app.get('/numerex_services/delivery/system_status/system_status', auth, function(req, res) {
	console.log("System status check");
	var body = 'true';
	res.send(body);
});

app.post('/numerex_services/delivery/message/message', auth, function(req, res) {
	console.log('\r\nMessage = ' + JSON.stringify(req.body));
	var body = 'OK';
	res.send(body);
});

http.createServer(app).listen(80);
https.createServer(options, app).listen(443);

console.log('App started')
