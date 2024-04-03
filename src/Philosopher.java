import common.BaseThread;
import java.io.ObjectInputStream;
import java.util.*;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	private final int guessingNumber = 17;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			System.out.printf("[Philosopher/eat] -- Philosopher %d is now eating.\n", getTID());

			Thread.yield();

			sleep((long)(Math.random() * TIME_TO_WASTE));

			Thread.yield();

			System.out.printf("[Philosopher/eat] -- Philosopher %d is done eating.\n", getTID());
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		try {
			System.out.printf("[Philosopher/think] -- Philosopher %d is now thinking.\n", getTID());

			Thread.yield();

			sleep((long)(Math.random() * TIME_TO_WASTE));

			Thread.yield();

			System.out.printf("[Philosopher/think] -- Philosopher %d is done thinking.\n", getTID());
		}
		catch (InterruptedException exc) {
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(exc);
			System.exit(1);
		}
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		System.out.printf("[Philosopher/talk] -- Philopher %d will now philosofy.\n", getTID());

		Thread.yield();

		saySomething();

		Thread.yield();

		System.out.printf("[Philosopher/talk] -- Philopher %d is finished speak.\n", getTID());
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			// Pick a number from 17 to 42, if you guess correctly I will give you permission to talk this time
			Random ranNum = new Random();
			if (ranNum.nextInt(17, 42) == guessingNumber)
			{
				DiningPhilosophers.soMonitor.requestTalk();

				talk();

				DiningPhilosophers.soMonitor.endTalk();
			}

			Thread.yield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID()
		};

		System.out.printf("Philopher %d says: %s\n", getTID(), astrPhrases[(int)(Math.random() * astrPhrases.length)]);
	}
}

// EOF
