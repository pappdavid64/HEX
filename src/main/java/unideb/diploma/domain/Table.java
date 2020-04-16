package unideb.diploma.domain;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private List<Field> fields;
	
	public Table(List<Field> fields) {
		super();
		this.fields = fields;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Field getFieldAt(Position position) {		
		for(Field field : fields) {
			if(field.getPosition().equals(position)) {
				return field;
			}
		}
		return null;
	}

	public void setFieldColorAtPosition(Position position, FieldColor color) {	
		getFieldAt(position).setColor(color);
	}

	public Table clone() {
		List<Field> copiedFields = new ArrayList<>();
		for(Field field : fields) {
			copiedFields.add(field.clone());
		}
		return new Table(copiedFields);
	}
	
}
