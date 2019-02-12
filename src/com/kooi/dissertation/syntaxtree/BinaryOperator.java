package com.kooi.dissertation.syntaxtree;

/**
 * 
 * A class for binary operator. This class extends the abstractoperator class. 
 * 
 * @author Kooi
 * @date 9th January 2019
 *
 */
public class BinaryOperator extends AbstractOperator {
	
	private DataType lhsType;
	private DataType rhsType;
	 
	//Constructor
	public BinaryOperator(String symbol, int precedence,DataType rhs,DataType lhs,DataType returnType) {
		super(symbol, precedence,returnType);
		lhsType = lhs;
		rhsType = rhs;
	}
	

	@Override
	public int comparePrecedence(Operator o) {
		if(o instanceof BinaryOperator) {
            return this.precedence > o.getPrecedence() ? 1 :
                    o.getPrecedence() == this.precedence ? 0 : -1;
        } else {
        	//compare precedence using the other operator, 
            return -o.comparePrecedence(this); //negate cause from the other side
        }
	}


}
