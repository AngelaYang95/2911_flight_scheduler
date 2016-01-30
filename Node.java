import java.util.LinkedList;

public class Node {
	private String cityName;
	private int delay;
	private LinkedList<Flight> neighbours;
	
	public Node (String name, int minutes) {
		cityName = name;
		delay = minutes;
		neighbours = new LinkedList<Flight>();
	}
	
	/**
	 * Add a flight/ connection between this Node and its
	 * neighbour
	 * @param Edge which connects this city TO the child
	 */
	public void addEdge(Flight toAdd) {
		neighbours.add(toAdd);
	}
	
	/**
	 * Gives the delay time of the city
	 * @return integer minutes
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Gives the city name of this Node
	 * @return String
	 */
	public String getName() {
		return cityName;
	}
	
	/**
	 * Gives the flight time in minutes between this
	 * city and a neighbouring city
	 * Excludes delay times
	 * @param neighbouring Node
	 * @return integer time in minutes
	 */
	public int getFlightTime(Node child) {
		Flight path = getEdge(child);
		return path.getFlightTime();
	}
	
	/**
	 * Gives reference to this cities list of neighbours
	 * @return LinkedList of Flights. Returns null if it 
	 * doesn't have any neighbours
	 */
	public LinkedList<Flight> getChildren() {
		if(!neighbours.isEmpty()) {
		
			return neighbours;
		} 
		
		return null;
	}
	
	/**
	 * Gives reference to the Flight that connect from
	 * this current city Node to a neighbouring one
	 * @param neighbour
	 * @return Flight / edge with the neighbour as the "to" Node
	 * and this city as the "from" Node
	 */
	public Flight getEdge(Node neighbour) {
		Flight found = null;
		for(Flight current: neighbours) {
			if(current.getOtherEnd(this).equals(neighbour)) {
				found = current;
			}
		}
		
		return found;
	}
	
	
	//used for testing
	public void showChildren() {
		Node toPrint;
		for(Flight current: neighbours) {
			toPrint = current.getOtherEnd(this);
			System.out.print(toPrint.getName() + " ");
		}
		System.out.println();
		System.out.println();
	}
	
}