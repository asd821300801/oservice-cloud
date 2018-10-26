package com.oservice.mina.serial.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

/** 
 * 编码
 * @author LingDu
 */
public class ByteArrayEncoder implements ProtocolEncoder {

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        out.write(message);  
        out.flush();
    }

    public void dispose(IoSession session) throws Exception {
    }

}
