package code;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class SearchProblem {
	
	//1-set of operators
	public Operator [] setOperators;
	
	public HashMap<String,ArrayList<ArrayList<Member>>>traversedStates;
	
	//2-INITIAL STATE
	public State initialState;
	
	//3-state space
	public abstract State stateSpace(Operator operator, State parentState);
		
	//4-Goal test
	public abstract boolean goalTest(State currentState);
	
	//5- Path cost
	public abstract int pathCost(Node currentNode, Operator operator);
}
