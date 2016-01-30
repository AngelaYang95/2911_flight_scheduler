import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * Analysis of Heuristic
 * The heuristic I am using is based off the absolute minimum cost
 * of the flight path (excluding the delay times). For every state
 * if the last location to the current location is a flight, the heuristic is updated.
 * If at goal state it returns 0.
 * Hence the heuristic is admissible as it will never overestimate.
 * 
 * The performance of the heuristic is dramatically faster than the zero heuristic on 
 * almost all scenarios (if not than equal to). It very evidently outperforms the zero 
 * heuristic on inputs above 5 flights.
 * It does slow around the 8 flight mark but sometimes can be quick on larger requirements
 * if the flights are chained together nicely. Thus some 13 Flight input will be
 * solved quicker than an 8 Flight one.
 * The slow performance and memory consumption is mostly due to 
 * the implementation of the states.
 * 
 * For 1 flight input it took about 0m0.140s under the zero and HeuristicA. Both expanding
 * 6 nodes and there being no notable difference. 
 * Under a 4 flight input such as the given sample; the zero heuristic initially
 * took 0m0.252s expanding 1149 nodes, the zero heuristic on a visited list bettered to 
 * 0m0.172s(131 nodes) and finally with heuristicA it expanded 54 nodes in the same time.
 * It was solved under 1M.
 * 
 * Under a 6 flight input, 1931 nodes were expanded but found within 2s. It did fine 
 * 4M but struggled under 2M. 
 * Under a 7 flight input and heuristicA 1110 nodes were expanded and found within 12s whereas
 * 61,791 nodes were expanded with the zero heuristic.
 * 
 * Under an 8 flight input where the flights require a lot of back and forth
 * the performance is drastically slowed down, however when comparing to the zero 
 * heuristic it is far quicker. The zero heuristic extends beyond 9 mins expanding
 * more than 1 million nodes whereas HeursticA took about 45s expanding 9982.
 * While HeuristicA runs fine under 8M, the program struggles
 * with complex 8 flight inputs under 4M. 
 * 
 * The A*star search in general WITH the visited list takes up a 
 * lot of performance time in solving whether it has been visited. 
 * The visited list does however save memory space. Without the visited List the program
 * run out of memory when performed on a complex 8 Flight input within 8MB. Nevertheless
 * memory consumption is still quite poor whereby for every 2 flights added 
 * the amount of memory required almost doubles. In regards to time, it quadrupled
 * for each flight added - O(n^2) making performance slow on large input, but often
 * not before running out of memory.
 * Being more memory efficient has come to prove really important, and it seems
 * my states store a few too many instances of other states making it 
 * difficult to handle longer paths.  
 * @author angelayang
 *
 */
public class FlightScheduler {
	private LinkedList<Flight> requiredFlights;
	private Graph map;
	private int numExpansions;
	
	
	public FlightScheduler (){
		requiredFlights = new LinkedList<Flight>();
		map = new Graph();
		numExpansions = 0;
	}
	
	public static void main(String[] args) {	
		FlightScheduler fs = new FlightScheduler();
		
		fs.readInputFile(args[0]);
		//fs.showFlights();
		if(fs.flightsNeeded()) {
			IStrategy calcH = new HeuristicA(fs.getFlights());  // change Heuristic here
			State goal = fs.aStarSearch(calcH);
			
			System.out.println(fs.getExp() + " nodes expanded");
			System.out.println("cost = " + goal.getCostSoFar());
			fs.showRoute(goal);
			//fs.showFlights();
		}
	}
	
	/**
	 * Access to the list of flights required
	 * @return Linked List of Flights
	 */
	private LinkedList<Flight>getFlights() {
		return requiredFlights;
	}
	
	/**
	 * Prints the goal state found in correct format
	 * @param optimum goal state 
	 */
	private void showRoute(State goal) {
		LinkedList<Flight> route = goal.getPathSoFar();
		for(Flight edge: route) {
			System.out.print("Flight " + edge.getFrom().getName() + " to ");
			System.out.println(edge.getTo().getName());
		}
	}
	
	//TESTING
	public void showFlights() {
		for(Flight curr: requiredFlights) {
			System.out.print(curr.getFrom().getName()+ " to ");
			System.out.println(curr.getTo().getName());
		}
	}
	// TESTING
	public void showMap() {
		map.showMap();
	}
	
	/**
	 * Checks if there are flights requested for a
	 * given period of time. 
	 * @return true if flights have been requested. false 
	 * otherwise
	 */
	private boolean flightsNeeded() {
		return !requiredFlights.isEmpty();
	}
	
	/**
	 * Takes in filename and reads formatted input
	 * Acts accordingly
	 * @param String filename
	 */
	private void readInputFile(String filename) {
		
		Scanner sc = null;
		
		try {
			
			sc = new Scanner(new FileReader(filename));
		
			String firstToken = "";
			String name1;
			String name2;
			String time;
			int minutes;
		
			while(sc.hasNext()) { // next line
				firstToken = sc.next();
				
				if(firstToken.equals("City")) {
					name1 = sc.next();
					time = sc.next();
					minutes = Integer.parseInt(time);
					buildGraph(name1, minutes);
					
				} else if (firstToken.equals("Time")) {
					name1 = sc.next();
					name2 = sc.next();
					time = sc.next();
					minutes = Integer.parseInt(time);
					connectCity(name1,name2,minutes);
					
				} else if (firstToken.equals("Flight")) {
					name1 = sc.next();
					name2 = sc.next();
					addFlight(name1, name2);
				} 	
			}
		}
		catch (FileNotFoundException e) {}
	    finally
	    {
	        if (sc != null) sc.close();
	    }
	}
	
	
	/**
	 * Appends the corresponding Flight edge to the list of 
	 * required flights
	 * @param String name of city from which the flight departs
	 * @param String name of city from which the flight arrives
	 */
	private void addFlight(String fromCity, String toCity) {
	
		Flight flight = map.getEdge(fromCity, toCity);
		requiredFlights.add(flight);
	}
	
	/**
	 * Appends a city node to the graph linked list 
	 * @param String name of the city
	 * @param integer time in minutes of the delay at this city
	 */
	private void buildGraph(String name, int time) {
		Node city = new Node(name, time);
		map.addCity(city);
	}
	
	/**
	 * Adds a edge (Flight) between two city Nodes with the flight time 
	 * in minutes as a weight on that edge.
	 * @param String name of first city
	 * @param String name of second city
	 * @param integer time in minutes of flight time
	 */
	private void connectCity(String name1, String name2, int time) {
		map.connectCities(name1,name2,time);
	}
	
	/**
	 * A* search
	 * 
	 * Finds the optimal path which contains all
	 * the flights requested in the required Flights list
	 * @precondition be given valid Flights. 
	 * @param a heuristic strategy
	 * @return the Goal State
	 */
	private State aStarSearch(IStrategy calcH) {
		numExpansions = 0;
		
		int hCost = 0; 
		int oldH = 0;
		
		LinkedList<Flight> childList, pathToAdd;
		int costToAdd = 0;
		Node child;
		LinkedList<State> visited = new LinkedList<State>();
		State initial = buildState("Sydney", null, costToAdd, 0);
		State current = initial, temp;
		Queue<State> toVisit = new PriorityQueue<State>();
		toVisit.add(initial);
		
		// BEGIN SEARCH
		while(!isGoalState(current)) {	
			current = toVisit.poll();
			visited.add(current);
			numExpansions++;
			
			childList = current.childEdges(); 
			oldH = current.getHeur();
			
			for(Flight path: childList) {
					
					child = path.getTo();
					pathToAdd = current.getPathSoFar();  
					
					if(pathToAdd==null) {
						pathToAdd = new LinkedList<Flight>();
					} 
					
					// ADDS EDGE [PARENT->CHILD]
					pathToAdd.add(path);
					costToAdd = current.getCostSoFar() + path.getFlightTime(); 
					
					temp = new State(child, pathToAdd, costToAdd, oldH);
					
					//ASSUMING EARLIER PATH WILL ALWAYS BE CHEAPER
					if(!visited(visited, temp)) {	
						hCost = calcH.calcHCost(temp);
						
						temp.updateH(hCost);
						//temp.showState();
						//System.out.println();
						
						if(!isGoalState(temp)) {
							
							temp.addDelayTime(path.destinationDelay());	
						}
						
						toVisit.add(temp);
					} 
			}
			
		}
	
		return current; 
	}
	
	/**
	 * Checks if the polled State is the goal State
	 * @param State to be checked
	 * @return true if it covers all necessary flights. false otherwise
	 */
	private boolean isGoalState (State toCheck) {
		int numFlights = requiredFlights.size();
	
		if(toCheck.numFlightsCovered(requiredFlights) == numFlights) {
			return true;
		} 
		return false;
	}
	
	/**
	 * Creates a state from information given
	 * @param cityName
	 * @param path
	 * @param gCost
	 * @param hCost
	 * @return State off given information and the current city
	 */
	private State buildState(String cityName, LinkedList<Flight>path, int gCost, int hCost) {
		return map.createState(cityName, path, gCost, hCost);	
	}
	
	/**
	 * Gives number of nodes expanded in most recent A*search
	 * @return integer number of expanded nodes
	 */
	private int getExp() {
		return numExpansions;
	}
	
	/**
	 * Loops through the visited list and checks if the state to be checked
	 * is the sameState as any of the states already visited
	 * @param visited, a list of states already visited
	 * @param toCheck, a state possibly to be added to the queue
	 * @return true if the state already exists, false otherwise
	 */
	private boolean visited(LinkedList<State> visited, State toCheck) {
		for(State curr: visited) {
			if(sameState(curr, toCheck)) {		
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if two states are equivalent states. States are the same
	 * if they cover the same required flights and the current 
	 * locations are the same
	 * @param visitedState, a state that has already been expanded
	 * @param toCheckState, a state which needs to be compared
	 * @return true if they are the same state, false otherwise
	 */
	private boolean sameState(State visitedState, State toCheckState) {
		boolean same = false;
		String name1 = visitedState.getLocation().getName();
		String name2 = toCheckState.getLocation().getName();
		if(name1.equals(name2)) {
			LinkedList<Flight> toCheckCovered = toCheckState.coveredFlights(requiredFlights);
			LinkedList<Flight> visitedCovered = visitedState.coveredFlights(requiredFlights);
			
			if(visitedCovered != null && toCheckCovered != null) {
				
				if(toCheckCovered.containsAll(visitedCovered) && visitedCovered.containsAll(toCheckCovered)) {
					same = true;
				}
				
			}
		}
		return same;
	}
}
























