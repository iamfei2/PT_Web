package be.christophedetroyer.bencoding.types;

public interface IBencodable
{
    byte[] bencode();
    String bencodedString();
}
