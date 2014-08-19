package org.cerebrate.mappings;

public class DimensionsMap {
	private String fieldName;
	private String formatAs;
	private String displayName;
	
	public DimensionsMap()
	{
		this.fieldName = null;
		this.formatAs = null;
		this.displayName = null;
	}
	
	public DimensionsMap(String fieldName, String formatAs, String displayName)
	{
		this.fieldName= fieldName;
		this.formatAs = formatAs;
		this.displayName = displayName;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	

}
