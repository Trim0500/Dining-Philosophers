import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	enum state { THINKING, HUNGRY, EATING, TALKING, REQUESTPHILOSPHY};
	static state[] states;
	int size;

	ReentrantLock lock = new ReentrantLock();

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		size = piNumberOfPhilosophers;
		states = new state[piNumberOfPhilosophers];
        Arrays.fill(states, state.THINKING);
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		states[piTID] = state.HUNGRY;
		test(piTID);
		while (states[piTID] != state.EATING)
			synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
	}

	/**
	 * When a given philosopher's done eating, they put the chopsticks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		states[piTID] = state.THINKING;
		test((piTID + this.size - 1) % this.size);
		test((piTID + 1) % this.size);
	}

	public synchronized void test(final int piTID)
	{
		if(states[(piTID + this.size - 1) % this.size] != state.EATING &&
			states[piTID] == state.HUNGRY &&
			states[(piTID + 1) % this.size] != state.EATING)
		{
			states[piTID] = state.EATING;
			notifyAll();
		}
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID)
	{
		states[piTID] = state.REQUESTPHILOSPHY;
		lock.lock();
		states[piTID] = state.TALKING;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		states[piTID] = state.THINKING;
		lock.unlock();
	}
}

// EOF
