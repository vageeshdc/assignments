import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class UiWindow extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Board minesBoard;
	boolean[][] revealed;
	int stat[][] ;
	int lev = -1;
	int n = 3;
	
	JFrame mainFrame;
	JPanel minePanel;
	JButton[][] mineButtons;
	JLabel statusLabel;
	JTextField tx;
	
	int s1,s2;
	
	JMenu menu1 = new JMenu("Undo");
	/**
	 * Construct a Minesweeper window of the dimensions of the board.
	 * 
	 * @param b
	 *            - the board
	 */
	UiWindow(Board b) {
		minesBoard = b;
		int N = minesBoard.N;
		stat = new int[N*N][2];
	}
	UiWindow()
	{
		minesBoard = new Board(3);
	}

	/**
	 * Create a board, and then construct an appropriate Minesweeper window
	 * 
	 * @param M
	 * @param N
	 * @param p
	 */
	UiWindow( int N) {
		minesBoard = new Board(N);
		stat = new int[N*N][2];
	}

	/**
	 * Create the GUI
	 */
	public void createAndShowGUI() {
		// Creates the main window. JFrame is a generic top level container.
		mainFrame = new JFrame("Minesweeper");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// We should set a minimum size to make the GUI look a little nicer
		// while making it.
		mainFrame.setMinimumSize(new Dimension(400, 400));

		Container mainPane = mainFrame.getContentPane();
		

		// Create a menu bar
		JMenuBar mBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		/////////////////
		//menu.
		//menu1.add
		menu1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(lev > -1)
				{
					int i = stat[lev][0];
					int j = stat[lev][1];
					
					minesBoard.undomov(i, j);
					lev--;
					
					Icon icon1 = new ImageIcon("blankimage.jpeg");
					mineButtons[i][j].setIcon(icon1);
					
					revealed[i][j] = false;
				}
			}
		});
		
		/////////////////////////////////////////////////////
		
		//tx = new JTextField(10);
		//tx.setName("Enter dimension");
		//menu.add(tx);
		 
		JMenuItem newItem = menu.add("New Vs Human");
		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				minesBoard.reset();
				newGame();
				/*if(!tx.getText().equals(""))
				{
					n = Integer.parseInt(tx.getText());

					minesBoard = new Board(n);
				}
				else
				{
					minesBoard = new Board(n);
				}*/

			}
		});
		
		
		
		//tx.addActionListener(this);
				
		menu.addSeparator();
		JMenuItem newItem1 = menu.add("New Vs CPU (first player)");
		newItem.addActionListener(new ActionListener() {
			
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				minesBoard.reset();
				newGame1(1);
			}
		});
		

		// Quit
		JMenuItem quitItem = menu.add("Quit");
		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		mBar.add(menu);
		mBar.add(menu1);
		
		mainPane.add(mBar, BorderLayout.PAGE_START);

		// Create status bar
		statusLabel = new JLabel("Start a new game");
		mainPane.add(statusLabel, BorderLayout.PAGE_END);

		// Create a MxN grid layout
		minePanel = new JPanel(new GridLayout(minesBoard.N, minesBoard.N));
		
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
		statusLabel.setText(String.format("Player 1 turn"));
		// Reset the reveal matrix
		revealed = new boolean[minesBoard.N][minesBoard.N];
		for (int i = 0; i < minesBoard.N; i++)
			for (int j = 0; j < minesBoard.N; j++)
				revealed[i][j] = false;

		// Remove everything in the panel, and reset it
		minePanel.removeAll();
		mineButtons = new JButton[minesBoard.N][minesBoard.N];
		for (int i = 0; i < minesBoard.N; i++) {
			for (int j = 0; j < minesBoard.N; j++) {
				final String coords = String.format("%c%d", 'a' + i, j);
				final int mi = i;
				final int mj = j;
				mineButtons[i][j] = new JButton("");
								
				Icon icon = new ImageIcon("blankimage.jpeg");
				mineButtons[i][j].setIcon(icon);
				mineButtons[i][j].setBackground(Color.white);
				mineButtons[i][j].addMouseListener( new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						
							System.out.println( coords );
							if(revealSquare(mi, mj,0) == true)
							{
								if(minesBoard.count%2 == 1)
								{
									//set an image!!
									Icon icon1 = new ImageIcon("oimage.jpeg");
									mineButtons[mi][mj].setIcon(icon1);
								}
								else
								{
									//other image!!
									Icon icon1 = new ImageIcon("ximage.jpeg");
									mineButtons[mi][mj].setIcon(icon1);
								}
							}
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
				minePanel.add(mineButtons[i][j]);
			}
		}
		mainFrame.pack();

		//updateStatus(getRemainingCount());
	}

	/////////////
	private void newGame1(int flag) {
		//////////////
		
		//////////////
		statusLabel.setText(String.format("Player 1 turn"));
		// Reset the reveal matrix
		revealed = new boolean[minesBoard.N][minesBoard.N];
		for (int i = 0; i < minesBoard.N; i++)
			for (int j = 0; j < minesBoard.N; j++)
				revealed[i][j] = false;

		// Remove everything in the panel, and reset it
		minePanel.removeAll();
		mineButtons = new JButton[minesBoard.N][minesBoard.N];
		for (int i = 0; i < minesBoard.N; i++) {
			for (int j = 0; j < minesBoard.N; j++) {
				final String coords = String.format("%c%d", 'a' + i, j);
				final int mi = i;
				final int mj = j;
				mineButtons[i][j] = new JButton("");
								
				Icon icon = new ImageIcon("blankimage.jpeg");
				mineButtons[i][j].setIcon(icon);
				
				mineButtons[i][j].addMouseListener( new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {
						
							System.out.println( coords );
							if(revealSquare(mi, mj,1) == true)
							{	
								if(minesBoard.count%2 == 1)
								{
									//set an image!!
									Icon icon1 = new ImageIcon("oimage.jpeg");
									mineButtons[mi][mj].setIcon(icon1);
								}
								else
								{
									//other image!!
									Icon icon1 = new ImageIcon("ximage.jpeg");
									mineButtons[mi][mj].setIcon(icon1);
								}
							}
					
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
				minePanel.add(mineButtons[i][j]);
			}
		}
		mainFrame.pack();

		//updateStatus(getRemainingCount());
	}
	///////////////
	
	private boolean revealSquare(int i, int j,int AI) {
		// First check if the square has already been revealed.
		if (revealed[i][j])
			return false;
		else {
			
			revealed[i][j] = true;
			int ar[] = minesBoard.move(i, j);
			
			if(ar[0] == -1)
				{
					
					if(minesBoard.count%2 == 1)
					{
						//set an image!!
						//Icon icon1 = new ImageIcon("oimage.jpeg");
						//mineButtons[mi][mj].setIcon(icon1);
						statusLabel.setText(String.format("Player 2 turn"));
					}
					else
					{
						//other image!!
						//Icon icon1 = new ImageIcon("ximage.jpeg");
						//mineButtons[mi][mj].setIcon(icon1);
						statusLabel.setText(String.format("Player 1 turn"));
					}
					return true;
				}
			else
				if(ar[0] == 3)
				{
					statusLabel.setText(String.format("It is a draw!"));
					return true;
				}
				else
					if(ar[0] == 1)
					{
						for (int ii = 0; ii < minesBoard.N; ii++) {
							for (int jj = 0; jj < minesBoard.N; jj++) {
								//if (minesBoard.getAdjacentBombs(ii, jj) == -1)
									//mineButtons[ii][jj].setText("X");
								mineButtons[ii][jj].setEnabled(false);
							}
						}
						statusLabel.setText(String.format("Congratulations, Player 1 won!"));
						paint(mainFrame.getGraphics(),ar[1],ar[2]);
						return true;
					}
					else
			if(ar[0] == 2)
			{
				statusLabel.setText(String.format("Congratulations, Player2  won!"));
				for (int ii = 0; ii < minesBoard.N; ii++) {
					for (int jj = 0; jj < minesBoard.N; jj++) {
						mineButtons[ii][jj].setEnabled(false);
					}
				}
				paint(mainFrame.getGraphics(),ar[1],ar[2]);
				return true;
			}
			//return false;
			

		}
		
		if(AI == 1)
		{
			int ar[] = minesBoard.AIM();

			for(int ii = 0;ii< minesBoard.N;ii++)
			{
				for(int jj = 0; jj < minesBoard.N;jj++)
				{
					if(minesBoard.arr[ii][jj] != 0)
					{
						revealed[i][j] = true;
					}
					else
					{
						revealed[i][j] = false;
					}
				}
			}
			
			
			if(ar[0] == -1)
				{
					
					if(minesBoard.count%2 == 1)
					{
						//set an image!!
						//Icon icon1 = new ImageIcon("oimage.jpeg");
						//mineButtons[mi][mj].setIcon(icon1);
						statusLabel.setText(String.format("Player 2 turn"));
					}
					else
					{
						//other image!!
						//Icon icon1 = new ImageIcon("ximage.jpeg");
						//mineButtons[mi][mj].setIcon(icon1);
						statusLabel.setText(String.format("Player 1 turn"));
					}
					return true;
				}
			else
				if(ar[0] == 3)
				{
					statusLabel.setText(String.format("It is a draw!"));
					return true;
				}
				else
					if(ar[0] == 1)
					{
						for (int ii = 0; ii < minesBoard.N; ii++) {
							for (int jj = 0; jj < minesBoard.N; jj++) {
								//if (minesBoard.getAdjacentBombs(ii, jj) == -1)
									//mineButtons[ii][jj].setText("X");
								mineButtons[ii][jj].setEnabled(false);
							}
						}
						statusLabel.setText(String.format("Congratulations, Player 1 won!"));
						paint(mainFrame.getGraphics(),ar[1],ar[2]);
						return true;
					}
					else
			if(ar[0] == 2)
			{
				statusLabel.setText(String.format("Congratulations, Player2  won!"));
				for (int ii = 0; ii < minesBoard.N; ii++) {
					for (int jj = 0; jj < minesBoard.N; jj++) {
						mineButtons[ii][jj].setEnabled(false);
					}
				}
				paint(mainFrame.getGraphics(),ar[1],ar[2]);
				return true;
			}
			
		}
		return false;
	}
	
 
	public void paint (Graphics g,int i,int j) {
	    Graphics2D g2 = (Graphics2D) g;
	    
	    System.out.println("x is"+minePanel.getX());
		System.out.println("y is"+minePanel.getY());
		System.out.println("width is"+minePanel.getWidth());
		System.out.println("height is"+minePanel.getHeight());
		
	    //...
	    int x1,y1,x2,y2;
	    if(i  == 1)
	    {
	    	y2 = y1 = minePanel.getY()+(minePanel.getHeight()/(2*minesBoard.N))+((j-1)*minePanel.getHeight()/minesBoard.N);
	    	x1 = minePanel.getX()+(minePanel.getWidth()/(2*minesBoard.N));
	    	x2 = minePanel.getX()+minePanel.getWidth() -(minePanel.getWidth()/(2*minesBoard.N));
	    }
	    else
	    	if(i == 2)
	    	{
	    		x2 = x1 = minePanel.getX()+(minePanel.getWidth()/(2*minesBoard.N))+((j-1)*minePanel.getWidth()/minesBoard.N);
		    	y1 = minePanel.getY()+(minePanel.getHeight()/(2*minesBoard.N));
		    	y2 = minePanel.getY()+minePanel.getHeight() -(minePanel.getHeight()/(2*minesBoard.N));
	    		
	    	}
	    	else
	    	{
	    		if(j == 1)
	    		{
	    			x1 = y1 = minePanel.getY()+(minePanel.getHeight()/(2*minesBoard.N));
	    			x2 = y2 = minePanel.getY()+minePanel.getHeight() -(minePanel.getHeight()/(2*minesBoard.N));
	    		}
	    		else
	    		{
	    			x2 = y1 = minePanel.getY()+(minePanel.getHeight()/(2*minesBoard.N));
	    			x1 = y2 = minePanel.getY()+minePanel.getHeight() -(minePanel.getHeight()/(2*minesBoard.N));
	    		}
	    	}
	    //minePanel.add(g2.draw(new Line2D.Double(x1, y1, x2, y2)));
	    //minePanel.add(new UiWindow());
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}