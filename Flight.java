/**
 * A Flight is the aerial connection between two Cities
 * Otherwise described as an Edge to a Node. They are
 * directional
 */
public class Flight {
	private Node from;
	private Node to;
	private int minutes;
	
	public Flight(Node fromCity, Node toCity, int flightTime) {
		from = fromCity;
		to = toCity;
		minutes = flightTime;
	}
	
	/**
	 * Gives flights time in minutes for this Flight
	 * Doesn't include delay time
	 * @return integer
	 */
	public int getFlightTime() {
		return minutes;
	}
	
	/**
	 * Gives the "from" location
	 * @return Node
	 */
	public Node getFrom() {
		return from;
	}
	
	/**
	 * Gives the "to" location
	 * @return Node
	 */
	public Node getTo() {
		return to;
	}
	
	/**
	 * Gives other end of this Node in 
	 * this particular Flight
	 * @param current Node
	 * @return Node
	 */
	public Node getOtherEnd(Node givenEnd) {
		if(givenEnd.equals(from)) {
			return to;
		} else {
			return from;
		}
	}
	
	/**
	 * Gives delay time in minutes
	 *  of the destination of "to" location
	 * @return integer 
	 */
	public int destinationDelay() {
		return to.getDelay();
	}
	
	/**
	 * Checks if two flights are equal
	 * They are equivalent if they move from the same 
	 * Node and arrive at the same node. Calculations are 
	 * directional
	 * @param Flight to be compared
	 * @return true if they are equal. false otherwise
	 */
	public boolean sameFlight (Flight path) {
		
		String departure = path.getFrom().getName();
		String landing = path.getTo().getName();
		
		if(from.getName().equals(departure) && 
		   to.getName().equals(landing)) {
			return true;
		}
		return false;
	}
}