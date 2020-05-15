package unideb.diploma.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * An object which class extends observable can be observed.
 * If something happens with the object, it can notify his observers. 
 * */
public abstract class Observable {
	/**
	 * The list of the observers.
	 * */
	protected List<Observer> observers = new ArrayList<>();
	
	/**
	 * Add observer to the observers.
	 * @param observer The observer who will be added.
	 * */
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	/**
	 * Remove observer from observers.
	 * @param observer The observer who will be removed.
	 * */
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}
	
	/**
	 * Notifying the observers.
	 * */
	public void notifyObservers() {
		for(Observer observer : observers) {
			observer.notify(this);
		}
	}
}
