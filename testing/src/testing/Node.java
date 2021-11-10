package testing;

public class Node {

	// 1-state : Access Team from class Search
	State currentState;

	// 2-parent Node
	Node parent;

	// 3-Operator
	Operator operator;

	// 4-Depth
	int depth = 0;

	// 5-Path cost
	int cost = 0;
	
	//REPEATED STATES SHOULD BE HANDLED HEREEEEEEEE!!!!!!!!!!!
	public Node(State currentState, Operator operator, Node parent, int depth, int cost) {
		this.currentState = currentState;
		this.operator = operator;
		this.parent = parent;
		this.depth = depth+1;
		this.cost = cost;
	

	}}