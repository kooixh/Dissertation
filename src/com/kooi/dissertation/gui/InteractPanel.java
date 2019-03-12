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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.kooi.dissertation.parser.ParseException;
import com.kooi.dissertation.rewriter.RewriteEngine;
import com.kooi.dissertation.rewriter.RewriteException;
import com.kooi.dissertation.rewriter.RewriteResult;
import com.kooi.dissertation.rewriter.RewriteStep;

public class InteractPanel extends JPanel {
	
		
	//components
	private JButton addRuleBtn;
	private JButton addOpBtn;
	private JButton addVarBtn;
	private JTextField rwField;
	private JTextArea resArea;
	private JButton rwButton;
	
	//fields
	private RewriteEngine rw;
	private StringBuilder currResult;
	
	
	//parent frame
	private HomeScreen home;


	/**
	 * Create the panel.
	 */
	public InteractPanel(HomeScreen h,RewriteEngine r) {
		
		home = h;
		rw = r;
		
		
		
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
		
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JPanel innerPane = new JPanel();
		innerPane.setLayout(new BoxLayout(innerPane,BoxLayout.X_AXIS));
		
		innerPane.add(addOpBtn);
		innerPane.add(addVarBtn);
		innerPane.add(addRuleBtn);
		
		this.add(innerPane);
		
		JPanel txtPane = new JPanel();
		txtPane.setLayout(new FlowLayout());
		txtPane.setPreferredSize(new Dimension(200,50));
		
		
		//input for string to rewrite
		
		rwField = new JTextField(20);
		rwField.setFont(new Font("Century Gothic",Font.PLAIN,12));
		txtPane.add(rwField);
		
		
		rwButton = new JButton("Rewrite");
		
		rwButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					RewriteResult o = rw.rewrite(rwField.getText());
					currResult = new StringBuilder();
					
					for(RewriteStep step : o.getListOfSteps()) {
						currResult.append(home.getParser().toInfix(home.getParser().postOrderTreverse(step.getTermRoot())));
						currResult.append(" By "+step.getRule());
						currResult.append("\n");
					}
					
					currResult.append("\nFinal term: "+home.getParser().toInfix(home.getParser().postOrderTreverse(o.getFinalTerm())));
					
					resArea.setText(currResult.toString());
					
				} catch (ParseException | RewriteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		});
		
		txtPane.add(rwButton);
		
		this.add(txtPane);
		
		JPanel resPanelFull = new JPanel();
		resPanelFull.setLayout(new BoxLayout(resPanelFull,BoxLayout.Y_AXIS));		
		resPanelFull.setBorder(BorderFactory.createTitledBorder("Result"));
		
		
		//text area for result
		JPanel resPane = new JPanel();
		resPane.setLayout(new BoxLayout(resPane,BoxLayout.Y_AXIS));
		
		
		resArea = new JTextArea();
		resArea.setFont(new Font("Century Gothic",Font.PLAIN,18));
		resArea.setEditable(false);
		resArea.setLineWrap(true);
		resArea.setWrapStyleWord(true);
		resPane.add(resArea);
		resArea.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Sit amet consectetur adipiscing elit pellentesque habitant morbi tristique. Massa sapien faucibus et molestie ac feugiat sed lectus. Varius quam quisque id diam vel quam. Neque vitae tempus quam pellentesque nec nam aliquam. Consequat interdum varius sit amet mattis vulputate enim nulla. Lectus quam id leo in vitae turpis massa sed. Quam elementum pulvinar etiam non quam lacus. Purus");
		
		JScrollPane resScroll = new JScrollPane(resPane);
		//resScroll.setPreferredSize(new Dimension(200,250));
		
		resPanelFull.add(resScroll);
		
		this.add(resPanelFull);
		

	}

}
