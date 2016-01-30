import java.util.LinkedList;

public class HeuristicA implements IStrategy{
	private int minCost;
	private LinkedList<Flight>minRoute;
	
	public HeuristicA(LinkedList<Flight> required) {
		minRoute = required;
		minCost = 0;
		calcMin(required);
		
	}
	
	/**
	 * Gives the minimum possible flight time of the path
	 * Doesn't include the delay times
	 * @param list of required Flights
	 */
	private void calcMin(LinkedList<Flight> required) {
		for(Flight current:required) {			
			minCost = minCost + current.getFlightTime();
		}
	}

	/**
	 * Calculates the Heuristic for a given state
	 */
	@Override
	public int calcHCost(State child) {
		int hCost = minCost;
		LinkedList<Flight> toBeCovered = child.coveredFlights(minRoute);
		
		Flight lastEdge = child.getLast();
		if(toBeCovered != null) {
			if(child.numEdge() > 1) {
				hCost = child.getHeur();
			}
			
			for(Flight current: toBeCovered) {

				if(current != null && current.sameFlight(lastEdge)) { // always satisfied
					
					hCost = hCost - lastEdge.getFlightTime();
					break;
				}
				
			}
		}
		if(hCost < 0) {
			return 0;
		}
		
		//hCost = 0;
		return hCost;
	}
}

