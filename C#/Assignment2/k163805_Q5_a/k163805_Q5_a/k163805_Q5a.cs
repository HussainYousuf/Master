using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;
using System.Timers;
using System.IO;

namespace k163805_Q5_a
{
    public partial class k163805_Q5a : ServiceBase
    {
        static Dictionary<string, DateTime> files = new Dictionary<string, DateTime>();
        static int init = 60 * 1000;
        static int interval = init;
        static Timer aTimer = new Timer();


        public k163805_Q5a()
        {
            InitializeComponent();
        } 

        protected override void OnStart(string[] args)
        {

            string[] fileArray = Directory.GetFiles(@ConfigurationManager.AppSettings["currentPath"], "*.*", SearchOption.AllDirectories);
            foreach (var item in fileArray)
            {
                //Console.WriteLine(File.GetLastWriteTime(@item));
                files.Add(item, File.GetLastWriteTime(@item));
            }

            aTimer.Elapsed += new ElapsedEventHandler(OnTimedEvent);
            aTimer.Interval = interval;
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
            string[] fileArray = Directory.GetFiles(@ConfigurationManager.AppSettings["currentPath"], "*.*", SearchOption.AllDirectories);
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
            if (!altered && interval <= 60 * 60 * 1000)  
            {
                interval += 2 * 60 * 1000;
                aTimer.Enabled = false;
                aTimer.Interval = interval;
                aTimer.Enabled = true;
            }
            if (altered)
            {
                interval = init;
                aTimer.Enabled = false;
                aTimer.Interval = interval;
                aTimer.Enabled = true;
                foreach (var item in alteredFiles)
                {
                    File.Copy(item, @ConfigurationManager.AppSettings["destPath"] + new FileInfo(item).Name, true);
                }
                //sendMail(alteredFiles);
            }
        }

    }
}
