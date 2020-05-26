package com.kooixiuhong.rewritesystem.app.syntaxtree;

/**
 * A class for binary operator. This class extends the abstractoperator class.
 *
 * @author Kooi
 * @date 9th January 2019
 */
public class BinaryOperator extends AbstractOperator {

    public BinaryOperator(String symbol, int precedence, DataType returnType) {
        super(symbol, precedence, returnType);
    }

    @Override
    public int comparePrecedence(Operator operator) {
        return (operator instanceof BinaryOperator) ? Integer.compare(this.precedence, operator.getPrecedence()) : -1;
    }

}
