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

import com.kooi.dissertation.syntaxtree.DataType;

public class AddVariable extends JFrame {

	private JPanel contentPane;
	private JTextField symbolField;
	private JButton addBtn;
	private JComboBox type;
	
	
	private Map<String,DataType> typeMap;
	private HomeScreen home;
	

	/**
	 * Create the frame.
	 */
	public AddVariable(HomeScreen h) {
		
		
		home = h;
		
		typeMap = new HashMap<>();
		typeMap.put("INT", DataType.INT);
		typeMap.put("NAT", DataType.NAT);
		typeMap.put("STRING", DataType.STRING);
		typeMap.put("BOOLEAN", DataType.BOOLEAN);
		typeMap.put("CONSTANT", DataType.CONST);
		
		
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				dispose();
			}
		});
		
		//setting up frame
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);
		
		
		
		
		addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(symbolField.getText().equals("")) {
					JOptionPane.showMessageDialog(AddVariable.this, "Field cannot be empty.","Missing values",JOptionPane.ERROR_MESSAGE);
				}else {
					home.getSig().addVariable(symbolField.getText(), typeMap.get(type.getSelectedItem()));
					home.updateUI();
					AddVariable.this.dispose();
				}

			}
		});
		
		
		
		//panel for symbol text field
		JPanel panelSymbol = new JPanel();
		panelSymbol.setLayout(new BoxLayout(panelSymbol,BoxLayout.X_AXIS));
		JLabel symLab = new JLabel("Variable name");
		symLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		symbolField = new JTextField(10);
		symbolField.setMaximumSize(symbolField.getPreferredSize());
		
		panelSymbol.add(symLab);
		panelSymbol.add(symbolField);
		
		
		//panel for return type
		JPanel tPanel = new JPanel();
		tPanel.setLayout(new BoxLayout(tPanel,BoxLayout.X_AXIS));
		JLabel tLab = new JLabel("Type");
		tLab.setFont(new Font("Century Gothic",Font.PLAIN,14));
		
		type = new JComboBox(typeMap.keySet().toArray());
		type.setMaximumSize(type.getPreferredSize());
		tPanel.add(tLab);
		tPanel.add(type);
		
		
		contentPane.add(panelSymbol);
		contentPane.add(tPanel);
		contentPane.add(addBtn);
		
	}
	
	
	public void showWindow() {
		setVisible(true);
	}

}
