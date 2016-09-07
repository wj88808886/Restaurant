import java.io.*;
import java.lang.Thread;
import java.util.*;

// Class restaurant.
public class Restaurant {

		// create thread name restaurant
		// the dinners
		private String threadName;
		
		// initial a writer to write to the file
		public static PrintWriter writer;
		// initial a queue that contains all the dinners
		public static Queue<dinner> dinnerslist;
		// initial a queue that contains all the orders that wait for cooks
		public static Queue<Order> orderslist;
		// initial a queue that have available cooks and cooks who are busy
		public static Queue<cook> cooksavalist;
		public static Queue<cook> cooksbusylist;
		// Initial a queue that cooks are waiting for machines all these queues are priority queue.
		public static Queue<cook> machines;
		
		// Create tables and machines for restaurant
		public static ArrayList<Table> Tables = new ArrayList<Table>();
		public static Machines M = new Machines(true,true,true);
		
		// implement dinner priority queue.
		public static Comparator<dinner> dinnerComparator = new Comparator<dinner>(){
			 
		        public int compare(dinner d1, dinner d2) {
		            return (int) (d1.time - d2.time);
		        }
		};
		    	
		// implement cooks priority queue.
		public static Comparator<cook> cookComparator = new Comparator<cook>(){
				 
			        public int compare(cook c1, cook c2) {
			            return (int) (c1.time - c2.time);
			        }
		};		   
		   
		// implement order priority queue.
		public static Comparator<Order> orderComparator = new Comparator<Order>(){
			 
			@Override
			public int compare(Order o1, Order o2) {
				return (int) (o1.time - o2.time);
			}
		};
		
		public Restaurant( String name){
			threadName = name;
			writer.println("Running " +  threadName );
	 	}
		

		// program start main.
		public static void main(String args[]) {		
			// enter the filename or path in the keyboard and enter
			BufferedReader myFile = null;
			
			//create data to store the input
			ArrayList<Integer> data  = new ArrayList<Integer>();
			System.out.println("enter the filename:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				String filename = br.readLine();
				myFile = new BufferedReader(new FileReader(filename));
				
//				myFile = new BufferedReader(new FileReader("TestData1.txt"));
				String line;
				// read lines from files and add it to data
				while((line=myFile.readLine())!=null) {
					// split it by space
					String[] array = line.split("\\s+");
					// then store all the int into data if its 
					for (int i = 0;i<array.length;i++){
						if (!array[i].isEmpty()){
							data.add(Integer.parseInt(array[i]));
						}
					}
				}
				// initial a new object for output file
				writer = new PrintWriter("Result"+filename, "UTF-8");
				
			} catch (IOException e) {
				// if error, output wrong input file and exits.
	            System.err.println("wrong input file");
	            System.exit(1);
			}
			
			// create time record
			// initialize and get dinners number, table number and cooks
			long startTime = System.currentTimeMillis();
			int dinners;
			int tablenum;
			int cooks;
			dinners = data.remove(0);
			tablenum = data.remove(0);
			
			// create new tables
			for (int i = 1; i<tablenum+1; i++){
				Tables.add(new Table(i));
			}
			cooks = data.remove(0);
			writer.println("dinners=" + dinners+ " tables=" + Tables.size() + " cooks=" + cooks);
			
			// start new restaurant
			// initialize different queues for available cooks, busy cooks orders and dinners/
			Restaurant R1 = new Restaurant( "restaurant");
			cooksavalist = new PriorityQueue<cook>(cooks,cookComparator);
			cooksbusylist = new PriorityQueue<cook>(cooks,cookComparator);
			orderslist = new PriorityQueue<Order>(data.size()/4,orderComparator);
			dinnerslist = new PriorityQueue<dinner>(data.size()/4,dinnerComparator);
			machines = new PriorityQueue<cook>(cooks,cookComparator);
			cook c;
			
			// start all the cook threads and add them into available cooks queue at the beginning
			for (int i = 0; i< cooks; i++){
				c = new cook(i+1,R1);
				c.start();
				cooksavalist.add(c);
			}		
			// create all the dinners threads and add them into dinners queue at the beginning
			for (int i = 0; i< data.size()/4;i++){
				dinnerslist.add(new dinner(i+1,R1,data.subList(4*i, 4*i+4)));
			}
			
			// poll all the dinners start those threads one by one by the arrival time.
			for (int i = 0; i <  data.size()/4;i++){
				dinner d = dinnerslist.poll();
				if (d.time > 120){
					System.out.println("dinner "+ d.dinnernumber + " arrives at the restaurant at time : "+ d.time);
					System.out.println("restaurant closed");
				}
				else{
					d.start();
				}
				try {
					// sleep 20 avoid some concurrency problems.
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//record the end time.
			writer.println("Total cost time: "+(System.currentTimeMillis() -startTime)*0.001 +"s");		
			writer.close();
			System.out.println("Done! please check the result file");
			System.exit(1);
		}
		
}
