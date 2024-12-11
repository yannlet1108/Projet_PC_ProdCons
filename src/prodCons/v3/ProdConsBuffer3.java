package prodCons.v3;

import java.util.concurrent.Semaphore;

import prodCons.IMessage;
import prodCons.IProdConsBuffer;
import prodCons.Message;

class ProdConsBuffer3 implements IProdConsBuffer {

	private int N;
	private IMessage buffer[];
	private int in;
	private int out;
	private int totmsg;
	Semaphore mutex;
	Semaphore empty;
	Semaphore full;

	/* isLastPut est un indicateur pour différencer le cas où le buffer est vide du cas où il est plein
	** Dans les 2 cas, on a in == out, la dinstinction se fait donc sur ce booléen 
	** Si isLastPut est true, alors la dernière opération est un put donc le buffer est plein
	** Si isLastPut est false, alors la dernière opération est un get (ou buffer tout juste initialisé) donc le buffer est vide
	*/
	private boolean isLastPut;
	

	ProdConsBuffer3(int bufSz) {
		N = bufSz;
		buffer = new Message[N];
		in = 0;
		out = 0;
		totmsg = 0;
		isLastPut = false; // buffer vide au début
		mutex = new Semaphore(1);
		full = new Semaphore(N, true);  // le deuxieme parametre garantit le respect de l'ordre d'arrivée (fifo)
		empty = new Semaphore(0, true);
		
	}

	@Override
	public void put(IMessage m) throws InterruptedException {
		full.acquire();

		mutex.acquire();
		buffer[in] = m;
		in = (in + 1) % N;
		isLastPut = true;
		totmsg++;
		System.out.println("Put message : " + ((Message)m).getId());
		mutex.release();

		empty.release();
	}

	@Override
	public Message get() throws InterruptedException {
		empty.acquire();

		mutex.acquire();
		Message m = (Message) buffer[out];
		out = (out + 1) % N;
		isLastPut = false;
		System.out.println("Consumer " + Thread.currentThread().getId() + " consumed message " + ((Message)m).getId());
		mutex.release();

		full.release();

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

}
