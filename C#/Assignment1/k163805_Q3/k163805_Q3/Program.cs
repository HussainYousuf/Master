using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace k163805_Q3
{
    class Program
    {
        static void Main(string[] args)
        {
            int million = 2000000;
            int[] array = new int[million];
            ArrayList arrayList = new ArrayList();
            List<int> list = new List<int>();
            Random rand = new Random();
            int num = 0;
            int sum = 0;
            for (int i = 0; i < million; i++)
            {
                num = rand.Next(10);
                array[i] = num;
                num = rand.Next(10);
                arrayList.Add(num);
                num = rand.Next(10);
                list.Add(num);
            }
            var sw = Stopwatch.StartNew();
            foreach (int item in array)
            {
                sum += item;
            }
            sw.Stop();
            TimeSpan time = sw.Elapsed;
            Console.WriteLine("ARRAY: Sum is {0} and time span is {1} milliseconds",sum,time.TotalMilliseconds);
            sum = 0;

            sw = Stopwatch.StartNew();
            foreach (int item in arrayList)
            {
                sum += item;
            }
            sw.Stop();
            time = sw.Elapsed;
            Console.WriteLine("ARRAYLIST: Sum is {0} and time span is {1} milliseconds", sum, time.TotalMilliseconds);
            sum = 0;

            sw = Stopwatch.StartNew();
            foreach (int item in list)
            {
                sum += item;
            }
            sw.Stop();
            time = sw.Elapsed;
            Console.WriteLine("LIST: Sum is {0} and time span is {1} milliseconds", sum, time.TotalMilliseconds);

            Console.WriteLine();

            sw = Stopwatch.StartNew();
            for (int i = 0; i < 5; i++)
            {
                arrayList.BinarySearch(rand.Next(10));
            }
            sw.Stop();
            time = sw.Elapsed;
            Console.WriteLine("ARRAYLIST: time span for search is {0} milliseconds", time.TotalMilliseconds);

            sw = Stopwatch.StartNew();
            for (int i = 0; i < 5; i++)
            {
                list.BinarySearch(rand.Next(10));
            }
            sw.Stop();
            time = sw.Elapsed;
            Console.WriteLine("LIST: time span for search is {0} milliseconds", time.TotalMilliseconds);

            Console.ReadLine();

        }
    }
}
