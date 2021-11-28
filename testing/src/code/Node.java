package code;

import java.util.Comparator;

public class Node implements Comparable<Node> {

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

	// REPEATED STATES SHOULD BE HANDLED HEREEEEEEEE!!!!!!!!!!!
	public Node(State currentState, Operator operator, Node parent, int depth, int cost, int h1, double h2) {
		this.currentState = currentState;
		this.operator = operator;
		this.parent = parent;
		this.depth = depth;
		this.cost = cost;
		this.h1 = h1;
		this.h2 = h2;
	}

	@Override
	public int compareTo(Node node2) {
		// TODO Auto-generated method stub
		if (this.currentState.rescuedHostages == node2.currentState.rescuedHostages) {
			if (this.currentState.deaths == node2.currentState.deaths)
				if (this.currentState.kills == node2.currentState.kills)
					return 0;

		}
		if (this.currentState.rescuedHostages == node2.currentState.rescuedHostages) {
			if (this.currentState.deaths == node2.currentState.deaths)
				if (this.currentState.kills > node2.currentState.kills) {
					return 1;
				} else {
					return -1;
				}
		}
		if (this.currentState.rescuedHostages == node2.currentState.rescuedHostages) {
			if (this.currentState.deaths > node2.currentState.deaths) {
				return 1;
			}
			return -1;

		}

		if (this.currentState.rescuedHostages > node2.currentState.rescuedHostages) {
			return 1;
		}
		return 0;

	}

}