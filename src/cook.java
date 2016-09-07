// cook class for each cook thread
public class cook extends Thread {

	// initialize several variable like order the dinner will make
	// cook number
	// cook local time
	private Restaurant r;
	public Order o;
	public int cooknumber;
	public int time;

	// construct the cook with its id number and the restaurant
	public cook(int num, Restaurant r) {
		cooknumber = num;
		this.r = r;
		time = 0;
		// time = 0 and order is null
		o = null;
	}

	// get order from dinner,
	public void getorder() {
		// luck the orderslist
		synchronized (Restaurant.orderslist) {
			try {
				// if no order, wait for dinner to make
				while (Restaurant.orderslist.isEmpty()) {

					Restaurant.orderslist.wait();
				}
				// otherwise get the order from orderlist, with the lowest time
				// priority
				// First come first make
				o = Restaurant.orderslist.poll();
				// synchronize time with order time and its local time
				time = Math.max(time, o.time);
				Restaurant.writer.println(
						"time " + time + " : cook " + cooknumber + " gets order from dinner " + o.dinnernumber);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		// remove itself from available cook queue and add itself to busy queue.
		synchronized (Restaurant.cooksavalist) {
			Restaurant.cooksavalist.remove(this);
		}
		synchronized (Restaurant.cooksbusylist) {
			Restaurant.cooksbusylist.add(this);
		}
		// add itself to wait for machines queues
		synchronized (Restaurant.machines) {
			Restaurant.machines.add(this);
		}
	}

	// ready to prepare food
	public void preparefood() {
		// lock the waiting for machines queue
		synchronized (Restaurant.machines) {
			// while there is something still need to be made
			while (o.Burger != 0 || o.Cokes != 0 || o.Fries != 0) {
				// test if the machine for burger is available and it need to
				// make burger or not
				if (Restaurant.M.b && o.Burger != 0) {
					// if so lock the machine and begin to make burger one time.
					synchronized (Restaurant.M) {
						// change burger maker to unavailable
						// set its local time maximum between its local time and
						// machine time
						Restaurant.M.b = false;
						time = Math.max(time, Restaurant.M.bt);
						Restaurant.writer
								.println("time " + time + " : cook " + cooknumber + " is using burger machine ");
						int b = o.Burger;
						// making burger and use burger number * 5 time and add
						// it to local time.
						time += b * 5;
						o.Burger = 0;
						// made burger set machine time to its local time
						// release the burger machine and notify others cooks if
						// anyone is waiting.
						Restaurant.M.bt = time;
						Restaurant.M.b = true;
						Restaurant.writer.println("time " + time + " : cook " + cooknumber
								+ " releases burger machine and " + b + " burgers are made");
						Restaurant.machines.notify();
					}
				}

				// similar to burger machine
				if (Restaurant.M.f && o.Fries != 0) {
					synchronized (Restaurant.M) {
						Restaurant.M.b = false;
						time = Math.max(time, Restaurant.M.ft);
						Restaurant.writer
								.println("time " + time + " : cook " + cooknumber + " is using fries machine ");
						int f = o.Fries;
						time += f * 3;
						o.Fries = 0;
						Restaurant.M.ft = time;
						Restaurant.M.b = true;
						Restaurant.writer.println("time " + time + " : cook " + cooknumber
								+ " releases fries machine and " + f + " fries are made");
						Restaurant.machines.notify();
					}
				}
				// similar to burger machine
				if (Restaurant.M.c && o.Cokes != 0) {
					synchronized (Restaurant.M) {
						Restaurant.M.b = false;
						time = Math.max(time, Restaurant.M.ct);
						Restaurant.writer
								.println("time " + time + " : cook " + cooknumber + " is using cokes machine ");
						int c = o.Cokes;
						time += c * 1;
						o.Cokes = 0;
						Restaurant.M.ct = time;
						Restaurant.M.b = true;
						Restaurant.writer.println("time " + time + " : cook " + cooknumber
								+ " releases cokes machine and " + c + " cokes are made");
						Restaurant.machines.notify();
					}
				}
				// if no machine is available and we still need to make
				// something, we wait to be notified
				else if (o.Burger != 0 || o.Cokes != 0 || o.Fries != 0) {
					try {
						Restaurant.machines.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			Restaurant.writer.println("time " + time + " : cook " + cooknumber + " finishes cooking order ");
			// made all the order's food remove itself from waiting for machines
			// queue
			Restaurant.machines.remove(this);
		}
	}

	// send food to dinner, notify the specific order dinner.
	public void sendfood() {
		synchronized (o) {
			o.time = time;
			o.notify();
		}
		Restaurant.writer.println("time " + time + " : cook " + cooknumber + " send food to dinner " + o.dinnernumber);
		// remove itself from busy cook queue and add itself to available queue.
		synchronized (Restaurant.cooksavalist) {
			Restaurant.cooksavalist.add(this);
		}
		synchronized (Restaurant.cooksbusylist) {
			Restaurant.cooksbusylist.remove(this);
		}
	}

	public void run() {
		// keep waiting for dinner
		while (true) {
			getorder();
			preparefood();
			sendfood();
		}
	}
}
