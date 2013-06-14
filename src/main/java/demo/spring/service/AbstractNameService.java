package demo.spring.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractNameService implements NameService, NameListeningService {

	private Lock lock = new ReentrantLock();
	private Set<NameListener> listeners = new HashSet<NameListener>();
	private SynchronousQueue<Runnable> queue = new SynchronousQueue<Runnable>();
	private ThreadPoolExecutor threadPool = 
			new ThreadPoolExecutor(1, 1, Long.MAX_VALUE, TimeUnit.MINUTES,
			queue);
	
	public void addListener(NameListener listener) {
		lock.lock();
		try {
			listeners.add(listener);
		} finally {
			lock.unlock();
		}
	}
	
	public void removeListener(NameListener listener) {
		lock.lock();
		try {
			listeners.remove(listener);
		} finally {
			lock.unlock();
		}
	}
	
	protected Set<NameListener> getListeners() {
		Set<NameListener> copiedListeners = new HashSet<NameListener>();
		lock.lock();
		try {
			copiedListeners.addAll(listeners);
		} finally {
			lock.unlock();
		}
		return copiedListeners;
	}
	
	public final String getName(String firstName, String lastName) {
		String name = doGetName(firstName, lastName);
		notifyListeners(name, firstName, lastName);
		return name;
	}
	
	abstract String doGetName(String firstName, String lastName);
	
	private void notifyListeners(final String finalName, final String firstName,
			final String lastName) {
		
		Runnable notifier = new Runnable() {
			public void run() {
				for (NameListener listener : getListeners()) {
					try {
						listener.newNameGiven(finalName, firstName, lastName);
					} catch (Exception e) {
						e.printStackTrace(System.err);
					}
				}
			}
		};
		
		threadPool.submit(notifier);
	}
	
}
