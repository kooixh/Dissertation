package com.kooi.dissertation.rewriter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.syntaxtree.ASTNode;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.NodeType;


/**
 * 
 * This class is the main rewrite engine. It performs parsing of input and rules. It also
 * invokes the parsing algorithm.
 * 
 * @author Kooi
 * @date 13 February 2019 
 *
 */
public class RewriteEngine implements Serializable{
	
	
	
	private Set<RewriteRule> rules;
	private Map<String,List<RewriteRule>> ruleMap;
	private ASTParser parser;

	
	private final static int MAX_ITERATION = 100;
	
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
	
	public RewriteEngine(ASTParser parser) {
		//this.context = context;
		this.parser = parser;
		this.rules = new HashSet<>();		
		ruleMap = new HashMap<>();
		
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * @param rule
	 */
	public void addRule(RewriteRule rule) {
		rules.add(rule);
		
		if(!ruleMap.containsKey(rule.getLhs().getValue())) {
			ruleMap.put(rule.getLhs().getValue(), new ArrayList<>());
			
		}
		ruleMap.get(rule.getLhs().getValue()).add(rule);
	}
	
	
	/**
	 * 
	 * 
	 * Get the set of rewrite rules for the system
	 * 
	 * @return
	 */
	public Set<RewriteRule> getRules(){
		return this.rules;
	}
	
	
	public void deleteRule(RewriteRule rule) {
		rules.remove(rule);
		
		List<RewriteRule> list = ruleMap.get(rule.getLhs().getValue());
		((ArrayList)list).remove(rule);
	}
	
	/**
	 * 
	 * 
	 * Rewrite function to rewrite an infix expression string into it's normal form.
	 * Output returned in post fix notation
	 * 
	 * @param input Infix expression string
	 * @return the normal form of input in post fix notation
	 * @throws ParseException
	 * @throws RewriteException
	 */
	public String rewritePostfix(String input) throws ParseException, RewriteException{
					
		return parser.postOrderTreverse(rewriteNode(input));
		
	}
	
	/**
	 * 
	 * This is the main rewrite function. It takes a string in infix form and return the 
	 * syntax tree node of the result.
	 * 
	 * @param infix expression to rewrite in infix form
	 * @return root Node of the syntax tree of the result
	 * @throws ParseException
	 * @throws RewriteException
	 */
	public Node rewriteNode(String infix) throws ParseException, RewriteException{
		
			int c = 0; //total iteration, prevents infinite
	
			Node root = parser.parseAST(infix);
			if(!checkTerm(root))
				throw new RewriteException("Term to rewrite cannot contain declared variable.");
			boolean flag = false;  //if flag is false then cannot be rewritten any further
			do {
				flag = search(root,null);
				c++;
			}while(flag && c<MAX_ITERATION);
			
			
			
			if(c == MAX_ITERATION)
				throw new RewriteException("Max Itertion reached, possible infinite rewrite");
		
			return root;
	}
	
	/**
	 * 
	 * 
	 * 
	 * 
	 * @param infix String of expression to rewrite in infix form
	 * @return A RewriteResult containing the result of the rewrite
	 * @throws ParseException
	 * @throws RewriteException
	 */
	public RewriteResult rewrite(String infix) throws ParseException,RewriteException{
		
		int c = 0; //total iteration, prevents infinite
		
		Node root = parser.parseAST(infix);
		
		if(!checkTerm(root))
			throw new RewriteException("Term to rewrite cannot contain declared variable.");		
		boolean flag = false;  //if flag is false then cannot be rewritten any further
		StringBuilder lastRule;
		List<RewriteStep> steps = new ArrayList<>();
		
		do {
			lastRule = new StringBuilder();
			flag = search(root,lastRule);
			
			//skips the final step 
			if(flag) {
				RewriteStep step = new RewriteStep(copy(root),lastRule.toString());
				steps.add(step); 
			}
			
			c++;
		}while(flag && c<MAX_ITERATION);
		
		if(c == MAX_ITERATION)
			throw new RewriteException("Max Itertion reached, possible infinite rewrite");
	
		return new RewriteResult(infix,steps,root);
		
	}
	
	public boolean singleSearch(Node node,RewriteRule r) throws RewriteException {
		
		if(!checkTerm(node))
			throw new RewriteException("Term to rewrite cannot contain declared variable.");
		
		
		return singleRewrite(node,r);
		
		
	}
	
	
	/**
	 * 
	 * 
	 * This method implements the algorithm to perform a single rewrite
	 * 
	 * @param t
	 * @param r
	 * @return
	 * @throws RewriteException 
	 */
	public boolean singleRewrite(Node t, RewriteRule r) throws RewriteException {
		
		if(t == null)
			return false;
		
		if(singleRewrite(t.getLeft(),r))
			return true;
		
		if(singleRewrite(t.getRight(),r))
			return true;
		
		Map<String,Node> vars = new HashMap<>();
		
		if(match(t,r.getLhs(),vars)) {
			substitute(t,r.getRhs(),vars);
			return true;
		}
		
		return false;

		
	}
	
	/**
	 * 
	 * Apply a rule to a term just once.
	 * 
	 * 
	 * @param term Term to rewrite
	 * @param rule Rewrite Rule to apply
	 * @return the node after applying the rule
	 * @throws RewriteException 
	 */
	public Node applyRule(Node term,RewriteRule rule) throws RewriteException {
		
		
		if(!checkTerm(term))
			throw new RewriteException("Term to rewrite cannot contain declared variable.");

		
		Map<String,Node> vars = new HashMap<>();
		
		if(match(term,rule.getLhs(),vars)) {
			substitute(term,rule.getRhs(),vars);
			return term;
		}
		return term;
	}
	

	
	/**
	 * 
	 * Call the rewrite method and return the result in infix form
	 * 
	 * @param infix expression to rewrite in infix string
	 * @throws RewriteException 
	 * @return result after rewrite in infix form
	 */
	public String rewriteInfix(String infix) throws ParseException,RewriteException {
		
		return parser.toInfix(rewritePostfix(infix));

	}
	
	
	//private functions for algorithm 
	
	
	
	//matching function to determine if term and rule match, vars map to keep track of 
	//the variables
	private boolean match(Node term , Node rule, Map<String,Node> vars) throws RewriteException {
		
		if(term == null || rule == null) {
			return term == null && rule == null; //they match if both are null
		}
		
		//if is variable then just need to match type and add to var list. 
		if(rule.getNodeType() ==NodeType.VARIABLE) {
			//if variable appeared before, check if current match prev
			if(vars.containsKey(rule.getValue())) {
				return term.equals(vars.get(rule.getValue()));
			}else {
				//if variable type is compatible with operator return type
				if(term.getNodeType()== NodeType.OPERATOR && term.getType() == rule.getType()) {
					vars.put(rule.getValue(), term); 
					return true;
				}else if(term.getNodeType() == NodeType.CONSTANT) {
					vars.put(rule.getValue(), term); 
					return true;
				}else if(term.getNodeType() == NodeType.VARIABLE && term.getType() == rule.getType()) {
					vars.put(rule.getValue(), term); 
					return true;
				}	
				return false;
			}
		}else { //if rule is not a variable then must match symbol
			
			
			if(term.getNodeType() == NodeType.VARIABLE) {
				throw new RewriteException("Term to rewrite cannot contain declared variable.");
			}
			
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
		((ASTNode)term).setNodeType(rule.getNodeType());
		((ASTNode)term).setType(rule.getType());
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
			((ASTNode)m).setNodeType(vars.get(initialValue).getNodeType());
			((ASTNode)m).setType(vars.get(initialValue).getType());
			((ASTNode)m).setRight(copy(vars.get(initialValue).getRight()));
			((ASTNode)m).setLeft(copy(vars.get(initialValue).getLeft()));
			
		}
		swap(m.getLeft(),vars);
		swap(m.getRight(),vars);
	}
	
	
	//copy function to copy one node into a new instance
	public Node copy(Node n) {
		
		if(n == null)
			return null;
		Node newNode = new ASTNode(n.getValue(),copy(n.getLeft()),copy(n.getRight()),n.getType(),n.getNodeType());
		return newNode;
		
	}
	
	//this method checks that a term is well formed
	private boolean checkTerm(Node n) {
		
		if(n == null)
			return true;
		
		if(n.getNodeType() == NodeType.VARIABLE)
			return false;
		
		if(!checkTerm(n.getLeft()))
			return false;
		
		return checkTerm(n.getRight());
	}
	
	//use a post-order traversal to search the syntax tree for matching rule
	//root keeps a reference to the original root so we can swap when we make a copy
	private boolean search(Node n,StringBuilder rName) throws RewriteException {
		
		if(n == null)
			return false;
		
		//if something is rewritten don't rewrite further
		if(search(n.getLeft(),rName))
			return true;
		if(search(n.getRight(),rName))
			return true;
		
		//try all the possible rule that can be match
		if(ruleMap.get(n.getValue()) == null)
			return false;
				
		for(RewriteRule r : ruleMap.get(n.getValue())) {
			Map<String,Node> vars = new HashMap<>();
			
			if(match(n,r.getLhs(),vars)) {
				substitute(n,r.getRhs(),vars);
				if(rName != null)
					rName.append(r.getName());
				return true;
			}
		}
		return false;
	}
	
	

}
