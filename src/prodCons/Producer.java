package prodCons;

public class Producer extends Thread {
    private IProdConsBuffer buffer;
    private int prodTime;
    private int nMsgProd;
    private int nMsgProdEachTime;


    public Producer(IProdConsBuffer buffer, int prodTime, int minProd, int maxProd, int maxProdEachTime) {
        this.buffer = buffer;
        this.prodTime = prodTime;
        this.nMsgProd = minProd + (int) (Math.random() * ((maxProd - minProd) + 1));
        this.nMsgProdEachTime = 1 + (int) (Math.random() * maxProdEachTime);
    }

    public void run() {

        for (int i = 0; i < nMsgProd; i++) {
            try {
                sleep(prodTime);
                if (nMsgProdEachTime == 1) {
                    buffer.put(new Message(i));
                }
                else {
                    Message m = new Message(i);
                    buffer.put(m, nMsgProdEachTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Producer " + getId() + " finished");
    }       
}
