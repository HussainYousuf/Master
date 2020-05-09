using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using System.Timers;
using System.Xml;

namespace k163805_Q3
{
    public partial class k163805_Q3 : ServiceBase
    {

        static List<string> SentEmails = new List<string>();

        public k163805_Q3()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {

            OnTimer(null, null);
            Timer timer = new Timer();
            timer.Interval =  15 * 60 * 1000;
            timer.Elapsed += new ElapsedEventHandler(OnTimer);
            timer.Start();

              
        }

        private void OnTimer(object sender, ElapsedEventArgs e)
        {

            string[] emails = Directory.GetFiles(@ConfigurationManager.AppSettings["path"]);
            foreach (var item in emails)
            {
                if (Path.GetExtension(item) == ".xml" && !SentEmails.Contains(item))
                {
                    SentEmails.Add(item);
                    try
                    {
                        string xml = File.ReadAllText(@item);
                        XmlDocument doc = new XmlDocument();
                        doc.LoadXml(xml);

                        string to = (doc.DocumentElement.ChildNodes[0].InnerText.Trim());
                        string subject = (doc.DocumentElement.ChildNodes[1].InnerText.Trim());
                        string body = (doc.DocumentElement.ChildNodes[2].InnerText.Trim());

                        MailMessage msg = new MailMessage();
                        msg.From = new MailAddress(ConfigurationManager.AppSettings["username"]);
                        msg.To.Add(to);
                        msg.Subject = subject;
                        msg.Body = body;

                        SmtpClient smt = new SmtpClient();
                        smt.Host = ConfigurationManager.AppSettings["host"];
                        System.Net.NetworkCredential ntcd = new NetworkCredential();
                        ntcd.UserName = ConfigurationManager.AppSettings["username"]; ;
                        ntcd.Password = ConfigurationManager.AppSettings["passwd"]; ;
                        smt.Credentials = ntcd;
                        smt.EnableSsl = true;
                        smt.Port = 587;
                        smt.Send(msg);

                    }
                    catch (Exception)
                    {
                        
                    }
                }
            }

        }

        protected override void OnStop()
        {

        }
    }
}
