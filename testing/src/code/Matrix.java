package code;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;

//import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public class Matrix extends SearchProblem {
	public static int limitDepth = 0;
	public static int numberOfNodesExpanded = 0;

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
		// genGrid();
		traversedStates = new HashMap<String, ArrayList<ArrayList<Member>>>();
		Operator[] SetOfOperator = { Operator.CARRY, Operator.UP, Operator.DOWN, Operator.LEFT, Operator.RIGHT,
				Operator.DROP, Operator.TAKEPILL, Operator.KILL, Operator.FLY };
//		Operator[] SetOfOperator = { Operator.CARRY,Operator.FLY, Operator.DOWN, Operator.UP, Operator.LEFT, Operator.RIGHT,
//				Operator.DROP, Operator.TAKEPILL, Operator.KILL };

//		ArrayList <Member> initialTeam = new ArrayList<Member>();
//		for(int i = 0; i < Team.length; i++) {
//			initialTeam.add(Team[i]);
//		}
		limitDepth = 0;
		// State initialState = new State(ex, ey, c, initialTeam);
		this.initialState = initialState;
		this.setOperators = SetOfOperator;

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
		no_pads = ((no_pads % 2 == 0) ? no_pads : no_pads - 1);
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
		ArrayList<Hostages> remainingHostages = new ArrayList<Hostages>();
		ArrayList<Hostages> carriedHostages = new ArrayList<Hostages>();
		ArrayList<Agents> Agents = new ArrayList<Agents>();
		ArrayList<Pill> remainingPills = new ArrayList<Pill>();
		ArrayList<Pad> Pads = new ArrayList<Pad>();
		int i = 0;
		int j = 0;
		int k = 0;

		for (i = 0; i < no_hosatges; i++) {
			Hostages x = new Hostages(m, n);
			// System.out.println("hpstagess "+no_hosatges);
			remainingHostages.add(x);
			members[i] = x;
			if (i == no_hosatges - 1) {
				HostagesLoc += x.x + "," + x.y + "," + x.damage;

			} else {
				HostagesLoc += x.x + "," + x.y + "," + x.damage + ",";
			}

		}

		for (j = i; j < no_pads + i; j += 2) {

			// Start and End needs to be added
			Pad pad1 = new Pad(m, n);
			Pad pad2 = new Pad(m, n);
			pad2.destX = pad1.x;
			pad2.destY = pad1.y;
			pad1.destX = pad2.x;
			pad1.destY = pad2.y;

			Pads.add(pad1);
			Pads.add(pad2);

			// System.out.println(pad1);

//			for(int test=0; test<members.length;test++) {
//			//System.out.println(members[test]);
//			}
			members[j] = pad1;
			members[j + 1] = pad2;

			if (j == no_pads + i - 2) {
				PadsLoc += pad1.x + "," + pad1.y + "," + pad2.x + "," + pad2.y + "," + pad2.x + "," + pad2.y + ","
						+ pad1.x + "," + pad1.y;

			} else {
				PadsLoc += pad1.x + "," + pad1.y + "," + pad2.x + "," + pad2.y + "," + pad2.x + "," + pad2.y + ","
						+ pad1.x + "," + pad1.y + ",";
			}

		}

		for (k = j; k < no_agents + j; k++) {
			Agents x = new Agents(m, n);

			Agents.add(x);

			members[k] = x;
			if (k == no_agents + j - 1) {
				AgentsLoc += x.x + "," + x.y;

			} else {
				AgentsLoc += x.x + "," + x.y + ",";
			}

		}

		for (i = k; i < no_pills + k; i++) {
			Pill x = new Pill(m, n);

			remainingPills.add(x);

			members[i] = x;
			if (i == no_pills + k - 1) {
				PillsLoc += x.x + "," + x.y;

			} else {
				PillsLoc += x.x + "," + x.y + ",";
			}

		}

		initialState = new State(nx, ny, c, 0, remainingHostages, carriedHostages, Agents, remainingPills, Pads, 0, 0,
				0, 0);
		return members;

	}

	public State stateSpace(Operator operator, State parentState) {

		State currentState = new State(parentState.xPosition, parentState.yPosition, parentState.carried,
				parentState.damageNeo, parentState.remainingHostages, parentState.carriedHostages, parentState.Agents,
				parentState.remainingPills, parentState.Pads, parentState.deaths, parentState.kills,
				parentState.numberOfMutatedAgents, parentState.rescuedHostages);

		// for (int i = 0; i < currentState.remainingHostages.size(); i++) {
		// Hostages tempHhh = currentState.remainingHostages.get(0);
		// System.out.println("hostagee"+tempHhh.x+" "+tempHhh.y);
//			break;
//			}

		if (operator.compareTo(Operator.UP) == 0 && currentState.damageNeo < 100 && (currentState.xPosition != 0)) {
			//System.out.println("up");

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
				if ((tempH.x == currentState.xPosition - 1 && tempH.y == currentState.yPosition
						&& tempH.damage >= 98)) {
					found2 = true;
//					index = i;
					break;
				}
			}

			if (!found && !found2) {

				currentState.xPosition = (currentState.xPosition == 0) ? 0 : currentState.xPosition - 1;

			}

		} else if (operator.compareTo(Operator.DOWN) == 0 && currentState.damageNeo < 100
				&& (currentState.xPosition != Matrix.m - 1)) {
			// System.out.println("down");

			boolean found = false;
			boolean found2 = false;

			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition + 1 && temp.y == currentState.yPosition)) {
					found = true;
					// index = i;
					break;
				}
			}

			for (int i = 0; i < currentState.remainingHostages.size(); i++) {
				Hostages tempH = currentState.remainingHostages.get(i);
				if ((tempH.x == currentState.xPosition + 1 && tempH.y == currentState.yPosition
						&& tempH.damage >= 98)) {
					found2 = true;
					// index = i;
					break;
				}
			}

			if (!found && !found2) {

				currentState.xPosition = (currentState.xPosition == Matrix.m - 1) ? Matrix.m - 1
						: currentState.xPosition + 1;

			}

		} else if (operator.compareTo(Operator.RIGHT) == 0 && currentState.damageNeo < 100
				&& (currentState.yPosition != Matrix.n - 1)) {
			//System.out.println("right");
			boolean found = false;
			boolean found2 = false;
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
				if ((tempH.x == currentState.xPosition && tempH.y == currentState.yPosition + 1
						&& tempH.damage >= 98)) {
					found2 = true;
					// index = i;
					break;
				}
			}

			if (!found && !found2) {
				currentState.yPosition = (currentState.yPosition == Matrix.n - 1) ? Matrix.n - 1
						: currentState.yPosition + 1;

			}

		} else if (operator.compareTo(Operator.LEFT) == 0 && currentState.damageNeo < 100
				&& (currentState.yPosition != 0)) {

			//System.out.println("left");
			boolean found = false;
			boolean found2 = false;
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
				// System.out.println("gggggg"+i+" "+tempH.damage);
				if ((tempH.x == currentState.xPosition && tempH.y == currentState.yPosition - 1
						&& tempH.damage >= 98)) {
					found2 = true;
					// index = i;
					break;
				}
			}

			if (!found && !found2) {
				currentState.yPosition = (currentState.yPosition == 0) ? 0 : currentState.yPosition - 1;

			}

		} else if (operator.compareTo(Operator.CARRY) == 0 && currentState.damageNeo < 100) {
			if (currentState.carried > 0) {
				boolean found = false;
				int index = 0;
				for (int i = 0; i < currentState.remainingHostages.size(); i++) {
					Member temp = currentState.remainingHostages.get(i);
					// System.out.println("HOATAGE"+i+temp.x +"," +temp.y);
					if (temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
						found = true;
						index = i;
						break;
					}
				}

				if (found) {
					// carry()
					// System.out.println("i am
					// here"+currentState.xPosition+","+currentState.yPosition+"found "+
					// currentState.remainingHostages.get(index).x+","+currentState.remainingHostages.get(index).y);
					currentState.carried--;
					currentState.carriedHostages.add(currentState.remainingHostages.remove(index));

				}
			}

		} else if (operator.compareTo(Operator.DROP) == 0 && currentState.xPosition == Matrix.tx
				&& currentState.yPosition == Matrix.ty && currentState.carried < Matrix.c
				&& currentState.damageNeo < 100) {

			for (int i = 0; i < currentState.carriedHostages.size(); i++) {
				Hostages temp = currentState.carriedHostages.get(i);
				if (temp.damage >= 98) {
					//System.out.println("yooo"+currentState.deaths);
					currentState.deaths+=1;
					//System.out.println("yooo"+currentState.deaths);
					
					//currentState.rescuedHostages += 1;
				}
			}
			// =currentState.carriedHostages.size();
			currentState.carried = Matrix.c;
			currentState.carriedHostages.clear();
		} else if (operator.compareTo(Operator.KILL) == 0 && currentState.damageNeo < 80) {
			// if(currentState.damageNeo > 80) {//do i need to do this check ?
			boolean found = false;
			boolean cont = true;

			for (int i = 0; i < currentState.remainingHostages.size(); i++) {
				Hostages tempH = currentState.remainingHostages.get(i);
				if ((tempH.x == currentState.xPosition && tempH.y == currentState.yPosition && tempH.damage >= 98)) {
					cont = false;
					// index = i;
					break;
				}
			}
			if (cont) {

				ArrayList<Agents> toremoveAgents = new ArrayList<Agents>();
				for (int i = 0; i < currentState.Agents.size(); i++) {
					Agents temp = currentState.Agents.get(i);
					if ((temp.x - 1 == currentState.xPosition && temp.y == currentState.yPosition)
							|| (temp.x + 1 == currentState.xPosition && temp.y == currentState.yPosition)
							|| (temp.x == currentState.xPosition && temp.y - 1 == currentState.yPosition)
							|| (temp.x == currentState.xPosition && temp.y + 1 == currentState.yPosition)) {
						found = true;
						toremoveAgents.add(temp);
					}
				}

				if (found) {
					currentState.damageNeo += 20;
					// check if nemo's dead wala avoided fo2??
					// if 100 dead ? Game Over
					for (int i = 0; i < toremoveAgents.size(); i++) {
						Agents a = toremoveAgents.get(i);
						if (a.mutated)
							currentState.numberOfMutatedAgents--;
						currentState.Agents.remove(a);
						currentState.kills += 1;
					}

				}
			}
			// }

		}

		else if (operator.compareTo(Operator.TAKEPILL) == 0 && currentState.damageNeo < 100) {
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

		else if (operator.compareTo(Operator.FLY) == 0 && currentState.damageNeo < 100) {
		//	System.out.println("GOA FLY  ");

			boolean found = false;
			int index = 0;
			for (int i = 0; i < currentState.Pads.size(); i++) {
				
				Pad temp = currentState.Pads.get(i);
				
				if (temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
					//System.out.println(temp.x+"  "+temp.y);
					found = true;
					index = i;
					break;
				}

			}
			if (found) {
				currentState.xPosition = currentState.Pads.get(index).destX;
				currentState.yPosition = currentState.Pads.get(index).destY;

			}
		}
		// add a cond for pikk
		if (operator.compareTo(Operator.TAKEPILL) != 0) {
			// How can i increase the health of a hostage on the truck
			// currentState = increaseDamageRemaining(currentState);
			HashMap<Integer, ArrayList> retVal = increaseDamageRemaining(currentState.remainingHostages,
					currentState.Agents, currentState.deaths, currentState.numberOfMutatedAgents);
			HashMap<Integer, ArrayList> retVal2 = increaseDamageCarried(currentState.carriedHostages,
					currentState.deaths);
			// ArrayList<Hostages> h= retVal.get(0);
//			for(int kk=0;kk<h.size();kk++) {
//				System.out.println("newteam output "+h.get(kk).x +"my y "+h.get(kk).y);
//			}
			currentState.remainingHostages = retVal.get(0);
			currentState.Agents = retVal.get(1);
			ArrayList<Integer> arrayOFNums = retVal.get(2);
			ArrayList<Integer> arrayOFNums2 = retVal2.get(1);

			currentState.numberOfMutatedAgents = arrayOFNums.get(1);

			currentState.deaths = arrayOFNums.get(0) + arrayOFNums2.get(0);
			currentState.carriedHostages = retVal2.get(0);

		}
		//System.out.println(currentState.deaths+"check");
		return currentState;
	}

	public ArrayList<Hostages> decreaseDamage(ArrayList<Hostages> remaining) {
		// SANDY
		// Taking a pill msh ha3di el zero taba law ana kont mot eh el nezamm ?
		ArrayList<Hostages> newTeam = new ArrayList<Hostages>();

		for (int i = 0; i < remaining.size(); i++) {
			Hostages temp = new Hostages(remaining.get(i).x, remaining.get(i).y, remaining.get(i).damage);
			if (temp.damage > 0 && temp.damage < 100) {
				temp.damage -= 20;
			}
			if (temp.damage <= 0) {
				temp.damage = 0;
			}
//			// 22 because when i take the pill -20 wana keda kda ha3mel -2 so total =20

			newTeam.add(temp);
		}

		return newTeam;
	}

	public Node generalSearch(QingFunction qing_func) {
		SearchProblem problem = this;
		ArrayList<Node> nodes = new ArrayList<Node>();

		// replace by h2 and h2 from heuristic function
		// System.out.println(initialState.xPosition);
		Node root = new Node(initialState, null, null, 0, 0, -1, -1);

		int h1 = getHeuristicManhatten(root);////// check this later
		// double h2 = getHeuristicEuclidean(root);
		root.h1 = h1;
		// root.h2 = h2;

		nodes.add(root);

		insertInTraversedStates(root.currentState);
		while (nodes.size() > 0) {
			Node temp = nodes.get(0);
			boolean goalReached = problem.goalTest(temp.currentState);
			
			if (goalReached) {
				//System.out.println(goalReached);
				// Node returned instead of "solution"
				// System.out.println(root.currentState.numberOfMutatedAgents+"ezaykooo");

				return temp;
			} else {
				nodes = this.Qing_Func(nodes, qing_func);
			}
		}
		return null;
	}

	public HashMap<Integer, ArrayList> increaseDamageRemaining(ArrayList<Hostages> remainingHostages,
			ArrayList<Agents> Agents, int oldDeaths, int oldMutated) {

		// System.out.println("gg "+ remainingHostages.size());
		// System.exit(0);
		// SANDY
		// what if my health reaches 100 do i convert him here
		// int =0 means it was carried so if it died msh h3mel haga
		// however if int =1 convert to Agent??
		int deathsNum = oldDeaths;
		int mutatedNum = oldMutated;
		ArrayList<Integer> returnedNums = new ArrayList<Integer>();

		HashMap<Integer, ArrayList> retVal = new HashMap<Integer, ArrayList>();

		ArrayList<Hostages> newTeam = new ArrayList<Hostages>();
		ArrayList<Agents> newAgents = new ArrayList<Agents>();
		newAgents = Agents;
		// Remaining Hostages
		for (int i = 0; i < remainingHostages.size(); i++) {
			Hostages temp = new Hostages(remainingHostages.get(i).x, remainingHostages.get(i).y,
					remainingHostages.get(i).damage);
			if (temp.damage < 100) {
				temp.damage += 2;

			}

			if (temp.damage >= 100) {

				deathsNum += 1;
				Hostages converted = temp;
				Agents newAgent = new Agents(converted.x, converted.y, true);
				// newAgent.mutated=true;
				mutatedNum += 1;
				newAgents.add(newAgent);

			} else {
				newTeam.add(temp);
			}

		}

		returnedNums.add(deathsNum);

		returnedNums.add(mutatedNum);
//		
//		System.out.println("suzee" + remainingHostages.size());
//		System.out.println("new" + newTeam.size());
//		System.out.println("size agents" + newAgents.size());

//		for(int t = 0; t<remainingHostages.size();t++) {
//			System.out.println("teamm" + t + remainingHostages.get(t).x + remainingHostages.get(t).y);
//	}
		// System.exit(0);
		retVal.put(0, newTeam);
		retVal.put(1, newAgents);
		retVal.put(2, returnedNums);

		return retVal;
	}

	public HashMap<Integer, ArrayList> increaseDamageCarried(ArrayList<Hostages> carried, int oldDeaths) {

		ArrayList<Hostages> newTeam = new ArrayList<Hostages>();
		int deathsNum = 0;

		ArrayList<Integer> returnedNums = new ArrayList<Integer>();
		HashMap<Integer, ArrayList> retVal = new HashMap<Integer, ArrayList>();

		for (int i = 0; i < carried.size(); i++) {

			Hostages temp = new Hostages(carried.get(i).x, carried.get(i).y, carried.get(i).damage);
			// temp.damage= carried.get(i).damage;

			if (temp.damage < 100) {
				temp.damage += 2;
				newTeam.add(temp);
			} else
			{
				deathsNum += 1;
			}

		}

		returnedNums.add(deathsNum);
		retVal.put(0, newTeam);
		retVal.put(1, returnedNums);

		return retVal;
	}

	public boolean goalTest(State currentState) {
		
		if (currentState.remainingHostages.size() == 0 && currentState.carried == c
				&& currentState.carriedHostages.size() == 0 && currentState.numberOfMutatedAgents == 0
				&& currentState.xPosition == tx && currentState.yPosition == ty) {
			//System.out.println(currentState.deaths+"ehna");

			return true;
		}

		return false;
	}

	@Override
	public int pathCost(Node currentNode, Operator operator) {
		// TODO Auto-generated method stub
		// Node parent=currentNode.parent;
		// int
		// parentActualCost=parent.cost-2*parent.currentState.kills-3*parent.currentState.deaths-4*parent.currentState.rescuedHostages;
//		int rescued=currentNode.currentState.rescuedHostages;
		int deathNumber = currentNode.currentState.deaths;
		int killNumber = currentNode.currentState.kills;
//		//int actualCost=(currentNode.currentState.remainingHostages.size()+currentNode.currentState.carriedHostages.size())*2;
//		int actualCost= (operator.compareTo(Operator.TAKEPILL) == 0 ?0: operator.compareTo(Operator.KILL)==0?20:2);
//		
//		return 4*rescued+ 3*deathNumber+ 2*killNumber+actualCost+currentNode.cost ;
		return 400000*deathNumber + 20000*killNumber + currentNode.depth;
//		return 0;
	}

	public void insertInTraversedStates(State currentState) {
		String currentKey = currentState.xPosition + "," + currentState.yPosition + "," + currentState.carried;
		// String currentKey = currentState.xPosition + "," + currentState.yPosition +
		// "," + currentState.carried + "," + currentState.damageNeo + "," +
		// currentState.kills + "," + currentState.deaths;
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

	public int getPositionCost(ArrayList<Node> nodes, int cost) {

		int start = 0;
		int end = nodes.size() - 1;
		int m = start + (end - start) / 2;

		if (nodes.size() == 1) {
			if (nodes.get(0).cost > cost) {
				return 0;
			} else
				return 1;
		}

		boolean found = false;

		while (start <= end) {
			m = start + (end - start) / 2;

			if (nodes.get(m).cost == cost) {
				// get m
				found = true;
				break;
			}

			if (nodes.get(m).cost < cost) {
				start = m + 1;
			}

			if (nodes.get(m).cost > cost) {
				end = m - 1;
			}

		}

		if (found) {
			for (int i = m; i < nodes.size(); i++) {
				if (nodes.get(i).cost > cost) {
					return i;
				}
			}
			return m;
		} else {
			for (int i = m; i >= 0; i--) {
				if (nodes.get(i).cost < cost) {
					return i;
				}
			}
			return m;
		}
	}

	public ArrayList<Node> Qing_Func(ArrayList<Node> nodes, QingFunction qing_fun) {
		SearchProblem problem = this;
		Node parent = nodes.remove(0);
//		System.out.println("martha hi "+parent.currentState);
//		if(numberOfNodesExpanded==23)
//			System.exit(0);
		// System.out.println("hiyomnatt" +
		// parent.currentState.remainingHostages.get(0).damage);
		numberOfNodesExpanded += 1;
		int j = 0;
		for (int i = 0; i < problem.setOperators.length; i++) {
			State childState = stateSpace(problem.setOperators[i], parent.currentState);
			if (!checkRepeatedStates(childState)) {
				Node childNode = new Node(childState, problem.setOperators[i], parent, parent.depth + 1, parent.cost,
						-1, -1);

				int h1 = getHeuristicManhatten(childNode);
				// double h2 = getHeuristicEuclidean(childNode);

				childNode.h1 = h1;
				// childNode.h2=h2;
				childNode.cost = pathCost(childNode, problem.setOperators[i]);

				// int costDifference = childNode.cost - parent.cost;
				// int heuristicDifference = parent.h1 - childNode.h1;

//				System.out.print(costDifference >= heuristicDifference);
//				System.out.println( "   " + costDifference + "    " + heuristicDifference + "   " + childNode.operator + "  ");

				if (qing_fun.compareTo(QingFunction.BF) == 0) {
					nodes.add(childNode);

					insertInTraversedStates(childState);

				} else if (qing_fun.compareTo(QingFunction.DF) == 0) {
					nodes.add(j++, childNode);
					insertInTraversedStates(childState);
				} else if (qing_fun.compareTo(QingFunction.UC) == 0) {
					insertInTraversedStates(childState);
					// insert in position using binary search according to cost...
					if (nodes.size() == 0) {
						nodes.add(childNode);
					} else {
						int index = getPositionCost(nodes, childNode.cost);
						System.out.println("New for Loop");
						for(int kkk=0;kkk<nodes.size();kkk++) {
							System.out.print(" Node "+kkk+ " My Cost is "+ nodes.get(kkk).cost +" w ana meen "+nodes.get(kkk).depth);
						}
						nodes.add(index, childNode);
					}
				}
				 else if (qing_fun.compareTo(QingFunction.GR1) == 0) {
					insertInTraversedStates(childNode.currentState);
					if (nodes.size() == 0) {
						nodes.add(childNode);
					} else {
						int index = getPositionH1(nodes, childNode.h1);
						nodes.add(index, childNode);
					}
				} 
				//else if (qing_fun.compareTo(QingFunction.GR2) == 0) {
//					insertInTraversedStates(childNode.currentState);
//					if (nodes.size() == 0) {
//						nodes.add(childNode);
//					} else {
//						int index = getPositionH2(nodes, childNode.h2);
//						nodes.add(index, childNode);
//					}
//
//				} 
				else if (qing_fun.compareTo(QingFunction.ID) == 0) {
					if (childNode.depth <= limitDepth) {
						nodes.add(j++, childNode);
						insertInTraversedStates(childState);
					}
				}
					else if (qing_fun.compareTo(QingFunction.AS1) == 0) {
					insertInTraversedStates(childNode.currentState);
					int f = childNode.cost + childNode.h1;
					if (nodes.size() == 0) {
						nodes.add(childNode);
					} else {
						int index = getPositionAS1(nodes, childNode.h1 + childNode.cost);
						nodes.add(index, childNode);
					}

				} 
				//else if (qing_fun.compareTo(QingFunction.AS2) == 0) {
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
			Node root = new Node(this.initialState, null, null, 0, 0, -1, -1);
			//System.out.println(root.currentState);

			nodes.add(root);
			numberOfNodesExpanded = 0;
			traversedStates.clear();
			insertInTraversedStates(root.currentState);
			limitDepth += 1;
		}

		return nodes;
	}
	
	public int getPositionH1(ArrayList<Node> nodes, int h1) {
		
		int start = 0;
		int end = nodes.size() - 1;
		int m = start + (end - start)/2;
		
		if(nodes.size() == 1) {
			if(nodes.get(0).h1 > h1) {
				return 0;
			} else return 1;
		}
		
		boolean found = false;
		
		while(start <= end) {
			m = start + (end - start)/2;
			
			if(nodes.get(m).h1 == h1) {
				// get m 
				found = true;
				break;
			}
			
			if(nodes.get(m).h1 < h1) {
				start = m + 1;
			}
			
			if(nodes.get(m).h1 > h1) {
				end = m - 1;
			}
			
		}
		
		if (found) {
			for (int i = m; i < nodes.size(); i++) {
				if (nodes.get(i).h1 > h1) {
					return i;
				}
			}
			return m;
		} else {
			for (int i = m; i >= 0; i--) {
				if (nodes.get(i).h1 < h1) {
					return i;
				}
			}
			return m;
		}
	}
public int getPositionAS1(ArrayList<Node> nodes, int f) {
		
		int start = 0;
		int end = nodes.size() - 1;
		int m = start + (end - start)/2;
		
		if(nodes.size() == 1) {
			Node node = nodes.get(0);
			if(node.cost + node.h1 > f) {
				return 0;
			} else return 1;
		}
		
		boolean found = false;
		
		while(start <= end) {
			m = start + (end - start)/2;
			Node node = nodes.get(m);
			
			if(node.cost + node.h1 == f) {
				// get m 
				found = true;
				break;
			}
			
			if(node.cost + node.h1 < f) {
				start = m + 1;
			}
			
			if(node.cost + node.h1 > f) {
				end = m - 1;
			}
			
		}
		
		if (found) {
			for (int i = m; i < nodes.size(); i++) {
				if (nodes.get(i).cost + nodes.get(i).h1 > f) {
					return i;
				}
			}
			return m;
		} else {
			for (int i = m; i >= 0; i--) {
				if (nodes.get(i).cost + nodes.get(i).h1 < f) {
					return i;
				}
			}
			return m;
		}
	}


	public boolean checkRepeatedStates(State currentState) {
		String currentKey = currentState.xPosition + "," + currentState.yPosition + "," + currentState.carried;
		// String currentKey = currentState.xPosition + "," + currentState.yPosition +
		// "," + currentState.carried + "," + currentState.damageNeo + "," +
		// currentState.kills + "," + currentState.deaths;
		ArrayList<ArrayList<Member>> currentMemberList = this.traversedStates.get(currentKey);
		if (currentMemberList == null || currentMemberList.size() == 0)
			return false;

		List<Member> combined = new ArrayList<Member>();
		combined.addAll(currentState.Agents);
		combined.addAll(currentState.remainingPills);
		combined.addAll(currentState.remainingHostages);
		// combined.add(currentState.damageNeo );
		// combined.addAll(currentState.remainingHostages);
		// combined.addAll(currentState.remainingHostages);

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

	private static void VisualizeSolution(String[] plan, int nx, int ny, int tbx, int tby,
			ArrayList<Hostages> remainingHostages, ArrayList<Agents> Agents, ArrayList<Pill> remainingPills,
			ArrayList<Pad> Pads) {
		JFrame frame = new JFrame("Visualize Solution");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(m, n));
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
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
		// System.out.println(remainingHostages.get(1).x);
		for (int i = 0; i < remainingHostages.size(); i++) {
//			System.out.println("hoo"+i);
//		    System.out.println(remainingHostages.get(i).x);
//		    System.out.println(remainingHostages.get(i).y);
//		    System.out.println(remainingHostages.get(i).damage);
			ButtonGrid[remainingHostages.get(i).x][remainingHostages.get(i).y]
					.setText("Hostage" + remainingHostages.get(i).damage);
		}
		for (int i = 0; i < Agents.size(); i++) {
			ButtonGrid[Agents.get(i).x][Agents.get(i).y].setText("Agent" + Agents.get(i).mutated);
		}
		for (int i = 0; i < Pads.size(); i++) {
			ButtonGrid[Pads.get(i).x][Pads.get(i).y].setText("Pads :" + Pads.get(i).destX + ',' + Pads.get(i).destY);
		}
		for (int i = 0; i < remainingPills.size(); i++) {
			ButtonGrid[remainingPills.get(i).x][remainingPills.get(i).y].setText("Pills");
		}

		ButtonGrid[tbx][tby].setText("Telephone Booth");

		for (int i = 0; i < plan.length; i++) {
			ButtonGrid[nx][ny].setBackground(Color.white);
			ButtonGrid[nx][ny].setText(ButtonGrid[nx][ny].getText().replace("Neo", ""));
			if (plan[i].equals("right") && ny < n - 1)
				ny += 1;
			if (plan[i].equals("left") && ny > 0)
				ny -= 1;
			if (plan[i].equals("up") && nx > 0) {
				nx -= 1;
			}
			if (plan[i].equals("down") && nx < m - 1)
				nx += 1;
			if (plan[i].equals("takepill"))
				ButtonGrid[nx][ny].setText(ButtonGrid[nx][ny].getText().replace("Pills", ""));
			if (plan[i].equals("carry"))
				ButtonGrid[nx][ny].setText(ButtonGrid[nx][ny].getText().replace("Hostage", ""));
			if (plan[i].equals("kill")) {
				if (nx + 1 >= 0 && nx + 1 < m) {
					ButtonGrid[nx + 1][ny].setText(ButtonGrid[nx + 1][ny].getText().replace("Agent", ""));
				}
				if (nx - 1 >= 0 && nx + 1 < m) {
					ButtonGrid[nx - 1][ny].setText(ButtonGrid[nx - 1][ny].getText().replace("Agent", ""));
				}

				if (ny + 1 >= 0 && ny + 1 < n) {
					ButtonGrid[nx][ny + 1].setText(ButtonGrid[nx][ny + 1].getText().replace("Agent", ""));
				}
				if (ny - 1 >= 0 && ny - 1 < n) {
					ButtonGrid[nx][ny - 1].setText(ButtonGrid[nx][ny - 1].getText().replace("Agent", ""));
				}

			}
			if (plan[i].equals("fly")) {
				// Pads:x,y
				// x,y
				String[] t = ButtonGrid[nx][ny].getText().split(":");
				String[] loc = t[1].split(",");
				System.out.println(loc[1]);
				nx = Integer.parseInt(loc[0]);
				ny = Integer.parseInt(loc[1].charAt(0)+"");

			}

			ButtonGrid[nx][ny].setBackground(Color.green);
			ButtonGrid[nx][ny].setText(ButtonGrid[nx][ny].getText() + " Neo");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public int getHeuristicManhatten(Node node) {
		State current = node.currentState;

		boolean goal = this.goalTest(current);
		if (goal) {
			return 0;
		}
		int killsNum=0;

		int distance = 0;
		int maxDamage=-11;
		int tbDistance = 0;
		int minDistance = 10000;
		boolean foundHighDamageHost=false;
		int[] hosatgesCost = new int[current.remainingHostages.size()];
		int[] mutatedHostagesCost = new int[current.Agents.size()];
	
		for (int i = 0; i < current.remainingHostages.size(); i++) {
			distance = Math.abs((current.xPosition - (current.remainingHostages.get(i).x)))
					+ Math.abs((current.yPosition - (current.remainingHostages.get(i).y)));
			tbDistance =  Math.abs((tx - (current.remainingHostages.get(i).x)))
					+ Math.abs((ty - (current.remainingHostages.get(i).y)));
			hosatgesCost[i] = distance + tbDistance;
			Hostages temp=current.remainingHostages.get(i);
			
			if(temp.damage>maxDamage && temp.damage+distance<100) {
				maxDamage=temp.damage;
				minDistance=hosatgesCost[i];
				foundHighDamageHost=true;
				
			}

		}
		if(foundHighDamageHost) {
			return minDistance;
		}
		distance=0;
		if(distance==0) {
			
		for (int i = 0; i < current.remainingHostages.size(); i++) {
			distance = Math.abs((current.xPosition - (current.remainingHostages.get(i).x)))
					+ Math.abs((current.yPosition - (current.remainingHostages.get(i).y)));
			hosatgesCost[i] = distance ;
			Hostages temp=current.remainingHostages.get(i);

			if (current.remainingHostages.get(i).damage+ distance >=100 ) {
				killsNum+=1;}
			//mutated
			if (current.remainingHostages.get(i).damage+ distance >=100 && hosatgesCost[i] < minDistance ) {
				//killsNum+=1;
				minDistance = hosatgesCost[i] ;
			}
		}

			
			for (int j = 0; j < current.Agents.size(); j++) {
				//System.out.println(current.Agents.get(j).x+"helllpppp"+j);

				if (current.Agents.get(j).mutated) {

					//System.out.println(current.Agents.get(j).x+"is mutated "+j);
					killsNum+=1;

					distance = Math.abs((current.xPosition - (current.Agents.get(j).x)))
							+ Math.abs((current.yPosition - (current.Agents.get(j).y)));
					
				
					mutatedHostagesCost[j] = distance;
					if (mutatedHostagesCost[j] < minDistance) {
						minDistance = mutatedHostagesCost[j];
					}
				}

			}

			return  minDistance +killsNum*20000;
		}else {
			return  Math.abs((tx - (current.xPosition)))+ Math.abs((ty - (current.yPosition))) +1;
		}


		}

//	public int getHeuristicManhatten(Node node) {
//		State current = node.currentState;
//
//		boolean goal = this.goalTest(current);
//		if (goal) {
//			return 0;
//		}
//
//		int distance = 0;
//		int tbDistance = 0;
//		int minDistance = 10000;
//		int[] hosatgesCost = new int[current.remainingHostages.size()];
//		int[] mutatedHostagesCost = new int[current.Agents.size()];
//		for (int i = 0; i < current.remainingHostages.size(); i++) {
//			distance = Math.abs((current.xPosition - (current.remainingHostages.get(i).x)))
//					+ Math.abs((current.yPosition - (current.remainingHostages.get(i).y)));
//			tbDistance =  Math.abs((tx - (current.remainingHostages.get(i).x)))
//					+ Math.abs((ty - (current.remainingHostages.get(i).y)));
//			hosatgesCost[i] = distance + tbDistance;
//			
//			if (current.remainingHostages.get(i).damage+ hosatgesCost[i]<100) {
//				minDistance = hosatgesCost[i];
//			}
////		
////			if (hosatgesCost[i] < minDistance) {
////				minDistance = hosatgesCost[i];
////			}
//		}
//
//		if (distance == 0) {
//			
//			for (int j = 0; j < current.Agents.size(); j++) {
//				//System.out.println(current.Agents.get(j).x+"helllpppp"+j);
//
//				if (current.Agents.get(j).mutated) {
//
//					System.out.println(current.Agents.get(j).x+"is mutated "+j);
//
//					distance = Math.abs((current.xPosition - (current.Agents.get(j).x)))
//							+ Math.abs((current.yPosition - (current.Agents.get(j).y)));
//					tbDistance =  Math.abs((tx - (current.Agents.get(j).x)))
//							+ Math.abs((ty - (current.Agents.get(j).y)));
//					
//					mutatedHostagesCost[j] = distance + tbDistance;
//					if (mutatedHostagesCost[j] < minDistance) {
//						minDistance = mutatedHostagesCost[j];
//					}
//				}
//
//			}
//
//			return  minDistance * current.numberOfMutatedAgents ;
//		}
//
//		return minDistance * current.remainingHostages.size() ;
//		//return 400000*current.deaths+20000*current.kills;
//		}

	public static String solve(String grid, String strategy, boolean visualize) {

		Matrix m1 = new Matrix();
		numberOfNodesExpanded = 0;
		String gridValues[] = grid.split(";");

		for (int k = 0; k < gridValues.length; k++) {
			System.out.println(gridValues[k]);
		}

		m1.m = Integer.parseInt(gridValues[0].split(",")[0]);
		m1.n = Integer.parseInt(gridValues[0].split(",")[1]);

		m1.c = Integer.parseInt(gridValues[1].split(",")[0]);

		m1.nx = Integer.parseInt(gridValues[2].split(",")[0]);

		m1.ny = Integer.parseInt(gridValues[2].split(",")[1]);

		m1.tx = Integer.parseInt(gridValues[3].split(",")[0]);
		m1.ty = Integer.parseInt(gridValues[3].split(",")[1]);

		String agentsList[] = gridValues[4].split(",");
		String pillsList[] = gridValues[5].split(",");
		String padsList[] = gridValues[6].split(",");
		String hostagesList[] = gridValues[7].split(",");
//		String membersHealth[]=gridValues[4].split(",");

		m1.members = new Member[(agentsList.length) / 2 + (pillsList.length) / 2 + (padsList.length) / 4
				+ (hostagesList.length) / 3];

		ArrayList<Hostages> remainingHostages = new ArrayList<Hostages>();
		ArrayList<Hostages> carriedHostages = new ArrayList<Hostages>();
		ArrayList<Agents> Agents = new ArrayList<Agents>();
		ArrayList<Pill> remainingPills = new ArrayList<Pill>();
		ArrayList<Pad> Pads = new ArrayList<Pad>();

		for (int i = 0; i < (agentsList.length) / 2; i++) {

			Agents agent = new Agents(m, n);
			agent.x = Integer.parseInt(agentsList[2 * i]);
			agent.y = Integer.parseInt(agentsList[2 * i + 1]);

			Agents.add(agent);

		}

		for (int i = 0; i < (pillsList.length) / 2; i++) {

			Pill pill = new Pill(m, n);
			pill.x = Integer.parseInt(pillsList[2 * i]);
			pill.y = Integer.parseInt(pillsList[2 * i + 1]);
			remainingPills.add(pill);

		}

		for (int i = 0; i < (padsList.length) / 8; i++) {

			Pad pad1 = new Pad(m, n);
			Pad pad2 = new Pad(m, n);
			pad1.x = Integer.parseInt(padsList[8 * i]);
			pad1.y = Integer.parseInt(padsList[8 * i + 1]);
			pad2.x = Integer.parseInt(padsList[8 * i + 2]);
			pad2.y = Integer.parseInt(padsList[8 * i + 3]);

			pad2.destX = pad1.x;
			pad2.destY = pad1.y;
			pad1.destX = pad2.x;
			pad1.destY = pad2.y;
			Pads.add(pad1);
			Pads.add(pad2);

		}

		for (int i = 0; i < (hostagesList.length) / 3; i++) {

			Hostages hostage = new Hostages(m, n);
			hostage.x = Integer.parseInt(hostagesList[3 * i]);
			hostage.y = Integer.parseInt(hostagesList[3 * i + 1]);
			hostage.damage = Integer.parseInt(hostagesList[3 * i + 2]);
//			System.out.println("gggg"+hostage.x);
//			System.out.println("gggg"+hostage.y);
//			System.out.println("gggg"+hostage.damage);
			remainingHostages.add(hostage);

		}

		// WE DIDN'T ADD THINGS TO MEMBERS

		// m1.members=membersHealth.length;
		// Member[] newTeam=new Member[m1.members];
//		for(int i =0;i<membersHealth.length;i++)
//		{
//			Member member = new Member(Integer.parseInt(membersList[2*i]),
//					Integer.parseInt(membersList[2*i+1]),
//					Integer.parseInt(membersHealth[i]));
//			newTeam[i]=member;
//		}
//		Team=newTeam;

		QingFunction qing_func = null;

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
		case "GR1":
			qing_func = QingFunction.GR1;
			break;
//		case "GR2":
//			qing_func = QingFunction.GR2;
//			break;
		case "AS1":
			qing_func = QingFunction.AS1;
			break;
//		case "AS2":
//			qing_func = QingFunction.AS2;
//			break;
		}

		// ArrayList <Member> initialTeam = new ArrayList<Member>();
//		for(int i = 0; i < Team.length; i++) {
//			initialTeam.add(Team[i]);
//		}

//		State initialState = new State(nx, n, c, initialTeam);
//		m1.initialState=initialState;

		initialState = new State(nx, ny, c, 0, remainingHostages, carriedHostages, Agents, remainingPills, Pads, 0, 0,
				0, 0);

		Node result = m1.generalSearch(qing_func);
		// System.out.println("hosttt" + remainingHostages.get(0).damage);
		// int totalDeath = result.cost/1000000;

		// Missing Visualize concept ..
		String plan = "";

		// delete me///////////////////////////////////////////
		Node n = new Node(initialState, null, null, 0, 0, -1, -1);
		n.h1 = m1.getHeuristicManhatten(n);
	//	n.h2 = m1.getHeuristicEuclidean(n);

		System.out.println("result cost ===> " +  result.cost);
		System.out.println("Heuristic of result===>" + result.h1 );
		System.out.println("heurist from node 1 ====> " + n.h1);
	//	System.out.println("heurist from node 2 ====> " + n.h2);
		///////////////////////////////////////////////////////

		if (result == null) {
			System.out.println("No Solution");
			return "No Solution";
		}

		int rakmelDeaths = result.currentState.deaths;
		int rakmelkills = result.currentState.kills;

//		if(result== null) {
//			System.out.println("No Solution");	
//			return "No Solution";
//		}

		while (result != null) {
			if (result.operator != null) {
				if (("" + result.operator).equals("TAKEPILL")) {
					plan = "," + ("takePill") + plan;
				} else {
					plan = "," + ("" + result.operator).toLowerCase() + plan;
				}
			}


			result = result.parent;

		}

		plan = plan.substring(1) + ";";
		plan += "" + rakmelDeaths + ";";
		plan += "" + rakmelkills + ";";

		plan += "" + numberOfNodesExpanded;
		if (visualize)
			VisualizeSolution(plan.split(";")[0].split(","), nx, ny, tx, ty, remainingHostages, Agents, remainingPills,
					Pads);
		System.out.println(plan);
		return plan;
	}

	public static void main(String[] args) {

		// check grid0 steps
		// time bta3 el tawela bel hash map
		// deaths w numberofmutants Fixed
		// genGrid problemooo Fixed
		// plan lowercase/uppercase plan Fixed
//	Matrix m=new Matrix();

		String grid0 = "5,5;2;3,4;1,2;0,3,1,4;2,3;4,4,0,2,0,2,4,4;2,2,91,2,4,62";
		String grid1 = "5,5;1;1,4;1,0;0,4;0,0,2,2;3,4,4,2,4,2,3,4;0,2,32,0,1,38";
		String grid2 = "5,5;2;3,2;0,1;4,1;0,3;1,2,4,2,4,2,1,2,0,4,3,0,3,0,0,4;1,1,77,3,4,34";
		String grid3 = "5,5;1;0,4;4,4;0,3,1,4,2,1,3,0,4,1;4,0;2,4,3,4,3,4,2,4;0,2,98,1,2,98,2,2,98,3,2,98,4,2,98,2,0,1";
		String grid4 = "5,5;1;0,4;4,4;0,3,1,4,2,1,3,0,4,1;4,0;2,4,3,4,3,4,2,4;0,2,98,1,2,98,2,2,98,3,2,98,4,2,98,2,0,98,1,0,98";
		String grid5 = "5,5;2;0,4;3,4;3,1,1,1;2,3;3,0,0,1,0,1,3,0;4,2,54,4,0,85,1,0,43";
		String grid6 = "5,5;2;3,0;4,3;2,1,2,2,3,1,0,0,1,1,4,2,3,3,1,3,0,1;2,4,3,2,3,4,0,4;4,4,4,0,4,0,4,4;1,4,57,2,0,46";
		String grid7 = "5,5;3;1,3;4,0;0,1,3,2,4,3,2,4,0,4;3,4,3,0,4,2;1,4,1,2,1,2,1,4,0,3,1,0,1,0,0,3;4,4,45,3,3,12,0,2,88";
		String grid8 = "5,5;2;4,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,3,3,3,3,4,4;4,0,17,1,2,54,0,0,46,4,1,22";
		String grid9 = "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
		String grid10 = "5,5;4;1,1;4,1;2,4,0,4,3,2,3,0,4,2,0,1,1,3,2,1;4,0,4,4,1,0;2,0,0,2,0,2,2,0;0,0,62,4,3,45,3,3,39,2,3,40";

//	//String grid12= "5,5;2;0,4;1,4;0,1,1,1,2,1,3,1,3,3,3,4;1,0,2,4;0,3,4,3,4,3,0,3;0,0,30,3,0,80,4,4,80";
//
		solve(grid1, "AS1", false);
		solve(grid1, "UC", false);
		// updateddd

		// double usageCPU = osBean.getProcessCpuLoad();
		// System.out.println("CPU LOAD: " + usageCPU);

//	java.lang.management.ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
//	 
//	for(Long threadID : threadMXBean.getAllThreadIds()) {
//	    ThreadInfo info = threadMXBean.getThreadInfo(threadID);
//	    System.out.println("Thread name: " + info.getThreadName());
//	    System.out.println("Thread State: " + info.getThreadState());
//	    System.out.println(String.format("CPU time: %s ns", 
//	      threadMXBean.getThreadCpuTime(threadID)));
//	  }

	}

}