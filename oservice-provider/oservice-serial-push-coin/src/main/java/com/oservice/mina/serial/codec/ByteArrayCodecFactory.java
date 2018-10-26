package com.oservice.mina.serial.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

/** 
 * 字节数组编码过滤工厂
 * @author LingDu
 */
public class ByteArrayCodecFactory implements ProtocolCodecFactory {

    private final ByteArrayEncoder encoder;

    private final ByteArrayDecoder decoder;

    /**
     * Constructor.
     */
    public ByteArrayCodecFactory() {
        encoder = new ByteArrayEncoder();
        decoder = new ByteArrayDecoder();
    }

    /**
     * Returns a new (or reusable) instance of ProtocolEncoder.
     */
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    /**
     * Returns a new (or reusable) instance of ProtocolDecoder.
     */
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

}
