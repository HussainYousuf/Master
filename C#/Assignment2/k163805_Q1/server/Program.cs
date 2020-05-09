using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace k163805_Q1
{

    public class Handler
    {
        TcpClient client;

        public Handler(TcpClient client) {
            this.client = client;
        }

        public string Reverse(string s)
        {
            char[] charArray = s.ToCharArray();
            Array.Reverse(charArray);
            return new string(charArray);
        }

        public void run() {
            NetworkStream stream = client.GetStream();
            int i;
            String data;
            byte[] bytes = new byte[4096];
            try
            {
                while ((i = stream.Read(bytes, 0, bytes.Length)) != 0)
                {
                    data = System.Text.Encoding.ASCII.GetString(bytes, 0, i);
                    Console.WriteLine("Received: {0}", data);

                    data = Reverse(data);

                    byte[] msg = System.Text.Encoding.ASCII.GetBytes(data);

                    stream.Write(msg, 0, msg.Length);
                    Console.WriteLine("Sent: {0}", data);
                }
            }
            catch(Exception e)
            {

            }

            stream.Close();
            client.Close();

        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            TcpListener server = new TcpListener(8080);
            server.Start();
            while (true) {
                TcpClient client = server.AcceptTcpClient();
                Handler handler = new Handler(client);
                new Thread(new ThreadStart(handler.run)).Start();
            }
        }
    }
}
