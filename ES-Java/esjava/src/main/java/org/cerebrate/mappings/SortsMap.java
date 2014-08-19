package org.cerebrate.mappings;

public class SortsMap {
	
	private String fieldName;
	private String formatAs;
	private String sortOrder;
	
	public SortsMap()
	{
		this.fieldName = null;
		this.formatAs = null;
		this.sortOrder = null;
	}
	
	public SortsMap(String fieldName, String formatAs, String sortOrder)
	{
		this.fieldName= fieldName;
		this.formatAs = formatAs;
		this.sortOrder = sortOrder;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFormatAs() {
		return formatAs;
	}

	public void setFormatAs(String formatAs) {
		this.formatAs = formatAs;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	

}
