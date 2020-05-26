package com.kooixiuhong.rewritesystem.app.parser;

import com.kooixiuhong.rewritesystem.app.syntaxtree.ASTNode;
import com.kooixiuhong.rewritesystem.app.syntaxtree.BinaryOperator;
import com.kooixiuhong.rewritesystem.app.syntaxtree.DataType;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Node;
import com.kooixiuhong.rewritesystem.app.syntaxtree.NodeType;
import com.kooixiuhong.rewritesystem.app.syntaxtree.Operator;
import com.kooixiuhong.rewritesystem.app.syntaxtree.UnaryOperator;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * This is a parser that parse infix equations to post-fix and AST. Parser contains
 * methods to convert traverse AST and convert post-fix to infix
 * <p>
 * The parser requires a context.
 *
 * @author Kooi
 * @date 4th Febuary 2019
 */
@AllArgsConstructor
public class ASTParser implements Serializable {

    private Signature signature;

    /**
     * Iterate through an Abstract syntax tree in post order, values are appended to a string
     * The post order treversal of an AST is the same as the post fix notaion.
     *
     * @param node root of the AST to treverse.
     * @return String value of the values in post order.
     */
    public String postOrderTraversal(Node node) {
        StringBuilder stringBuilder = new StringBuilder();
        postOrderTraversalUtil(node, stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * Method to parse an infix expression into an abstract syntax tree using a modified
     * Shunting Yard algorithm
     *
     * @param infix A string containing the infix notation
     * @return root ASTNode of the syntax tree
     * @throws ParseException Exception is thrown for mismatch parenthesis
     */
    public Node parseAST(String infix) throws ParseException {

        Stack<String> opStack = new Stack<>();
        Stack<Node> nodes = new Stack<>();
        List<String> list = tokenizeString(infix);

        if (!verifyTerm(list))
            throw new ParseException("Syntax Error");

        //Begin Shunting Yard
        for (String symbol : list) {
            if (symbol.equals("(")) {
                opStack.push("(");
            } else if (symbol.equals(")")) {
                boolean foundOpen = false;
                while (!opStack.isEmpty()) { //append all operator until the opening parenthesis
                    String previous = opStack.pop();
                    if (previous.equals("(")) {
                        foundOpen = true;
                        break;
                    } else {
                        addNodeToStack(nodes, previous);
                    }
                }
                if (!foundOpen) //mismatch if no opening is found
                    throw new ParseException("Mismatch parenthesis");
            } else {
                if (signature.isOperator(symbol)) {
                    Operator current = signature.getOperator(symbol);
                    while (!opStack.isEmpty()) {
                        Operator last = signature.getOperator(opStack.peek());
                        if (last == null) //if the last op is opening parenthesis
                            break;
                        //add all the operators that are lower or same precedence as current
                        if (current instanceof BinaryOperator && (current.comparePrecedence(last) == 0 || current.comparePrecedence(last) == -1)) {
                            opStack.pop();
                            addNodeToStack(nodes, last.getSymbol());
                        } else {
                            break;
                        }
                    }
                    opStack.push(symbol);
                } else {
                    if (signature.isVariable(symbol))
                        nodes.push(new ASTNode(symbol, null, null, signature.getVariable(symbol), NodeType.VARIABLE));
                    else
                        nodes.push(new ASTNode(symbol, null, null, DataType.CONST, NodeType.CONSTANT));
                }
            }
        }
        //add the remaining operators
        while (!opStack.isEmpty()) {
            String previous = opStack.pop();
            //there should be no parenthesis left
            if (previous.equals("("))
                throw new ParseException("Mismatch parenthesis");
            addNodeToStack(nodes, previous);
        }
        //end shunting yard

        return nodes.peek();
    }


    /**
     * Method to parse an infix expression to Reverse Polish Notation (RPN) using the
     * Shunting Yard algorithm
     *
     * @param infix A string containing the infix notation
     * @return The expression in RPN
     * @throws ParseException Exception is thrown for mismatch parenthesis
     */
    public String parseRPN(String infix) throws ParseException {

        StringBuilder stringBuilder = new StringBuilder();
        Stack<String> opStack = new Stack<>(); //operator stack
        List<String> tokens = tokenizeString(infix);

        if (!verifyTerm(tokens))
            throw new ParseException("Syntax Error");

        //Begin Shunting Yard
        for (String symbol : tokens) {
            if (symbol.equals("(")) {
                opStack.push("(");
            } else if (symbol.equals(")")) {
                boolean foundOpen = false;
                //append all operator until the opening parenthesis
                while (!opStack.isEmpty()) {
                    String previous = opStack.pop();
                    if (previous.equals("(")) {
                        foundOpen = true;
                        break;
                    } else {
                        stringBuilder.append(" ").append(previous);
                    }
                }
                if (!foundOpen)
                    throw new ParseException("Mismatach parenthesis");
            } else {
                if (signature.isOperator(symbol)) {
                    Operator current = signature.getOperator(symbol);
                    while (!opStack.isEmpty()) {
                        Operator last = signature.getOperator(opStack.peek());
                        //if the last op is opening parenthesis
                        if (last == null)
                            break;
                        //add all the operators that are lower or same precedence as current
                        if (current instanceof BinaryOperator && (current.comparePrecedence(last) == 0 || current.comparePrecedence(last) == -1)) {
                            opStack.pop();
                            if (stringBuilder.length() == 0)
                                stringBuilder.append(symbol);
                            else
                                stringBuilder.append(" ").append(last.getSymbol());
                        } else {
                            break;
                        }
                    }
                    opStack.push(symbol);
                } else {
                    if (stringBuilder.length() == 0)
                        stringBuilder.append(symbol);
                    else
                        stringBuilder.append(" ").append(symbol);
                }
            }
        }
        //add the remaining operators
        while (!opStack.isEmpty()) {
            String previous = opStack.pop();
            //there should be no parenthesis left
            if (previous.equals("("))
                throw new ParseException("Mismatch parenthesis");
            stringBuilder.append(" ").append(previous);
        }
        return stringBuilder.toString();
    }

    /**
     * This method converts an expression in postfix form to infix
     *
     * @param rpn Expression in postfix
     * @return The expression in infix form.
     * @throws ParseException
     */
    public String toInfix(String rpn) throws ParseException {

        Stack<String> exprStack = new Stack<>();
        List<String> tokens = Arrays.asList(rpn.split(" "));

        for (int i = 0; i < tokens.size(); i++) {
            String s = tokens.get(i);
            if (!signature.isOperator(s)) { //if not operator
                exprStack.push(s);
            } else {
                //if unary
                Operator operator = signature.getOperator(s);

                if (operator instanceof UnaryOperator) {
                    String next = "(" + operator.getSymbol() + " " + exprStack.pop() + ")";
                    exprStack.push(next);
                } else {
                    String right = exprStack.pop();
                    String left = exprStack.pop();
                    //omit the last parenthesis
                    if (i != tokens.size() - 1) {
                        String next = "(" + left + " " + operator.getSymbol() + " " + right + ")";
                        exprStack.push(next);
                    } else {
                        String next = left + " " + operator.getSymbol() + " " + right;
                        exprStack.push(next);
                    }
                }
            }
        }

        return exprStack.pop();

    }

    /**
     * Method to tokenize a string based on the operators.
     * <p>
     * e.g
     * "12+2-2" -> {"12","+","2","-","2"}
     *
     * @param expression String to split
     * @return ArrayList of string after split
     */
    public List<String> tokenizeString(String expression) {
        //remove white spaces from the input
        expression = expression.replaceAll("\\s+", "");
        //build the regex
        StringBuilder regex = new StringBuilder();
        for (String character : signature.getOperatorSet()) {
            if (isMetaCharacter(character))
                regex.append("(\\" + character + ")|"); //escape the special character
            else
                regex.append("(" + character + ")|");
        }
        regex.append("(\\()|");
        regex.append("(\\))");

        //using java string split and look ahead and look behind regex,
        //we can split the string while preserving the delim(operators)
        String[] arr = expression.split("((?<=(" + regex.toString() + "))|(?=(" + regex.toString() + ")))");

        return Arrays.asList(arr);
    }

    private void postOrderTraversalUtil(Node node, StringBuilder stringBuilder) {
        if (node == null)
            return;
        postOrderTraversalUtil(node.getLeft(), stringBuilder);
        postOrderTraversalUtil(node.getRight(), stringBuilder);
        if (stringBuilder.length() == 0)
            stringBuilder.append(node.getValue());
        else
            stringBuilder.append(" ").append(node.getValue());
    }

    private void addNodeToStack(Stack<Node> nodes, String symbol) {
        Node right = null;
        Node left = null;
        if (!nodes.isEmpty())
            right = nodes.pop();
        if (!nodes.isEmpty())
            left = nodes.pop();
        Operator operator = signature.getOperator(symbol);
        if (operator instanceof BinaryOperator)
            nodes.push(new ASTNode(symbol, left, right, operator.getReturnType(), NodeType.OPERATOR));
        else {
            //if it is a unary operator then just pop one operands
            if (left != null)
                nodes.push(left);
            nodes.push(new ASTNode(symbol, null, right, operator.getReturnType(), NodeType.OPERATOR));
        }
    }


    private boolean verifyTerm(List<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (signature.isOperator(token)) {
                Operator operator = signature.getOperator(token);
                if (operator instanceof BinaryOperator) {
                    if (i == 0 || i == tokens.size() - 1 || signature.isOperator(tokens.get(i - 1))
                            || tokens.get(i - 1).equals("(") || tokens.get(i + 1).equals(")")
                            || (signature.isOperator(tokens.get(i + 1)) && signature.getOperator(tokens.get(i + 1)) instanceof BinaryOperator)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    //helper function to check if a string is a meta character
    private boolean isMetaCharacter(String expression) {
        if (expression.length() > 1)
            return false;
        char c = expression.charAt(0);
        return (c == ')' || c == '(' || c == '[' || c == ']' || c == '{' || c == '}' || c == '\\' || c == '^' || c == '$'
                || c == '|' || c == '?' || c == '*' || c == '+' || c == '.' || c == '<' || c == '>' || c == '-' || c == '=' || c == '!');
    }

}
