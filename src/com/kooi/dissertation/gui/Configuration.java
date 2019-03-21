package com.kooi.dissertation.gui;

import java.io.Serializable;

import com.kooi.dissertation.parser.ASTParser;
import com.kooi.dissertation.parser.Signature;
import com.kooi.dissertation.rewriter.RewriteEngine;
/**
 * 
 * 
 * This is a configuration file to hold the save data, including engine, parser and signature
 * 
 * @author Kooi
 *
 */
public class Configuration implements Serializable{
	
	private RewriteEngine rw;
	private ASTParser parser;
	private Signature sig;
	
	
	public Configuration(RewriteEngine rw,ASTParser p, Signature sig) {
		this.rw = rw;
		this.parser = p;
		this.sig = sig;
	}
	
	public RewriteEngine getRw() {
		return rw;
	}
	public ASTParser getParser() {
		return parser;
	}
	public Signature getSig() {
		return sig;
	}
	
	
	
	

}
