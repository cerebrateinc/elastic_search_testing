package org.cerebrate.esjava;
import java.util.ArrayList;
import java.util.Iterator;

import org.cerebrate.mappings.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JsonParser {
	JSONParser parser = new JSONParser();
	Mappings mappings;
	ArrayList <FactsMap> factsMap = new ArrayList <FactsMap>() ;
	ArrayList <ConditionsMap> conditionsMap = new ArrayList <ConditionsMap>()  ;
	ArrayList <SortsMap> sortsMap = new ArrayList <SortsMap>() ;
	ArrayList <DimensionsMap> dimensionsMap = new ArrayList <DimensionsMap>() ;

	public void jsonParse(String json) throws ParseException
	{

		JSONObject jsonObject =  (JSONObject) parser.parse(json);
		String  tableName = (String ) jsonObject.get("tableName");
		JSONArray  dimensionMap = (JSONArray ) jsonObject.get("dimensionMap");
		JSONArray  factMap = (JSONArray ) jsonObject.get("factMap");
		JSONArray  sortMap = (JSONArray ) jsonObject.get("sortMap");
		JSONArray  conditionMap = (JSONArray ) jsonObject.get("conditionsMap");


		if (factMap == null)
			factsMap = null;
		else 
		{
			Iterator i = factMap.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				System.out.println("language "+ (String)innerObj.get("fieldName") +
						" with level " + (String)innerObj.get("displayName")+" and atlast format as "
						+ (String)innerObj.get("formatAs")+"  ");
				FactsMap fm = new FactsMap((String)innerObj.get("fieldName"),
						(String)innerObj.get("formatAs"),(String)innerObj.get("displayName"));
				factsMap.add(fm); 
			}
		}

		if (dimensionMap == null)
			dimensionsMap = null;
		else 
		{
			Iterator i = dimensionMap.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();

				DimensionsMap dm = new DimensionsMap((String)innerObj.get("fieldName"),
						(String)innerObj.get("formatAs"),(String)innerObj.get("displayName"));
				System.out.println("language "+ dm.getFieldName() +
						" with level " + dm.getDisplayName() +"and atlast format as "
						+ dm.getFormatAs());
				dimensionsMap.add(dm); 
			}
		}



		if (sortMap == null)
			sortsMap = null;
		else 
		{
			Iterator i = sortMap.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				System.out.println("language "+ (String)innerObj.get("fieldName") +
						" with level " + (String)innerObj.get("sortOrder")+"and atlast format as "
						+ (String)innerObj.get("formatAs"));
				SortsMap sm = new SortsMap((String)innerObj.get("fieldName"),
						(String)innerObj.get("formatAs"),(String)innerObj.get("sortOrder"));
				sortsMap.add(sm); 
			}
		}

		if (conditionMap == null)
			conditionsMap = null;
		else 
		{
			Iterator i = conditionMap.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				ConditionsMap cm = new ConditionsMap((String)innerObj.get("fieldName"),
						(String)innerObj.get("formatAs"),(String)innerObj.get("fieldsValue"),
						(String)innerObj.get("comparisonOperator"));
				conditionsMap.add(cm); 
			}
		}

		mappings = new Mappings (tableName,factsMap,conditionsMap,sortsMap,dimensionsMap);
	}
}