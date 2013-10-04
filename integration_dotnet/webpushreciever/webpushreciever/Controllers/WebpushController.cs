using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using webpushreciever.Models;

namespace webpushreciever.Controllers
{
    public class WebpushController : ApiController
    {


        
        [HttpGet]
        [ActionName("system_status")]
        public bool getStatus()
        {
            return true;
        }
        



        [HttpPost]
        [ActionName("message")]
        public void processMessage([FromBody]Message message)
        {
            System.Console.WriteLine("Got a message " + message.id);

        }

    }
}
