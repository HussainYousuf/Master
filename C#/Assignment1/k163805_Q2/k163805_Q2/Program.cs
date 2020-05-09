using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

// I have generated my own random input file 
namespace k163805_Q2
{
    class Program
    {

        static void GenerateRandomInputFile() {
            Random rand = new Random();
            StreamWriter writer = new StreamWriter("Q1Input.txt");

            for (int i = 1; i <= 24; i++)
            {
                if (rand.Next(2) == 0)
                {
                    writer.WriteLine("G{0},Inactive", i);
                }
                else
                {
                    writer.WriteLine("G{0},Active", i);
                }
            }

            writer.Close();
        }

        static void Main(string[] args)
        {

            Console.WriteLine("Reading data from random generated file");
            GenerateRandomInputFile();  // comment this line for your own file

            string[] lines = File.ReadAllLines("Q1Input.txt");
            bool[] isActive = new bool[24];         // this I have used for storage
            int index = 0;

            foreach (string line in lines)
            {
                if(line.Split(',')[1] == "Active") isActive[index] = true;
                else isActive[index] = false;
                Console.WriteLine("is G" + (index+1) + " active? " + isActive[index]);
                index++;
            }
                
            Console.ReadLine();

        }
    }
}
