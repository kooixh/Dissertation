package com.kooixiuhong.rewritesystem.app.syntaxtree;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a node for an abstract syntax tree. the AST is in the form
 * of a binary tree with each node having a string value.
 *
 * @author Kooi
 * @date 30th January 2019
 */
@AllArgsConstructor
@Data
public class ASTNode implements Node {
    //fields
    protected String value;
    protected Node left;
    protected Node right;
    protected DataType type;
    protected NodeType nodeType;

    public ASTNode(String value, DataType type, NodeType nodeType) {
        this.value = value;
        this.type = type;
        this.nodeType = nodeType;
        left = null;
        right = null;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    //getters and setters
    public void setRight(Node right) {
        this.right = right;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public String toString() {
        return this.getValue() + "(" + this.getNodeType() + "):" + this.getType();
    }


}
