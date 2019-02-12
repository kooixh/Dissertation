package com.kooi.dissertation.syntaxtree;


/**
 * 
 * An abstract operator class, this class contains the fields and constructor. 
 * 
 * 
 * @author Kooi
 * @date 6th February 2019
 */
public abstract class AbstractOperator implements Operator {
	
	

	//fields
	protected String symbol;
	protected int precedence;
	protected DataType returnType;
	
	
	//constructor
	protected AbstractOperator(String symbol, int precedence) {
		this.symbol = symbol;
		this.precedence = precedence;
		this.returnType = DataType.VOID;
	}
	
	protected AbstractOperator(String symbol, int precedence,DataType returnType) {
		this.symbol = symbol;
		this.precedence = precedence;
		this.returnType = returnType;
	}
	
	
	//HashCode and equals uses symbol, you cannot have different operators with the same symbol
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractOperator other = (AbstractOperator) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}
	

	public abstract int comparePrecedence(Operator op);

	
	
	
	/**
	 * Method to get the precedence of the operator
	 */
	public int getPrecedence() {
		return this.precedence;
	}


	/**
	 * Method to get the symbol of the operator
	 */
	public String getSymbol() {
		return this.symbol;
	}
	
	public DataType getReturnType() {
		return returnType;
	}

}
