using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ComponentModel;

namespace com.baidu.api.samples.lib
{
    static class HeaderUtil
    {
        private static string username = null;

        private static string password = null;

        private static string token = null;

        public static void readHeader()
        {
            Console.WriteLine("Please input your username:");
            username = Console.ReadLine();

            Console.WriteLine("Please input your password:");
            password = Console.ReadLine();

            Console.WriteLine("Please input your token:");
            token = Console.ReadLine();
        }

        public static object loadHeader(object obj)
        {
            if (username == null || password == null || token == null)
            {
                readHeader();
            }

            foreach (PropertyDescriptor descriptor in TypeDescriptor.GetProperties(obj))
            {
                string name = descriptor.Name;
                if (name == "username")
                {
                    descriptor.SetValue(obj, username);
                }
                if (name == "password")
                {
                    descriptor.SetValue(obj, password);
                }
                if (name == "token")
                {
                    descriptor.SetValue(obj, token);
                }
            }
            return obj;
        }
    }
}
