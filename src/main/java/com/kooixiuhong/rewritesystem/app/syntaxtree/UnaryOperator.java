package com.kooixiuhong.rewritesystem.app.syntaxtree;

/**
 * A class that represents a Unary operator. This class extends the abstract operator class.
 *
 * @author Kooi
 * @date 6th February 2019
 */
public class UnaryOperator extends AbstractOperator {

    public UnaryOperator(String symbol, int precedence, DataType returnType) {
        super(symbol, precedence, returnType);
    }

    /**
     *
     * Comparing the precedence of this operator with another. Unary operators always have
     * higher precedence compared to binary operator.
     *
     * @return 1 is current operator has higher precedence, 0 if equal, -1 is lower
     */
    @Override
    public int comparePrecedence(Operator operator) {
        return (operator instanceof BinaryOperator) ? 1 : Integer.compare(this.precedence, operator.getPrecedence());
    }

}
