import java.util.List;
//dinner threads
public class dinner extends Thread{
	
	// initialize several variable like order the dinner will make, table the dinner will seat
	// dinner number
	// dinner local time
	private Restaurant r;
	public Order o;
	private Table seat;
	public int dinnernumber;
	public int time;
	
	// construct a dinner with its dinner number, the restaurant and its order
	public dinner(int num, Restaurant r, List<Integer> order){
		this.r = r;
		o = new Order(order.get(1),order.get(2),order.get(3),num);
		o.time = order.get(0);
		dinnernumber = num;
		time = order.get(0);
	}
	// take seat function to make the dinner have a seat.
	public void takeseat(){
		//lock the tables
        synchronized (Restaurant.Tables) {
        	try {
        		// dinners arrives at the restaurant.
        		Restaurant.writer.println("time " + time + " : dinner "+ dinnernumber + " arrives at the restaurant");
        		// if no tables available now, just wait for notify.
        		while (Restaurant.Tables.isEmpty()){        			
        			Restaurant.Tables.wait();
        		}
        		//if there is available table, seat it and remove it from available table list.
        		// synchronized the time with its own time and the table available time and order time by choosing the maximum value of its own time and table time
        		seat = Restaurant.Tables.remove(0);
        		seat.time = Math.max(time, seat.time);
        		o.time = seat.time;
        		time = seat.time;
        		Restaurant.writer.println("time " + time + " : dinner "+ dinnernumber + " gets table " + seat.tableid);
        	}
        	catch (InterruptedException e) {
        		throw new RuntimeException(e);
			}
        }
	}
	
	// make order, add order to orderlist so that cook can be notified.
	public void makeorder(){
		synchronized (Restaurant.orderslist) {
    		Restaurant.writer.println("time " + o.time + " : dinner "+ dinnernumber + " makes an order: "+o.Burger+ " burgers and " + o.Fries + " fries and " + o.Cokes + " cokes.");
			Restaurant.orderslist.add(o);
			Restaurant.orderslist.notify();
		}
	}
	// get food from cook who cook the orders food.
	public void getfood(){	
		synchronized(o){
			try {
				//Restaurant.writer.println(dinnernumber);
				o.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// here synchronized its own time with the completed order time.
		time = Math.max(time,o.time);
	}

	// eating....add 30 to its own local time
	public void eat(){
		Restaurant.writer.println("time " + time + " : dinner "+ dinnernumber + " start eating");
		time += 30;
	}
	// leave make table available and synchronized table time.
	public void leave(){
		synchronized (Restaurant.Tables) {
			seat.time = time;
			Restaurant.Tables.add(seat);			
			Restaurant.Tables.notify();
		}
	}
	
    public void run() {
    	takeseat();
        makeorder();
        getfood();
        eat();
        leave();
		Restaurant.writer.println("time " + time + " : dinner "+ dinnernumber + " leaves the restaurant");
    }
}
