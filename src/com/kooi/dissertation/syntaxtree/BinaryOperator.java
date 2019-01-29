package com.kooi.dissertation.syntaxtree;

/**
 * 
 * A class for binary operator. This class implements the operator interface. 
 * 
 * @author Kooi
 *
 */
public class BinaryOperator implements Operator {
	
	//fields
	private String symbol;
	private int precedence;
	
	//constructors
	public BinaryOperator(String symbol, int precedence) {
		
		if(symbol == null || precedence < 0) {
			throw new IllegalArgumentException();
		}
		this.symbol = symbol;
		this.precedence = precedence;
	}	
	public BinaryOperator(String symbol) {
		if(symbol == null)
			throw new IllegalArgumentException();
		this.precedence  = 0;
		this.symbol = symbol;
	}
	 

	@Override
	public int comparePrecedence(Operator op) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	/**
	 * Method to get the precedence of the operator
	 */
	public int getPrecedence() {
		return this.precedence;
	}

	@Override
	/**
	 * Method to get the symbol of the operator
	 */
	public String getSymbol() {
		return this.symbol;
	}

}
