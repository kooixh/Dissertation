package com.kooi.dissertation.rewriter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.Operator;

/**
 * 
 * This class is the main rewrite engine. It performs parsing of input and rules. It also
 * invokes the parsing algorithm.
 * 
 * @author Kooi
 * @date 13 February 2019 
 *
 */
public class RewriteEngine {
	
	
	
	private Set<RewriteRule> rules;
	private Map<String,DataType> variables;
	private Map<String,Operator> operators;
	private ASTParser parser;
	
	
	public RewriteEngine(Set<RewriteRule> rules, Set<Operator> ops, Map<String,DataType> vars) {
		operators = new HashMap<>();
		this.variables = vars;
		for(Operator o : ops) {
			operators.put(o.getSymbol(), o);
		}
		parser = new ASTParser(ops,variables);
		this.rules = rules;		
	}
	
	
	public String rewrite(String input) {
		
		//TODO implement
		
		
		return "";
	}
	
	

}
