package com.kooi.dissertation.syntaxtree;

/**
 * 
 * The interface for an operator.
 * Every operator should be able to compare precedence with another operator
 * 
 * @author Kooi
 * @date 29th January 2019
 *
 */
public interface Operator {
	
	
	public int comparePrecedence(Operator op);
	public int getPrecedence();
	public String getSymbol();

}
