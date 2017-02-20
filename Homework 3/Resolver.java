import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Resolver {
	private Set<String> classMap ;
	private List<String> classList;
	private Map<String,List<String>> predicateMap;
	
	public Resolver(Set<String> classMap, Map<String,List<String>> predicateMap) {
		this.classMap = classMap;
		this.predicateMap = predicateMap;
		classList =  new ArrayList<String>(classMap);
		
	}
	
	public boolean resolution(String query){
		String negatedQuery = negateQuery(query);
		
		classMap.add(negatedQuery);
		classList.add(0, negatedQuery);
		

		/*System.out.println("KB");
		for (String clause : classList){
			System.out.println(clause);
		}
		System.out.println();*/
		
		
		int sizeDiff = -1;
		while(sizeDiff != 0 ){
			int size = classList.size();
			for (int i = 0; i < classList.size()  ; i++){
				if (classList.size() > 50000){
					return false;
				}
				//System.out.println(classList.size());
				String clause = classList.get(i);
				if(unify(clause)){
					return true;
				}
			}
			sizeDiff = classMap.size() - size;
			
		}
		
		
		
		/*Set<String> keys = predicateMap.keySet();
		
		for (String key : keys){
			System.out.println("Key : " + key);
			for (String clause : predicateMap.get(key)){
				System.out.println(clause);
			}
		}*/
		
		return false;
	}
	
	/*
	 * Unify checks all the clause in the KB predicate map wherever a negation of a token of aClause is present
	 * and unifies aClause with all of those clauses wherever possible.
	 * 
	 * Returns true if an empty clause if found
	 * Returns false if all possible unification are done for aClause
	 * */
	private boolean unify(String aClause) {
		String[] aTokens = aClause.split("\\|");
		
		if(aTokens.length == 1){
			String check = negateQuery(aTokens[0]);
			if (classMap.contains(check)){
				return true;
			}
		}
		
		//Looping through each of the tokens in aClause
		for ( int i = 0 ; i < aTokens.length; i++){
			String aToken = negateQuery(aTokens[i]);

			// Get arguments, predicate of ~aToken
			String[] aArguments = getArguments(aToken);
			String aPredicate = getPredicate(aToken);
			
			// Getting all clauses which have ~aToken
			List<String> clauseList = predicateMap.get(aPredicate);
			
			// If aClause belongs to clauseList, move on to next aToken. 
			// DO NOT UNIFY
			boolean aBelongsToClauseList = false;
			
			if (clauseList != null){
				for ( int j = 0; j < clauseList.size(); j++){
					if(aClause.equals(clauseList.get(j))){
						aBelongsToClauseList = true;
					}
				}
			}
		
			if (aBelongsToClauseList){
				continue;
			}
			
			
			// Looping through all the clauses that contain ~aToken
			if (clauseList != null){				
				for ( int  j = 0; j < clauseList.size(); j++){
					String bclause = clauseList.get(j);
					
					
					
					//Map to hold all the unification mappings
					HashMap<String,String> argMap =  null;
					String[] bTokens = bclause.split("\\|");
					
					//Looping through all the tokens of bClause to find ~aToken
					String bToken = null;
					boolean mayResolve = false;
					boolean willResolve = false;
					
					for ( int m = 0; m < bTokens.length; m++){
						bToken =  bTokens[m];
						String[] bArguments = null;
						String bPredicate = getPredicate(bToken);
						
						if (bPredicate.equals(aPredicate)){
							// Found ~aToken in bclause
							// Main part of Unification begins
							argMap = new HashMap<String,String>();
							bArguments = getArguments(bToken);
							for ( int k = 0; k < bArguments.length; k++){
								if (bArguments[k].equals(aArguments[k])){
									// SAME variable or Constant
									if (isAConstant(bArguments[k]))
										mayResolve = true;
									else
										mayResolve = false;
									argMap.put(bArguments[k], bArguments[k]);
									continue;
								} else {
									if (isAConstant(aArguments[k]) && isAConstant(bArguments[k])){
										// This token will not unify with aToken
										willResolve = false;
										mayResolve = false;
										break;
									} else if (isAConstant(aArguments[k])){
										// This will unify, unless something bad happens in the
										// later arguments
										willResolve = true;
										argMap.put(bArguments[k], aArguments[k]);
									} else if (isAConstant(bArguments[k])){
										// This will unify, unless something bad happens in the later
										// arguments
										willResolve = true;
										argMap.put(aArguments[k], bArguments[k]);
									} else {
										argMap.put(aArguments[k], bArguments[k]);
										// TODO : SERIOUSLY RECONSIDER THISSSSS!
										if (bTokens.length == 1 || aTokens.length == 1)
											willResolve = true;
									}
								}
							}
							if (willResolve){
								break;
							} else if (mayResolve){
								if (aTokens.length == 1){
									//System.out.println("Here "+aToken+" "+bToken);
									willResolve = true;
									break;
								} else {
									continue;
								}
							}
						}
					}
					
					if (willResolve){
						//System.out.println("UNIFY"+" "+aClause+" "+bclause);
						String newA = new String(aClause);
						String newB = new String(bclause);
						
						String[] newATokens = newA.split("\\|");
						String[] newBTokens = newB.split("\\|");
						newA = "";

						//System.out.println("NEWB : "+bToken);
						for ( int m = 0; m < newATokens.length; m++){
							String newToken = newATokens[m];
							if (!newToken.equals(negateQuery(aToken))){
								String[] newArgs = getArguments(newToken);
								newToken = getPredicate(newToken)+"(";
								for ( int n = 0; n < newArgs.length; n++){
									if (argMap.get(newArgs[n]) != null){
										newToken = newToken+argMap.get(newArgs[n]);
									} else {
										newToken = newToken+newArgs[n];
									}
									if (n !=  newArgs.length-1){
										newToken = newToken+",";
									}
								}
								newA  =  newA + newToken+")";
								if (m !=  newATokens.length-1){
									newA = newA+"|";
								}
							}
						}
						
						
						newB = "";
						for ( int m = 0; m < newBTokens.length; m++){
							String newToken = newBTokens[m];
							//System.out.println(newToken+" "+bToken);
							if (!newToken.equals(bToken)){
								String[] newArgs = getArguments(newToken);
								newToken = getPredicate(newToken)+"(";
								for ( int n = 0; n < newArgs.length; n++){
									if (argMap.get(newArgs[n]) != null){
										newToken = newToken+argMap.get(newArgs[n]);
									} else {
										newToken = newToken+newArgs[n];
									}
									if (n !=  newArgs.length-1){
										newToken = newToken+",";
									}
								}
								newB =  newB + newToken+")";
								if (m !=  newBTokens.length-1){
									newB = newB+"|";
								}
							}
						}
						
						String newString = null;
						if (newA.isEmpty() && newB.isEmpty()){
							return true;
						} else if (newA.isEmpty()){
							if(newB.charAt(newB.length()-1) == '|'){
								newB = newB.substring(0, newB.length()-1);
							}
							newString = newB;
						} else if (newB.isEmpty()){
							if(newA.charAt(newA.length()-1) == '|'){
								newA = newA.substring(0, newA.length()-1);
							}
							newString = newA;
						} else {
							if(newA.charAt(newA.length()-1) == '|'){
								newA = newA.substring(0, newA.length()-1);
							}
							if(newB.charAt(newB.length()-1) == '|'){
								newB = newB.substring(0, newB.length()-1);
							}
							newString = newA+"|"+newB;
						}
						
						String[] args = newString.split("\\|");
						HashMap<String,Boolean> argsMap =  new HashMap<String,Boolean>();
						for ( int m = 0; m < args.length; m++){
							//if (argsMap.get(negateQuery(args[m])) == null){
								argsMap.put(args[m], true);
								//System.out.println("Arg map : "+args[m]+" "+argMap.size());
							//} 
							
						}
						newString = "";
						Set<String> keys = argsMap.keySet();
						if (!keys.isEmpty()){
							for ( String key : keys){
									newString = newString+key+"|";
									//System.out.println("Arg SET : "+newString);
							}
							newString = newString.substring(0, newString.length()-1);
							if (newString.isEmpty()){
								return true;
							}
						}
						if (newString.isEmpty() || newString.equals("")){
							continue;
						}

						
						if(classMap.add(newString)){
							/*System.out.println(aClause+" "+bclause);
							System.out.println("ADDDING NEW CLAUSE: "+newString);
							System.out.println();*/
							
							String[] tokens = newString.split("\\|");
							
							if (tokens.length == 1){
								if (classMap.contains(negateQuery(newString))){
									return true;
								}
							}
							
							classList.add(newString);
						
						
							String[] tokenArray = newString.split("\\|");
							for ( int m = 0; m < tokenArray.length; m++){
								if (predicateMap.get(getPredicate(tokenArray[m])) != null){
									predicateMap.get(getPredicate(tokenArray[m])).add(newString);
								} else {
									List<String> list = new ArrayList<String>();
									list.add(newString);
									predicateMap.put(getPredicate(tokenArray[m]), list);
								}
							}
						} else {
							//System.out.println("ALREADY IN KB: "+newString);
						}
					}
				}
			}
		}
		return false;
	}

	private boolean isAConstant(String string) {
		if (string.charAt(0) >= 65 && string.charAt(0) <= 90){
			return true;
		}
		return false;
	}

	private String[] getArguments(String token) {
		token = token.split("\\(")[1].split("\\)")[0];
		
		return token.split(",");
	}

	private static String getPredicate(String query){
		String[] split = query.split("\\(");
		return split[0];
	}
	
	private static String negateQuery(String predicate){
		if (predicate.charAt(0) == '('){
			predicate = predicate.substring(1, predicate.length()-1);
		}
		predicate = "~"+predicate;
		if (predicate.substring(0, 2).equals("~~")){
			predicate = predicate.substring(2);
		}
		return predicate;
	}
	
	
	

}
