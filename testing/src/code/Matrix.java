package code;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public class Matrix extends SearchProblem {
	public static int numberOfMutatedAgents = 0;
	public static int numberOfNodesExpanded = 0;
	public static int deaths = 0;
	public static int kills = 0;
	
	public static State initialState;

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
	public Matrix() {
		super();
		//genGrid();
		traversedStates= new HashMap<String,ArrayList<ArrayList<Member>>>();
		Operator[] SetOfOperator = { Operator.UP, Operator.DOWN, Operator.RIGHT, Operator.LEFT, Operator.CARRY,
				Operator.DROP, Operator.FLY,Operator.TAKEPILL,Operator.KILL };
		
//		ArrayList <Member> initialTeam = new ArrayList<Member>();
//		for(int i = 0; i < Team.length; i++) {
//			initialTeam.add(Team[i]);
//		}
		//limitDepth=0;
		//State initialState = new State(ex, ey, c, initialTeam);
		this.initialState=initialState;
		this.setOperators=SetOfOperator;

	}

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
		ArrayList<Hostages> remainingHostages=new ArrayList<Hostages>();
		ArrayList<Hostages> carriedHostages=new ArrayList<Hostages>();
		ArrayList<Agents> Agents=new ArrayList<Agents>();
		ArrayList<Pill> remainingPills=new ArrayList<Pill>();
		ArrayList<Pad> Pads=new ArrayList<Pad>();
		int i = 0;
		//System.out.println(no_hosatges);
		for (i = 0; i < no_hosatges; i++) {
			Hostages x = new Hostages(m, n);
			//System.out.println("hpstagess "+no_hosatges);
			remainingHostages.add(x);
			
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
			
			Pads.add(pad1);
			Pads.add(pad2);
			
			//System.out.println(pad1);
			
			for(int test=0; test<members.length;test++) {
			//System.out.println(members[test]);
			}
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
			
			Agents.add(x);
			
			members[i] = x;
			if (i == no_agents - 1) {
				AgentsLoc += x.x + "," + x.y;

			} else {
				AgentsLoc += x.x + "," + x.y + ",";
			}

		}

		for (i = i; i < no_pills + i; i++) {
			Pill x = new Pill(m, n);
			
			remainingPills.add(x);
			
			members[i] = x;
			if (i == no_pills - 1) {
				PillsLoc += x.x + "," + x.y;

			} else {
				PillsLoc += x.x + "," + x.y + ",";
			}

		}
		
		initialState = new State(nx, ny, c, 0 ,remainingHostages, carriedHostages ,Agents,  remainingPills, Pads);
		return members;

	}

	public State stateSpace(Operator operator, State parentState) {

		State currentState = new State(parentState.xPosition, parentState.yPosition, parentState.carried,
				parentState.damageNeo, parentState.remainingHostages, parentState.carriedHostages, parentState.Agents,
				parentState.remainingPills, parentState.Pads);
		
		
	//	for (int i = 0; i < currentState.remainingHostages.size(); i++) {
			//Hostages tempHhh = currentState.remainingHostages.get(0);
			//System.out.println("hostagee"+tempHhh.x+"  "+tempHhh.y);
//			break;
//			}

		if (operator.compareTo(Operator.UP) == 0) {
			System.out.println("up");

			boolean found = false;
			boolean found2 = false;


			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition - 1 && temp.y == currentState.yPosition)) {
					found = true;
//					index = i;
					break;
				}
			}
			for (int i = 0; i < currentState.remainingHostages.size(); i++) {
				Hostages tempH = currentState.remainingHostages.get(i);
				if ((tempH.x == currentState.xPosition - 1 && tempH.y == currentState.yPosition&& tempH.damage>=98)) {
					found2 = true;
//					index = i;
					break;
				}
			}

			if (!found && !found2) {
				

				currentState.xPosition = (currentState.xPosition == 0) ? 0 : currentState.xPosition - 1;

			}

		} else if (operator.compareTo(Operator.DOWN) == 0) {
			System.out.println("down");

			boolean found = false;
			boolean found2 = false;


			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition + 1 && temp.y == currentState.yPosition)) {
					found = true;
					//index = i;
					break;
				}
			}
			
			for (int i = 0; i < currentState.remainingHostages.size(); i++) {
				Hostages tempH = currentState.remainingHostages.get(i);
				if ((tempH.x == currentState.xPosition +1 && tempH.y == currentState.yPosition&& tempH.damage>=98)) {
					found2 = true;
					//index = i;
					break;
				}
			}
			
		

			if (!found && !found2) {
				
				
				currentState.xPosition = (currentState.xPosition == Matrix.m - 1) ? Matrix.m - 1
						: currentState.xPosition + 1;

			}

		} else if (operator.compareTo(Operator.RIGHT) == 0) {
			System.out.println("right");
			boolean found = false;
			boolean found2=false;
			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition && temp.y == currentState.yPosition + 1)) {
					found = true;
					index = i;
					break;
				}
			}

			for (int i = 0; i < currentState.remainingHostages.size(); i++) {
				Hostages tempH = currentState.remainingHostages.get(i);
				if ((tempH.x == currentState.xPosition  && tempH.y == currentState.yPosition+1&& tempH.damage>=98)) {
					found2 = true;
					//index = i;
					break;
				}
			}

			if (!found && !found2) {
				currentState.yPosition = (currentState.yPosition == Matrix.n - 1) ? Matrix.n - 1
						: currentState.yPosition + 1;

			}

		} else if (operator.compareTo(Operator.LEFT) == 0) {

			System.out.println("left");
			boolean found = false;
			boolean found2=false;
			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition && temp.y == currentState.yPosition - 1)) {
					found = true;
					index = i;
					break;
				}
			}
			for (int i = 0; i < currentState.remainingHostages.size(); i++) {
				Hostages tempH = currentState.remainingHostages.get(i);
				if ((tempH.x == currentState.xPosition  && tempH.y == currentState.yPosition-1&& tempH.damage>=98)) {
					found2 = true;
					//index = i;
					break;
				}
			}

			if (!found && !found2) {
				currentState.yPosition = (currentState.yPosition == 0) ? 0 : currentState.yPosition - 1;

			}

		} else if (operator.compareTo(Operator.CARRY) == 0) {
			if (currentState.carried > 0) {
				boolean found = false;
				int index = 0;
				for (int i = 0; i < currentState.remainingHostages.size(); i++) {
					Member temp = currentState.remainingHostages.get(i);
					//System.out.println("HOATAGE"+i+temp.x +"," +temp.y);
					if (temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
						found = true;
						index = i;
						break;
					}
				}

				if (found) {
					//carry()
					//System.out.println("i am here"+currentState.xPosition+","+currentState.yPosition+"found "+ currentState.remainingHostages.get(index).x+","+currentState.remainingHostages.get(index).y);
					currentState.carried--;
					currentState.carriedHostages.add(currentState.remainingHostages.remove(index));

				}
			}

		} else if (operator.compareTo(Operator.DROP) == 0 && currentState.xPosition == Matrix.tx
				&& currentState.yPosition == Matrix.ty && currentState.carried < Matrix.c) {

			for (int i = 0; i < currentState.carriedHostages.size(); i++) {
				Hostages temp = currentState.carriedHostages.get(i);
				if (temp.damage >= 100) {
					deaths += 1;
				}
			}
			currentState.carried = Matrix.c;
			currentState.carriedHostages.clear();
		} else if (operator.compareTo(Operator.KILL) == 0) {
			// if(currentState.damageNeo > 80) {//do i need to do this check ?
			boolean found = false;
			boolean cont = true;

			for (int i = 0; i < currentState.remainingHostages.size(); i++) {
				Hostages tempH = currentState.remainingHostages.get(i);
				if ((tempH.x == currentState.xPosition  && tempH.y == currentState.yPosition&& tempH.damage>=98)) {
					cont=false;
					//index = i;
					break;
				}
			}
			if(cont) {

			ArrayList<Integer> indices=new ArrayList<Integer>();
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x - 1 == currentState.xPosition && temp.y == currentState.yPosition)
						|| (temp.x + 1 == currentState.xPosition && temp.y == currentState.yPosition)
						|| (temp.x == currentState.xPosition && temp.y - 1 == currentState.yPosition)
						|| (temp.x == currentState.xPosition && temp.y + 1 == currentState.yPosition)) {
					found = true;
					indices.add(i);
				}
			}
			
	

			if (found) {
				currentState.damageNeo += 20;
				// check if nemo's dead wala avoided fo2??
				// if 100 dead ? Game Over
				for(int i=0;i<indices.size();i++) {
					Agents a= 	currentState.Agents.get(indices.get(i));
					if (a.mutated)
						numberOfMutatedAgents--;
					currentState.Agents.remove(indices.get(i));
					kills += 1;
				}
				
				

			}
			}
			// }

		}

		else if (operator.compareTo(Operator.TAKEPILL) == 0) {
			// if(currentState.damageNeo > 0) {//do i need to do this check ?
			boolean found = false;
			int index = 0;
			for (int i = 0; i < currentState.remainingPills.size(); i++) {
				Pill temp = currentState.remainingPills.get(i);
				if (temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
					found = true;
					index = i;
					break;
				}
			}

			if (found) {
				
				currentState.damageNeo -= 20;
				if (currentState.damageNeo <= 0) {
					currentState.damageNeo = 0;
				}

				// check if nemo's dead wala avoided fo2??
				// if 100 dead ? Game Over
				currentState.remainingHostages = decreaseDamage(currentState.remainingHostages);
				currentState.carriedHostages = decreaseDamage(currentState.carriedHostages);

				currentState.remainingPills.remove(index);
			}

			// }
			// }

		}

		else if (operator.compareTo(Operator.FLY) == 0) {

			boolean found = false;
			int index = 0;
			for (int i = 0; i < currentState.Pads.size(); i++) {
				Pad temp = currentState.Pads.get(i);
				if (temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
					found = true;
					index = i;
					break;
				}

				if (found) {
					currentState.xPosition = currentState.Pads.get(index).destX;
					currentState.yPosition = currentState.Pads.get(index).destY;

				}
			}
		}
		// add a cond for pikk
		if (operator.compareTo(Operator.TAKEPILL) != 0) {
			// How can i increase the health of a hostage on the truck
			//currentState = increaseDamageRemaining(currentState);
			HashMap<Integer, ArrayList> retVal = increaseDamageRemaining(currentState.remainingHostages, currentState.Agents);
//			ArrayList<Hostages> h= retVal.get(0);
//			for(int kk=0;kk<h.size();kk++) {
//				System.out.println("newteam output "+h.get(kk).x +"my y "+h.get(kk).y);
//			}
			currentState.remainingHostages = retVal.get(0);
			currentState.Agents = retVal.get(1);
			
			currentState.carriedHostages = increaseDamageCarried(currentState.carriedHostages);

		}
		return currentState;
	}

	public ArrayList<Hostages> decreaseDamage(ArrayList<Hostages> remaining) {
		// SANDY
		// Taking a pill msh ha3di el zero taba law ana kont mot eh el nezamm ?

		for (int i = 0; i < remaining.size(); i++) {
			Hostages temp = remaining.get(i);
			if (temp.damage > 0 && temp.damage < 100) {
				temp.damage -= 20;
			}
			if (temp.damage <= 0) {
				temp.damage = 0;
			}
//			// 22 because when i take the pill -20 wana keda kda ha3mel -2 so total =20

			remaining.set(i, temp);
		}

		return remaining;
	}

	public Node generalSearch(QingFunction qing_func) {
		SearchProblem problem = this;
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		// replace by h2 and h2 from heuristic function
		//System.out.println(initialState.xPosition);
		Node root = new Node(initialState, null, null, 0, 0, -1, -1);
		
		// int h1 = getHeuristicManhatten(root);//////check this later
		// double h2 = getHeuristicEuclidean(root);
		// root.h1 = h1;
		// root.h2 = h2;

		nodes.add(root);
		
		insertInTraversedStates(root.currentState);
		while (nodes.size() > 0) {
			Node temp = nodes.get(0);
			boolean goalReached = problem.goalTest(temp.currentState);
			if (goalReached) {
				// Node returned instead of "solution"
				return temp;
			} else {
				nodes = this.Qing_Func(nodes, qing_func);
			}
		}
		return null;
	}
	
	
	
	public HashMap<Integer, ArrayList> increaseDamageRemaining(ArrayList<Hostages> remainingHostages, ArrayList<Agents> Agents) {
		
	//	System.out.println("gg "+ remainingHostages.size());
		//System.exit(0);
		// SANDY
		// what if my health reaches 100 do i convert him here
		// int =0 means it was carried so if it died msh h3mel haga
		// however if int =1 convert to Agent??
		
		HashMap<Integer, ArrayList> retVal = new HashMap<Integer,ArrayList>(); 
	
		ArrayList<Hostages> newTeam = new ArrayList<Hostages>();
		ArrayList<Agents> newAgents = new ArrayList<Agents>();
		newAgents = Agents;

		// Remaining Hostages
		for (int i = 0; i < remainingHostages.size(); i++) {
		
		
			//System.out.println("gg "+ remainingHostages.size() + " " + i );
			Hostages temp =new Hostages(remainingHostages.get(i).x,remainingHostages.get(i).y,remainingHostages.get(i).damage);
			System.out.println("ana hena 2"+temp.x+"    "+temp.y+" "+temp.damage);
			//temp.damage= remainingHostages.get(i).damage;
		
			if (temp.damage < 100) {
				temp.damage += 2;
				//newTeam.add(temp);

		
			}
			if (temp.damage >= 100) {
				System.out.println("convertinggg" + numberOfMutatedAgents);
				
				deaths += 1;
				Hostages converted = temp;
				Agents newAgent = new Agents(converted.x, converted.y, true);
				//newAgent.mutated=true;
				numberOfMutatedAgents++;
				newAgents.add(newAgent);



			}
			else {
//			
				newTeam.add(temp);
				

				}

				
	
//			}		
			
			

		}
		
//		
//		System.out.println("suzee" + remainingHostages.size());
//		System.out.println("new" + newTeam.size());
//		System.out.println("size agents" + newAgents.size());
		
//		for(int t = 0; t<remainingHostages.size();t++) {
//			System.out.println("teamm" + t + remainingHostages.get(t).x + remainingHostages.get(t).y);
//	}
	    //System.exit(0);
		retVal.put(0, newTeam); 
		retVal.put(1, newAgents); 
		
//		for(int t = 0; t<newAgents.size();t++) {
//		System.out.println("agent" + t + newAgents.get(t).x + newAgents.get(t).y);
//	}
			
		System.out.println("newwew ret team" + retVal.get(0).size());
		System.out.println("newwew ret agents" + retVal.get(1).size());
		

		return retVal;
	}
	
	
	
	
	
	
	
	
	
	

//	public State increaseDamageRemaining(State CurrentState) {
//		// SANDY
//		// what if my health reaches 100 do i convert him here
//		// int =0 means it was carried so if it died msh h3mel haga
//		// however if int =1 convert to Agent??
//
//		// Remaining Hostages
//		for (int i = 0; i < CurrentState.remainingHostages.size(); i++) {
//			Hostages temp = CurrentState.remainingHostages.get(i);
//			if (temp.damage < 100) {
//				temp.damage += 2;
//				CurrentState.remainingHostages.set(i, temp);
//			}
//			if (temp.damage >= 100) {
//				deaths += 1;
//				Hostages converted = CurrentState.remainingHostages.remove(i);
//				Agents newAgent = new Agents(converted.x, converted.y);
//				newAgent.mutated=true;
//				numberOfMutatedAgents++;
//				CurrentState.Agents.add(newAgent);
//				i--;
//
//			}
//
//		}
//
//		return CurrentState;
//	}

	public ArrayList<Hostages> increaseDamageCarried(ArrayList<Hostages> carried) {
		ArrayList<Hostages> newTeam = new ArrayList<Hostages>();

		for (int i = 0; i < carried.size(); i++) {
			
			Hostages temp =new Hostages(carried.get(i).x,carried.get(i).y,carried.get(i).damage);
			//temp.damage= carried.get(i).damage;

			if(temp.damage<=100) {
				temp.damage += 2;
				newTeam.add(temp);			}
			
			
		}
		

		return newTeam;
	}

	public boolean goalTest(State currentState) {
		if (currentState.remainingHostages.size() == 0 && currentState.carried == c
				&& currentState.carriedHostages.size() == 0 && numberOfMutatedAgents==0) {
			return true;
		}
		return false;
	}

	@Override
	public int pathCost(Node currentNode, Operator operator) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void insertInTraversedStates(State currentState) {
		String currentKey = currentState.xPosition + "," + currentState.yPosition + "," + currentState.carried;
		ArrayList<ArrayList<Member>> currentMemberList = this.traversedStates.get(currentKey);

		List<Member> combined = new ArrayList<Member>();
		combined.addAll(currentState.Agents);
		combined.addAll(currentState.remainingPills);
		combined.addAll(currentState.remainingHostages);

		if (currentMemberList == null || currentMemberList.size() == 0) {
			ArrayList<ArrayList<Member>> newMemberList = new ArrayList<ArrayList<Member>>();
			newMemberList.add((ArrayList<Member>) combined);
			this.traversedStates.put(currentKey, newMemberList);
			return;
		}
		currentMemberList.add((ArrayList<Member>) combined);
		this.traversedStates.put(currentKey, currentMemberList);
	}

	public ArrayList<Node> Qing_Func(ArrayList<Node> nodes, QingFunction qing_fun) {
		SearchProblem problem = this;
		Node parent = nodes.remove(0);
		System.out.println("martha hi "+parent.currentState);
//		if(numberOfNodesExpanded==23)
//			System.exit(0);
		//System.out.println("hiyomnatt" + parent.currentState.remainingHostages.get(0).damage);
		numberOfNodesExpanded += 1;
		int j = 0;
		for (int i = 0; i < problem.setOperators.length; i++) {
			State childState = stateSpace(problem.setOperators[i], parent.currentState);
			//System.out.println(problem.setOperators[i] + "kmfkm");
			if (!checkRepeatedStates(childState)) {
				Node childNode = new Node(childState, problem.setOperators[i], parent, parent.depth + 1, parent.cost,
						-1, -1);
				
				//int h1 = getHeuristicManhatten(childNode);
				//double h2 = getHeuristicEuclidean(childNode);

				//childNode.h1 = h1;
				//childNode.h2 = h2;
				//childNode.cost = pathCost(childNode, problem.setOperators[i]);

				//int costDifference = childNode.cost - parent.cost;
				//int heuristicDifference = parent.h1 - childNode.h1;

//				System.out.print(costDifference >= heuristicDifference);
//				System.out.println( "   " + costDifference + "    " + heuristicDifference + "   " + childNode.operator + "  ");

				if (qing_fun.compareTo(QingFunction.BF) == 0) {
					nodes.add(childNode);
					
					insertInTraversedStates(childState);
					
				} else if (qing_fun.compareTo(QingFunction.DF) == 0) {
					nodes.add(j++, childNode);
					insertInTraversedStates(childState);
				}
//				} else if (qing_fun.compareTo(QingFunction.UC) == 0) {
//					insertInTraversedStates(childState);
//					// insert in position using binary search according to cost...
//					if (nodes.size() == 0) {
//						nodes.add(childNode);
//					} else {
//						int index = getPositionCost(nodes, childNode.cost);
//						nodes.add(index, childNode);
//					}
//				} else if (qing_fun.compareTo(QingFunction.GR1) == 0) {
//					insertInTraversedStates(childNode.currentState);
//					if (nodes.size() == 0) {
//						nodes.add(childNode);
//					} else {
//						int index = getPositionH1(nodes, childNode.h1);
//						nodes.add(index, childNode);
//					}
//				} else if (qing_fun.compareTo(QingFunction.GR2) == 0) {
//					insertInTraversedStates(childNode.currentState);
//					if (nodes.size() == 0) {
//						nodes.add(childNode);
//					} else {
//						int index = getPositionH2(nodes, childNode.h2);
//						nodes.add(index, childNode);
//					}
//
//				} else if (qing_fun.compareTo(QingFunction.ID) == 0) {
//					if (childNode.depth <= limitDepth) {
//						nodes.add(j++, childNode);
//						insertInTraversedStates(childState);
//					}
//
//				} else if (qing_fun.compareTo(QingFunction.AS1) == 0) {
//					insertInTraversedStates(childNode.currentState);
//					int f = childNode.cost + childNode.h1;
//					if (nodes.size() == 0) {
//						nodes.add(childNode);
//					} else {
//						int index = getPositionAS1(nodes, childNode.h1 + childNode.cost);
//						nodes.add(index, childNode);
//					}
//
//				} else if (qing_fun.compareTo(QingFunction.AS2) == 0) {
//					insertInTraversedStates(childNode.currentState);
//					if (nodes.size() == 0) {
//						nodes.add(childNode);
//					} else {
//						int index = getPositionAS2(nodes, childNode.h2 + childNode.cost);
//						nodes.add(index, childNode);
//					}
//
//				}

			}
		}

		if (nodes.size() == 0 && qing_fun.compareTo(QingFunction.ID) == 0) {
			Node root = new Node(problem.initialState, null, null, 0, 0, -1, -1);

			nodes.add(root);
			numberOfNodesExpanded = 0;
			traversedStates.clear();
			insertInTraversedStates(root.currentState);
			//limitDepth += 1;
		}

		return nodes;
	}

	public boolean checkRepeatedStates(State currentState) {
		String currentKey = currentState.xPosition + "," + currentState.yPosition + "," + currentState.carried;
		ArrayList<ArrayList<Member>> currentMemberList = this.traversedStates.get(currentKey);
		if (currentMemberList == null || currentMemberList.size() == 0)
			return false;

		List<Member> combined = new ArrayList<Member>();
		combined.addAll(currentState.Agents);
		combined.addAll(currentState.remainingPills);
		combined.addAll(currentState.remainingHostages);

		for (int i = 0; i < currentMemberList.size(); i++) {
			if (checkEqualityOfMembers((ArrayList<Member>) combined, currentMemberList.get(i))) {
				return true;
			}
		}

		return false;
	}

	public static boolean checkEqualityOfMembers(ArrayList<Member> a1, ArrayList<Member> a2) {

		if (a1.size() != a2.size()) {
			return false;
		}

		for (int i = 0; i < a1.size(); i++) {
			if (a1.get(i).x != a2.get(i).x || a1.get(i).y != a2.get(i).y)
				return false;
		}
		return true;
	}
	
	
	
	
	private static void VisualizeSolution(String [] plan,int nx,int ny,int tbx,int tby,ArrayList<Hostages>remainingHostages,ArrayList<Agents>Agents,ArrayList<Pill>remainingPills,ArrayList<Pad>Pads) {
		//System.out.println("annnaaaa hena");
		//System.out.println("host" + remainingHostages.get(0).damage);
		JFrame frame = new JFrame("Visualize Solution");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(m,n));
		frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        frame.pack();
        frame.setVisible(true);
		JButton[][] ButtonGrid = new JButton[m][n];
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
            	ButtonGrid[x][y] = new JButton();
            	ButtonGrid[x][y].setEnabled(false);
                frame.add(ButtonGrid[x][y]);
            }
 
        }
        ButtonGrid[nx][ny].setText("Initial Position");
        //System.out.println(remainingHostages.get(1).x);
		for(int i =0;i<remainingHostages.size();i++)
		{
//			System.out.println("hoo"+i);
//		    System.out.println(remainingHostages.get(i).x);
//		    System.out.println(remainingHostages.get(i).y);
//		    System.out.println(remainingHostages.get(i).damage);
			ButtonGrid[remainingHostages.get(i).x][remainingHostages.get(i).y].setText("Hostage"+remainingHostages.get(i).damage);
		}
		for(int i =0;i<Agents.size();i++)
		{
			ButtonGrid[Agents.get(i).x][Agents.get(i).y].setText("Agent"+Agents.get(i).mutated);
		}
		for(int i =0;i<Pads.size();i++)
		{
			ButtonGrid[Pads.get(i).x][Pads.get(i).y].setText("Pads :"+Pads.get(i).destX+','+Pads.get(i).destY);
		}
		for(int i =0;i<remainingPills.size();i++)
		{
			ButtonGrid[remainingPills.get(i).x][remainingPills.get(i).y].setText("Pills");
		}
		
		ButtonGrid[tbx][tby].setText("Telephone Booth");
		
		for(int i =0;i<plan.length;i++)
		{
			ButtonGrid[nx][ny].setBackground(Color.white);
			ButtonGrid[nx][ny].setText(ButtonGrid[nx][ny].getText().replace("Neo", ""));
			if(plan[i].equals("right"))
				ny+=1;
			if(plan[i].equals("left"))
				ny-=1;
			if(plan[i].equals("up"))
				nx-=1;
			if(plan[i].equals("down"))
				nx+=1;
			if(plan[i].equals("takepill"))
				ButtonGrid[nx][ny].setText(ButtonGrid[nx][ny].getText().replace("Pills", ""));
			if(plan[i].equals("carry"))
				ButtonGrid[nx][ny].setText(ButtonGrid[nx][ny].getText().replace("Hostage", ""));
			if(plan[i].equals("kill")) {
				ButtonGrid[nx+1][ny].setText(ButtonGrid[nx+1][ny].getText().replace("Agent", ""));
				ButtonGrid[nx-1][ny].setText(ButtonGrid[nx-1][ny].getText().replace("Agent", ""));
				ButtonGrid[nx][ny+1].setText(ButtonGrid[nx][ny+1].getText().replace("Agent", ""));
				ButtonGrid[nx][ny-1].setText(ButtonGrid[nx][ny-1].getText().replace("Agent", ""));
			}
			if(plan[i].equals("fly")) {
				//Pads:x,y
				//x,y
				String[] t = ButtonGrid[nx][ny].getText().split(":");
				String[] loc=t[1].split(",");
				nx= Integer.parseInt(loc[0]);
				ny= Integer.parseInt(loc[1]);
				
			}
				
			ButtonGrid[nx][ny].setBackground(Color.green);
			ButtonGrid[nx][ny].setText(ButtonGrid[nx][ny].getText()+" Neo");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}
	
public static String solve(String grid, String strategy, boolean visualize) {
		
		Matrix m1 = new Matrix();
		numberOfNodesExpanded=0;
		String gridValues[] =grid.split(";");
		
		for(int k = 0; k < gridValues.length; k++) {
			System.out.println(gridValues[k]);			
		}
		
		m1.m=Integer.parseInt(gridValues[0].split(",")[0]);
		m1.n=Integer.parseInt(gridValues[0].split(",")[1]);
	
		m1.c=Integer.parseInt(gridValues[1].split(",")[0]);
		
		m1.nx= Integer.parseInt(gridValues[2].split(",")[0]);
		
		
		m1.ny= Integer.parseInt(gridValues[2].split(",")[1]);
		

		m1.tx= Integer.parseInt(gridValues[3].split(",")[0]);
		m1.ty= Integer.parseInt(gridValues[3].split(",")[1]);
		

		String agentsList[] = gridValues[4].split(",");
		String pillsList[] = gridValues[5].split(",");
		String padsList[] = gridValues[6].split(",");
		String hostagesList[] = gridValues[7].split(",");
//		String membersHealth[]=gridValues[4].split(",");
		
		m1.members = new Member[(agentsList.length)/2 + (pillsList.length)/2 + (padsList.length)/4 + (hostagesList.length)/3];
		
		
		ArrayList<Hostages> remainingHostages=new ArrayList<Hostages>();
		ArrayList<Hostages> carriedHostages=new ArrayList<Hostages>();
		ArrayList<Agents> Agents=new ArrayList<Agents>();
		ArrayList<Pill> remainingPills=new ArrayList<Pill>();
		ArrayList<Pad> Pads=new ArrayList<Pad>();
		
		
		for(int i =0;i<(agentsList.length)/2;i++)
		{
			
			Agents agent = new Agents(m, n);
			agent.x = Integer.parseInt(agentsList[2*i]);
			agent.y = Integer.parseInt(agentsList[2*i+1]);

			Agents.add(agent);
			
		}
		
		for(int i =0;i<(pillsList.length)/2;i++)
		{
			
			Pill pill = new Pill(m, n);
			pill.x = Integer.parseInt(pillsList[2*i]);
			pill.y = Integer.parseInt(pillsList[2*i+1]);
			remainingPills.add(pill);
			
		}
		
		for(int i =0;i<(padsList.length)/8;i++)
		{
			
			Pad pad1 = new Pad(m, n);
			Pad pad2 = new Pad(m, n);
			pad1.x = Integer.parseInt(padsList[8*i]);
			pad1.y = Integer.parseInt(padsList[8*i+1]);
			pad2.x = Integer.parseInt(padsList[8*i+2]);
			pad2.y = Integer.parseInt(padsList[8*i+3]);
			
			pad2.destX = pad1.x;
			pad2.destY = pad1.y;
			pad1.destX = pad2.x;
			pad1.destY = pad2.y;
			Pads.add(pad1);
			Pads.add(pad2);
			
		}
		
		for(int i =0;i<(hostagesList.length)/3;i++)
		{
	
			Hostages hostage = new Hostages(m, n);
			hostage.x = Integer.parseInt(hostagesList[3*i]);
			hostage.y = Integer.parseInt(hostagesList[3*i+1]);
			hostage.damage = Integer.parseInt(hostagesList[3*i+2]);
//			System.out.println("gggg"+hostage.x);
//			System.out.println("gggg"+hostage.y);
//			System.out.println("gggg"+hostage.damage);
			remainingHostages.add(hostage);
			
		}
		
		
		
		//WE DIDN'T ADD THINGS TO MEMBERS
		
		
		//m1.members=membersHealth.length;
		//Member[] newTeam=new Member[m1.members];
//		for(int i =0;i<membersHealth.length;i++)
//		{
//			Member member = new Member(Integer.parseInt(membersList[2*i]),
//					Integer.parseInt(membersList[2*i+1]),
//					Integer.parseInt(membersHealth[i]));
//			newTeam[i]=member;
//		}
//		Team=newTeam;
		
		QingFunction qing_func=null;
		
		switch (strategy) {
		case "BF":
			qing_func = QingFunction.BF;
			break;
		case "DF":
			qing_func = QingFunction.DF;
			break;
		case "ID":
			qing_func = QingFunction.ID;
			break;
		case "UC":
			qing_func = QingFunction.UC;
			break;
//		case "GR1":
//			qing_func = QingFunction.GR1;
//			break;
//		case "GR2":
//			qing_func = QingFunction.GR2;
//			break;
//		case "AS1":
//			qing_func = QingFunction.AS1;
//			break;
//		case "AS2":
//			qing_func = QingFunction.AS2;
//			break;
		}
		
		//ArrayList <Member> initialTeam = new ArrayList<Member>();
//		for(int i = 0; i < Team.length; i++) {
//			initialTeam.add(Team[i]);
//		}
		
//		State initialState = new State(nx, n, c, initialTeam);
//		m1.initialState=initialState;
		
		initialState = new State(nx, ny, c, 0 ,remainingHostages, carriedHostages ,Agents,  remainingPills, Pads);
		
		
		
		Node result = m1.generalSearch(qing_func); 
		//System.out.println("hosttt" + remainingHostages.get(0).damage);
		//int totalDeath = result.cost/1000000;
		
		// Missing Visualize concept ..
		String plan="";
		
		// delete me///////////////////////////////////////////
		Node n = new Node(initialState, null, null, 0, 0 , -1, -1);
//		n.h1 = m1.getHeuristicManhatten(n);
//		n.h2 = m1.getHeuristicEuclidean(n);
				
//		System.out.println("result cost ===> " +  result.cost);
//		System.out.println("heurist from node 1 ====> " + n.h1);
//		System.out.println("heurist from node 2 ====> " + n.h2);
		///////////////////////////////////////////////////////
		
		while(result != null) {
			if(result.operator != null) 
				plan=","+(""+result.operator).toLowerCase()+plan;
			result = result.parent;
		}
		plan=plan.substring(1)+";";
		plan+=""+deaths+";";
		plan+=""+kills+";";
		
		plan+=""+numberOfNodesExpanded;
		if(visualize)
			VisualizeSolution(plan.split(";")[0].split(","),nx,ny,tx,ty,remainingHostages,Agents,remainingPills,Pads);
		System.out.println(plan);
		return plan;
	}


	


public static void main(String[] args) {

	
	//check grid0 steps
	//time bta3 el tawela bel hash map
	//deaths w numberofmutants
	//genGrid problemooo
	// plan lowercase/uppercase plan 
	
	String grid0 = "5,5;2;4,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,3,3,3,3,4,4;4,0,17,1,2,54,0,0,46,4,1,22";
	String grid1 = "5,5;1;1,4;1,0;0,4;0,0,2,2;3,4,4,2,4,2,3,4;0,2,32,0,1,38";
	String grid2 = "8,8;1;2,4;5,3;0,4,1,4,3,0,7,7,5,6;0,1,1,3;4,4,3,1,3,1,4,4,0,7,7,0,7,0,0,7;5,5,97";
	String grid3 = "6,6;2;2,4;2,2;0,4,1,4,3,0,4,2;0,1,1,3;4,4,3,1,3,1,4,4;0,0,94,1,2,38,4,1,76,4,0,80";
	String grid4 = "7,7;3;0,0;0,6;0,3,0,4,2,3,4,5,6,6,5,4;0,2,4,3;2,0,0,5,0,5,2,0;1,0,83,2,5,38,6,4,66,2,6,20";
	String grid5 = "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
	String grid6 = "6,6;2;2,2;2,4;0,1,1,0,3,0,4,1,4,3,3,4,1,4,0,3;0,2;1,3,4,2,4,2,1,3;0,0,2,0,4,2,4,0,2,4,4,98,1,1,98";
	String grid7 = "7,7;4;3,3;0,2;0,1,1,0,1,1,1,2,2,0,2,2,2,4,2,6,1,4;5,5,5,0;5,1,2,5,2,5,5,1;0,0,98,3,2,98,4,4,98,0,3,98,0,4,98,0,5,98,5,4,98";
	String grid8 = "7,7;4;5,3;2,5;0,0,0,2,0,6,2,3,5,1;1,1,2,6,6,0;4,0,1,6,1,6,4,0;0,4,33,1,4,1,4,3,11,5,4,2,5,5,69,3,1,95";
	String grid9 = "7,7;3;4,3;3,2;1,0,1,1,1,3,2,5,5,3,5,5;4,2,2,6,6,2,4,4;1,4,6,4,6,4,1,4,4,0,6,6,6,6,4,0,6,0,0,6,0,6,6,0;0,2,98,1,5,26,3,3,70,6,3,90,6,5,32";
	String grid10 = "6,6;1;2,2;2,4;0,1,1,0,3,0,4,1,4,3,3,4,1,4,0,3,1,5;0,2;1,3,4,2,4,2,1,3;0,5,90,1,2,92,4,4,2,5,5,1,1,1,98";
	String grid11 = "9,9;2;8,0;3,5;0,1,0,3,1,0,1,1,1,2,0,7,1,8,3,8,6,1,6,5;0,6,2,8;8,1,4,5,4,5,8,1;0,0,95,0,2,98,0,8,94,2,5,13,2,6,39";

	System.out.println(solve(grid2, "BF", true));
	
	//double usageCPU = osBean.getProcessCpuLoad();
	//System.out.println("CPU LOAD: " + usageCPU);
	
//	java.lang.management.ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
//	 
//	for(Long threadID : threadMXBean.getAllThreadIds()) {
//	    ThreadInfo info = threadMXBean.getThreadInfo(threadID);
//	    System.out.println("Thread name: " + info.getThreadName());
//	    System.out.println("Thread State: " + info.getThreadState());
//	    System.out.println(String.format("CPU time: %s ns", 
//	      threadMXBean.getThreadCpuTime(threadID)));
//	  }
	
	System.out.println();
	
//	double physicalMEMORY = 100 * ((double)Runtime.getRuntime().totalMemory() - (double)Runtime.getRuntime().freeMemory()) 
//			/ Runtime.getRuntime().totalMemory();
//	System.out.println("RAM USAGE: " + physicalMEMORY);
	
}


	
}
