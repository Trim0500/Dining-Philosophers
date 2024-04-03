import java.util.ArrayList;
import java.util.List;
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
	state[] states;
	ArrayList<Condition> conditions;
	int size;



	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		this.size = piNumberOfPhilosophers;
		this.states = new state[piNumberOfPhilosophers];
		for(int i = 0; i < this.states.length; i++) {
			this.states[i] = state.THINKING;
		}
		Lock lock = new ReentrantLock();
		this.conditions = new ArrayList<Condition>();
		for(int i = 0; i < piNumberOfPhilosophers; i++) {
			this.conditions.add(lock.newCondition());
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
		this.states[piTID] = state.HUNGRY;
		test(piTID);
		if (states[piTID] != state.EATING)
			try {
				this.conditions.get(piTID).wait();
			} catch (InterruptedException e) {
				System.err.println("pickUp:");
				DiningPhilosophers.reportException(e);
				System.exit(1);
			}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		this.states[piTID] = state.THINKING;
		test((piTID + this.size - 1) % this.size);
		test((piTID + 1) % this.size);
	}

	public synchronized void test(final int piTID)
	{
		if(this.states[(piTID + this.size - 1) % this.size] != state.EATING && this.states[piTID] == state.HUNGRY && this.states[(piTID + 1) % this.size] != state.EATING) {
			this.states[piTID] = state.EATING;
			this.conditions.get(piTID).signal();
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
