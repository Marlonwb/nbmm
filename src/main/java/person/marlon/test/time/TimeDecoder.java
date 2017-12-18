package person.marlon.test.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Although the first solution has resolved the problem with the TIME client, the modified handler does not look that
 * clean. Imagine a more complicated protocol which is composed of multiple fields such as a variable length field.
 * Your ChannelInboundHandler implementation will become unmaintainable very quickly.

 * As you may have noticed, you can add more than one ChannelHandler to a ChannelPipeline, and therefore, you can split
 * one monolithic ChannelHandler into multiple modular ones to reduce the complexity of your application. For example,
 * you could split TimeClientHandler into two handlers:

 * TimeDecoder which deals with the fragmentation issue, and
 * the initial simple version of TimeClientHandler.
 * Fortunately, Netty provides an extensible class which helps you write the first one out of the box:
 *
 *-----------------------------------------------------------------------------------------------------------------
 * (1)ByteToMessageDecoder is an implementation of ChannelInboundHandler which makes it easy to deal with the fragmentation
 * issue.
 * ----------------------------------------------------------------------------------------------------------------
 * Additionally, Netty provides out-of-the-box decoders which enables you to implement most protocols very easily and
 * helps you avoid from ending up with a monolithic unmaintainable handler implementation. Please refer to the following
 * packages for more detailed examples:

 *      io.netty.example.factorial for a binary protocol, and
 *      io.netty.example.telnet for a text line-based protocol.

 */
public class TimeDecoder  extends ByteToMessageDecoder {

    // (2)ByteToMessageDecoder calls the decode() method with an internally maintained cumulative buffer whenever new data is received.
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            // (3)decode() can decide to add nothing to out where there is not enough data in the cumulative buffer.
            // ByteToMessageDecoder will call decode() again when there is more data received.
            return;
        }

        // (4)If decode() adds an object to out, it means the decoder decoded a message successfully.
        // ByteToMessageDecoder will discard the read part of the cumulative buffer.
        // Please remember that you don't need to decode multiple messages.
        // ByteToMessageDecoder will keep calling the decode() method until it adds nothing to out.

//        out.add(in.readBytes(4));
        out.add(new UnixTime(in.readUnsignedInt()));//Much simpler and elegant

        //If you are an adventurous person, you might want to try the ReplayingDecoder which simplifies the decoder
        // even more. You will need to consult the API reference for more information though.
        //out.add(in.readBytes(4));
    }
}
//package io.netty.example.time;
//
//        import java.util.Date;
//
//public class TimeClientHandler extends ChannelInboundHandlerAdapter {
//    private ByteBuf buf;
//
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) {

//        //(1)A ChannelHandler has two life cycle listener methods: handlerAdded() and handlerRemoved(). You can
//        //perform an arbitrary (de)initialization task as long as it does not block for a long time.

//        buf = ctx.alloc().buffer(4);
//    }
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) {
//        buf.release(); // (1)
//        buf = null;
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf m = (ByteBuf) msg;
//        buf.writeBytes(m); // (2)First, all received data should be cumulated into buf.
//        m.release();
//
//        if (buf.readableBytes() >= 4) {
//        //(3)And then, the handler must check if buf has enough data, 4 bytes in this example, and proceed to the actual business logic.
//        //Otherwise, Netty will call the channelRead() method again when more data arrives, and eventually all 4 bytes will be cumulated.
//            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}