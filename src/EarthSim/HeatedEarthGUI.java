package EarthSim;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.Buffer;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class HeatedEarthGUI extends JFrame {

	private boolean simulatorOwnThread=true;
	private boolean presentationOwnThread=true;
	private BlockingQueue<Message> queue;
	private HeatedEarthPresentation display; 
	private HeatedEarthSimulation sim;
	private String initiative;
	private Long startTime;
	private boolean paused=false;


	JButton runButton = new JButton("Run Simulation");
	int textSize=30;
	JTextField gridSize = new JTextField();
	JTextField simTimeStep = new JTextField();
	JTextField displayRate = new JTextField();
	final JButton start = new JButton();
	JPanel rightPanel = new JPanel();
	private boolean testing=false;
	private JLabel time = new JLabel();

	public HeatedEarthGUI(boolean presentationThread, boolean simulatorOwnThread,String initiative,Integer bufferSize)
	{
		super();
		this.presentationOwnThread=presentationThread;
		this.simulatorOwnThread=simulatorOwnThread;
		this.queue=new ArrayBlockingQueue<Message>(bufferSize);
		this.initiative = initiative;
		display = new HeatedEarthPresentation(0,queue,displayRate,paused);
		
	}

	public void displayGui()
	{
		this.add(createMainGrid());
		this.pack();
		this.setTitle("Heated Earth Simulation");
		this.setResizable(false);
		this.setVisible(true);
		sim = new HeatedEarthSimulation(Integer.valueOf(gridSize.getText()),Integer.valueOf(simTimeStep.getText()),queue);
		//Set initiative 
		if("S".equalsIgnoreCase(initiative)){
			sim.setPresentation(display);
		}else if("P".equalsIgnoreCase(initiative)){
			display.setSimulation(sim);
		}
		display.setTime(time);

	}
	public void setTesting(boolean testing){
		this.testing=testing;
		display.setTesting(testing);
	}
	public JPanel createMainGrid(){
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel label = new JLabel();
		label.setText("   ");

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		c.gridx=1;
		c.gridy=0;
		pane.add(label,c);
		label = new JLabel();
		label.setText("   ");

		c.weightx=1.0;
		c.gridx=1;
		c.gridy=2;
		pane.add(label,c);

		c.weightx=.0;
		c.gridx=0;
		c.gridy=1;
		pane.add(createMenu(),c);

		JPanel displayControls = new JPanel();
		start.setIcon(new ImageIcon("images/play.png"));
		start.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				if(display.getRunning()){
					if(paused){
						paused=false;
						display.setPaused(false);
						sim.setPaused(false);
						start.setIcon(new ImageIcon("images/pause.png"));
					}else{
						paused=true;
						display.setPaused(true);
						sim.setPaused(true);
						start.setIcon(new ImageIcon("images/play.png"));
					}
					repaint();
					
				}
			}
		});
		displayControls.add(start);

		JButton stop = new JButton();
		stop.setIcon(new ImageIcon("images/stop.png"));
		stop.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{

				display.setRunning(false);
				sim.setRunning(false);
				queue.clear();
				paused=true;
				repaint();

			}
		});
		displayControls.add(stop);
		JButton restart = new JButton();
		restart.setIcon(new ImageIcon("images/replay.png"));
		restart.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{

				sim.setRunning(false);
				display.setRunning(false);
				paused = false;
				run();

			}
		});
		displayControls.add(restart);
		JLabel timeLabel =new JLabel("Running Time:");
		timeLabel.setFont(new Font("Arial", 0, textSize));
		displayControls.add(timeLabel);
		time.setFont(new Font("Arial", 0, textSize));
		time.setText("0 s");
		displayControls.add(time);
		
		c.gridx=1;
		c.gridy=2;
		pane.add(displayControls,c);


		c.weightx=.8;
		c.weighty=.8;
		c.gridx=1;
		c.gridy=1;
		c.gridwidth=3;
		//c.ipadx=20;
		pane.add(display,c);




		return pane;
	}
	private JPanel createMenu() {
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new FlowLayout());

		JPanel smallGrid = new JPanel();
		smallGrid.setFont(new Font("Arial", 0, textSize));
		smallGrid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		smallGrid.setLayout(new GridLayout(0, 1));

		JLabel gridSizeLabel = new JLabel("Dimension of Grid(Degrees):");
		gridSizeLabel.setFont(new Font("Arial", 0, textSize));

		smallGrid.add(gridSizeLabel);
		gridSize.setFont(new Font("Arial", 0, textSize));
		gridSize.setToolTipText("Enter an Integer greater than zero.");
		gridSize.setText("15");
		gridSize.setInputVerifier(new InputVerifier()
		{
			@Override
			public boolean verify(JComponent input)
			{
				String text = ((JTextField) input).getText();
				try
				{
					Integer value = Integer.valueOf(text);
					while(180%value!=0){
						value--;
					}
					gridSize.setText(value+"");
					if (value < 0)
					{
						gridSize.setText("");
						return false;
					} else
					{
						return true;
					}
				} catch (NumberFormatException e)
				{
					gridSize.setText("");
					return false;
				}
			}
		});
		smallGrid.add(gridSize);

		JLabel topLabel = new JLabel("Simulation Time Step(ms):");
		topLabel.setFont(new Font("Arial", 0, textSize));
		smallGrid.add(topLabel);
		simTimeStep.setFont(new Font("Arial", 0, textSize));
		simTimeStep.setText("1");
		simTimeStep.setToolTipText("Enter an Integer between zero and 100.");
		simTimeStep.setInputVerifier(new InputVerifier()
		{
			@Override
			public boolean verify(JComponent input)
			{
				String text = ((JTextField) input).getText();
				try
				{
					Integer value = Integer.valueOf(text);
					if (value < 0 || value > 10000)
					{
						simTimeStep.setText("");
						return false;
					} else
					{
						return true;
					}
				} catch (NumberFormatException e)
				{
					simTimeStep.setText("");
					return false;
				}
			}
		});
		smallGrid.add(simTimeStep);

		JLabel bottomLabel = new JLabel("Display Rate(ms):");
		bottomLabel.setFont(new Font("Arial", 0, textSize));
		smallGrid.add(bottomLabel);
		displayRate.setFont(new Font("Arial", 0, textSize));
		displayRate.setText("1");
		displayRate.setToolTipText("Enter an Integer between zero and 100.");
		displayRate.setInputVerifier(new InputVerifier()
		{
			@Override
			public boolean verify(JComponent input)
			{
				String text = ((JTextField) input).getText();
				try
				{
					Integer value = Integer.valueOf(text);
					if (value < 0 || value > 10000)
					{
						displayRate.setText("");
						return false;
					} else
					{
						return true;
					}
				} catch (NumberFormatException e)
				{
					displayRate.setText("");
					return false;
				}
			}
		});
		smallGrid.add(displayRate);

		runButton.setFont(new Font("Arial", 0, textSize));


		runButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				display.setRunning(false);
				sim.setRunning(false);
				queue.clear();
				repaint();
				
				display.setGridSize(Integer.valueOf(gridSize.getText()));
				sim.setGridSize(Integer.valueOf(gridSize.getText()));
				run();
			}
		});
		smallGrid.add(runButton);
		innerPanel.add(smallGrid);
		return innerPanel;
	}
	
	
	//runs both simulator and presentation
	public void run(){
		sim.reset();
		display.reset();
		startTime=(new Date()).getTime();
		queue.clear();
		start.setIcon(new ImageIcon("images/pause.png"));
		paused=false;
		
		if(simulatorOwnThread){	
			new Thread()
			{
				@Override
				public void run()
				{
					if("S".equalsIgnoreCase(initiative) || "G".equalsIgnoreCase(initiative)){
					sim.run();
					}

				}
			}.start();

		}
		
		if(presentationOwnThread){
			new Thread()
			{
				@Override
				public void run()
				{	if("P".equalsIgnoreCase(initiative) || "G".equalsIgnoreCase(initiative)){
						display.run();
					}
				}
			}.start();
		}
		if("P".equalsIgnoreCase(initiative) || "G".equalsIgnoreCase(initiative)){
		if(!presentationOwnThread){
			display.run();
		}
		}
		if("S".equalsIgnoreCase(initiative) || "G".equalsIgnoreCase(initiative)){
		if(!simulatorOwnThread){	
			sim.run();
		}
		}
	}

}
