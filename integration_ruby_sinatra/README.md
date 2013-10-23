Delivery Type 		Web Service Push / WEBSVC-PUSH
Delivery Format 	Type JSON / JSON

Params

	url				https://yoururl.com/numerex_services/delivery/message
	username	 	username
	password 		password
	auth-type 		basic

curl --user username:password -H "Content-Type: application/json" -X POST http://yoururl.com:3000/numerex_services/delivery/message -d '{"device_name_type":"imei", "device_name":"357820023702392"}'
curl --user username:password -H "Content-Type: application/json" -X POST https://yoururl.com:3000/numerex_services/delivery/message -d '{"device_name_type":"imei", "device_name":"357820023702392"}' -k

=== Ruby usage
The below snippet indicates how to subclass and use Numerex's WebPushReceiver mechanism in ruby.

```ruby
require 'numerex/web_push_receiver'
class ExampleReceiver < Numerex::WebPushReceiver
  #note: message is a hash
  def message_received(message)
    #this is where you handle the incoming message. store it in a database, process it, whatever.
    puts "Message received: #{message}"
  end

  def authenticate username, password
    <your authentication code goes here, return true if authenticated, false otherwise>
  end
end

#To start this service with thin, use the following:
require 'thin'
web_push_receiver = ExampleReceiver.new
Thin::Server.start('0.0.0.0', 3000, web_push_receiver)