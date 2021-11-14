package testing;

import java.util.ArrayList;
import java.util.Random;

public class TheMatrix {
	public static int deaths=0;
	public static int kills=0;

	// generate grid coordinate
	public static int m;
	public static int n;
	// generate number of members
	public static int no_hosatges;
	public static String HostagesLoc;

	public static int no_agents;
	public static String AgentsLoc;

	public static int no_pills;
	public static String PillsLoc;

	public static int no_pads;
	public static String PadsLoc;

	public static int c;
	public static Member members[];
//	public static Hostages hostages_Arr[];
//	public static Agents agents_Arr[];
//	public static Pill pills_Arr[];

	// generate telephone coordinates
	public static int tx;
	public static int ty;

	// generate Neo coordinates
	public static int nx;
	public static int ny;

	public static int rand(int max, int min) {
		int randNumber;
		randNumber = (int) (Math.random() * ((max - min) + 1)) + min;
		return randNumber;
	}

	public static String genGrid() {
		String randomGrid = "";
		m = rand(5, 15);
		n = rand(5, 15);
		no_hosatges = rand(3, 10);
		no_pills = rand(1, 10);
		int max_agents = (m * n) - (no_hosatges + no_pills + 2);
		no_agents = rand(5, max_agents);

		int max_pads = ((m * n) - (2 + no_pills + no_hosatges + no_agents));
		no_pads = (int) ((rand(1, max_pads)));
		members = new Member[no_pads + no_hosatges + no_pills + no_agents];
		c = rand(1, 4);
		members = genMap();
		// positon of Neo
		Random rand = new Random(); // instance of random class
		int nx = rand.nextInt(m);
		int ny = rand.nextInt(n);
		// Generating the telephone booth location and making sure it's at a different
		// location
		int tx = rand.nextInt(m);
		int ty = rand.nextInt(n);

		while (tx == nx && ty == ny) {
			tx = rand.nextInt(m);
			ty = rand.nextInt(n);
		}

		randomGrid = m + "," + n + ";" + c + ";" + nx + "," + ny + ";" + tx + "," + ty + ";" + AgentsLoc + ";"
				+ PillsLoc + ";" + PadsLoc + ";" + HostagesLoc;

		return randomGrid;

	}

	public static Member[] genMap() {
		int i = 0;
		for (i = 0; i < no_hosatges; i++) {
			Hostages x = new Hostages(m, n);
			members[i] = x;
			if (i == no_hosatges - 1) {
				HostagesLoc += x.x + "," + x.y + "," + x.damage;

			} else {
				HostagesLoc += x.x + "," + x.y + "," + x.damage + ",";
			}

		}
		for (i = i; i < no_pads + i; i += 2) {
			// Start and End needs to be added
			Pad pad1 = new Pad(m, n);
			Pad pad2 = new Pad(m, n);
			pad2.destX = pad1.x;
			pad2.destY = pad1.y;
			pad1.destX = pad2.x;
			pad1.destY = pad2.y;
			members[i] = pad1;
			members[i + 1] = pad2;

			if (i == no_pads - 1) {
				PadsLoc += pad1.x + "," + pad1.y + "," + pad2.x + "," + pad2.y + "," + pad2.x + "," + pad2.y + ","
						+ pad1.x + "," + pad1.y;

			} else {
				PadsLoc += pad1.x + "," + pad1.y + "," + pad2.x + "," + pad2.y + "," + pad2.x + "," + pad2.y + ","
						+ pad1.x + "," + pad1.y + ",";
			}

		}

		for (i = i; i < no_agents + i; i++) {
			Agents x = new Agents(m, n);
			members[i] = x;
			if (i == no_agents - 1) {
				AgentsLoc += x.x + "," + x.y;

			} else {
				AgentsLoc += x.x + "," + x.y + ",";
			}

		}

		for (i = i; i < no_pills + i; i++) {
			Pill x = new Pill(m, n);
			members[i] = x;
			if (i == no_pills - 1) {
				PillsLoc += x.x + "," + x.y;

			} else {
				PillsLoc += x.x + "," + x.y + ",";
			}

		}
		return members;

	}

public State stateSpace(Operator operator, State parentState){
		
		State currentState = new State(parentState.xPosition,parentState.yPosition, parentState.carried, parentState.damageNeo, parentState.remainingHostages,parentState.carriedHostages ,parentState.Agents, parentState.remainingPills, parentState.Pads);
		
		if (operator.compareTo(Operator.UP) == 0) {
			
			
			boolean found = false; 
			int index = 0;
			for(int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if((temp.x == currentState.xPosition-1&& temp.y == currentState.yPosition)) {
					found = true;
					index = i;
					break;
				}
			}
			
			if(!found) {
				
				currentState.xPosition=(currentState.xPosition==0)? 0 : currentState.xPosition - 1;
				
			}
	
		
		} else if (operator.compareTo(Operator.DOWN) == 0) {
			
			boolean found = false; 
			int index = 0;
			for(int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if((temp.x == currentState.xPosition+1&& temp.y == currentState.yPosition)) {
					found = true;
					index = i;
					break;
				}
			}
			
			if(!found) {
				currentState.xPosition=(currentState.xPosition==TheMatrix.m-1) ? TheMatrix.m-1:currentState.xPosition + 1;
				
			}
			
		} else if (operator.compareTo(Operator.RIGHT) == 0) {
			boolean found = false; 
			int index = 0;
			for(int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if((temp.x == currentState.xPosition&& temp.y == currentState.yPosition+1)) {
					found = true;
					index = i;
					break;
				}
			}
			
			if(!found) {
				currentState.yPosition=(currentState.yPosition==TheMatrix.n-1) ? TheMatrix.n-1:currentState.yPosition + 1;
				
			}
			
			
		} else if (operator.compareTo(Operator.LEFT) == 0) {
			
			boolean found = false; 
			int index = 0;
			for(int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if((temp.x == currentState.xPosition&& temp.y == currentState.yPosition-1)) {
					found = true;
					index = i;
					break;
				}
			}
			
			if(!found) {
				currentState.yPosition=(currentState.yPosition==0) ? 0:currentState.yPosition - 1;
				
			}
			
		} else if (operator.compareTo(Operator.CARRY) == 0) {
			if(currentState.carried > 0) {
				boolean found = false; 
				int index = 0;
				for(int i = 0; i < currentState.remainingHostages.size(); i++) {
					Member temp = currentState.remainingHostages.get(i);
					if(temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
						found = true;
						index = i;
						break;
					}
				}
				
				if(found) {
					currentState.carried--;
					currentState.carriedHostages.add(currentState.remainingHostages.remove(index));
					
					
				}
			}
			
		} else if (operator.compareTo(Operator.DROP) == 0 && currentState.xPosition==TheMatrix.tx 
				&& currentState.yPosition==TheMatrix.ty && currentState.carried < TheMatrix.c ) {
			currentState.carried = TheMatrix.c;
			currentState.carriedHostages.clear();
			
		}
		else if (operator.compareTo(Operator.KILL) == 0) {
			//if(currentState.damageNeo > 80) {//do i need to do this check ?
				boolean found = false; 
				int index = 0;
				for(int i = 0; i < currentState.Agents.size(); i++) {
					Agents temp = currentState.Agents.get(i);
					if((temp.x-1 == currentState.xPosition&& temp.y == currentState.yPosition)||(temp.x+1 == currentState.xPosition && temp.y == currentState.yPosition)||(temp.x == currentState.xPosition && temp.y-1 == currentState.yPosition)||(temp.x == currentState.xPosition && temp.y+1 == currentState.yPosition) ) {
						found = true;
						index = i;
						break;
					}
				}
				
				if(found) {
					currentState.damageNeo+=20;
					//check if nemo's dead wala avoided fo2??
					//if 100 dead ? Game Over 
					currentState.Agents.remove(index);
					
					
				}
			//}
			
		}
		
		else if (operator.compareTo(Operator.TAKEPILL) == 0) {
			//if(currentState.damageNeo > 0) {//do i need to do this check ?
				boolean found = false; 
				int index = 0;
				for(int i = 0; i < currentState.remainingPills.size(); i++) {
					Pill temp = currentState.remainingPills.get(i);
					if(temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
						found = true;
						index = i;
						break;
					}
				}
				
				if(found) {
					currentState.damageNeo-=20;
					if (currentState.damageNeo<=0) {
						currentState.damageNeo=0;
					}
					
				
					//check if nemo's dead wala avoided fo2??
					//if 100 dead ? Game Over 
					currentState.remainingHostages=decreaseDamage(currentState.remainingHostages);
					currentState.carriedHostages=decreaseDamage(currentState.carriedHostages);

					currentState.remainingPills.remove(index);
				}
					
				//}
			//}
			
		}
		
		
		else if (operator.compareTo(Operator.FLY) == 0) {
			
				boolean found = false; 
				int index = 0;
				for(int i = 0; i < currentState.Pads.size(); i++) {
					Pad temp = currentState.Pads.get(i);
					if(temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
						found = true;
						index = i;
						break;
					}
				
				
				if(found) {
					currentState.xPosition=currentState.Pads.get(index).destX;
					currentState.yPosition=currentState.Pads.get(index).destY;
					
					
				}
			}
		}
		//add a cond for pikk
		if(operator.compareTo(Operator.TAKEPILL) != 0) {
		//How can i increase the health of a hostage on the truck
		currentState = increaseDamageRemaining(currentState);
		currentState.carriedHostages = increaseDamageCarried(currentState.carriedHostages);

		
		}
		return currentState;
	}

	public ArrayList<Hostages> decreaseDamage(ArrayList<Hostages> remaining) {
		// SANDY
		// Taking a pill msh ha3di el zero taba law ana kont mot eh el nezamm ?

		for (int i = 0; i < remaining.size(); i++) {
			Hostages temp = remaining.get(i);
			if (temp.damage > 0 && temp.damage<100) {
				temp.damage -= 20;
			}
			if (temp.damage <= 0) {
				temp.damage =0;
			}
//			// 22 because when i take the pill -20 wana keda kda ha3mel -2 so total =20

			remaining.set(i, temp);
		}

		return remaining;
	}

	public State increaseDamageRemaining(State CurrentState) {
		// SANDY
		// what if my health reaches 100 do i convert him here
		// int =0 means it was carried so if it died msh h3mel haga
		// however if int =1 convert to Agent??

		 //Remaining Hostages
			for (int i = 0; i < CurrentState.remainingHostages.size(); i++) {
				Hostages temp = CurrentState.remainingHostages.get(i);
				if (temp.damage < 100) {
					temp.damage += 2;
				CurrentState.remainingHostages.set(i, temp);
				}
				if(temp.damage>=100) {
					deaths+=1;
					Hostages converted=CurrentState.remainingHostages.remove(i);
					Agents newAgent=new Agents(converted.x, converted.y);
					CurrentState.Agents.add(newAgent);
					i--;

				}

				
			}
			
		return CurrentState;
	}
	
	public ArrayList<Hostages> increaseDamageCarried(ArrayList<Hostages> carried) {
		
		for (int i = 0; i < carried.size(); i++) {
			Hostages temp = carried.get(i);
			temp.damage += 2;
			carried.set(i, temp);
		}
		

		return carried;
	}
	
		
	
	
	
	
	
	
}
