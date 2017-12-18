package person.marlon.test.discard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *  ByteBuf is a reference-counted object which has to be released explicitly via the release() method.
     *  Please keep in mind that it is the handler's responsibility to release any reference-counted object passed to
     *  the handler. Usually, channelRead() handler method is implemented like the following:
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
        //((ByteBuf) msg).release(); // (3)

        //=========================Looking into the Received Data========================================
        /*
        Usually, channelRead() handler method is implemented like the following:
         */
        //        try {
        //            // Do something with msg
        //        } finally {
        //            ReferenceCountUtil.release(msg);
        //        }

//        ByteBuf in = (ByteBuf) msg;
//        try {
//            // (1)This inefficient loop can actually be simplified to: System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII))
//            while (in.isReadable()) {
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            //Alternatively, you could do in.release() here.
//            ReferenceCountUtil.release(msg); // (2)
//        }

        //==============================Writing an Echo Server===================================
        /*(1)
         * A ChannelHandlerContext object provides various operations that enable you to trigger various I/O events and operations.
         * Here, we invoke write(Object) to write the received message in verbatim. Please note that we did not release the received message unlike we did in the DISCARD example.
         * It is because Netty releases it for you when it is written out to the wire.
         */
        ctx.write(msg);
        /* (2)
         * ctx.write(Object) does not make the message written out to the wire. It is buffered internally, and then flushed out to the wire by ctx.flush().
         * Alternatively, you could call ctx.writeAndFlush(msg) for brevity.
         */
        ctx.flush();

    }

    /**
     * The exceptionCaught() event handler method is called with a Throwable when an exception was raised by Netty
     * due to an I/O error or by a handler implementation due to the exception thrown while processing events. In most
     * cases, the caught exception should be logged and its associated discard should be closed here,
     * although the implementation of this method can be different depending on what you want to do to deal with
     * an exceptional situation. For example, you might want to send a response message with an error code before
     * closing the connection.
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
