import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class homework {
	
	public static void main(String[] args) {
		System.out.println("FOL LOGIC");
		File file =  new File("input.txt");
		Scanner scan = null;
		int numberOfQueries, numStatements;
		String[] queries = null, statements = null;
		try {
			scan =  new Scanner(file);
			String string =  scan.nextLine();
			numberOfQueries = Integer.parseInt(string);
			queries = new String[numberOfQueries];
			for ( int i = 0; i < numberOfQueries; i++){
				StringBuilder builder = new StringBuilder(scan.nextLine());
				for (int j = 0 ; j < builder.length(); j++){
					if (builder.charAt(j) == ' ' || builder.charAt(j) == '\t' || builder.charAt(j) == '\n'){
						builder.replace(j, j+1, "");
						j--;
					}
				}
				queries[i] = builder.toString();
			}
			
			numStatements = Integer.parseInt(scan.nextLine());
			statements = new String[numStatements];
			for (int i = 0; i < numStatements; i++){
				StringBuilder builder = new StringBuilder(scan.nextLine());
				for (int j = 0 ; j < builder.length(); j++){
					if (builder.charAt(j) == ' ' || builder.charAt(j) == '\t' || builder.charAt(j) == '\n'){
						builder.replace(j, j+1, "");
						j--;
					}
				}
				statements[i] = builder.toString();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			scan.close();
		}
		
		
		CNFConverter cnfConverter =  new CNFConverter();
		statements = cnfConverter.convertToCNF(statements);
		
		
		
		
		Boolean[] result = new Boolean[queries.length];
		
		
		for ( int i = 0; i < queries.length; i++){
			HashMap<String,List<String>> predicateMap =  cnfConverter.getPredicateMap();
			HashMap<String,List<String>> predicateMapClone = new HashMap<String,List<String>>();
			Set<String> keys = predicateMap.keySet();
			for ( String key : keys){
				List<String> list = predicateMap.get(key);
				List<String> listClone =  new ArrayList<String>();
				for (String item : list){
					listClone.add(item);
				}
				predicateMapClone.put(key, listClone);
			}
			
			Set<String> classMap = cnfConverter.getClassMap();
			HashSet<String> classMapClone = new HashSet<String>();
			for (String item: classMap){
				classMapClone.add(item);
			}
			
			
			Resolver resolver =  new Resolver( classMapClone, predicateMapClone);
			result[i] = resolver.resolution(queries[i]);
		}
		File outFile = new File("output.txt");
		try {
			FileWriter fileWriter = new FileWriter(outFile);
			
			for ( int i = 0; i < result.length; i++){
				if (result[i]){
					fileWriter.write("TRUE\n");
					System.out.println("TRUE");
				} else {
					fileWriter.write("FALSE\n");
					System.out.println("FALSE");
				}
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
