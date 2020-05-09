using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.ServiceProcess;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Timers;
using System.Web;
 

namespace k163805_Q4
{
    public partial class k163805_Q4 : ServiceBase
    {

        public class NewsItem
        {
            public string title, description, channel;
            public DateTime time;

            public NewsItem(string title, string time, string description, string channel)
            {
                this.title = title;
                this.time = DateTime.Parse(time);
                this.description = description;
                this.channel = channel;
            }

            public override String ToString()
            {
                description = description.Replace("\n", "\n\t\t");
                title = title.Replace("\n", "\n\t\t");

                return String.Format("<NewsItem>\n\t<Title>\n\t\t{0}\n\t</Title>\n\t<Description>\n\t\t{1}\n\t</Description>\n\t<PublishedDate>\n\t\t{2}\n\t</PublishedDate>\n\t<NewsChannel>\n\t\t{3}\n\t</NewsChannel>\n</NewsItem>", title, description, time, channel);
            }

        }

        static void rssParser(string url, List<NewsItem> newsItems)
        {
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12;
            var request = (HttpWebRequest)WebRequest.Create(url);
            var response = (HttpWebResponse)request.GetResponse();
            string responseString;
            using (var stream = response.GetResponseStream())
            {
                using (var reader = new StreamReader(stream, Encoding.UTF8))
                {
                    responseString = HttpUtility.HtmlDecode(reader.ReadToEnd());
                }
            }
            int start = responseString.IndexOf("title>") + 6;
            int end = responseString.IndexOf("</title>");
            string channel = responseString.Substring(start, end - start);

            responseString = responseString.Substring(responseString.IndexOf("item"));

            Regex reg = new Regex(@"<(?:pubDate|description|title)>([\s\S]*?)<\/(?:pubDate|description|title)>");
            Match m = reg.Match(responseString);
            while (m.Success)
            {
                string title = m.Groups[1].ToString();
                m = m.NextMatch();
                string pub = m.Groups[1].ToString();
                m = m.NextMatch();
                string desc = m.Groups[1].ToString();
                m = m.NextMatch();
                newsItems.Add(new NewsItem(title, pub, desc, channel));
            }
            newsItems.Sort((x, y) => y.time.CompareTo(x.time));
        }

        public k163805_Q4()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {
            OnTimer(null, null);
            Timer timer = new Timer();
            timer.Interval = 5 * 60 * 1000;
            timer.Elapsed += new ElapsedEventHandler(OnTimer);
            timer.Start();
            Console.ReadLine();
        }

        private static void OnTimer(object sender, ElapsedEventArgs e)
        {
            Console.WriteLine("Writing to {0} at {1}", "file", DateTime.Now);
            List<NewsItem> newsItems = new List<NewsItem>();
            String[] urls = { "https://tribune.com.pk/feed/", "https://www.brecorder.com/feed/" };

            foreach (var url in urls) rssParser(url, newsItems);
            String xml = "";
            foreach (NewsItem item in newsItems)
            {
                xml += item;
                xml += "\n";
            }
            System.IO.File.WriteAllText(@ConfigurationManager.AppSettings["path"], xml);
        }

        protected override void OnStop()
        {
        }
    }
}
