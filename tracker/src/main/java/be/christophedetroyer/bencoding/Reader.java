package be.christophedetroyer.bencoding;

import be.christophedetroyer.bencoding.types.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Reader
{
    private int currentByteIndex;
    private byte[] datablob;

    public Reader(File file) throws IOException
    {
        datablob = IOUtils.toByteArray(new FileInputStream(file));
    }

    public Reader(InputStream input) throws IOException
    {
        datablob = IOUtils.toByteArray(input);
    }

    public Reader(String s)
    {
        datablob = s.getBytes();
    }

    public synchronized List<IBencodable> read()
    {
        this.currentByteIndex = 0;
        long fileSize = datablob.length;

        List<IBencodable> dataTypes = new ArrayList<IBencodable>();
        while (currentByteIndex < fileSize)
            dataTypes.add(readSingleType());

        return dataTypes;
    }

    private IBencodable readSingleType()
    {
        byte current = datablob[currentByteIndex];
        switch (current)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return readByteString();
            case 'd':
                return readDictionary();
            case 'i':
                return readInteger();
            case 'l':
                return readList();
        }
        throw new Error("Parser in invalid state at byte " + currentByteIndex);
    }

    private BList readList()
    {
        if (readCurrentByte() != 'l')
            throw new Error("Error parsing list. Was expecting a 'l' but got " + readCurrentByte());
        currentByteIndex++; // Skip over the 'l'

        BList list = new BList();
        while (readCurrentByte() != 'e')
            list.add(readSingleType());

        currentByteIndex++; // Skip the 'e'
        return list;
    }

    private BByteString readByteString()
    {
        String lengthAsString = "";
        int lengthAsInt;
        byte[] bsData;

        byte current = readCurrentByte();
        while (current >= 48 && current <= 57)
        {
            lengthAsString = lengthAsString + Character.toString((char)current);
            currentByteIndex++;
            current = readCurrentByte();
        }
        lengthAsInt = Integer.parseInt(lengthAsString);

        if (readCurrentByte() != ':')
            throw new Error("Read length of byte string and was expecting ':' but got " + readCurrentByte());
        currentByteIndex++; // Skip over the ':'.

        bsData = new byte[lengthAsInt];
        for (int i = 0; i < lengthAsInt; i++)
        {
            bsData[i] = readCurrentByte();
            currentByteIndex++;
        }

        return new BByteString(bsData);
    }

    private BDictionary readDictionary()
    {
        if (readCurrentByte() != 'd')
            throw new Error("Error parsing dictionary. Was expecting a 'd' but got " + readCurrentByte());
        currentByteIndex++; // Skip over the 'd'

        BDictionary dict = new BDictionary();
        while (readCurrentByte() != 'e')
        {
            BByteString key = (BByteString) readSingleType();
            IBencodable value = readSingleType();

            dict.add(key, value);
        }
        currentByteIndex++;

        return dict;
    }


    private BInt readInteger()
    {
        if (readCurrentByte() != 'i')
            throw new Error("Error parsing integer. Was expecting an 'i' but got " + readCurrentByte());
        currentByteIndex++;
        String intString = "";
        byte current = readCurrentByte();
        while (current >= 48 && current <= 57 || current == 45)
        {
            intString = intString + Character.toString((char)current);
            currentByteIndex++;
            current = readCurrentByte();
        }

        if (readCurrentByte() != 'e')
            throw new Error("Error parsing integer. Was expecting 'e' at end but got " + readCurrentByte());

        currentByteIndex++;
        return new BInt(Long.parseLong(intString));
    }

    private byte readCurrentByte()
    {
        return datablob[currentByteIndex];
    }
}
