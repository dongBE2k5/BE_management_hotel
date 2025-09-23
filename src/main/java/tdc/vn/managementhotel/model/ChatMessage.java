package tdc.vn.managementhotel.model;

public class ChatMessage {
    private String sender;
    private String content;
    private String time;

    // Constructors
    public ChatMessage() {}
    public ChatMessage(String sender, String content, String time) {
        this.sender = sender;
        this.content = content;
        this.time = time;
    }

    // Getter & Setter
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}

