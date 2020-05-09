using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Configuration;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace k163805_Q2
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string to = this.textBox1.Text;
            string subject = this.textBox2.Text;
            string msg = this.richTextBox1.Text;
            msg = msg.Replace("\n", "\n\t\t");
            //long hash = to.GetHashCode() + subject.GetHashCode() + msg.GetHashCode();
            string xml = String.Format("<EmailMessage>\n\t<To>{0}</To>\n\t<Subject>{1}</Subject>\n\t<MessageBody>\n\t\t{2}\n\t</MessageBody>\n</EmailMessage>", to, subject, msg);
            //string fileName = subject + "_" + hash + ".xml";
            string fileName = Microsoft.VisualBasic.Interaction.InputBox("Enter file name",
                       "Save as",
                       "",
                       0,
                       0);
            if(fileName != "") System.IO.File.WriteAllText(@ConfigurationManager.AppSettings["path"]+fileName+".xml", xml);
            this.Close();
        }


    }
}
