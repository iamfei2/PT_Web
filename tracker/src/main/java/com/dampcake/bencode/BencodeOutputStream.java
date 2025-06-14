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

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;


public class BencodeOutputStream extends FilterOutputStream {

    private final Charset charset;

    public BencodeOutputStream(final OutputStream out, final Charset charset) {
        super(out);

        if (charset == null) throw new NullPointerException("charset cannot be null");
        this.charset = charset;
    }

    public BencodeOutputStream(final OutputStream out) {
        this(out, Bencode.DEFAULT_CHARSET);
    }

    public Charset getCharset() {
        return charset;
    }

    public void writeString(final String s) throws IOException {
        write(encode(s));
    }

    public void writeString(final ByteBuffer buff) throws IOException {
        write(encode(buff.array()));
    }

    public void writeNumber(final Number n) throws IOException {
        write(encode(n));
    }

    public void writeList(final Iterable<?> l) throws IOException {
        write(encode(l));
    }

    public void writeDictionary(final Map<?, ?> m) throws IOException {
        write(encode(m));
    }

    private byte[] encode(final String s) throws IOException {
        if (s == null) throw new NullPointerException("s cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(Integer.toString(s.length()).getBytes(charset));
        buffer.write(Bencode.SEPARATOR);
        buffer.write(s.getBytes(charset));

        return buffer.toByteArray();
    }

    private byte[] encode(final byte[] b) throws IOException {
        if (b == null) throw new NullPointerException("b cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        buffer.write(Integer.toString(b.length).getBytes(charset));
        buffer.write(Bencode.SEPARATOR);
        buffer.write(b);

        return buffer.toByteArray();
    }

    private byte[] encode(final Number n) throws IOException {
        if (n == null) throw new NullPointerException("n cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(Bencode.NUMBER);
        buffer.write(Long.toString(n.longValue()).getBytes(charset));
        buffer.write(Bencode.TERMINATOR);

        return buffer.toByteArray();
    }

    private byte[] encode(final Iterable<?> l) throws IOException {
        if (l == null) throw new NullPointerException("l cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(Bencode.LIST);
        for (Object o : l)
            buffer.write(encodeObject(o));
        buffer.write(Bencode.TERMINATOR);

        return buffer.toByteArray();
    }

    private byte[] encode(final Map<?, ?> map) throws IOException {
        if (map == null) throw new NullPointerException("map cannot be null");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(Bencode.DICTIONARY);
        for (Map.Entry<?, ?> e : map.entrySet()) {
            buffer.write(encode(e.getKey().toString()));
            buffer.write(encodeObject(e.getValue()));
        }
        buffer.write(Bencode.TERMINATOR);

        return buffer.toByteArray();
    }

    private byte[] encodeObject(final Object o) throws IOException {
        if (o == null) throw new NullPointerException("Cannot write null objects");

        if (o instanceof Number)
            return encode((Number) o);
        if (o instanceof Iterable<?>)
            return encode((Iterable<?>) o);
        if (o instanceof Map<?, ?>)
            return encode((Map<?, ?>) o);
        if (o instanceof ByteBuffer)
            return encode(((ByteBuffer) o).array());

        return encode(o.toString());
    }
}
