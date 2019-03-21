package com.kooi.dissertation.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.kooi.dissertation.syntaxtree.BinaryOperator;
import com.kooi.dissertation.syntaxtree.DataType;
import com.kooi.dissertation.syntaxtree.Operator;
import com.kooi.dissertation.syntaxtree.UnaryOperator;

public class AddOperator extends JFrame {

	private JPanel contentPane;

	private JTextField symbolField;
	private JTextField precedenceField;
	private JComboBox opType;
	private JComboBox opRetType;
	private JButton addBtn;
	
	private HomeScreen home;
	
	
	
	
	private Map<String,DataType> typeMap;
	
	
	/**
	 * Create the frame.
	 */
	public AddOperator(HomeScreen h) {
		
		
		typeMap = new HashMap<>();
		
		
		typeMap.put("INT", DataType.INT);
		typeMap.put("NAT", DataType.NAT);
		typeMap.put("STRING", DataType.STRING);
		typeMap.put("BOOLEAN", DataType.BOOLEAN);
		
		home = h;
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				dispose();
			}
		});
		
		
		
		//Add operator button
		addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				
				String symbol = symbolField.getText();
				String precd = precedenceField.getText();

				
				if(symbol.equals("") || precd.equals("")) {
					JOptionPane.showMessageDialog(AddOperator.this, "Field cannot be empty.","Missing values",JOptionPane.ERROR_MESSAGE);
				}	
				else if(symbol.equals("{") || symbol.equals("}") || symbol.equals("(") || symbol.equals(")") || symbol.equals("[") || symbol.equals("]")) {
					JOptionPane.showMessageDialog(AddOperator.this, "Your operator symbol is invalid, cannot be any sort of brackets.","Invalid values",JOptionPane.ERROR_MESSAGE);
				}else {
					
					
					//int precedence = 0;
					try {
						int precedence = Integer.parseInt(precd);
						if(precedence <=0 ) {
							JOptionPane.showMessageDialog(AddOperator.this, "Precedence must be a number greater than 0.","Invalid values",JOptionPane.ERROR_MESSAGE);

						}else {
							Operator o;
							
							if(opType.getSelectedIndex() == 0 ) {
								o = new UnaryOperator(symbol,precedence,typeMap.get(opRetType.getSelectedItem()));
							}else {
								o = new BinaryOperator(symbol,precedence,typeMap.get(opRetType.getSelectedItem()));
							}
	
							home.getSig().addOperator(o);
							home.updateUI();
							AddOperator.this.dispose();
						}
					}catch (NumberFormatException nfe){
						JOptionPane.showMessageDialog(AddOperator.this, "Precedence must be a number greater than 0.","Invalid values",JOptionPane.ERROR_MESSAGE);
					}
					
					
				}
				
				

				
				
			}
		});
		
		
		
		//setting up frame
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);
		
		
		
		//panel for symbol text field
		JPanel panelSymbol = new JPanel();
		panelSymbol.setLayout(new BoxLayout(panelSymbol,BoxLayout.X_AXIS));
		JLabel symLab = new JLabel("Operator Symbol");
		symLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		symbolField = new JTextField(10);
		symbolField.setMaximumSize(symbolField.getPreferredSize());
		
		panelSymbol.add(symLab);
		panelSymbol.add(symbolField);
		
		
		//panel for operator type

		JPanel panelType = new JPanel();
		panelType.setLayout(new BoxLayout(panelType,BoxLayout.X_AXIS));
		JLabel typeLab = new JLabel("Operator type");
		typeLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		
		String[] ops = {"Unary","Binary"};
		opType = new JComboBox(ops);
		opType.setMaximumSize(opType.getPreferredSize());
		panelType.add(typeLab);
		panelType.add(opType);
		
		
		//panel for precedence
		
		JPanel panelPrec = new JPanel();
		panelPrec.setLayout(new BoxLayout(panelPrec,BoxLayout.X_AXIS));
		JLabel precLab = new JLabel("Operator precedence");
		precLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		precedenceField = new JTextField(10);
		precedenceField.setMaximumSize(precedenceField.getPreferredSize());
		
		panelPrec.add(precLab);
		panelPrec.add(precedenceField);
		
		
		
		//panel for return type
		
		JPanel rtPanel = new JPanel();
		rtPanel.setLayout(new BoxLayout(rtPanel,BoxLayout.X_AXIS));
		JLabel rtLab = new JLabel("Return type");
		rtLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		
		opRetType = new JComboBox(typeMap.keySet().toArray());
		opRetType.setMaximumSize(opRetType.getPreferredSize());
		rtPanel.add(rtLab);
		rtPanel.add(opRetType);
		
		
		
		
		
		contentPane.add(panelSymbol);
		contentPane.add(panelType);
		contentPane.add(panelPrec);
		contentPane.add(rtPanel);
		contentPane.add(addBtn);
		
	}
	
	
	public void showWindow() {
		setVisible(true);
	}

}
