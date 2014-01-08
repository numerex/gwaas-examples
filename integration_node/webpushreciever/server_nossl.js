var express = require('express')
var http = require('http')
var measured = require('measured')

var expressApp = express()
var meteredMessageCollection = new measured.Collection('messages')
var mps = meteredMessageCollection.meter('mps')

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
    mps.mark()
    console.log('\r\nMessage = ' + JSON.stringify(req.body))
    console.log('\r\nMetrics = ' + JSON.stringify(meteredMessageCollection.toJSON()))
    var body = 'OK'
    res.send(body)
    mps.end('Thanks')
})

setInterval(function() {
    console.log('\r\nPeriodic Metrics = ' + JSON.stringify(meteredMessageCollection.toJSON()))
}, 1000 * 6 * 5)

http.createServer(expressApp).listen(8080)

console.log('App started')
