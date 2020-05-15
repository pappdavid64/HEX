package unideb.diploma.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;


/**
 * Utility class for filtering a list of fields by color.
 * */
public class FieldList implements Iterable<Field> {
	
	/**
	 * List of the fields which can be filtered.
	 * */
	private List<Field> fields;
	
	/**
	 * Base constructor.
	 * */
	public FieldList() {
		fields = new ArrayList<>();
	}
	
	/**
	 * Constructor of the FieldList.
	 * @param fields The fields which can be filtered.
	 * */
	public FieldList(List<Field> fields) {
		this.fields = new ArrayList<>(fields);
	}
	
	/**
	 * Filters the fields by a color.
	 * @param color The color which we want to keep.
	 * @return The list of fields, which color is equals to the color.
	 * */
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

	/**
	 * Filters the fields by a color.
	 * @param color The color which we do not want to keep.
	 * @return The list of fields, which color is not equals to the color.
	 * */
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
	
	/**
	 * Add one field to the fields.
	 * @param field The field which will be added.
	 * */
	public void add(Field field) {
		fields.add(field);
	}
	
	/**
	 * Add a collection of fields to the fields.
	 * @param fields The fields which will be added.
	 * */
	public void addAll(Collection<Field> fields) {
		this.fields.addAll(fields);
	}
	
	/**
	 * Remove one field from the fields.
	 * @param field The field which will be removed.
	 * */
	public void remove(Field field) {
		fields.remove(field);
	}
	
	/**
	 * Remove a collection of fields from the fields.
	 * @param fields The fields which will be removed.
	 * */
	public void removeAll(Collection<Field> fields) {
		this.fields.removeAll(fields);
	}

	/**
	 * Gets the fields.
	 * @return The fields.
	 * */
	public List<Field> getFields(){
		return fields;
	}
	
	/**
	 * Get the iterator of the fields.
	 * @return The iterator of the fields.
	 * */
	@Override
	public Iterator<Field> iterator() {
		return fields.iterator();
	}
	
}
