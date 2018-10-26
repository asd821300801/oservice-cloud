package com.oservice.mina.serial.config;


import com.oservice.mina.serial.codec.ByteArrayCodecFactory;
import com.oservice.mina.serial.net.IoHandler;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Mina2.x Config
 *
 * @author LingDu
 */
@Configuration
public class MinaConfig {
    private static Logger logger = LoggerFactory.getLogger(MinaConfig.class);

    @Value("${serial.port}")
    private Integer port;

    @Bean
    public ByteArrayCodecFactory byteArrayCodecFactory() {
        return new ByteArrayCodecFactory();
    }

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(port);
    }

    @Bean
    public IoHandler ioHandler() {
        return new IoHandler();
    }


    @Bean
    public IoAcceptor ioAcceptor() throws Exception {

        IoAcceptor acceptor = new NioSocketAcceptor();

        //设置过滤器
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(byteArrayCodecFactory()));//多线程前，否则会导致解码混乱的情况
        acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter());
        //acceptor.getFilterChain().addLast( "logger", loggingFilter() );

        //创建一个handler来实时处理客户端的连接和请求
        acceptor.setHandler(ioHandler());

        //设置读写缓冲区大小
        acceptor.getSessionConfig().setReadBufferSize(2048);
        //空闲时间   通道均在10 秒内无任何操作就进入空闲状态
        //acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
        acceptor.bind(inetSocketAddress());

        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());

        if (acceptor.isActive()) {
            logger.info("端口重用...");
            logger.info("服务端初始化完成......");
            logger.info("服务已启动....开始监听...." + acceptor.getLocalAddresses());
        } else {
            logger.info("服务端初始化失败......");
        }
        return acceptor;
    }

}
