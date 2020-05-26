package com.kooixiuhong.rewritesystem.app.syntaxtree;

/**
 * The interface for an operator.
 * Every operator should be able to compare precedence with another operator
 *
 * @author Kooi
 * @date 29th January 2019
 */
public interface Operator {
    int comparePrecedence(Operator operator);
    DataType getReturnType();
    int getPrecedence();
    String getSymbol();
}
