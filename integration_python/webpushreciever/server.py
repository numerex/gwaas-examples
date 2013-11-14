from http.server import BaseHTTPRequestHandler, HTTPServer
import cgi
import json

class CustomHandler(BaseHTTPRequestHandler):

        def do_POST(self):
                if self.headers['Authorization'] == None:
                        self.send_response(401)
                        self.wfile.write(bytes('no auth header received', 'UTF-8'))
                        self.end_headers()
                elif self.headers['Authorization'] == 'Basic YW5kcmV3aXN0aGVjb29sZXN0OmFuZHJld2lzdGhlY29vbGVzdA==':
                        self.send_response(200)
                        self.wfile.write(bytes(self.headers['Authorization'], 'UTF-8'))
                        self.wfile.write(bytes(' authenticated!', 'UTF-8'))
                        content_length = int(self.headers['Content-length'])

                        req_str = ''
                        if (content_length):
                                to_read = content_length

                        while len(req_str) < content_length:
                                _req_str = self.rfile.read(to_read)
                                to_read -= len(_req_str)
                                req_str += _req_str.decode('UTF-8')
                        self.end_headers()

                        print ('\r\n\r\n-----------\r\ngot a message:  ' + str(req_str))


                else:
                        self.send_response(401)
                        self.wfile.write(bytes(self.headers['Authorization'], 'UTF-8'))
                        self.wfile.write(bytes(' not authenticated', 'UTF-8'))
                        self.end_headers()

def main():
        try:
                httpd = HTTPServer(('', 8080), CustomHandler)
                print ('started httpd...')
                httpd.serve_forever()
        except KeyboardInterrupt:
                print ('^C received, shutting down server')
                httpd.socket.close()

if __name__ == '__main__':
        main()

