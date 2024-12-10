package prodCons.v1;

import prodCons.IMessage;
import prodCons.IProdConsBuffer;
import prodCons.Message;

public class Consumer extends Thread {
    private IProdConsBuffer buffer;
    private int consTime;

    Consumer(IProdConsBuffer buffer, int consTime) {
        this.buffer = buffer;
        this.consTime = consTime;
    }

    public void run() {
        while (true) {
            try {
                IMessage m = buffer.get();
                sleep(consTime);
                System.out.println("Consumer " + Thread.currentThread().getId() + " consumed message " + ((Message)m).getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
