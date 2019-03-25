package com.kooi.dissertation.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.rewriter.RewriteEngine;
import com.kooi.dissertation.rewriter.RewriteException;
import com.kooi.dissertation.rewriter.RewriteResult;
import com.kooi.dissertation.rewriter.RewriteRule;
import com.kooi.dissertation.rewriter.RewriteStep;
import com.kooi.dissertation.rewriter.SearchEngine;
import com.kooi.dissertation.rewriter.SearchNode;
import com.kooi.dissertation.syntaxtree.Node;

public class InteractPanel extends JPanel {
	
		
	//components
	private JButton addRuleBtn;
	private JButton addOpBtn;
	private JButton addVarBtn;
	private JTextField rwField;
	private JTextArea resArea;
	private JButton rwButton;
	private JLabel intStat; 
	
	//fields
	private RewriteEngine rw;
	private StringBuilder currResult;
	private Node interactiveRoot;
	
	
	//parent frame
	private HomeScreen home;
	
	//constant
	private final int BOUND = 100;


	/**
	 * Create the panel.
	 */
	public InteractPanel(HomeScreen h,RewriteEngine r) {
		
		home = h;
		rw = r;
		
		//button to show the various windows 
		
		addRuleBtn = new JButton("Add Rule");
		addRuleBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				AddRule ar = new AddRule(home);
				ar.showWindow();
			}
			
		});
		
		
		addOpBtn = new JButton("Add Operator");
		addOpBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				AddOperator ao = new AddOperator(home);
				ao.showWindow();
			}
			
		});
		
		
		addVarBtn = new JButton("Add Variable");
		addVarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddVariable av = new AddVariable(home);
				av.showWindow();
			}
		});
		
		
		
		//setting up the panel
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JPanel innerPane = new JPanel();
		innerPane.setLayout(new BoxLayout(innerPane,BoxLayout.X_AXIS));
		
		innerPane.add(addOpBtn);
		innerPane.add(addVarBtn);
		innerPane.add(addRuleBtn);
		
		this.add(innerPane);
		
		JPanel txtPane = new JPanel();
		txtPane.setLayout(new BoxLayout(txtPane,BoxLayout.X_AXIS));
		txtPane.setPreferredSize(new Dimension(200,50));
		
		
		//input for string to rewrite
		
		rwField = new JTextField(20);
		rwField.setFont(new Font("Century Gothic",Font.PLAIN,12));
		rwField.setMaximumSize(rwField.getPreferredSize());
		txtPane.add(rwField);
		
		
		rwButton = new JButton("Rewrite");
		
		rwButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					
					//reset interactive mode
					if(interactiveRoot != null) {
						intStat.setText("OFF");
						intStat.setForeground(Color.RED);
						interactiveRoot = null;
					}
					
					
					if(rwField.getText().equals("")) {
						JOptionPane.showMessageDialog(InteractPanel.this, "Field cannot be empty, enter a term to be rewritten.","Missing values",JOptionPane.ERROR_MESSAGE);

					}else {
						RewriteResult o = rw.rewrite(rwField.getText());
						currResult = new StringBuilder();
						currResult.append("Initial term: "+rwField.getText()+"\n");
						
						
						for(RewriteStep step : o.getListOfSteps()) {
							currResult.append("\u2b91 "+home.getParser().toInfix(home.getParser().postOrderTreverse(step.getTermRoot())));
							currResult.append(" By "+step.getRule());
							currResult.append("\n");
						}
						
						currResult.append("\nFinal term: "+home.getParser().toInfix(home.getParser().postOrderTreverse(o.getFinalTerm())));
						
						resArea.setText(currResult.toString());
					}
					
					
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(InteractPanel.this, "An error is encountered during parsing, check for mismatch parenthesis.","Parsing Exception",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}catch( RewriteException e2) {
					JOptionPane.showMessageDialog(InteractPanel.this, "An error is encountered during rewriting, check for possible infinite rewrite rule.","Rewrite Exception",JOptionPane.ERROR_MESSAGE);
					e2.printStackTrace();
				}
			}
			
			
		});
		
		txtPane.add(rwButton);
		
		this.add(txtPane);
		
		JPanel resPanelFull = new JPanel();
		resPanelFull.setLayout(new BoxLayout(resPanelFull,BoxLayout.Y_AXIS));		
		resPanelFull.setBorder(BorderFactory.createTitledBorder("Result"));
		
		
		//analysis capabilities panel
		JPanel analPane = new JPanel(); 
		analPane.setLayout(new BoxLayout(analPane,BoxLayout.X_AXIS));
		
		
		JButton intButton = new JButton("Interactive");
		
		
		intStat = new JLabel("OFF");
		intStat.setForeground(Color.RED);
		intStat.setFont(new Font("Century Gothic",Font.PLAIN,12));
		
		
		intButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
		
				//create a selector of all possible rules
				Object[] rules = rw.getRules().toArray();
				RewriteRule r = (RewriteRule)JOptionPane.showInputDialog(
	                    home,
	                    "Choose a rule to apply\n",
	                    "Choose rule to apply",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    rules,
	                    "");
				
				
				if(r != null) {
					
					//if first interactive run, clear resArea and set on 
					if(interactiveRoot == null) {
						try {
							interactiveRoot = home.getParser().parseAST(rwField.getText());
							intStat.setText("ON");
							intStat.setForeground(Color.GREEN);
							resArea.setText("");
						} catch (ParseException e1) {
							JOptionPane.showMessageDialog(InteractPanel.this, "An error is encountered during parsing, check for syntax errors.","Parsing Exception",JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}	
					}
				
					try {
						if(rw.singleSearch(interactiveRoot,r))
							resArea.append("\u2b91 "+home.getParser().toInfix(home.getParser().postOrderTreverse(interactiveRoot))+" By "+r.getName()+"\n");
						else
							resArea.append("\u2b91 "+home.getParser().toInfix(home.getParser().postOrderTreverse(interactiveRoot))+" Rule does not apply!!"+"\n");
					} catch (ParseException e1) {
						JOptionPane.showMessageDialog(InteractPanel.this, "An error is encountered during parsing, check for syntax errors.","Parsing Exception",JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
				
				
			}
		});
		
		JButton searchButton = new JButton("Search");
		
		searchButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			
				SearchWindow sw = new SearchWindow(InteractPanel.this,home);
				sw.setVisible(true);
			}
		});
		
		
		JButton visButton = new JButton("Visualise");
		
		
		visButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchEngine se = new SearchEngine(home.getEngine());
				try {
					Node n = home.getParser().parseAST(rwField.getText());
					SearchNode searchRoot = se.buildSearchTree(n, BOUND);
					
					(new Thread() {
						public void run() {
							SearchTreeVisualiser stv = new SearchTreeVisualiser(home,searchRoot);
							stv.setVisible(true);
						  }
					}).start();
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(InteractPanel.this, "An error is encountered during parsing, check for syntax errors.","Parsing Exception",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			
		});
		
		
		analPane.add(intButton);	
		analPane.add(intStat);
		analPane.add(searchButton);
		analPane.add(visButton);
		
		this.add(analPane);
		//text area for result
		JPanel resPane = new JPanel();
		resPane.setLayout(new BoxLayout(resPane,BoxLayout.Y_AXIS));
		
		
		resArea = new JTextArea();
		resArea.setFont(new Font("Century Gothic",Font.PLAIN,12));
		resArea.setEditable(false);
		resArea.setLineWrap(true);
		resArea.setWrapStyleWord(true);
		resPane.add(resArea);
		
		
		JScrollPane resScroll = new JScrollPane(resPane);
		
		resPanelFull.add(resScroll);
		
		this.add(resPanelFull);
	}
	
	
	
	/**
	 * 
	 * Set the result are in the panel
	 * 
	 * @param text String to set in the Text area
	 * 
	 */
	public void setResultArea(String text) {
		this.resArea.setText(text);
	}
	

}
