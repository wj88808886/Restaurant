// class order
public class Order {
	// the number of burger and fries and cokes in this order.
	// the local time of this order
	// who make this order.
	public int Burger;
	public int Fries;
	public int Cokes;
	public int time;
	public int dinnernumber;
	
	public Order(int burger, int fries, int cokes, int dinnernumber) {
		Burger = burger;
		Fries = fries;
		Cokes = cokes;
		time = 0;
		this.dinnernumber = dinnernumber;
	}
	
}
