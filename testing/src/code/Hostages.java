package code;

public class Hostages extends Member {
	

	int damage;


	public Hostages(int m, int n) {
		super(m,n);
		this.damage=(int)Math.floor(Math.random()*(99-1+1)+1);
		
	  }
	public Hostages(int x, int y,int damage) {
		super();
		this.x=x;
		this.y=y;
		this.damage=damage;
		
	  }
	
	
	
	

}
