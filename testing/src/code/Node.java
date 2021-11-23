package code;

public class Node {

	// 1-state : Access Team from class Search
	public State currentState;

	// 2-parent Node
	public Node parent;

	// 3-Operator
	public Operator operator;

	// 4-Depth
	public int depth = 0;

	// 5-Path cost
	public int cost = 0;
	
	// heuristic ... 
	public int h1 = 0;
	public double h2 = 0;
	
	//REPEATED STATES SHOULD BE HANDLED HEREEEEEEEE!!!!!!!!!!!
	public Node(State currentState, Operator operator, Node parent, int depth, int cost, int h1, double h2) {
		this.currentState = currentState;
		this.operator = operator;
		this.parent = parent;
		this.depth = depth;
		this.cost = cost;
		this.h1 = h1;
		this.h2 = h2;
	}

}