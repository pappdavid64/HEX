package unideb.diploma.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;

public class FieldList implements Iterable<Field> {
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
	
	public void add(Field field) {
		fields.add(field);
	}
	
	public void addAll(Collection<Field> fields) {
		this.fields.addAll(fields);
	}
	
	public void remove(Field field) {
		fields.remove(field);
	}
	
	public void removeAll(Collection<Field> fields) {
		this.fields.removeAll(fields);
	}

	public List<Field> getFields(){
		return fields;
	}
	
	@Override
	public Iterator<Field> iterator() {
		return fields.iterator();
	}
	
}
