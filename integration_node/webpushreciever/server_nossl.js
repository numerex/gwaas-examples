var express = require('express')
var http = require('http')
var measured = require('measured')

var expressApp = express()
var measuredCollection = new measured.Collection('http')
var rps = measuredCollection.meter('requestsPerSecond')

var auth = express.basicAuth(function(user, pass) {
    return (user == "andrewisthecoolest" && pass == "andrewisthecoolest")
},'Authentication required')

expressApp.use(express.logger())
expressApp.use(express.bodyParser())

expressApp.get('/numerex_services/delivery/system_status/system_status', auth, function(req, res) {
    console.log("System status check")
    var body = 'true'
    res.send(body)
})

expressApp.post('/numerex_services/delivery/message/message', auth, function(req, res) {
    rps.mark()
    console.log('\r\nMessage = ' + JSON.stringify(req.body))
    console.log('\r\nMetrics = ' + JSON.stringify(measuredCollection.toJSON()))
    var body = 'OK'
    res.send(body)
    rps.end('Thanks')
})

setInterval(function() {
    console.log('\r\nPeriodic Metrics = ' + JSON.stringify(measuredCollection.toJSON()))
}, 10000);
http.createServer(expressApp).listen(8080)

console.log('App started')
