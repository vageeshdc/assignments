import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class musicwavplayer extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6824833034934442044L;
	static private final String newline = "\n";
	JButton openButton, saveButton;
	JTextArea log;
	JFileChooser fc;
	private final int BUFFER_SIZE = 128000;
	private File soundFile;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceLine;
	int flag = 0;

	public musicwavplayer() {
		super(new BorderLayout());

		// Create the log
		log = new JTextArea(5, 20);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		// Create a file chooser
		fc = new JFileChooser();

		// Create the open button.
		openButton = new JButton("play");
		openButton.addActionListener(this);

		// Create the stop button.
		saveButton = new JButton("Stop");
		saveButton.addActionListener(this);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(openButton);
		buttonPanel.add(saveButton);

		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {

		// Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(musicwavplayer.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				soundFile = fc.getSelectedFile();
				flag = 0;
				// This is where application would open the file.
				log.append("Opening: " + soundFile.getName() + "." + newline);
				log.setCaretPosition(log.getDocument().getLength());

				try {
					audioStream = AudioSystem.getAudioInputStream(soundFile);
				} catch (Exception e1) {

				}

				audioFormat = audioStream.getFormat();

				DataLine.Info info = new DataLine.Info(SourceDataLine.class,
						audioFormat);
				try {
					sourceLine = (SourceDataLine) AudioSystem.getLine(info);
					sourceLine.open(audioFormat);
				} catch (Exception e1) {
					e1.printStackTrace();
					System.exit(1);
				}

				sourceLine.start();

				int nBytesRead = 0;
				byte[] abData = new byte[BUFFER_SIZE];
				while ((nBytesRead != -1) && (flag == 0)) {
					try {
						nBytesRead = audioStream.read(abData, 0, abData.length);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if (nBytesRead >= 0) {
						@SuppressWarnings("unused")
						int nBytesWritten = sourceLine.write(abData, 0,
								nBytesRead);
					}
				}
				flag = 0;
				sourceLine.drain();
				sourceLine.close();

			} else {
				log.append("Open command cancelled by user." + newline);
				log.setCaretPosition(log.getDocument().getLength());
			}

			// Handle save button action.
		} else if (e.getSource() == saveButton) {
			if (flag == 0) {
				flag = 1;
				log.append("Closing: " + soundFile.getName() + "." + newline);

				log.setCaretPosition(log.getDocument().getLength());
			}
		}
	}

	/**
	 * Create the GUI and show it.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Myplayer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new musicwavplayer());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}
