package be.christophedetroyer.torrent;

import be.christophedetroyer.bencoding.Reader;
import be.christophedetroyer.bencoding.Utils;
import be.christophedetroyer.bencoding.types.BByteString;
import be.christophedetroyer.bencoding.types.BDictionary;
import be.christophedetroyer.bencoding.types.BInt;
import be.christophedetroyer.bencoding.types.BList;
import be.christophedetroyer.bencoding.types.IBencodable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TorrentParser
{
    public static Torrent parseTorrent(InputStream input) throws IOException
    {
        Reader r = new Reader(input);
        return parseTorrent(r);
    }

    public static Torrent parseTorrent(String filePath) throws IOException
    {
        Reader r = new Reader(new File(filePath));
        return parseTorrent(r);
    }

    public static Torrent parseTorrent(Reader r) throws IOException
    {
        List<IBencodable> x = r.read();
        if (x.size() != 1)
            throw new Error("Parsing .torrent yielded wrong number of bencoding structs.");
        try
        {
            return parseTorrent(x.get(0));
        } catch (ParseException e)
        {
            System.err.println("Error parsing torrent!");
        }
        return null;
    }

    private static Torrent parseTorrent(Object o) throws ParseException
    {
        if (o instanceof BDictionary)
        {
            BDictionary torrentDictionary = (BDictionary) o;
            BDictionary infoDictionary = parseInfoDictionary(torrentDictionary);

            Torrent t = new Torrent();
            t.setAnnounce(parseAnnounce(torrentDictionary));
            t.setInfo_hash(Utils.SHAsum(infoDictionary.bencode()));
            t.setName(parseTorrentLocation(infoDictionary));
            t.setPieceLength( parsePieceLength(infoDictionary));
            t.setPieces(parsePiecesHashes(infoDictionary));
            t.setPiecesBlob(parsePiecesBlob(infoDictionary));
            t.setFileList(parseFileList(infoDictionary));
            t.setComment(parseComment(torrentDictionary));
            t.setCreatedBy(parseCreatorName(torrentDictionary));
            t.setCreationDate(parseCreationDate(torrentDictionary));
            t.setAnnounceList(parseAnnounceList(torrentDictionary));
            t.setTotalSize(parseSingleFileTotalSize(infoDictionary));
            t.setSingleFileTorrent(null != infoDictionary.find(new BByteString("length")));
            return t;
        } else
        {
            throw new ParseException("Could not parse Object to BDictionary", 0);
        }
    }

    private static Long parseSingleFileTotalSize(BDictionary info)
    {
        if (null != info.find(new BByteString("length")))
            return ((BInt) info.find(new BByteString("length"))).getValue();
        return null;
    }


    private static BDictionary parseInfoDictionary(BDictionary dictionary)
    {
        if (null != dictionary.find(new BByteString("info")))
            return (BDictionary) dictionary.find(new BByteString("info"));
        else
            return null;
    }

    private static Date parseCreationDate(BDictionary dictionary)
    {
        if (null != dictionary.find(new BByteString("creation date")))
            return new Date(Long.parseLong(dictionary.find(new BByteString("creation date")).toString()));
        return null;
    }

    private static String parseCreatorName(BDictionary dictionary)
    {
        if (null != dictionary.find(new BByteString("created by")))
            return dictionary.find(new BByteString("created by")).toString();
        return null;
    }

    private static String parseComment(BDictionary dictionary)
    {
        if (null != dictionary.find(new BByteString("comment")))
            return dictionary.find(new BByteString("comment")).toString();
        else
            return null;
    }

    private static Long parsePieceLength(BDictionary info)
    {
        if (null != info.find(new BByteString("piece length")))
            return ((BInt) info.find(new BByteString("piece length"))).getValue();
        else
            return null;
    }

    private static String parseTorrentLocation(BDictionary info)
    {
        if (null != info.find(new BByteString("name")))
            return info.find(new BByteString("name")).toString();
        else
            return null;
    }

    private static String parseAnnounce(BDictionary dictionary)
    {
        if (null != dictionary.find(new BByteString("announce")))
            return dictionary.find(new BByteString("announce")).toString();
        else
            return null;
    }

    private static byte[] parsePiecesBlob(BDictionary info)
    {
        if (null != info.find(new BByteString("pieces")))
        {
            return ((BByteString) info.find(new BByteString("pieces"))).getData();
        } else
        {
            throw new Error("Info dictionary does not contain pieces bytestring!");
        }
    }

    private static List<String> parsePiecesHashes(BDictionary info)
    {
        if (null != info.find(new BByteString("pieces")))
        {
            List<String> sha1HexRenders = new ArrayList<String>();
            byte[] piecesBlob = ((BByteString) info.find(new BByteString("pieces"))).getData();
            if (piecesBlob.length % 20 == 0)
            {
                int hashCount = piecesBlob.length / 20;
                for (int currHash = 0; currHash < hashCount; currHash++)
                {
                    byte[] currHashByteBlob = Arrays.copyOfRange(piecesBlob, 20 * currHash, (20 * (currHash + 1)));
                    String sha1 = Utils.bytesToHex(currHashByteBlob);
                    sha1HexRenders.add(sha1);
                }
            } else
            {
                throw new Error("Error parsing SHA1 piece hashes. Bytecount was not a multiple of 20.");
            }
            return sha1HexRenders;
        } else
        {
            throw new Error("Info dictionary does not contain pieces bytestring!");
        }
    }

    private static List<TorrentFile> parseFileList(BDictionary info)
    {
        if (null != info.find(new BByteString("files")))
        {
            List<TorrentFile> fileList = new ArrayList<TorrentFile>();
            BList filesBList = (BList) info.find(new BByteString("files"));

            Iterator<IBencodable> fileBDicts = filesBList.getIterator();
            while (fileBDicts.hasNext())
            {
                Object fileObject = fileBDicts.next();
                if (fileObject instanceof BDictionary)
                {
                    BDictionary fileBDict = (BDictionary) fileObject;
                    BList filePaths = (BList) fileBDict.find(new BByteString("path"));
                    BInt fileLength = (BInt) fileBDict.find(new BByteString("length"));
                    List<String> paths = new LinkedList<String>();
                    Iterator<IBencodable> filePathsIterator = filePaths.getIterator();
                    while (filePathsIterator.hasNext())
                        paths.add(filePathsIterator.next().toString());

                    fileList.add(new TorrentFile(fileLength.getValue(), paths));
                }
            }
            return fileList;
        }
        return null;
    }

    private static List<String> parseAnnounceList(BDictionary dictionary)
    {
        if (null != dictionary.find(new BByteString("announce-list")))
        {
            List<String> announceUrls = new LinkedList<String>();

            BList announceList = (BList) dictionary.find(new BByteString("announce-list"));
            Iterator<IBencodable> subLists = announceList.getIterator();
            while (subLists.hasNext())
            {
                BList subList = (BList) subLists.next();
                Iterator<IBencodable> elements = subList.getIterator();
                while (elements.hasNext())
                {
                    BByteString tracker = (BByteString) elements.next();
                    announceUrls.add(tracker.toString());
                }
            }
            return announceUrls;
        } else
        {
            return null;
        }
    }
}
