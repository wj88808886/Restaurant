import java.util.ArrayList;
import java.util.Queue;

//three type of machines that make burgers, fries and cokes
public class Machines {
	
	// b, f,c stands for whether they are available or not. True means available
	// bt ft ct stand for each local time of three machines
	public boolean b;
	public boolean f;
	public boolean c;
	public int bt;
	public int ft;
	public int ct;
	
	public Machines(boolean b,boolean f,boolean c){
		this.b = b;
		this.f = f;
		this.c = c;
		bt = 0;
		ft = 0;
		ct = 0;
	}
}
