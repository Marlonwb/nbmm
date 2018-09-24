package person.marlon.protocolbuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import person.marlon.message.MyMessage;

public class ProtocolBufServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//用于接收客户端连接的线程池，仅负责接收客户端的连接，不做复杂业务处理，故越小越好，节省系统资源
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // Decoders
                            socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder
                                    (1048576, 0, 4, 0, 4));
                            socketChannel.pipeline().addLast("protobufDecoder", new ProtobufDecoder(MyMessage.getDefaultInstance()));

                            // Encoder
                            socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                            socketChannel.pipeline().addLast("protobufEncoder", new ProtobufEncoder());

                            socketChannel.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
                            //socketChannel.pipeline().addLast(new LoginAuthRespHandler());
                            //socketChannel.pipeline().addLast("HeartBeatHandler",new HeartBeatRespHandler())
                        }
                    });

            //serverBootstrap.bind(NettyConstant.REMOTEIP,NettyConstant.PORT).sync();

            // Bind and start to accept incoming connections.
            ChannelFuture f = serverBootstrap.bind("127.0.0.1", 80).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
         }
    }
}
