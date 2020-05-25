package unideb.diploma.view.graphic;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import unideb.diploma.domain.Field;
import unideb.diploma.logic.human.SimpleHumanPlayer;
import unideb.diploma.observer.Observable;
import unideb.diploma.observer.Observer;

/**
 * Responsive for the appearance of one field.
 * */
public class FieldView extends Polygon implements Observer {
	
	/**
	 * Constant to convert degree to radian.
	 * */
	private static final double TO_RADIAN = Math.PI / 180;
	
	/**
	 * The field which the display will belongs to.
	 * */
	private Field field;
	
	
	/**
	 * Constructor of the FieldView.
	 * @param field The field which the display will belongs to.
	 * @param size The size of FieldView.
	 * @param xOffset The offset of x, the starting x position of the FieldView.
	 * @param yOffset The offset of y, the starting y position of the FieldView.
	 * */
	public FieldView(Field field, double size, double xOffset, double yOffset) {
		
		for(int i = 0; i < 6; ++i){
			double radian = (30 + i * 60) * TO_RADIAN; 
			double x = size * Math.cos(radian) + xOffset;
			double y = size * Math.sin(radian) + yOffset;
			
			this.getPoints().addAll(x, y);
		}

		this.field = field;
		this.setStroke(Color.BLACK);
		this.setFill(field.getColor().getValue());
		addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {SimpleHumanPlayer.setPositionOnClick(field.getPosition());});
		field.addObserver(this);
	}
	
	/**
	 * Gets the height of a FieldView.
	 * @param size The size of the field.
	 * @return the height of a FieldView.
	 * */
	public static double getHeight(double size){
		return size * ( Math.sin(30 * TO_RADIAN) -  Math.sin(210 * TO_RADIAN));		
	}
	

	/**
	 * Gets the width of a FieldView.
	 * @param size The size of the field.
	 * @return the width of a FieldView.
	 * */
	public static double getWidth(double size){
		return size * ( Math.cos(30 * TO_RADIAN) -  Math.cos(150 * TO_RADIAN));
	}

	/**
	 * The FieldView observs the field.
	 * Whenever the color of the field is set, its notifying the FieldView.
	 * @param observable The field.
	 * */
	@Override
	public void notify(Observable observable) {
		setFill(field.getColor().getValue());
	}
}
