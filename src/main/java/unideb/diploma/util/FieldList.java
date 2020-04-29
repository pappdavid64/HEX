package unideb.diploma.util;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;

public class FieldList {
	private List<Field> fields;
		
	public FieldList() {
		fields = new ArrayList<>();
	}
	
	public FieldList(List<Field> fields) {
		this.fields = new ArrayList<>(fields);
	}
	
	public List<Field> withColor(FieldColor color){
		List<Field> copyOfElements = new ArrayList<>(fields);
		List<Field> forRemove = new ArrayList<>();
		copyOfElements.forEach((element) -> {
			if(element.getColor() != color) {
				forRemove.add(element);
			}
		});
		copyOfElements.removeAll(forRemove);
		return copyOfElements;
	}
	
	public List<Field> withoutColor(FieldColor color){
		List<Field> copyOfElements = new ArrayList<>(fields);
		List<Field> forRemove = new ArrayList<>();
		copyOfElements.forEach((element) -> {
			if(element.getColor() == color) {
				forRemove.add(element);
			}
		});
		copyOfElements.removeAll(forRemove);
		return copyOfElements;
	}
	
}
