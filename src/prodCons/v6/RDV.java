package prodCons.v6;

import java.util.concurrent.Semaphore;

public class RDV {

	Semaphore rdv = new Semaphore(0);
	int nexpected;
	int narrived = 0;

	Semaphore mutex = new Semaphore(1); // exclusion mutuelle

	RDV (int nexpected) {
		this.nexpected = nexpected;
	}

	void come() {
		try {
			mutex.acquire();
			narrived++;
			if (narrived < nexpected) {
				//System.out.println("Acquired : " + Thread.currentThread().getId());
				mutex.release();
				rdv.acquire();
			} else {
				//System.out.println("Released : " + Thread.currentThread().getId());
				rdv.release(nexpected - 1);
				mutex.release();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}