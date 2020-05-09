using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net.Mail;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using System.Timers;


namespace k163805_Q5_b
{
    public partial class k163805_Q5b : ServiceBase
    {

        static Dictionary<string, DateTime> files = new Dictionary<string, DateTime>();
        static Timer aTimer = new Timer();

        public k163805_Q5b()
        {
            InitializeComponent();
        }

        protected override void OnStart(string[] args)
        {
            string[] fileArray = Directory.GetFiles(@ConfigurationManager.AppSettings["path"],
                "*.*", SearchOption.AllDirectories);
            foreach (var item in fileArray)
            {
                //Console.WriteLine(File.GetLastWriteTime(@item));
                files.Add(item, File.GetLastWriteTime(@item));
            }

            aTimer.Elapsed += new ElapsedEventHandler(OnTimedEvent);
            aTimer.Interval = 15 * 60 * 1000;
            aTimer.Enabled = true;

            Console.ReadLine();
        }

        protected override void OnStop()
        {
        }

        private static void OnTimedEvent(object sender, ElapsedEventArgs e)
        {

            Console.WriteLine("checking files");
            List<string> alteredFiles = new List<string>();
            bool altered = false;
            string[] fileArray = Directory.GetFiles(@ConfigurationManager.AppSettings["path"], "*.*", SearchOption.AllDirectories);
            foreach (var item in fileArray)
            {
                if (files.ContainsKey(item))
                {
                    if (DateTime.Compare(files[item], File.GetLastWriteTime(@item)) != 0)
                    {
                        Console.WriteLine("{0} has been altered", item);
                        files[item] = File.GetLastWriteTime(@item);
                        altered = true;
                        alteredFiles.Add(item);
                    }
                }
                else
                {
                    Console.WriteLine("{0} has been created", item);
                    files.Add(item, File.GetLastWriteTime(@item));
                    altered = true;
                    alteredFiles.Add(item);
                }
            }
            if (altered)
            {
                sendMail(alteredFiles);
            }
        }

        private static void sendMail(List<string> alteredFiles)
        {
            string text = "The following files have been altered or created: \n";
            text += string.Format("{0,-100}{1}\n", "FileName", "FileSize in bytes");
            foreach (var item in alteredFiles)
            {
                text += string.Format("{0,-100}{1}\n", item.Trim(), new FileInfo(item).Length.ToString().Trim());
            }
            MailMessage msg = new MailMessage();
            msg.From = new MailAddress(@ConfigurationManager.AppSettings["username"]);
            msg.To.Add(@ConfigurationManager.AppSettings["username"]);
            msg.Subject = @ConfigurationManager.AppSettings["subject"];
            msg.Body = text;

            SmtpClient smt = new SmtpClient();
            smt.Host = @ConfigurationManager.AppSettings["host"];
            System.Net.NetworkCredential ntcd = new System.Net.NetworkCredential();
            ntcd.UserName = @ConfigurationManager.AppSettings["username"];
            ntcd.Password = @ConfigurationManager.AppSettings["passwd"];
            smt.Credentials = ntcd;
            smt.EnableSsl = true;
            smt.Port = 587;
            smt.Send(msg);
        }

    }
}
