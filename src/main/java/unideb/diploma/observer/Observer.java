package unideb.diploma.observer;

/**
 * An object which class implements Observer can observe an observable object.
 * */
public interface Observer {
	
	/**
	 * The action which happens when the observable object notifies his obervers.
	 * @param observable The observable object.
	 * */
	void notify(Observable observable);
}
