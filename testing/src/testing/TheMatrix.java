package testing;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

public class TheMatrix extends SearchProblem {
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
	public TheMatrix() {
		super();
		genGrid();
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
		for (i = 0; i < no_hosatges; i++) {
			Hostages x = new Hostages(m, n);
			
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

		if (operator.compareTo(Operator.UP) == 0) {

			boolean found = false;
			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition - 1 && temp.y == currentState.yPosition)) {
					found = true;
					index = i;
					break;
				}
			}

			if (!found) {

				currentState.xPosition = (currentState.xPosition == 0) ? 0 : currentState.xPosition - 1;

			}

		} else if (operator.compareTo(Operator.DOWN) == 0) {

			boolean found = false;
			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition + 1 && temp.y == currentState.yPosition)) {
					found = true;
					index = i;
					break;
				}
			}

			if (!found) {
				currentState.xPosition = (currentState.xPosition == TheMatrix.m - 1) ? TheMatrix.m - 1
						: currentState.xPosition + 1;

			}

		} else if (operator.compareTo(Operator.RIGHT) == 0) {
			boolean found = false;
			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition && temp.y == currentState.yPosition + 1)) {
					found = true;
					index = i;
					break;
				}
			}

			if (!found) {
				currentState.yPosition = (currentState.yPosition == TheMatrix.n - 1) ? TheMatrix.n - 1
						: currentState.yPosition + 1;

			}

		} else if (operator.compareTo(Operator.LEFT) == 0) {

			boolean found = false;
			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x == currentState.xPosition && temp.y == currentState.yPosition - 1)) {
					found = true;
					index = i;
					break;
				}
			}

			if (!found) {
				currentState.yPosition = (currentState.yPosition == 0) ? 0 : currentState.yPosition - 1;

			}

		} else if (operator.compareTo(Operator.CARRY) == 0) {
			if (currentState.carried > 0) {
				boolean found = false;
				int index = 0;
				for (int i = 0; i < currentState.remainingHostages.size(); i++) {
					Member temp = currentState.remainingHostages.get(i);
					if (temp.x == currentState.xPosition && temp.y == currentState.yPosition) {
						found = true;
						index = i;
						break;
					}
				}

				if (found) {
					currentState.carried--;
					currentState.carriedHostages.add(currentState.remainingHostages.remove(index));

				}
			}

		} else if (operator.compareTo(Operator.DROP) == 0 && currentState.xPosition == TheMatrix.tx
				&& currentState.yPosition == TheMatrix.ty && currentState.carried < TheMatrix.c) {

			for (int i = 0; i < currentState.carriedHostages.size(); i++) {
				Hostages temp = currentState.carriedHostages.get(i);
				if (temp.damage >= 100) {
					deaths += 1;
				}
			}
			currentState.carried = TheMatrix.c;
			currentState.carriedHostages.clear();
		} else if (operator.compareTo(Operator.KILL) == 0) {
			// if(currentState.damageNeo > 80) {//do i need to do this check ?
			boolean found = false;
			int index = 0;
			for (int i = 0; i < currentState.Agents.size(); i++) {
				Agents temp = currentState.Agents.get(i);
				if ((temp.x - 1 == currentState.xPosition && temp.y == currentState.yPosition)
						|| (temp.x + 1 == currentState.xPosition && temp.y == currentState.yPosition)
						|| (temp.x == currentState.xPosition && temp.y - 1 == currentState.yPosition)
						|| (temp.x == currentState.xPosition && temp.y + 1 == currentState.yPosition)) {
					found = true;
					index = i;
					break;
				}
			}

			if (found) {
				currentState.damageNeo += 20;
				// check if nemo's dead wala avoided fo2??
				// if 100 dead ? Game Over
				currentState.Agents.remove(index);
				kills += 1;

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

		Node root = new Node(problem.initialState, null, null, 0, 0, -1, -1);
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
		// solution not yet finalized in case of failure
		return null;
	}

	public State increaseDamageRemaining(State CurrentState) {
		// SANDY
		// what if my health reaches 100 do i convert him here
		// int =0 means it was carried so if it died msh h3mel haga
		// however if int =1 convert to Agent??

		// Remaining Hostages
		for (int i = 0; i < CurrentState.remainingHostages.size(); i++) {
			Hostages temp = CurrentState.remainingHostages.get(i);
			if (temp.damage < 100) {
				temp.damage += 2;
				CurrentState.remainingHostages.set(i, temp);
			}
			if (temp.damage >= 100) {
				deaths += 1;
				Hostages converted = CurrentState.remainingHostages.remove(i);
				Agents newAgent = new Agents(converted.x, converted.y);
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

	public boolean goalTest(State currentState) {
		if (currentState.remainingHostages.size() == 0 && currentState.carried == c
				&& currentState.carriedHostages.size() == 0) {
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

		numberOfNodesExpanded += 1;
		int j = 0;
		for (int i = 0; i < problem.setOperators.length; i++) {
			State childState = stateSpace(problem.setOperators[i], parent.currentState);

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
	
	
	
	
//	private static void VisualizeSolution(String [] plan,Member[] team,int nx,int ny,int tbx,int tby) {
//	
//		JFrame frame = new JFrame("Visualize Solution");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setLayout(new GridLayout(m,n));
//		frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
//        frame.pack();
//        frame.setVisible(true);
//		JButton[][] ButtonGrid = new JButton[m][n];
//        for (int y = 0; y < n; y++) {
//            for (int x = 0; x < m; x++) {
//            	ButtonGrid[x][y] = new JButton();
//            	ButtonGrid[x][y].setEnabled(false);
//                frame.add(ButtonGrid[x][y]);
//            }
// 
//        }
//        ButtonGrid[nx][ny].setText("Initial Position");
//        
//		for(int i =0;i<Team.length;i++)
//		{
//			ButtonGrid[Team[i].x][Team[i].y].setText("Member "+Team[i].damage);
//		}
//		ButtonGrid[submarineX][submarineY].setText("Submarine");
//		
//		for(int i =0;i<plan.length;i++)
//		{
//			ButtonGrid[ex][ey].setBackground(Color.white);
//			ButtonGrid[ex][ey].setText(ButtonGrid[ex][ey].getText().replace(" Ethan", ""));
//			if(plan[i].equals("right"))
//				ey+=1;
//			if(plan[i].equals("left"))
//				ey-=1;
//			if(plan[i].equals("up"))
//				ex-=1;
//			if(plan[i].equals("down"))
//				ex+=1;
//			if(plan[i].equals("carry"))
//				ButtonGrid[ex][ey].setText(ButtonGrid[ex][ey].getText().replace("Member", ""));
//			ButtonGrid[ex][ey].setBackground(Color.green);
//			ButtonGrid[ex][ey].setText(ButtonGrid[ex][ey].getText()+" Ethan");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		
//	}
	
public static String solve(String grid, String strategy, boolean visualize) {
		
		TheMatrix m1 = new TheMatrix();
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
			hostage.x = Integer.parseInt(hostagesList[2*i]);
			hostage.y = Integer.parseInt(hostagesList[2*i+1]);
			hostage.damage = Integer.parseInt(hostagesList[2*i+2]);
			
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
			VisualizeSolution(plan.split(";")[0].split(","),Team,nx,ny,tx,ty);
		return plan;
	}


	
	
}
