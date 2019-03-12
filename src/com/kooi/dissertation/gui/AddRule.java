package com.kooi.dissertation.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.kooi.dissertation.rewriter.RewriteRule;
import com.kooi.dissertation.rewriter.RewriteRuleFactory;

import java.awt.CardLayout;

public class AddRule extends JFrame {

	private JPanel contentPane;
	
	private JTextField lhsTextField;
	private JTextField rhsTextField;
	private JTextField nameField;
	private JButton addBtn;
	
	private HomeScreen home;
	
	private RewriteRuleFactory rFac;


	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public AddRule(HomeScreen h) {
		
		home = h;
		
		rFac = new RewriteRuleFactory(h.getParser());
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				dispose();
			}
		});
		
		
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		
		//add rule button
		
		addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RewriteRule rule = rFac.getRewriteRule(lhsTextField.getText(), rhsTextField.getText(), nameField.getText());
				home.getEngine().addRule(rule);
				home.updateUI();
				AddRule.this.dispose();
			}
			
		});
		
		
		
		//lhs field
		
		JPanel panelLhs = new JPanel();
		lhsTextField = new JTextField(10);
		panelLhs.setLayout(new BoxLayout(panelLhs, BoxLayout.X_AXIS));
		JLabel lhsLab = new JLabel("Left-Hand Side");
		lhsLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		panelLhs.add(lhsLab);
		panelLhs.add(lhsTextField);
		
		
		//rhs field
		
		JPanel panelRhs = new JPanel();
		rhsTextField = new JTextField(10);
		panelRhs.setLayout(new BoxLayout(panelRhs, BoxLayout.X_AXIS));
		JLabel rhsLab = new JLabel("Right-Hand Side");
		rhsLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		panelRhs.add(rhsLab);
		panelRhs.add(rhsTextField);
		
		
		
		//rule name
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel,BoxLayout.X_AXIS));
		JLabel nameLab = new JLabel("Rule name");
		nameLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		namePanel.add(nameLab);
		nameField = new JTextField(10);
		namePanel.add(nameField);
				
		lhsTextField.setMaximumSize( lhsTextField.getPreferredSize() );
		rhsTextField.setMaximumSize( rhsTextField.getPreferredSize() );
		nameField.setMaximumSize(nameField.getPreferredSize());
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
		
		btnPanel.add(addBtn);
		contentPane.add(panelLhs);
		contentPane.add(panelRhs);
		contentPane.add(namePanel);
		contentPane.add(btnPanel);
		setContentPane(contentPane);
		
	}
	
	
	
	public void showWindow() {
		setVisible(true);
	}

}
