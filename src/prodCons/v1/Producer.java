package prodCons.v1;

import prodCons.IMessage;
import prodCons.IProdConsBuffer;
import prodCons.Message;

class Producer extends Thread {
    private IProdConsBuffer buffer;
    private int prodTime;
    private int nMsgProd;

    public Producer(IProdConsBuffer buffer, int prodTime, int minProd, int maxProd) {
        this.buffer = buffer;
        this.prodTime = prodTime;
        this.nMsgProd = minProd + (int)(Math.random() * ((maxProd - minProd) + 1));
    }

    public void run() {

        for (int i = 0; i < nMsgProd; i++) {
            try {
                IMessage m = new Message(i);
                buffer.put(m);
                sleep(prodTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }       
}