package prodCons.v6;

import prodCons.IMessage;
import prodCons.IProdConsBuffer;
import prodCons.Message;

class ProdConsBuffer6 implements IProdConsBuffer {

	private int N;
	private IMessage buffer[];
	private int in;
	private int out;
	private int totmsg;
	private boolean isPutting = false;

	/* isLastPut est un indicateur pour différencer le cas où le buffer est vide du cas où il est plein
	** Dans les 2 cas, on a in == out, la dinstinction se fait donc sur ce booléen 
	** Si isLastPut est true, alors la dernière opération est un put donc le buffer est plein
	** Si isLastPut est false, alors la dernière opération est un get (ou buffer tout juste initialisé) donc le buffer est vide
	*/
	private boolean isLastPut;
	

	ProdConsBuffer6(int bufSz) {
		N = bufSz;
		buffer = new Message[N];
		in = 0;
		out = 0;
		totmsg = 0;
		isLastPut = false; // buffer vide au début
	}

	@Override
	public void put(Message m) throws InterruptedException {
		put(m, 1);
	}

	@Override
	public void put(Message m, int n) throws InterruptedException {
		RDV rdv = new RDV(n + 1);
		synchronized (this) {
			while (isPutting || !((nmsg() + n) <= N)) {
				wait();
			}
			isPutting = true;
			m.setRDV(rdv);
			for (int i = 0; i < n; i++) {
				buffer[in] = m;
				in = (in + 1) % N;
				totmsg++;
				System.out.println("-> Put : " + m.getId() + " multiput : " + (i + 1) + "/" + n);
				isLastPut = true;
			}
			isPutting = false;
			//printBuffer();
			notifyAll();
		}
		rdv.come();
	}

	@Override
	public Message get() throws InterruptedException {
		Message m;
		synchronized (this) {
			while (!(nmsg() > 0)) {
				wait();
			}
			//waiting for n iterations to be completed
			m = (Message) buffer[out];
			out = (out + 1) % N;
			isLastPut = false;
			System.out.println("<- get Consumer " + Thread.currentThread().getId() + " message: " + m.getId());
			//printBuffer();
			notifyAll();
		}
		m.getRDV().come();
		return m;
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
	public Message[] get(int k) throws InterruptedException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'get'");
	}


	void printBuffer() {
		System.out.println("Buffer : [");
		for (int i = 0; i < N; i++) {
			if (buffer[i] != null) {
				System.out.println(buffer[i].getId() + ", ");
			} else {
				System.out.println("null");
			}
		}
		System.out.println("]");
	}

}
