package com.kooi.dissertation.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.kooi.dissertation.syntaxtree.ASTNode;
import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Node;
import com.kooi.dissertation.syntaxtree.NodeType;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.UnaryOperator;

/**
 * 
 * This is a parser that parse infix equations to post-fix and AST. Parser contains
 * methods to convert traverse AST and convert post-fix to infix
 * 
 * The parser requires a context.
 * 
 * @author Kooi
 * @date 4th Febuary 2019
 *
 */
public class ASTParser implements Serializable{
	
	//fields
	private Signature context;
	
	//constructor
	public ASTParser(Signature context) {
		this.context = context;
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
	
	//helper method to add an operator node to the stack of node
	private void addNodeToStack(Stack<Node> n, String s) {
		Node right = null;
		Node left = null;
		
		if(!n.isEmpty())
			right = n.pop();
		if(!n.isEmpty())
			left = n.pop();
		
			Operator o = context.getOperator(s);
			if(o instanceof BinaryOperator)
				n.push(new ASTNode(s,left,right,o.getReturnType(),NodeType.OPERATOR));
			else {
				
				//if it is a unary operator then just pop one operands
				if(left != null)
					n.push(left);
				n.push(new ASTNode(s,null,right,o.getReturnType(),NodeType.OPERATOR));
			}
	
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
				
				if(context.isOperator(s)) {

					Operator current = context.getOperator(s);

					while(!opStack.isEmpty()) {
						Operator last = context.getOperator(opStack.peek());

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
					if(context.isVariable(s))
						nodes.push(new ASTNode(s,null,null,context.getVariable(s),NodeType.VARIABLE));
					else
						nodes.push(new ASTNode(s,null,null,DataType.CONST,NodeType.CONSTANT));
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
				
				if(context.isOperator(s)) {
					
					Operator current = context.getOperator(s);
					
					while(!opStack.isEmpty()) {
						Operator last = context.getOperator(opStack.peek());
						
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
	
	/**
	 * 
	 * This method converts an expression in postfix form to infix
	 * 
	 * @param rpn Expression in postfix 
	 * @return The expression in infix form.
	 * @throws ParseException
	 */
	public String toInfix(String rpn) throws ParseException{
		
		
		StringBuilder sb = new StringBuilder();
		Stack<String> exprStack = new Stack<>();
		
		List<String> tokens = Arrays.asList(rpn.split(" "));
		
		for(int i =0;i<tokens.size();i++) {
			String s = tokens.get(i);
			
			if(!context.isOperator(s)) { //if not operator
				exprStack.push(s);
			}else {
				
				//if unary
				Operator o = context.getOperator(s);
				
				if(o instanceof UnaryOperator) {
					String next = "("+o.getSymbol()+" "+exprStack.pop()+")";
					exprStack.push(next);
				}else {
					String r = exprStack.pop();
					String l = exprStack.pop();
					
					//omit the last parenthesis
					if(i != tokens.size()-1) {
						String next = "("+l+" "+o.getSymbol()+" "+r+")";
						exprStack.push(next);
					}else {
						String next = l+" "+o.getSymbol()+" "+r;
						exprStack.push(next);
					}
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
		
		for(String c: context.getOperatorSet()) {
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
