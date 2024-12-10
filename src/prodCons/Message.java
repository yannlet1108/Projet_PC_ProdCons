package prodCons;

public class Message implements IMessage {
    private int id;
    private long ProducerId;

    public Message(int id) {
        this.id = id;
        this.ProducerId = Thread.currentThread().getId();
    }

    public String getId() {
        return "Producer " + ProducerId + " - Message " + id;
    }
}
