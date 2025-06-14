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

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BencodeInputStream extends FilterInputStream {

    private static final int EOF = -1;

    private final Charset charset;
    private final boolean useBytes;
    private final PushbackInputStream in;

    public BencodeInputStream(final InputStream in, final Charset charset, boolean useBytes) {
        super(new PushbackInputStream(in));
        this.in = (PushbackInputStream) super.in;

        if (charset == null) throw new NullPointerException("charset cannot be null");
        this.charset = charset;
        this.useBytes = useBytes;
    }

    public BencodeInputStream(final InputStream in, final Charset charset) {
        this(in, charset, false);
    }


    public BencodeInputStream(final InputStream in) {
        this(in, Bencode.DEFAULT_CHARSET);
    }

    public Charset getCharset() {
        return charset;
    }

    private int peek() throws IOException {
        int b = in.read();
        in.unread(b);
        return b;
    }

    public Type nextType() throws IOException {
        int token = peek();
        checkEOF(token);

        return typeForToken(token);
    }

    private Type typeForToken(int token) {
        for (Type type : Type.values()) {
            if (type.validate(token))
                return type;
        }

        return Type.UNKNOWN;
    }

    public String readString() throws IOException {
        return new String(readStringBytesInternal(), getCharset());
    }


    public ByteBuffer readStringBytes() throws IOException {
        return ByteBuffer.wrap(readStringBytesInternal());
    }

    private byte[] readStringBytesInternal() throws IOException {
        int token = in.read();
        validateToken(token, Type.STRING);

        StringBuilder buffer = new StringBuilder();
        buffer.append((char) token);
        while ((token = in.read()) != Bencode.SEPARATOR) {
            validateToken(token, Type.STRING);

            buffer.append((char) token);
        }

        int length = Integer.parseInt(buffer.toString());
        byte[] bytes = new byte[length];
        read(bytes);
        return bytes;
    }

    public Long readNumber() throws IOException {
        int token = in.read();
        validateToken(token, Type.NUMBER);

        StringBuilder buffer = new StringBuilder();
        while ((token = in.read()) != Bencode.TERMINATOR) {
            checkEOF(token);

            buffer.append((char) token);
        }

        return new BigDecimal(buffer.toString()).longValue();
    }

    public List<Object> readList() throws IOException {
        int token = in.read();
        validateToken(token, Type.LIST);

        List<Object> list = new ArrayList<Object>();
        while ((token = in.read()) != Bencode.TERMINATOR) {
            checkEOF(token);

            list.add(readObject(token));
        }

        return list;
    }

    public Map<String, Object> readDictionary() throws IOException {
        int token = in.read();
        validateToken(token, Type.DICTIONARY);

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        while ((token = in.read()) != Bencode.TERMINATOR) {
            checkEOF(token);

            in.unread(token);
            map.put(readString(), readObject(in.read()));
        }

        return map;
    }

    private Object readObject(final int token) throws IOException {
        in.unread(token);

        Type type = typeForToken(token);

        if (type == Type.STRING && !useBytes)
            return readString();
        if (type == Type.STRING && useBytes)
            return readStringBytes();
        if (type == Type.NUMBER)
            return readNumber();
        if (type == Type.LIST)
            return readList();
        if (type == Type.DICTIONARY)
            return readDictionary();

        throw new InvalidObjectException("Unexpected token '" + new String(Character.toChars(token)) + "'");
    }

    private void validateToken(final int token, final Type type) throws IOException {
        checkEOF(token);

        if (!type.validate(token)) {
            in.unread(token);
            throw new InvalidObjectException("Unexpected token '" + new String(Character.toChars(token)) + "'");
        }
    }

    private void checkEOF(final int b) throws EOFException {
        if (b == EOF) throw new EOFException();
    }
}
