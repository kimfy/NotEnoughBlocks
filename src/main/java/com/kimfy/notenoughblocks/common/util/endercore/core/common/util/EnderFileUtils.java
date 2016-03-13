package com.kimfy.notenoughblocks.common.util.endercore.core.common.util;

import java.io.*;
import java.net.URI;
import java.util.Deque;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class EnderFileUtils
{
    /**
     * @param directory The directory to zip the contents of. Content structure will be
     *                  preserved.
     * @param zipfile   The zip file to output to.
     * @throws IOException
     * @author McDowell - http://stackoverflow.com/questions/1399126/java-util-zip
     * -recreating-directory-structure
     */
    @SuppressWarnings("resource")
    public static void zipFolderContents(File directory, File zipfile) throws IOException
    {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        Closeable res = out;
        try
        {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty())
            {
                directory = queue.pop();
                for (File child : directory.listFiles())
                {
                    String name = base.relativize(child.toURI()).getPath();
                    if (child.isDirectory())
                    {
                        queue.push(child);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    }
                    else
                    {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(child, zout);
                        zout.closeEntry();
                    }
                }
            }
        }
        finally
        {
            res.close();
        }
    }

    /**
     * @see #zipFolderContents(File, File)
     */
    private static void copy(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        while (true)
        {
            int readCount = in.read(buffer);
            if (readCount < 0)
            {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    /**
     * @see #zipFolderContents(File, File)
     */
    private static void copy(File file, OutputStream out) throws IOException
    {
        InputStream in = new FileInputStream(file);
        try
        {
            copy(in, out);
        }
        finally
        {
            in.close();
        }
    }
}
