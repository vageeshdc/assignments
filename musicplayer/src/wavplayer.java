import java.awt.*;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.MouseEvent;
	import java.awt.event.MouseListener;
	import java.awt.geom.Line2D;
import java.io.File;

	import javax.swing.Icon;
	import javax.swing.ImageIcon;
	import javax.swing.JButton;
	import javax.swing.JComboBox;
import javax.swing.JFileChooser;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JMenu;
	import javax.swing.JMenuBar;
	import javax.swing.JMenuItem;
	import javax.swing.JPanel;
	import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
	
public class wavplayer extends JFrame implements ActionListener{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		JFrame mainFrame;
		JPanel minePanel;
		JButton[][] mineButtons;
		JLabel statusLabel;
		JTextField tx;
		JButton openButton, playButton,stopButton;
	    JFileChooser fc;
		File file;
		/**
		 * Create the GUI
		 */
		public void createAndShowGUI() {
			// Creates the main window. JFrame is a generic top level container.
			mainFrame = new JFrame("wavplayer");
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// We should set a minimum size to make the GUI look a little nicer
			// while making it.
			mainFrame.setMinimumSize(new Dimension(100, 200));

			Container mainPane = mainFrame.getContentPane();
			
			// Create a menu bar
			JMenuBar mBar = new JMenuBar();
			JMenu menu = new JMenu("File");
			 
			/*JMenuItem newItem = menu.add("New Vs Human");
			newItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					newGame();

				}
			});*/
			
			
			
			// Quit
			JMenuItem quitItem = menu.add("Quit");
			quitItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					mainFrame.dispose();
				}
			});
			mBar.add(menu);
			
			mainPane.add(mBar, BorderLayout.PAGE_START);

			// Create status bar
			statusLabel = new JLabel("Start a new game");
			mainPane.add(statusLabel, BorderLayout.PAGE_END);

			// Create a MxN grid layout
			//minePanel = new JPanel(new GridLayout(minesBoard.N, minesBoard.N));
			playButton = new JButton("PLAY!");
			stopButton  = new JButton("STOP..");
			minePanel.add(playButton);
			minePanel.add(stopButton);
			playButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					newGame();

				}
			});
			
			
			
			
			mainPane.add(minePanel, BorderLayout.CENTER);
			// Pack is required actually decide how components should be laid out.
			// It should be called before the GUI is displayed.
			mainFrame.pack();
			// Of course, the frame needs to be made visible.
			mainFrame.setVisible(true);
		}

		private void newGame() {
			//////////////
			
			//////////////
			statusLabel.setText(String.format("Playing song..."));
			
			//file chooser
			fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
			 int returnVal = fc.showOpenDialog(wavplayer.this);

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                file = fc.getSelectedFile();
	                //This is where a real application would open the file.
	    
	            }
			//minePanel.removeAll();
			//mineButtons = new JButton[minesBoard.N][minesBoard.N];
			
					//mineButtons[i][j] = new JButton("");
					//playButton = new JButton("PLAY!");	
					//stopButton  = new JButton("STOP..");
					//Icon icon = new ImageIcon("blankimage.jpeg");
					//mineButtons[i][j].setIcon(icon);
					/*//mineButtons[i][j].setBackground(Color.white);
					mineButtons[i][j].addMouseListener( new MouseListener() {
						@Override
						public void mouseClicked(MouseEvent e) {
							
								
						}

						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mousePressed(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub
							
						}
					});
					//minePanel.add(playButton);
*/					//minePanel.add(stopButton);
				
			mainFrame.pack();

			//updateStatus(getRemainingCount());
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

	

}
