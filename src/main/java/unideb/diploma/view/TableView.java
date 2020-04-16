package unideb.diploma.view;

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

public class TableView extends AnchorPane {
	Table table;
	double fieldHeight;
	double fieldWidth;
	double fieldSize;
	
	public TableView(double fieldSize, Table table) {
		this.table = table;
		this.fieldSize = fieldSize;
		fieldHeight = FieldView.getHeight(fieldSize);
		fieldWidth = FieldView.getWidth(fieldSize);
		
		generateFieldViews(fieldSize, fieldWidth * 0.5, fieldHeight * 0.5, fieldWidth , fieldHeight);
		
		this.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));		

	}

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

