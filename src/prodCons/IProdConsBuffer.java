package prodCons;

public interface IProdConsBuffer {

	/**
	 * Put the message m in the buffer
	 **/
	void put(IMessage m) throws InterruptedException;

	/**
	 * Retrieve a message from the buffer, following a FIFO order (if M1 was put
	 * before M2, M1 is retrieved before M2)
	 **/
	Message get() throws InterruptedException;

	/**
	 * Returns the number of messages currently available in the buffer
	 **/
	int nmsg();

	/**
	 * Returns the total number of messages that have been put in the buffer since
	 * its creation
	 **/
	int totmsg();

}
