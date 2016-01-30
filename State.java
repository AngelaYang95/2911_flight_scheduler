import java.util.LinkedList;


public class State implements Comparable<State>{
	private Node location;
	private LinkedList<Flight> pathSoFar;
	private int costSoFar;
	private int hCost;
	
	
	public State(Node cityNode, LinkedList<Flight>path, int minutes, int h) {
		location = cityNode;
		pathSoFar = path;
		costSoFar = minutes;
		hCost = h;
	}
	
	/**
	 * Gives number of flights which are covered in the path so far
	 * If at initial state, it'll return zero
	 * @param Linked List of required flights
	 * @return integer
	 */
	public int numFlightsCovered (LinkedList<Flight> flights) {
		LinkedList<Flight>clone = this.getPathSoFar();
		
		int numFound = 0;
		Flight path;
		Flight[] pathSoFar;
		
		if(clone != null) {
			pathSoFar = new Flight[clone.size()];
			clone.toArray(pathSoFar);
			
			for(Flight required: flights) {
				for(int i = 0; i < clone.size(); i++) {
					path = pathSoFar[i];
					if(path != null && required.sameFlight(path)) {
						pathSoFar[i] = null;
						numFound++; 
						break;
					}
				}
			}
		}
		return numFound;
	}
	
	/**
	 * Finds flights which still need to be found.
	 * Compliment of the intersection of flight in path so far 
	 * and required flights.
	 * @param Linked List of the required flights
	 * @return Linked list of required Flights that
	 * aren't yet covered by the path so far
	 * 
	 */
	public LinkedList<Flight> coveredFlights(LinkedList<Flight> flights) {
		LinkedList<Flight>clone = this.getPathSoFar();
		Flight path;
		Flight[] pathSoFar;
		LinkedList<Flight> toReturn = new LinkedList<Flight>();
		
		if(clone != null) {
			pathSoFar = new Flight[clone.size()];
			clone.toArray(pathSoFar);
			
			for(Flight required: flights) {
				for(int i = 0; i < clone.size(); i++) {
					path = pathSoFar[i];
					if(path != null && required.sameFlight(path)) {
						toReturn.add(path);
						pathSoFar[i] = null;
						
						break;
					}
				}
			}
			
			return toReturn;
		}
		return null;
	}
	
	public Flight getLast() {
		return pathSoFar.getLast();
	}
	
	/**
	 * Gives number of Flights in path
	 * @precondition not to be called when at initial state
	 * @return integer 
	 */
	public int numEdge() {
		return pathSoFar.size();
	}
	
	/**
	 * Updates the costSoFar to add the delay time at the 
	 * current airport
	 * @param integer minutes in delay
	 */
	public void addDelayTime(int minutes) {
		costSoFar += minutes;
	}
	
	/**
	 * Gives a list of Flights containing the current
	 * location and its immediate neighbours
	 * @return LinkedList of Flights 
	 */
	public LinkedList<Flight> childEdges() {
		return location.getChildren();
	}
	
	/**
	 * Gives clone of path so far. No reference to 
	 * original list. Gives NULL if at initial state
	 * @return a deep copy of the path so far
	 */
	public LinkedList<Flight> getPathSoFar() {
		
		if(pathSoFar == null) {
			return null;
		}
		LinkedList<Flight> pathClone = new LinkedList<Flight>();
		
		for(Flight element: pathSoFar) {
			
			pathClone.add(element);
		}
		return pathClone;
	}
	
	@Override
	public int compareTo(State toCompare) {
		
		return (this.calcF() - toCompare.calcF());
	}
	
	private int calcF() {
		return hCost + costSoFar;
	}
	
	/**
	 * Gives the Heuristic of this state
	 * @return int
	 */
	public int getHeur() {
		return hCost;
	}
	
	/**
	 * Gives the cost of the path so far including delay times
	 * @return int
	 */
	public int getCostSoFar() {
		return costSoFar;
	}
	
	/**
	 * Set the heuristic of the state
	 * @param integer for heuristic value 
	 */
	public void updateH(int cost) {
		hCost = cost;
	}
	
	/**
	 * Gets reference to location Node
	 * @return Node
	 */
	public Node getLocation() {
		return location;
	}
	
	/**
	 * Gives string representation of the current city
	 * @return String
	 */
	public String city() {
		return location.getName();
	}
	
	//for testing
	public void showState() {
		System.out.println("location is: " + location.getName());
		System.out.println("cost so far is: " + costSoFar);
		System.out.println("heuristic is: " + hCost);
		
		if(pathSoFar != null) {
			for(Flight current: pathSoFar) {
				System.out.print("[" + current.getFrom().getName() + "-");
				System.out.println(current.getTo().getName() + "]");
			}	
		}

	}
}