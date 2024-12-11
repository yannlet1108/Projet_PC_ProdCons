package prodCons.v4;

import prodCons.IMessage;
import prodCons.IProdConsBuffer;
import prodCons.Message;
import java.util.concurrent.locks.*;

class ProdConsBuffer4 implements IProdConsBuffer {

	private int N;
	private IMessage buffer[];
	private int in;
	private int out;
	private int totmsg;
	final Lock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();

	/* isLastPut est un indicateur pour différencer le cas où le buffer est vide du cas où il est plein
	** Dans les 2 cas, on a in == out, la dinstinction se fait donc sur ce booléen 
	** Si isLastPut est true, alors la dernière opération est un put donc le buffer est plein
	** Si isLastPut est false, alors la dernière opération est un get (ou buffer tout juste initialisé) donc le buffer est vide
	*/
	private boolean isLastPut;
	

	ProdConsBuffer4(int bufSz) {
		N = bufSz;
		buffer = new Message[N];
		in = 0;
		out = 0;
		totmsg = 0;
		isLastPut = false; // buffer vide au début
	}

	@Override
	public void put(IMessage m) throws InterruptedException {
		lock.lock();
		try {
			while (!(nmsg() < N)) {
				notFull.await();
			}
			buffer[in] = m;
			in = (in + 1) % N;
			isLastPut = true;
			totmsg++;
			System.out.println("Put message : " + ((Message)m).getId());
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public synchronized Message get() throws InterruptedException {
		lock.lock();
		try {
			while (!(nmsg() > 0)) {
				notEmpty.await();
			}
			Message m = (Message) buffer[out];
			out = (out + 1) % N;
			isLastPut = false;
			System.out.println("Consumer " + Thread.currentThread().getId() + " consumed message " + ((Message)m).getId());
			notFull.signal();
			return m;
		} finally {
			lock.unlock();
		}
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
