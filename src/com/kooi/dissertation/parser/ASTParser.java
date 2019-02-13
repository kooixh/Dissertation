package com.kooi.dissertation.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.kooi.dissertation.syntaxtree.ASTNode;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.ConstantNode;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.OperatorNode;
import com.kooi.dissertation.syntaxtree.UnaryOperator;
import com.kooi.dissertation.syntaxtree.VariableNode;

/**
 * 
 * This is a parser that parse infix equations to post-fix and AST.
 * 
 * The parser requires the set of operations.
 * 
 * @author Kooi
 * @date 4th Febuary 2019
 *
 */
public class ASTParser {
	
	//fields
	private Map<String,Operator> operators; //the operators
	private Map<String,DataType> variables; //variable names and their type
	
	
	//constructor
	public ASTParser(Set<Operator> ops,Map<String,DataType> vars) {
		operators = new HashMap<>();
		variables = vars;
		for(Operator o : ops) {
			operators.put(o.getSymbol(), o);
		}
	}
	
	public ASTParser(Map<String,Operator> ops,Map<String,DataType> vars) {
		operators = ops;
		variables = vars;
	}
	
	/**
	 * 
	 * Iterate through an Abstract syntax tree in post order, values are appended to a string
	 * The post order treversal of an AST is the same as the post fix notaion.
	 * 
	 * @param The root of the AST to treverse.
	 * @return String value of the values in post order. 
	 */
	public String postOrderTreverse(Node n) {
		StringBuilder sb = new StringBuilder();
		
		postOrderTreverseUtil(n,sb);
		
		return sb.toString();
	}
	
	private void postOrderTreverseUtil(Node n, StringBuilder sb) {
		
		if(n == null)
			return;
		
		postOrderTreverseUtil(n.getLeft(),sb);
		postOrderTreverseUtil(n.getRight(),sb);
		
		if(sb.length() == 0)
			sb.append(n.getValue());
		else
			sb.append(" ").append(n.getValue());
		
	}
	
	//helper method to add a node to the stack of node
	private void addNodeToStack(Stack<Node> n, String s) {
		Node right = null;
		Node left = null;
		
		if(!n.isEmpty())
			right = n.pop();
		if(!n.isEmpty())
			left = n.pop();
		
		
		if(operators.keySet().contains(s)) {
			Operator o = operators.get(s);
			if(o instanceof BinaryOperator)
				n.push(new OperatorNode(s,left,right,o.getReturnType()));
			else {
				
				//if it is a unary operator then just pop one operands
				if(left != null)
					n.push(left);
				n.push(new OperatorNode(s,null,right,o.getReturnType()));
			}
		}	
		else if(variables.containsKey(s))
			n.push(new VariableNode(s,left,right,variables.get(s)));
		else
			n.push(new ConstantNode(s,left,right));
	}
	
	/**
	 * 
	 * Method to parse an infix expression into an abstract syntax tree using a modified
	 * Shunting Yard algorithm
	 * 
	 * @param infix A string containing the infix notation
	 * @return root ASTNode of the syntax tree
	 * @throws ParseException Exception is thrown for mismatch parenthesis
	 */
	public Node parseAST(String infix) throws ParseException{
		
		Stack<String> opStack = new Stack<>();
		Stack<Node> nodes = new Stack<>();
		
		
		List<String> list = tokenizeString(infix);
				
		//Begin Shunting Yard

		for(String s:list) {

			//cases, '(' , ')' , is operator, not operator

			if(s.equals("(")) {

				opStack.push("(");
			}

			else if(s.equals(")")) {

				boolean foundOpen = false;

				//append all operator until the opening parenthesis
				while(!opStack.isEmpty()) {
					String p = opStack.pop();

					if(p.equals("(")) {
						foundOpen = true;
						break;
					}else {
						addNodeToStack(nodes,p);
					}
				}

				//mismatch if no opening is found
				if(!foundOpen)
					throw new ParseException("Mismatach parenthesis");

			}else {
				
				if(operators.containsKey(s)) {

					Operator current = operators.get(s);

					while(!opStack.isEmpty()) {
						Operator last = operators.get(opStack.peek());

						//if the last op is opening parenthesis
						if(last == null)
							break;

						//add all the operators that are lower or same precedence as current
						if(current instanceof BinaryOperator && (current.comparePrecedence(last) == 0 || current.comparePrecedence(last) == -1)) {
							opStack.pop();
							addNodeToStack(nodes,last.getSymbol());
						}else {
							break;
						}
					}

					opStack.push(s);

				}else {
					if(variables.containsKey(s))
						nodes.push(new VariableNode(s,null,null,variables.get(s)));
					else
						nodes.push(new ConstantNode(s,null,null));
				}
			}

		}
		
		//add the remaining operators
		while(!opStack.isEmpty()) {
			String c = opStack.pop();
			//there should be no parenthesis left
			if(c.equals("("))
				throw new ParseException("Mismatach parenthesis");
			addNodeToStack(nodes,c);
		}

		//end shunting yard
		
		
		return nodes.peek();
	}
	
	
	/**
	 * 
	 * Method to parse an infix expression to Reverse Polish Notation (RPN) using the 
	 * Shunting Yard algorithm
	 * 
	 * 
	 * @param infix A string containing the infix notation
	 * @return The expression in RPN 
	 * @throws ParseException Exception is thrown for mismatch parenthesis
	 */
	public String parseRPN(String infix) throws ParseException {
		
		StringBuilder sb = new StringBuilder();
		Stack<String> opStack = new Stack<>(); //operator stack
		List<String> list = tokenizeString(infix);
		
		//Begin Shunting Yard
		
		for(String s:list) {
			
			//cases, '(' , ')' , is operator, not operator
			
			if(s.equals("(")) {
				
				opStack.push("(");
			}
			
			else if(s.equals(")")) {
				
				boolean foundOpen = false;
				
				//append all operator until the opening parenthesis
				while(!opStack.isEmpty()) {
					String p = opStack.pop();
					
					if(p == "(") {
						foundOpen = true;
						break;
					}else {
						sb.append(" ").append(p);
					}
				}
				
				//mismatch if no opening is found
				if(!foundOpen)
					throw new ParseException("Mismatach parenthesis");
				
			}else {
				
				if(operators.containsKey(s)) {
					
					Operator current = operators.get(s);
					
					while(!opStack.isEmpty()) {
						Operator last = operators.get(opStack.peek());
						
						//if the last op is opening parenthesis
						if(last == null)
							break;
						
						//add all the operators that are lower or same precedence as current
						if(current instanceof BinaryOperator && (current.comparePrecedence(last) == 0 || current.comparePrecedence(last) == -1)) {
							opStack.pop();
							if(sb.length()==0)
								sb.append(s);
							else
								sb.append(" ").append(last.getSymbol());
						}else {
							break;
						}
					}
					
					opStack.push(s);
					
				}else {
					if(sb.length()==0)
						sb.append(s);
					else
						sb.append(" ").append(s);
				}
			}
			
		}
		//add the remaining operators
        while(!opStack.isEmpty()) {
        	String c = opStack.pop();
        	//there should be no parenthesis left
        	if(c.equals("("))
        		throw new ParseException("Mismatach parenthesis");
            sb.append(" ").append(c);
        }
	
        //end shunting yard
        
		return sb.toString();
		
		
	}
	
	
	public String toInfix(String rpn) throws ParseException{
		
		
		StringBuilder sb = new StringBuilder();
		Stack<String> exprStack = new Stack<>();
		
		List<String> tokens = Arrays.asList(rpn.split(" "));
		
		for(String s: tokens) {
			
			if(!operators.containsKey(s)) { //if not operator
				exprStack.push(s);
			}else {
				
				//if unary
				Operator o = operators.get(s);
				
				if(o instanceof UnaryOperator) {
					String next = "("+o.getSymbol()+exprStack.pop()+")";
					exprStack.push(next);
				}else {
					String r = exprStack.pop();
					String l = exprStack.pop();
					String next = "("+l+o.getSymbol()+r+")";
					exprStack.push(next);
				}
				
			}
		}
		
		return exprStack.pop();
	
	}
	
	/**
	 * Method to tokenize a string based on the operators. 
	 * 
	 * e.g
	 * "12+2-2" -> {"12","+","2","2","2"}
	 * 
	 * 
	 * @param s String to split
	 * @return ArrayList of string after split
	 */
	public List<String> tokenizeString(String s){
		
		//remove white spaces from the input
		s = s.replaceAll("\\s+","");
	
		//build the regex
		StringBuilder regex = new StringBuilder();
		
		for(String c: operators.keySet()) {
			if(isMetaCharacter(c))
				regex.append("(\\"+c+")|"); //escape the special character
			else
				regex.append("("+c+")|");
		}
		regex.append("(\\()|");
		regex.append("(\\))");

		//using java string split and look ahead and look behind regex,
		//we can split the string while preserving the delim(operators)

		String[] arr = s.split("((?<=("+regex.toString()+"))|(?=("+regex.toString()+")))");

		return Arrays.asList(arr);
	}
	
	
	//helper function to check if a string is a meta character
	private boolean isMetaCharacter(String s) {
		
		if(s.length() > 1)
			return false;
		
		char c = s.charAt(0);
		
		return (c == ')' || c == '(' || c == '[' || c == ']' || c == '{' || c == '}' || c == '\\' || c == '^' || c == '$'
				|| c == '|'|| c == '?'|| c == '*'|| c == '+' || c == '.' || c == '<'|| c == '>'|| c == '-'|| c == '=' || c == '!');

	}

}
