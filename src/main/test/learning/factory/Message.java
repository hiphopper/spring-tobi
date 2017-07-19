package learning.factory;

/**
 * Created by Administrator on 2017-07-19.
 */
public class Message {
    private String text;

    private Message(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static Message newMessage(String text){
        return new Message(text);
    }
}
