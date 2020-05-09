using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace k163805_Q4
{

    public sealed class DynamicList : IList
    {
        int size = 2000000;
        object[] array;
        int index = -1;

        public DynamicList() {
            array = new object[size];
        }

        public int Add(object value)
        {
            if (index < size)
            {
                array[++index] = value;
                return 1;
            }
            return 0;
        }

        public void Clear()
        {
            array = null;
            array = new object[size];
            index = -1;
        }

        public bool Contains(object value)
        {
            for (int i = 0; i <= index; i++)
            {
                if (array[i].Equals(value)) return true;
            }
            return false;
        }

        public int IndexOf(object value)
        {
            for (int i = 0; i <= index; i++)
            {
                if (array[i].Equals(value)) return i;
            }
            return -1;
        }

        public void Insert(int index, object value)
        {
            if(index <= this.index)
                array[index] = value;
        }

        public bool IsFixedSize
        {
            get { return true; }
        }

        public bool IsReadOnly
        {
            get { return false; }
        }

        public void Remove(object value)
        {
            for (int i = 0; i <= index; i++)
            {
                if (array[i].Equals(value)) array[i] = null;
            }
        }

        public void RemoveAt(int index)
        {
            if (index <= this.index)
                array[index] = null;
        }

        public object this[int index]
        {
            get
            {
                return array[index];
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void CopyTo(Array array, int index)
        {
            if (index <= this.index)
            {
                for (int i = 0; i < index; i++)
                {
                    array.SetValue(this.array[i], i);
                }
            }
        }

        public int Count
        {
            get {
                int count = 0;
                for (int i = 0; i <= index; i++)
                {
                    if (array[i] != null) count++;
                }
                return count;
            }
        }

        public bool IsSynchronized
        {
            get { return false; }
        }

        public object SyncRoot
        {
            get { throw new NotImplementedException(); }
        }

        public IEnumerator GetEnumerator()
        {
            throw new NotImplementedException();
        }

        public void DisplayAll() {
            for (int i = 0; i <= index; i++)
            {
                if(array[i] != null)
                    Console.Write("{0},", array[i]);
            }
            Console.WriteLine();
        }

        public bool Find(object value)
        {
            for (int i = 0; i <= index; i++)
            {
                if (array[i].Equals(value)) return true;
            }
            return false;
        }

        public int GetAsInt(int index) {
            return (int) array[index];
        }

    }

    class Program
    {
        static void Main(string[] args)
        {
            DynamicList list = new DynamicList();
            list.Add(5);
            list.Add("hello");
            list.Add(true);
            list.Add(1.5);
            list.DisplayAll();
            Console.WriteLine(list.Find("hello"));
            Console.WriteLine(list[1]);
            Console.ReadLine();
        }
    }
}
