package prodCons;

public class Consumer extends Thread {
    private IProdConsBuffer buffer;
    private int consTime;
    private int nMsgConsummedEachTime;

    public Consumer(IProdConsBuffer buffer, int consTime, int maxMsgConsummedEachTime) {
        this.buffer = buffer;
        this.consTime = consTime;
        this.nMsgConsummedEachTime = 1 + (int) (Math.random() * maxMsgConsummedEachTime);
        System.out.println(nMsgConsummedEachTime);
    }

    public void run() {
        while (true) {
            try {
                sleep(consTime);
                if (nMsgConsummedEachTime == 1) {
                    IMessage m = buffer.get();
                }
                else {
                    IMessage[] m = buffer.get(nMsgConsummedEachTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
