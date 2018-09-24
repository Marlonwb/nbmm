package person.marlon.protocolbuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import person.marlon.message.MyMessage;

public class ProtocolBufClient extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MyMessage req = (MyMessage) msg;
        MyMessage res = MyMessage.newBuilder().setText(
                                             "Did you say '" + req.getText() + "'?").build();
        //ch.write(res);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
