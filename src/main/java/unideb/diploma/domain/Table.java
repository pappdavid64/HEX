package unideb.diploma.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the table of the game.
 * */
public class Table {
	/**
	 * The fields of the table.
	 * */
	private List<Field> fields;
	
	/**
	 * Constructor.
	 * @param fields The fields of the table.
	 * */
	public Table(List<Field> fields) {
		super();
		this.fields = fields;
	}

	/**
	 * Gets the fields.
	 * @return The fields.
	 * */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * Gets the field at the position.
	 * @param position The position.
	 * @return The field at the position.
	 * */
	public Field getFieldAt(Position position) {		
		for(Field field : fields) {
			if(field.getPosition().equals(position)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Sets the field color at the position.
	 * @param position The position.
	 * @param color The color will be set.
	 * */
	public void setFieldColorAtPosition(Position position, FieldColor color) {	
		getFieldAt(position).setColor(color);
	}

	/**
	 * Cloning the table.
	 * @return Copy of the table.
	 * */
	public Table clone() {
		List<Field> copiedFields = new ArrayList<>();
		for(Field field : fields) {
			copiedFields.add(field.clone());
		}
		return new Table(copiedFields);
	}
	
}
