require 'sinatra'
require 'json'
module Numerex
  class WebPushReceiver < Sinatra::Base
    helpers do
      def protected!
        return if authorized?
        headers['WWW-Authenticate'] = 'Basic realm="Restricted Area"'
        halt 401, "Not authorized\n"
      end

      def authorized?
        @auth ||=  Rack::Auth::Basic::Request.new(request.env)
        @auth.provided? and @auth.basic? and @auth.credentials and authenticate(@auth.credentials[0], @auth.credentials[1])
      end
    end

    before do
      if request.request_method == "POST" and request.content_type=="application/json"
        body_parameters = request.body.read
        parsed = body_parameters && body_parameters.length >= 2 ? JSON.parse(body_parameters) : nil
        params.merge!(parsed)
      end
    end

    get '/numerex_services/delivery/system_status/system_status' do
      'true'
    end

    post '/numerex_services/delivery/message' do
      protected!
      message_received params
      'ok'
    end

    def message_received message
      puts "Message received: #{message}"
    end

    def authenticate username, password
      username == 'admin' and password == 'admin'
    end
  end
end
