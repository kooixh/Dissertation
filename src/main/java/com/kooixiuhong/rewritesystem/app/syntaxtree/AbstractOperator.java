package com.kooixiuhong.rewritesystem.app.syntaxtree;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * An abstract operator class, this class contains the fields and constructor.
 *
 * @author Kooi
 * @date 6th February 2019
 */
@AllArgsConstructor
@Data
public abstract class AbstractOperator implements Operator, Serializable {
    protected String symbol;
    protected int precedence;
    protected DataType returnType;

    public abstract int comparePrecedence(Operator op);

}
