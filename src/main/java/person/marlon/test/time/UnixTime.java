package person.marlon.test.time;

import java.util.Date;

/**
 * Speaking in POJO instead of ByteBuf
 * All the examples we have reviewed so far used a ByteBuf as a primary data structure of a protocol message. In this
 * section, we will improve the TIME protocol client and server example to use a POJO instead of a ByteBuf.

 * The advantage of using a POJO in your ChannelHandlers is obvious; your handler becomes more maintainable and reusable
 * by separating the code which extracts information from ByteBuf out from the handler. In the TIME client and server
 * examples, we read only one 32-bit integer and it is not a major issue to use ByteBuf directly. However, you will find
 * it is necessary to make the separation as you implement a real world protocol.
 *
 * We can now revise the TimeDecoder to produce a UnixTime instead of a ByteBuf.

 *@Override
 *  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
 *
 *      if (in.readableBytes() < 4) {
 *          return;
 *      }

 *      out.add(new UnixTime(in.readUnsignedInt()));
 *  }
 */
public class UnixTime {
    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }
}
