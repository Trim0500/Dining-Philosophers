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
	static final Lock lock = new ReentrantLock();
	static ArrayList<Condition> conditions;
	int size;



	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		size = piNumberOfPhilosophers;
		states = new state[piNumberOfPhilosophers];
        Arrays.fill(states, state.THINKING);
		conditions = new ArrayList<Condition>();
		for(int i = 0; i < piNumberOfPhilosophers; i++) {
			conditions.add(lock.newCondition());
		}
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
		lock.lock();

		try {
			states[piTID] = state.HUNGRY;
			test(piTID);
			if (states[piTID] != state.EATING)
				conditions.get(piTID).await();
		}
		catch (InterruptedException ignored) { }
		finally {
			lock.unlock();
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
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
			conditions.get(piTID).signal();
		}
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// ...
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		// ...
	}
}

// EOF
