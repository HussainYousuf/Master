using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using k163805_Q4;
using System.Collections;
using System.Diagnostics;
using System.Threading;

namespace k163805_Q5
{
    class Program
    {
        static void Main(string[] args)
        {
            int million = 20000; // this is not 2 million because dynamic list implementation (casting from object to int) was too slow 
            int[] array = new int[million];
            ArrayList arrayList = new ArrayList();
            List<int> list = new List<int>();
            DynamicList dynamicList = new DynamicList();

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
                num = rand.Next(10);
                dynamicList.Add(num);
            }
            var sw = Stopwatch.StartNew();
            foreach (int item in array)
            {
                sum += item;
            }
            sw.Stop();
            TimeSpan time = sw.Elapsed;
            Console.WriteLine("ARRAY: Sum is {0} and time span is {1} milliseconds", sum, time.TotalMilliseconds);
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
            for (int i = 0; i < dynamicList.Count; i++)
            {
                sum += dynamicList.GetAsInt(i);
            }
            sw.Stop();
            time = sw.Elapsed;
            Console.WriteLine("DYNAMICLIST: Sum is {0} and time span is {1} milliseconds", sum, time.TotalMilliseconds);
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
                arrayList.IndexOf(rand.Next(10));
            }
            sw.Stop();
            time = sw.Elapsed;
            Console.WriteLine("ARRAYLIST: time span for search is {0} milliseconds", time.TotalMilliseconds);

            sw = Stopwatch.StartNew();
            for (int i = 0; i < 5; i++)
            {
                list.IndexOf(rand.Next(10));
            }
            sw.Stop();
            time = sw.Elapsed;
            Console.WriteLine("LIST: time span for search is {0} milliseconds", time.TotalMilliseconds);

            sw = Stopwatch.StartNew();
            for (int i = 0; i < 5; i++)
            {
                dynamicList.IndexOf(rand.Next(10));
            }
            sw.Stop();
            time = sw.Elapsed;
            Console.WriteLine("DYNAMICLIST: time span for search is {0} milliseconds", time.TotalMilliseconds);

            Console.ReadLine(); 

            

        }
    }
}
