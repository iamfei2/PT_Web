package be.christophedetroyer.bencoding;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Utils
{

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4]; // Get left part of byte
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Get right part of byte
        }
        return new String(hexChars);
    }

    public static byte readNthByteFromFile(String path, long nth)
    {
        RandomAccessFile rf = null;
        try
        {
            rf = new RandomAccessFile(path, "r");

            if (rf.length() < nth)
                throw new EOFException("Reading outside of bounds of file");

            rf.seek(nth);
            byte curr = rf.readByte();
            rf.close();

            return curr;
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            assert rf != null;
            try
            {
                rf.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static boolean isBitSet(byte b, int position)
    {
        return ((b >> position) & 1) == 1;
    }

    public static void printByte(byte b)
    {
        String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        System.out.println(s1);
    }
    public static boolean allAscii(byte[] data)
    {
        for (byte b : data)
            if (isBitSet(b, 7))
                return false;
        return true;
    }


    public static String SHAsum(byte[] input)
    {
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("SHA-1");
            return byteArray2Hex(md.digest(input));
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static String byteArray2Hex(final byte[] bytes)
    {
        Formatter formatter = new Formatter();
        for (byte b : bytes)
        {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static byte[] stringToAsciiBytes(String s)
    {
        byte[] ascii = new byte[s.length()];
        for(int charIdx = 0; charIdx < s.length(); charIdx++)
        {
            ascii[charIdx] = (byte) s.charAt(charIdx);
        }
        return ascii;
    }
}
