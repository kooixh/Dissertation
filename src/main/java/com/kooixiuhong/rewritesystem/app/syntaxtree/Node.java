package com.kooixiuhong.rewritesystem.app.syntaxtree;

import java.io.Serializable;

/**
 * This is the interface for a Node.
 *
 * @author Kooi
 * @date 12 February 2019
 */
public interface Node extends Serializable {
    String getValue();
    DataType getType();
    NodeType getNodeType();
    Node getRight();
    Node getLeft();
    int hashCode();
    boolean equals(Object obj);
}
