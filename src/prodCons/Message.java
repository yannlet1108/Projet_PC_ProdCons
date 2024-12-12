package prodCons;

import prodCons.v6.RDV;

public class Message implements IMessage {
    private int id;
    private long ProducerId;
    private RDV rdv;

    public Message(int id) {
        this.id = id;
        this.ProducerId = Thread.currentThread().getId();
    }

    public String getId() {
        return "Producer " + ProducerId + " - Message " + id;
    }

    public void setRDV (RDV rdv) {
        this.rdv = rdv;
    }
    
    public RDV getRDV () {
        return rdv;
    }
}
