using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace k163805_Q1
{
    class Program
    {
        static void Main()
        {

            TcpClient client = new TcpClient("localhost", 8080);
            NetworkStream stream = client.GetStream();
            byte[] data;
            Console.WriteLine("Enter quit to exit");
            String msg = Console.ReadLine();
            try
            {
                while (msg != "quit")
                {
                    data = System.Text.Encoding.ASCII.GetBytes(msg);
                    stream.Write(data, 0, data.Length);
                    Console.WriteLine("Sent: {0}", msg);
                    data = new byte[4096];
                    int read = stream.Read(data, 0, data.Length);
                    msg = System.Text.Encoding.ASCII.GetString(data, 0, read);
                    Console.WriteLine("Received: {0}", msg);
                    msg = Console.ReadLine();
                }
            }
            catch (Exception e) { }

            stream.Close();
            client.Close();
        }
    }
}
