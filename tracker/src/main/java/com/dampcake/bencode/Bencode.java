/*
 * Copyright 2015 Adam Peck.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dampcake.bencode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Map;


public final class Bencode {

    static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static final char NUMBER = 'i';

    static final char LIST = 'l';

    static final char DICTIONARY = 'd';

    static final char TERMINATOR = 'e';

    static final char SEPARATOR = ':';

    private final Charset charset;
    private final boolean useBytes;

    public Bencode() {
        this(DEFAULT_CHARSET);
    }

    public Bencode(final Charset charset) {
        this(charset, false);
    }

    public Bencode(final boolean useBytes) {
        this(DEFAULT_CHARSET, useBytes);
    }

    public Bencode(final Charset charset, final boolean useBytes) {
        if (charset == null) throw new NullPointerException("charset cannot be null");

        this.charset = charset;
        this.useBytes = useBytes;
    }

    public Charset getCharset() {
        return charset;
    }

    public Type type(final byte[] bytes) {
        if (bytes == null) throw new NullPointerException("bytes cannot be null");

        BencodeInputStream in = new BencodeInputStream(new ByteArrayInputStream(bytes), charset, useBytes);

        try {
            return in.nextType();
        } catch (Throwable t) {
            throw new BencodeException("Exception thrown during type detection", t);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(final byte[] bytes, final Type<T> type) {
        if (bytes == null) throw new NullPointerException("bytes cannot be null");
        if (type == null) throw new NullPointerException("type cannot be null");
        if (type == Type.UNKNOWN) throw new IllegalArgumentException("type cannot be UNKNOWN");

        BencodeInputStream in = new BencodeInputStream(new ByteArrayInputStream(bytes), charset, useBytes);

        try {
            if (type == Type.NUMBER)
                return (T) in.readNumber();
            if (type == Type.LIST)
                return (T) in.readList();
            if (type == Type.DICTIONARY)
                return (T) in.readDictionary();
            return (T) in.readString();
        } catch (Throwable t) {
            throw new BencodeException("Exception thrown during decoding", t);
        }
    }

    public byte[] encode(final String s) {
        if (s == null) throw new NullPointerException("s cannot be null");

        return encode(s, Type.STRING);
    }

    public byte[] encode(final Number n) {
        if (n == null) throw new NullPointerException("n cannot be null");

        return encode(n, Type.NUMBER);
    }


    public byte[] encode(final Iterable<?> l) {
        if (l == null) throw new NullPointerException("l cannot be null");

        return encode(l, Type.LIST);
    }

    public byte[] encode(final Map<?, ?> m) {
        if (m == null) throw new NullPointerException("m cannot be null");

        return encode(m, Type.DICTIONARY);
    }

    private byte[] encode(final Object o, final Type type) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BencodeOutputStream bencode = new BencodeOutputStream(out, charset);

        try {
            if (type == Type.STRING)
                bencode.writeString((String) o);
            else if (type == Type.NUMBER)
                bencode.writeNumber((Number) o);
            else if (type == Type.LIST)
                bencode.writeList((Iterable) o);
            else if (type == Type.DICTIONARY)
                bencode.writeDictionary((Map) o);
        } catch (Throwable t) {
            throw new BencodeException("Exception thrown during encoding", t);
        }

        return out.toByteArray();
    }
}
