package testing;

import java.util.ArrayList;

public class State {
	public int xPosition;
	public int yPosition;
	public int carried;
	public int damageNeo;

	
	
	// 0: FULL
	ArrayList<Hostages> remainingHostages;
	ArrayList<Hostages> carriedHostages;
	ArrayList<Agents> Agents;
	ArrayList<Pill> remainingPills;
	ArrayList<Pad> Pads;



	

	// HOW TO POPULATE THE STATE SPACE
	public State(int xPosition, int yPosition, int carried, int damageNeo ,ArrayList<Hostages> remainingHostages,ArrayList<Hostages> carriedHostages ,ArrayList<Agents> Agents, ArrayList<Pill> remainingPills,ArrayList<Pad> Pads) {
		super();
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.carried = carried;
		this.damageNeo=damageNeo;
		
		
		
//		this.remaining = remaining;
		this.remainingHostages=new ArrayList<Hostages>();
		for(int i =0;i<remainingHostages.size();i++)
		{
			this.remainingHostages.add(remainingHostages.get(i));
		}
		
		this.carriedHostages=new ArrayList<Hostages>();
		for(int i =0;i<carriedHostages.size();i++)
		{
			this.carriedHostages.add(carriedHostages.get(i));
		}
		
		this.Agents=new ArrayList<Agents>();
		for(int i =0;i<Agents.size();i++)
		{
			this.Agents.add(Agents.get(i));
		}
		
		this.remainingPills=new ArrayList<Pill>();
		for(int i =0;i<remainingPills.size();i++)
		{
			this.remainingPills.add(remainingPills.get(i));
		}
		this.Pads=new ArrayList<Pad>();
		for(int i =0;i<Pads.size();i++)
		{
			this.Pads.add(Pads.get(i));
		}
		
		
	}

	// or in Class Node to compare "cost" is it possible to have same state and less
	// cost ?

}