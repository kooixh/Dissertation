package com.kooixiuhong.rewritesystem.app.gui;

import com.kooixiuhong.rewritesystem.app.parser.ASTParser;
import com.kooixiuhong.rewritesystem.app.parser.Signature;
import com.kooixiuhong.rewritesystem.app.rewriter.RewriteEngine;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * This is a configuration file to hold the save data, including engine, parser and signature
 *
 * @author Kooi
 */
@AllArgsConstructor
@Getter
public class Configuration implements Serializable {
    private RewriteEngine rewriteEngine;
    private ASTParser parser;
    private Signature signature;
}
