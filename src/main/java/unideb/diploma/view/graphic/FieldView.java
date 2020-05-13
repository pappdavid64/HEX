package unideb.diploma.view.graphic;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import unideb.diploma.domain.Field;
import unideb.diploma.logic.human.SimpleHumanPlayer;
import unideb.diploma.observer.Observable;
import unideb.diploma.observer.Observer;

public class FieldView extends Polygon implements Observer {
	
	private static final double TO_RADIAN = Math.PI / 180;
	
	private Field field;
	
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
	
	public static double getHeight(double size){
		return size * ( Math.sin(30 * TO_RADIAN) -  Math.sin(210 * TO_RADIAN));		
	}
	
	public static double getWidth(double size){
		return size * ( Math.cos(30 * TO_RADIAN) -  Math.cos(150 * TO_RADIAN));
	}

	@Override
	public void notify(Observable observable) {
		setFill(field.getColor().getValue());
	}
}
