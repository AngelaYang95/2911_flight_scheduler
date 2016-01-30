import java.util.LinkedList;

public class Graph {
	private LinkedList<Node> cityList;
	
	// CONSTRUCTOR
	public Graph() {
		cityList = new LinkedList<Node>();
	}
	
	/**
	 * Add the Node to the list of nodes in the graph
	 * @param city Node to be added
	 */
	public void addCity(Node city) {
		cityList.add(city);
	}
	
	/**
	 * Finds Nodes in graph which correlate to the city names and 
	 * gives it to join() to connect
	 * @param String name of city
	 * @param String name of city
	 * @param flightTime in minutes between cities
	 */
	public void connectCities(String name1, String name2, int flightTime) {
		Node from = getCity(name1);
		Node to = getCity(name2);
		join(from, to, flightTime);
	}
	
	/**
	 * Gives the edge between two nodes corresponding to the 
	 * city names given. The edge given is directed so that 
	 * the String from is the "from" node in the edge etc.
	 * @param String name of city which the edge starts
	 * @param String name of city which the edge ends
	 * @return
	 */
	public Flight getEdge(String from, String to) {
		Node fromCity = getCity(from);
		Node toCity = getCity(to);
		return fromCity.getEdge(toCity);
	}
	
	/**
	 * Creates two edges/ Flights to connect the Nodes in both ways
	 * @param Node from, one end
	 * @param Node to, the other end
	 * @param flightTime in minutes
	 */
	private void join(Node from, Node to, int flightTime) {
		Flight toAddFrom = new Flight(from,to,flightTime);
		Flight toAddTo = new Flight(to,from,flightTime);
		
		from.addEdge(toAddFrom);
		to.addEdge(toAddTo);
	}
	
	/**
	 * Gives the node in graph corresponding to the city name
	 * @param String name of city
	 * @return Node. Gives null if the node wasn't found in graph
	 */
	private Node getCity(String name) {
		Node city = null;
		for(Node current: cityList) {
			if(current.getName().equals(name)) {
				city = current;
			}
		}
		return city;
	}
	
	/**
	 * Builds a new state 
	 * @param name of current city 
	 * @param Linked List of flights so far
	 * @param gCost: the flight cost so far
	 * @param hCost: the heuristic cost
	 * @return State based off parameter information
	 */
	public State createState(String name, LinkedList<Flight> path, int gCost, int hCost) {
		Node city = getCity(name);
		State cityState = new State(city, path, gCost, hCost);
		return cityState;
	}
	
	// used for testing
	public void showMap() {
		
		for(Node current: cityList) {
			System.out.println(current.getName() + " children are: ");
			current.showChildren();
		}
	}
}