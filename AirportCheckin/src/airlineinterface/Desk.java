package airlineinterface;

public class Desk {

	private WaitingQueue queue;

private deskExisits true;
	
	/* Constructor that takes in a queue object
	*  Will start a method that loops endlessly.
	*  IE: all people have been checked in, it will pause.
	*  Else if will checkin people.
	*/
	public Desk(WaitingQueue queue ) {
		this.queue = queue;   //Wrong as you need to interact

		//TODO create a conditional statment that kills/Pauses the desk. Depends on what garbage collection does?
		// Conditional statement to keep the desk checkin working.
		while(deskExisits) {
		startCheckin();
		}
	}
	
	/*  takes in the customer at the front of the queue
	 * using getNextCusomer from queue class.
	 * It will loop around whilst the queue has customers.
	 */
	
	// .hasNext returns a boolean if the queue has customers
	private void startCheckinIn(){
		timeDelay(); // Order matters, adding a time delay at the start might avoid a lot of headache?
					// Technically checking is there are customer in queue should constantly happen ... 
		if(queue.hasNext()) {
			checkIn(queue.getNextCustomer());
		}
		else {
			//TODO pause the method until new memebers are added to the queue. 
		}
	}
	private void timeDelay() {
		//TODO creates a time delay thats used on each check in
	}
	
	private void checkIn(Customer c) {
		c.setCheckedIn(weight,volume);
		SystemModel.addCustomerToFlight(c.getFlightCode());
	}
	
	
}
