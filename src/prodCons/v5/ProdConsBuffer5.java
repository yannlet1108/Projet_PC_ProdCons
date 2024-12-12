package prodCons.v5;

import prodCons.IMessage;
import prodCons.IProdConsBuffer;
import prodCons.Message;

class ProdConsBuffer5 implements IProdConsBuffer {

	private int N;
	private IMessage buffer[];
	private int in;
	private int out;
	private int totmsg;
	boolean isMultigetting = false;

	/* isLastPut est un indicateur pour différencer le cas où le buffer est vide du cas où il est plein
	** Dans les 2 cas, on a in == out, la dinstinction se fait donc sur ce booléen 
	** Si isLastPut est true, alors la dernière opération est un put donc le buffer est plein
	** Si isLastPut est false, alors la dernière opération est un get (ou buffer tout juste initialisé) donc le buffer est vide
	*/
	private boolean isLastPut;
	

	ProdConsBuffer5(int bufSz) {
		N = bufSz;
		buffer = new Message[N];
		in = 0;
		out = 0;
		totmsg = 0;
		isLastPut = false; // buffer vide au début
	}

	@Override
	public synchronized void put(Message m) throws InterruptedException {
		while (!(nmsg() < N)) {
			wait();
		}
		buffer[in] = m;
		in = (in + 1) % N;
		isLastPut = true;
		totmsg++;
		System.out.println("-> Put : " + m.getId());
		notifyAll();
	}

	@Override
	public synchronized Message get() throws InterruptedException {
		return get(1)[0];
	}
	
	@Override
	public synchronized Message[] get(int k) throws InterruptedException {
		while (isMultigetting) {
			wait();
		}
		isMultigetting = true;
		Message[] messages = new Message[k];

		for (int i = 0; i < k; i++) {
			while (!(nmsg() > 0)) {
				wait();
			}
			Message m = (Message) buffer[out];
			messages[i] = m;
			out = (out + 1) % N;
			isLastPut = false;
			System.out.println("<- get Consumer " + Thread.currentThread().getId() + " message: " + m.getId());
			notifyAll();
		}
		isMultigetting = false;
		notifyAll();
		return messages;
	}

	@Override
	public int nmsg() {
		int nmsg = (in - out + N) % N;
		if (nmsg == 0) {
			if (isLastPut) {
				return N;
			} else {
				return 0;
			}
		}
		return nmsg;
	}

	@Override
	public int totmsg() {
		return totmsg;
	}

	@Override
	public void put(Message m, int n) throws InterruptedException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'put'");
	}

}
