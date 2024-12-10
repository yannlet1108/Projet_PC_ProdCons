package prodCons;

public class Consumer extends Thread {
    private IProdConsBuffer buffer;
    private int consTime;

    public Consumer(IProdConsBuffer buffer, int consTime) {
        this.buffer = buffer;
        this.consTime = consTime;
    }

    public void run() {
        while (true) {
            try {
                IMessage m = buffer.get();
                sleep(consTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
