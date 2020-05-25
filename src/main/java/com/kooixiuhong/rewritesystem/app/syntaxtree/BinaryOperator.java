package com.kooixiuhong.rewritesystem.app.syntaxtree;

/**
 * A class for binary operator. This class extends the abstractoperator class.
 *
 * @author Kooi
 * @date 9th January 2019
 */
public class BinaryOperator extends AbstractOperator {

    //Constructor
    public BinaryOperator(String symbol, int precedence, DataType returnType) {
        super(symbol, precedence, returnType);
    }

    @Override
    public int comparePrecedence(Operator o) {
        if (o instanceof BinaryOperator) {
            return Integer.compare(this.precedence, o.getPrecedence());
        } else {
            //compare precedence using the other operator,
            return -1; //negate cause from the other side
        }
    }


}
