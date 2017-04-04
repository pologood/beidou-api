using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Net;

namespace com.baidu.api.samples.lib
{
    class ImageUtils
    {
        public static byte[] GetImageDataFromFile(String fileName)
        {
            MemoryStream stream = new MemoryStream();
            try
            {
                FileStream imageFile = new FileStream(fileName, FileMode.Open);
                int i = 0;
                byte[] buffer = new byte[1024];
                while ((i = imageFile.Read(buffer, 0, buffer.Length)) != 0)
                {
                    stream.Write(buffer, 0, i);
                }
            }
            catch (FileNotFoundException e)
            {
                throw e;
            }
            catch (IOException e)
            {
                throw e;
            }
            finally
            {
                stream.Close();
            }

            return (stream != null) ? stream.ToArray() : null;
        }


        public static byte[] GetImageDataFromUrl(String url)
        {
            try
            {
                WebClient client = new WebClient();
                return client.DownloadData(url);
            }
            catch (WebException e)
            {
                throw e;
            }
        }
    }
}
