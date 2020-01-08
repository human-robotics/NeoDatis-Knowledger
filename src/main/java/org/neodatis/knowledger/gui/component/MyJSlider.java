package org.neodatis.knowledger.gui.component;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

public class MyJSlider extends JPanel implements ChangeListener{
	private String label;
	private JSlider slider;
	private JLabel value;
	
	public MyJSlider(String label,int start, int end, int initialValue){
		this.label = label;
		setLayout(new BorderLayout(5,5));
		value = new JLabel(label+"="+initialValue);
		slider = new JSlider(start, end);
		slider.setValue(initialValue);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.addChangeListener(this);
		slider.setBorder(new EmptyBorder(10,2,0,2));
		
		add(value,BorderLayout.WEST);
		add(slider,BorderLayout.EAST);

		
	}

	public void stateChanged(javax.swing.event.ChangeEvent e) {
		//System.out.println(slider.getValue());
		slider.setToolTipText(""+slider.getValue());	
		value.setText(label+"="+slider.getValue());
		newValue(slider.getValue());
	};
	public void newValue(int newValue){
		System.out.println(newValue);
	}

	public static void main(String[] args) {
		JPanel panel = new MyJSlider("value",1,1000,100);

		JFrame frame = new JFrame("MyJSlider");
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	

}
