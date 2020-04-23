package airlineinterface;

import java.util.ArrayList;
import java.util.List;

/* Observer pattern - subject that gets observed */
public abstract class Observable {
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	// Methods used to add and remove observers
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}
	
	// Callable only by inheriting class
	protected void notifyObservers() {
		for(Observer o : observers) {
			o.onNotify();
		}
	}
}
