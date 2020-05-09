using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

// I tried to use pointers but vs-studio complained something about unsafe and fixed assignment 
// got around unsafe but fixed was not working for me


namespace k163805_Q6
{
    public class BubbleSort {
        int len;
        int[] array;

        public BubbleSort(int len) {
            this.len = len;
            this.array = new int[len];
            Random rand = new Random();
            for (int i = 0; i < len; i++)
            {
                this.array[i] = rand.Next(len);
            }
        }

        public void sort()
        {
            for (int i = 0; i < this.len-1; i++)
            {
                for (int j = 0; j < this.len-1-i; j++)
                {
                    if (this.array[j] > this.array[j + 1])
                    {
                        int temp = this.array[j];
                        this.array[j] = this.array[j + 1];
                        this.array[j + 1] = temp;
                    }
                }
            }
        }

        public void display()
        {
            foreach (int item in this.array)
            {
                Console.WriteLine(item);
            }
            Console.ReadLine();
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            BubbleSort sort = new BubbleSort(20);
            sort.sort();
            sort.display();
        }
    }
}
