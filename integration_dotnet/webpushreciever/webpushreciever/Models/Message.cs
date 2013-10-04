using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace webpushreciever.Models
{
    public class Message
    {
        public string id { get; set; }
        public DateTime timestamp { get; set; }
        public string type { get; set; }
        public Dictionary<string, string> headers { get; set; }
        public Dictionary<string, string> data { get; set; }

    }
}