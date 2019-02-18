package com.kooi.dissertation.rewriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.syntaxtree.ASTNode;
import com.kooi.dissertation.syntaxtree.ConstantNode;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.OperatorNode;
import com.kooi.dissertation.syntaxtree.VariableNode;

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
	private Map<String,List<RewriteRule>> ruleMap;
	private ASTParser parser;
	
	
	public RewriteEngine(Set<RewriteRule> rules, ASTParser parser) {
		//this.context = context;
		this.parser = parser;
		this.rules = rules;		
		ruleMap = new HashMap<>();
		
		//make a map of lhs node to the rule. For efficient look up 
		for(RewriteRule r: this.rules) {
			if(!ruleMap.containsKey(r.getLhs().getValue())) {
				ruleMap.put(r.getLhs().getValue(), new ArrayList<>());
				
			}
			ruleMap.get(r.getLhs().getValue()).add(r);
		}
	}
	
	
	/**
	 * 
	 * 
	 * Rewrite function to rewrite an infix expression string into it's normal form.
	 * Output returned in post fix notation
	 * 
	 * @param input Infix expression string
	 * @return the normal form of input in post fix notation
	 */
	public String rewritePostfix(String input) {
					
		return parser.postOrderTreverse(rewrite(input));
		
	}
	
	/**
	 * 
	 * This is the main rewrite function. It takes a string in infix form and return the 
	 * syntax tree node of the result.
	 * 
	 * @param infix expression to rewrite in infix form
	 * @return root Node of the syntax tree of the result
	 */
	public Node rewrite(String infix) {
		try {
			Node root = parser.parseAST(infix);
			boolean flag = false;  //if flag is false then cannot be rewritten any further
			
			do {
				flag = search(root,root);
			}while(flag);
			
			
			return root;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * Call the rewrite method and return the result in infix form
	 * 
	 * @param infix expression to rewrite in infix string
	 * @return result after rewrite in infix form
	 */
	public String rewriteInfix(String infix) {
		
		try {
			return parser.toInfix(rewritePostfix(infix));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	
	//private functions for algorithm 
	
	
	
	//matching function to determine if term and rule match, vars map to keep track of 
	//the variables
	private boolean match(Node term , Node rule, Map<String,Node> vars) {
		
		if(term == null || rule == null) {
			return term == null && rule == null; //they match if both are null
		}
		
		//if is variable then just need to match type and add to var list. 
		if(rule instanceof VariableNode) {
			//if variable appeared before, check if current match prev
			if(vars.containsKey(rule.getValue())) {
				return term.equals(vars.get(rule.getValue()));
			}else {
				//if variable type is compatible with operator return type
				if(term instanceof OperatorNode && ((OperatorNode) term).getReturnType() == ((VariableNode)rule).getType()) {
					vars.put(rule.getValue(), term); 
					return true;
				}else if(term instanceof ConstantNode) {
					vars.put(rule.getValue(), term); 
					return true;
				}else if(term instanceof VariableNode && ((VariableNode)term).getType() == ((VariableNode)rule).getType()) {
					vars.put(rule.getValue(), term); //replace with a copy of term 
					return true;
				}	
				return false;
			}
		}else { //if rule is not a variable then must match symbol
			
			if(rule.getValue().equals(term.getValue())) //recursively match the sub-expression
				return (match(term.getLeft(),rule.getLeft(),vars) && match(term.getRight(),rule.getRight(),vars));
			
			return false;
		}
		
	}
	
	//substitute function in rewrite algorithm. Swap the term node with rule and fill the 
	//variables
	private void substitute(Node term , Node rule, Map<String,Node> vars) {
		
		//swap the term to be the rule
		((ASTNode)term).setValue(rule.getValue());
		((ASTNode)term).setRight(copy(rule.getRight()));
		((ASTNode)term).setLeft(copy(rule.getLeft()));
		//fill in variable
		swap(term,vars);
		
		
	}
	
	
	//function to swap the node with the variable node
	private void swap(Node m,Map<String,Node> vars) {
		if(m == null)
			return;
		
		//check node value is a variable
		if(vars.containsKey(m.getValue())) {
			String initialValue = m.getValue();
			
			//set the respective node
			((ASTNode)m).setValue(vars.get(initialValue).getValue());
			((ASTNode)m).setRight(copy(vars.get(initialValue).getRight()));
			((ASTNode)m).setLeft(copy(vars.get(initialValue).getLeft()));
			
		}
		swap(m.getLeft(),vars);
		swap(m.getRight(),vars);

	}
	
	
	//copy function to copy one node into a new instance
	private Node copy(Node n) {
		
		if(n == null)
			return null;
		Node newNode;
		
		if(n instanceof VariableNode)
			newNode = new VariableNode(n.getValue(),((VariableNode)n).getType());
		else if(n instanceof OperatorNode)
			newNode = new OperatorNode(n.getValue(),((OperatorNode)n).getReturnType());
		else
			newNode = new ConstantNode(n.getValue());
		
		((ASTNode)newNode).setLeft(copy(n.getLeft()));
		((ASTNode)newNode).setRight(copy(n.getRight()));
		
		return newNode;
		
	}
	
	//use a post-order traversal to search the syntax tree for matching rule
	//root keeps a reference to the original root so we can swap when we make a copy
	private boolean search(Node n,Node root) {
		
		if(n == null)
			return false;
		
		
		
		if(search(n.getLeft(),n.getLeft()))
			return true;
		if(search(n.getRight(),n.getRight()))
			return true;
		
		//try all the possible rule that can be match
		if(ruleMap.get(n.getValue()) == null)
			return false;
				
		for(RewriteRule r : ruleMap.get(n.getValue())) {
			Map<String,Node> vars = new HashMap<>();
			
			if(match(n,r.getLhs(),vars)) {
				substitute(n,r.getRhs(),vars);
				//if n is copied then update the root referenece
				if(n != root)
					root = n;
					
				return true;
				
			}
		}
		return false;
	}
	
	

}
