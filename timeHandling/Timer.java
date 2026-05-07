package timeHandling;

public class Timer extends Thread {
	private int s, m;
	private boolean work;
	private TimeListener listener;
	
	public void setListener(TimeListener listener) {
		this.listener = listener;
	}
	
	public void run() {
		try {
			while(!isInterrupted()) {
				synchronized (this) {
					while(!work) {
						wait();
					}
				}
				
				sleep(1000);
				s++;
				if(s % 60 == 0) {
					m ++;
					s = 0;
				}
				System.out.println(m + ":" + s);
				if(listener != null)
					listener.onTick(m, s);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void go() {
		work = true;
		notify();
	}
	
	public synchronized void pause() {
		work = false;
	}
	
	public synchronized void reset() {
		m = s = 0;
	}
	
	public synchronized void end() {
		work = false;
		reset();
	}
	
	public String toString() {
		return String.format("%02d:%02d", m, s);
	}
}
