package com.shuketa.server;

import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

/*
 * 骨格クラス
 */
public abstract class AbstractServer{

	public boolean didReceiveData(InputStream inputStream) {

		//readFromObject( inputStream );

		readFormStream( inputStream );

		return false;
	}
    
	
	void readFormStream(InputStream inputStream){
		JsonParser parser = Json.createParser(inputStream);
		
		String keyString = null;
        //適当に出力してみる
        while(parser.hasNext()){
            JsonParser.Event event = parser.next();
            switch(event){

			case START_OBJECT:
				System.out.print(" KEY:"+keyString +" "+ event + "-->");
				break;
			case END_OBJECT:
				System.out.print("<--" + event + " ");
				break;
			case START_ARRAY:
				System.out.print(" KEY:"+keyString +" "+ event + "-->");
				break;
			case END_ARRAY:
				System.out.print("<--" + event + " ");
				break;
			case VALUE_NUMBER:
				System.out.println( keyString + " = " + parser.getInt());
				break;
			case VALUE_STRING:
				System.out.println(keyString + " = " +  parser.getString());
				break;
			case VALUE_TRUE:
//				System.out.print("true/*" + event + "*/, ");
				break;
			case VALUE_FALSE:
//				System.out.print("false/*" + event + "*/, ");
				break;
			case VALUE_NULL:
//			System.out.print("null/*" + event + "*/, ");
				break;
			case KEY_NAME:
				keyString = parser.getString();
//				System.out.println("Key  =" + parser.getString());
				break;

			default:
				System.out.print("Unrecognized Event : ");
			}
        }
	}
	
	void readFromObject(InputStream inputStream) {
		JsonReader reader = Json.createReader(inputStream);
		JsonStructure jsonst = reader.read();

		switch (jsonst.getValueType()) {
		case OBJECT:
			System.out.println("OBJECT");
			JsonObject object = (JsonObject) jsonst;
			for (String name : object.keySet())
				navigateTree(object.get(name), name);
			break;
		case ARRAY:
			System.out.println("ARRAY");
			JsonArray array = (JsonArray) jsonst;
			for (JsonValue val : array)
				navigateTree(val, null);
			break;
		case STRING:
			JsonString st = (JsonString) jsonst;
			System.out.println("STRING " + st.getString());
			break;
		case NUMBER:
			JsonNumber num = (JsonNumber) jsonst;
			System.out.println("NUMBER " + num.toString());
			break;
		case TRUE:
		case FALSE:
		case NULL:
			System.out.println(jsonst.getValueType().toString());
			break;
		default:
			break;
		}

	}

	public static void navigateTree(JsonValue tree, String key) {
		System.out.print("navigateTree");
		
		   if (key != null)
		      System.out.print("Key " + key + ": ");
		   switch(tree.getValueType()) {
		      case OBJECT:
		         System.out.println("OBJECT");
		         JsonObject object = (JsonObject) tree;
		         for (String name : object.keySet())
		            navigateTree(object.get(name), name);
		         break;
		      case ARRAY:
		         System.out.println("ARRAY");
		         JsonArray array = (JsonArray) tree;
		         for (JsonValue val : array)
		            navigateTree(val, null);
		         break;
		      case STRING:
		         JsonString st = (JsonString) tree;
		         System.out.println("STRING " + st.getString());
		         break;
		      case NUMBER:
		         JsonNumber num = (JsonNumber) tree;
		         System.out.println("NUMBER " + num.toString());
		         break;
		      case TRUE:
		      case FALSE:
		      case NULL:
		         System.out.println(tree.getValueType().toString());
		         break;
		   }
		}
	
}
