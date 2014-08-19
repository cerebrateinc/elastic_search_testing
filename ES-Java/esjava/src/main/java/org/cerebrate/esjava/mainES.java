package org.cerebrate.esjava;
import java.util.Scanner;
public class mainES {

	public static void main( String[] args ) throws Exception
	{

		CreateIndex io = new CreateIndex();
		//io.createLocalCluster("venusES");
		io.createTransportClient("venusES");
		Scanner keyboard = new Scanner(System.in);
		String json = keyboard.nextLine();
		JsonParser jp = new JsonParser();
		jp.jsonParse(json);
		
		/*System.out.println("Enter JSON string: ");
		String json = keyboard.nextLine();
		System.out.println("Enter Index name: ");
		String index = keyboard.nextLine();
		System.out.println("Enter type name: ");
		String type = keyboard.nextLine();
		io.create(json, index, type);
		//io.createIndexIfNeeded("venus");
		System.out.println(io.existsIndex("venus"));
		AddData ad = new AddData(io.getClient());
		System.out.println("Enter JSON string1: ");
		String json1 = keyboard.nextLine();
		System.out.println("Enter JSON string2: ");
		String json2 = keyboard.nextLine();
		System.out.println("Enter Index name: ");
		String index1 = keyboard.nextLine();
		System.out.println("Enter type name: ");
		String type1 = keyboard.nextLine();
		ad.add(json1,json2, type1, index1);
		
		System.out.println("Enter JSON string: ");
		String jsonn = keyboard.nextLine();
		System.out.println("Enter Index name: ");
		String index2 = keyboard.nextLine();
		System.out.println("Enter type name: ");
		String type2 = keyboard.nextLine();
		io.create(jsonn, index2, type2);
		*/

		
		
		
		System.out.println(io.existsIndex("venus"));
	}


}
