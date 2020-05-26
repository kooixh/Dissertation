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

    protected String value;
    protected Node left;
    protected Node right;
    protected DataType type;
    protected NodeType nodeType;

    public ASTNode(String value, DataType type, NodeType nodeType) {
        this.value = value;
        this.type = type;
        this.nodeType = nodeType;
        this.left = null;
        this.right = null;
    }

    @Override
    public String toString() {
        return this.getValue() + "(" + this.getNodeType() + "):" + this.getType();
    }

}
