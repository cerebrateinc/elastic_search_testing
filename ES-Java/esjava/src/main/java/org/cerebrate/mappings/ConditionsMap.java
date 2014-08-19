package org.cerebrate.mappings;

public class ConditionsMap {
	
	private String fieldName;
	private String formatAs;
	private String comparisonOperator;
	private String fieldsValue;
	
	public ConditionsMap()
	{
		this.fieldName = null;
		this.formatAs = null;
		this.comparisonOperator = null;
		this.fieldsValue = null;
	}
	
	public ConditionsMap(String fieldName, String formatAs, String fieldsValue, String comparisonOperator)
	{
		this.fieldName= fieldName;
		this.formatAs = formatAs;
		this.fieldsValue = fieldsValue;
		this.comparisonOperator = comparisonOperator;
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

	public String getComparisonOperator() {
		return comparisonOperator;
	}

	public void setComparisonOperator(String comparisonOperator) {
		this.comparisonOperator = comparisonOperator;
	}

	public String getFieldsValue() {
		return fieldsValue;
	}

	public void setFieldsValue(String fieldsValue) {
		this.fieldsValue = fieldsValue;
	}

}
