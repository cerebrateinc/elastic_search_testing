package org.cerebrate.mappings;

import java.util.ArrayList;

public class Mappings {

	private String TableName;
	
	private ArrayList <FactsMap> factsMap;
	private ArrayList <ConditionsMap> conditionsMap;
	private ArrayList <SortsMap> sortsMap;
	private ArrayList <DimensionsMap> dimensionsMap;

	public Mappings (String TableName, ArrayList <FactsMap> factsMap,
			ArrayList <ConditionsMap> conditionsMap,ArrayList <SortsMap> sortsMap, ArrayList <DimensionsMap> dimensionsMap )
	{
		this.TableName = TableName;
		this.conditionsMap = conditionsMap;
		this.dimensionsMap = dimensionsMap;
		this.factsMap = factsMap;
		this.sortsMap = sortsMap;
	}

	public String getTableName() {
		return TableName;
	}
	public void setTableName(String tableName) {
		TableName = tableName;
	}
	public ArrayList<FactsMap> getFactsMap() {
		return factsMap;
	}
	public void setFactsMap(ArrayList<FactsMap> factsMap) {
		this.factsMap = factsMap;
	}
	public ArrayList<ConditionsMap> getConditionsMap() {
		return conditionsMap;
	}
	public void setConditionsMap(ArrayList<ConditionsMap> conditionsMap) {
		this.conditionsMap = conditionsMap;
	}
	public ArrayList<SortsMap> getSortsMap() {
		return sortsMap;
	}
	public void setSortsMap(ArrayList<SortsMap> sortsMap) {
		this.sortsMap = sortsMap;
	}
	public ArrayList<DimensionsMap> getDimensionsMap() {
		return dimensionsMap;
	}
	public void setDimensionsMap(ArrayList<DimensionsMap> dimensionsMap) {
		this.dimensionsMap = dimensionsMap;
	}

}
