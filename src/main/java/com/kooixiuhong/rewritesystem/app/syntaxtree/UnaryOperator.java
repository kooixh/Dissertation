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

    @Override
    /**
     *
     * Comparing the precedence of this operator with another. Unary operators always have
     * higher precedence compared to binary operator.
     *
     * @return 1 is current operator has higher precedence, 0 if equal, -1 is lower
     */
    public int comparePrecedence(Operator op) {
        if (op instanceof BinaryOperator)
            return 1;

        return Integer.compare(this.precedence, op.getPrecedence());
    }


}
