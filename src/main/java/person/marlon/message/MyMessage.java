package person.marlon.message;

import com.google.protobuf.*;

//import javax.xml.ws.handler.MessageContext;
import java.util.Map;

public class MyMessage extends AbstractMessage {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private MyMessage(Builder builder) {
        this.text = builder.text;
    }

    public static Builder newBuilder(){
        return new Builder();
    }


    public static MyMessage getDefaultInstance(){
        return newBuilder().build();
    }

    @Override
    public Parser<? extends Message> getParserForType() {
        return null;
    }

    @Override
    public Message.Builder newBuilderForType() {
        return null;
    }

    @Override
    public Message.Builder toBuilder() {
        return null;
    }

    @Override
    public Message getDefaultInstanceForType() {
        return null;
    }

    @Override
    public Descriptors.Descriptor getDescriptorForType() {
        return null;
    }

    @Override
    public Map<Descriptors.FieldDescriptor, Object> getAllFields() {
        return null;
    }

    @Override
    public boolean hasField(Descriptors.FieldDescriptor field) {
        return false;
    }

    @Override
    public Object getField(Descriptors.FieldDescriptor field) {
        return null;
    }

    @Override
    public int getRepeatedFieldCount(Descriptors.FieldDescriptor field) {
        return 0;
    }

    @Override
    public Object getRepeatedField(Descriptors.FieldDescriptor field, int index) {
        return null;
    }

    @Override
    public UnknownFieldSet getUnknownFields() {
        return null;
    }

    public static class Builder {

        String text;

        Builder(){
            new Builder();
        }

        Builder(String text){
            this.text = text;
        }

        public Builder setText(String text){
            this.text = text;
            return this;
        }

        public MyMessage build(){
            return new MyMessage(this);
        }
    }
}
