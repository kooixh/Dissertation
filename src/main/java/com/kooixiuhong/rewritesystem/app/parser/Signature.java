package com.kooixiuhong.rewritesystem.app.parser;

import com.kooixiuhong.rewritesystem.app.syntaxtree.DataType;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Operator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is use to represent a context. A context is the set of variables and operators
 * used to perform parsing and rewriting.
 *
 * @author Kooi
 * @date 14 February 2019
 */
public class Signature implements Serializable {

    private Map<String, Operator> operators;
    private Map<String, DataType> variables;

    //constructors
    public Signature(Set<Operator> ops, Map<String, DataType> vars) {
        this.operators = new HashMap<>();
        for (Operator o : ops) {
            operators.put(o.getSymbol(), o);
        }
        this.variables = vars;
    }

    public Signature(Map<String, Operator> ops, Map<String, DataType> vars) {
        this.operators = ops;
        this.variables = vars;
    }

    public Signature() {
        this.operators = new HashMap<>();
        this.variables = new HashMap<>();
    }


    /**
     * Add a new variable to the context. Existing variable with the same name will be overridden.
     *
     * @param id   name of the variable
     * @param type type of the variable, using DataType Enum
     */
    public void addVariable(String id, DataType type) {
        variables.put(id, type);
    }

    /**
     * Check if a given id is a variable in the context
     *
     * @param id name of the variable
     * @return True if id is a variable in the context. Otherwise false
     */
    public boolean isVariable(String id) {
        return variables.containsKey(id);
    }

    /**
     * Return the DataType of a given variable
     *
     * @param id name of variable
     * @return DataType of the variable
     */
    public DataType getVariable(String id) {
        return variables.get(id);
    }

    /**
     * Add a new operator to the context. Operator will override existing operator if
     * the symbol already in context.
     *
     * @param o Operator to add.
     */
    public void addOperator(Operator o) {
        operators.put(o.getSymbol(), o);
    }

    /**
     * Check if a given symbol is an operator in the context
     *
     * @param symbol for operator
     * @return true if there is an operator with the given symbol in the context, else false.
     */
    public boolean isOperator(String symbol) {
        return operators.containsKey(symbol);
    }

    /**
     * Return the operator with the symbol.
     *
     * @param symbol Symbol of operator
     * @return The operator object, null if operator not in context
     */
    public Operator getOperator(String symbol) {
        return operators.get(symbol);
    }

    /**
     * This method return all the symbols that are operator.
     *
     * @return set of string of all operator symbols
     */
    public Set<String> getOperatorSet() {
        return operators.keySet();
    }

    /**
     * This method return all the symbols that are operator.
     *
     * @return set of string of all operator symbols
     */
    public Set<String> getVariableSet() {
        return variables.keySet();
    }

    public void deleteOperator(String op) {
        operators.remove(op);
    }

    public void deleteVariable(String var) {
        variables.remove(var);
    }

}
