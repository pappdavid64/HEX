package unideb.diploma.view.graphic;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.Table;


/**
 * Responsive for the appearance of the board.
 * */
public class TableView extends AnchorPane {
	
	/**
	 * The table, which the display will belong to.
	 * */
	private Table table;
	
	/**
	 * The height of a field.
	 * */
	private double fieldHeight;
	
	/**
	 * The width of a field.
	 * */
	private double fieldWidth;
	
	/**
	 * Constructor of the TableView.
	 * @param fieldSize The size of a field.
	 * @param table The table, which the display will belong to.
	 * */
	public TableView(double fieldSize, Table table) {
		this.table = table;
		fieldHeight = FieldView.getHeight(fieldSize);
		fieldWidth = FieldView.getWidth(fieldSize);
		
		generateFieldViews(fieldSize, fieldWidth * 0.5, fieldHeight * 0.5, fieldWidth , fieldHeight);
		
		this.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));		

	}

	/**
	 * Generates the FieldViews for the table's fields.
	 * @param fieldSize The size of the field.
	 * @param xOffset The size of the offset of x.
	 * @param yOffset The size of the offset of y.
	 * @param fieldWidth The width of the fields.
	 * @param fieldHeight The height of the fields.
	 * */
	public void generateFieldViews(double fieldSize, double xOffset, double yOffset, double fieldWidth, double fieldHeight) {
		getChildren().clear();
		List<Field> fields = table.getFields();
		ObservableList<Node> children = this.getChildren();
		for(Field field : fields) {
			FieldView fieldView = new FieldView(
					field,
					fieldSize,
					50 + field.getY() * fieldWidth + xOffset * field.getX(),
					50 + field.getX() * fieldHeight + field.getX() * yOffset
					);
			children.add(fieldView);
			}
	}
}

