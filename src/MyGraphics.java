import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * MyGraphics is responsible for rendering the GUI application for
 * the Towers of Hanoi game. It manages different screen states
 * (start, difficulty selection, game, and win) and handles user interactions.
 */
public class MyGraphics extends JFrame {
	private JPanel mainPanel;
	private int elapsedSeconds, difficulty, moveCount = 0, screen;
	private final Disk[] inferiorDisk = new Disk[7];
	private JLabel timerLabel;
	private List<Disk> disks;
	private final Color[] diskColor = {
		new Color(150, 156, 78),
		new Color(104, 163, 149),
		new Color(58, 43, 117),
		new Color(133, 62, 110),
		new Color(78, 110, 82),
		new Color(201, 163, 73),
		new Color(200, 40, 40)
	};
	private Timer gameTimer;
	private final Tower[] towers = new Tower[3];
    private JLabel movesLabel;
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * Constructor. Initializes the application window and sets up the initial UI state.
     */
    public MyGraphics() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
		SwingUtilities.invokeLater(this::setFullScreen);

		mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setOpaque(true);
		mainPanel.setBackground(new Color(215, 196, 183));
		mainPanel.setBounds(0, 0, this.getPreferredSize().width, this.getPreferredSize().height);

		displayStartScreen();
		add(mainPanel);
		setVisible(true);
	}

    /**
     * Sets the window to fullscreen mode.
     */
	private void setFullScreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);
    }

    /**
     * Adds a close button to the UI, which exits the application on click.
     */
    private void paintCloseBtn() {
		RoundedBtn closeBtn = new RoundedBtn("", 32, 32, 16, new Color(247, 87, 87), Color.BLACK);
		closeBtn.setIcon(new ImageIcon(getClass().getResource("../assets/close_24px.png")));
        closeBtn.setMargin(new Insets(2, 5, 2, 5));
        closeBtn.addActionListener(_ -> {System.exit(0);});
		closeBtn.setBounds(SCREEN_SIZE.width - 42, 10, 32, 32);

        mainPanel.add(closeBtn);
	}

    /**
     * Adds a back button to the UI, which navigates to the previous screen state.
     */
    private void paintBackBtn() {
		RoundedBtn backBtn = new RoundedBtn("", 32, 32, 16, new Color(143, 207, 153), Color.BLACK);
		backBtn.setIcon(new ImageIcon(getClass().getResource("../assets/arrow_back_24px.png")));
		backBtn.setMargin(new Insets(2, 5, 2, 5));
		backBtn.addActionListener(_ -> {
            if (screen == 2) {
				stopTimer();
				moveCount = 0;
				elapsedSeconds = 0;
				if (disks != null) disks.clear();
				for (int i = 0; i < 3; i++) towers[i] = null;
				for (int i = 0; i < 7; i++) inferiorDisk[i] = null;

				displayDifficultySelectionScreen();
			}
        });
		backBtn.setBounds(10, 10, 32, 32);

		mainPanel.add(backBtn);
	}

    /**
     * Adds the top panel containing the timer and move count.
     */
    private void paintTopPanel() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setOpaque(false);

		int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        timerLabel = new JLabel(String.format("Time %02d:%02d", minutes, seconds));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
		timerLabel.setForeground(Color.DARK_GRAY);
		timerLabel.setBounds(0, 0, 200, timerLabel.getPreferredSize().height);
		timerLabel.setOpaque(false);

        movesLabel = new JLabel("Moves " + moveCount);
        movesLabel.setFont(new Font("Arial", Font.BOLD, 24));
		movesLabel.setForeground(Color.DARK_GRAY);
		movesLabel.setBounds(250, 0, 200, movesLabel.getPreferredSize().height);
		movesLabel.setOpaque(false);

		topPanel.setPreferredSize(new Dimension(450, timerLabel.getPreferredSize().height));
		topPanel.setBounds((SCREEN_SIZE.width - 450) / 2, 20, 450, topPanel.getPreferredSize().height);

        topPanel.add(timerLabel);
        topPanel.add(movesLabel);

        mainPanel.add(topPanel);
	}

    /**
     * Removes game components from the main panel, preparing for a screen refresh.
     */
    public void removeGameComponents() {
        for (int i = 0; i < 3; i++) mainPanel.remove(towers[i]);
		for (int i = 0; i < disks.size(); i++) mainPanel.remove(disks.get(i));
	}

    /**
     * Repaints the screen to reflect changed game state.
     * @param draggedDisk the disk currently being dragged by the mouse
     */
    public void repaintScreen(Disk draggedDisk) {
		removeGameComponents();
		if (draggedDisk != null) mainPanel.add(draggedDisk);

        for (int i = 0; i < 3; i++) {
			mainPanel.add(towers[i]);
		}
        for (int i = disks.size() - 1; 0 <= i; i--) {
			if (draggedDisk != null && i == disks.indexOf(draggedDisk)) continue;
			mainPanel.add(disks.get(i));
		}
	}

    /**
     * Displays the start screen.
     */
	private void displayStartScreen() {
		screen = 0;
		mainPanel.removeAll();
		paintCloseBtn();

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		
        JLabel header = new JLabel("Hanoi Towers");
        header.setFont(new Font("Arial", Font.BOLD, 120));
		header.setForeground(Color.DARK_GRAY);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
		
        RoundedBtn btn = new RoundedBtn("Start", 200, 80, 10, new Color(196, 164, 132), Color.DARK_GRAY);
        btn.setFont(new Font("Arial", Font.PLAIN, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.addActionListener(_ -> displayDifficultySelectionScreen());
		
		int panelWidth = header.getPreferredSize().width;
		int panelHeight = header.getPreferredSize().height + 350 + btn.getPreferredSize().height;
		
		header.setBounds(0, 100, header.getPreferredSize().width + 3, header.getPreferredSize().height);
		btn.setBounds((panelWidth - btn.getPreferredSize().width) / 2, header.getPreferredSize().height + 350, btn.getPreferredSize().width, btn.getPreferredSize().height);

		panel.setBounds((SCREEN_SIZE.width - panelWidth) / 2, (SCREEN_SIZE.height - panelHeight) / 2, panelWidth, panelHeight);
		panel.add(header);
		panel.add(btn);

        mainPanel.add(panel);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void displayDifficultySelectionScreen() {
		screen = 1;
		mainPanel.removeAll();
		paintCloseBtn();
		paintBackBtn();

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);

		JLabel label = new JLabel("Select the difficulty");
		label.setFont(new Font("Arial", Font.BOLD, 120));
		label.setForeground(Color.DARK_GRAY);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);

		label.setBounds(0, 0, label.getPreferredSize().width+5, label.getPreferredSize().height);
		
		RoundedBtn easyButton = new RoundedBtn("Easy", 200, 80, 10, new Color(122, 171, 55), Color.DARK_GRAY);
		easyButton.addActionListener(_ -> displayGameScreen(1));
		RoundedBtn mediumButton = new RoundedBtn("Medium", 200, 80, 10, new Color(103, 137, 181), Color.DARK_GRAY);
		mediumButton.addActionListener(_ -> displayGameScreen(2));
		RoundedBtn hardButton = new RoundedBtn("Hard", 200, 80, 10, new Color(189, 87, 87),  Color.DARK_GRAY);
		hardButton.addActionListener(_ -> displayGameScreen(3));
		
		for (JButton button : new JButton[]{easyButton, mediumButton, hardButton}) {
			button.setFont(new Font("Arial", Font.PLAIN, 50));
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
		}

		int panelWidth = label.getPreferredSize().width;
		int panelHeight = label.getPreferredSize().height + 380; //Label size + 100gap + btn + 20gap + btn + 2gap + btn

		easyButton.setBounds((panelWidth - 200) / 2, label.getPreferredSize().height + 100, 200, 80);
		mediumButton.setBounds((panelWidth - 200) / 2, label.getPreferredSize().height + 200, 200, 80);
		hardButton.setBounds((panelWidth - 200) / 2, label.getPreferredSize().height + 300, 200, 80);

		panel.setBounds((SCREEN_SIZE.width - panelWidth) / 2, (SCREEN_SIZE.height - panelHeight) / 2, panelWidth, panelHeight);

		panel.add(label);
		panel.add(easyButton);
		panel.add(mediumButton);
		panel.add(hardButton);
		
		mainPanel.add(panel);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void displayGameScreen(int difficulty) {
        screen = 2;
		mainPanel.removeAll();
		paintCloseBtn();
		paintBackBtn();
		paintTopPanel();

		this.difficulty = difficulty;

		int maxDiskWidth = 180 + difficulty * 40;
		int towerHeight = (difficulty*2+1) * 30 + 50;

		int xGame = (SCREEN_SIZE.width - 103 - (maxDiskWidth * 3)) / 2;
		int yGame = (SCREEN_SIZE.height - towerHeight - 30) / 2;

        for (int i = 0; i < 3; i++) {
            towers[i] = new Tower(new Color(139, 90, 43), 64, towerHeight, xGame + (maxDiskWidth + 50) * i + (maxDiskWidth - 65) / 2, yGame, this);
        }

        disks = new ArrayList<>();
		int totalDisk = difficulty * 2 + 1;
        for (int i = totalDisk; i > 0 ; i--) {
			Disk disk = new Disk(diskColor[totalDisk - i], 30, 180 + i * 20, this);
			inferiorDisk[totalDisk - i] = totalDisk == i ? null : disks.get(totalDisk - i - 1);
			disks.add(disk);
			disk.isTop(false);
			disk.relocateDisk(towers[0].getBounds().x + 1, yGame + towerHeight + 15);//(int)((double)diskHeight*COSINE)
			if (i == 1){
				disk.isTop(true);
				towers[0].topDisk(disk);
			}
			disk.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (!disk.isTop())return;
					disk.mouseX(e.getX());
					disk.mouseY(e.getY());
					towers[disk.towerIndex()].topDisk(inferiorDiskOf(disk));
					repaintScreen(diskByDiameter(disk.diskDiameter()));
				}

				@Override
				public void mouseReleased(MouseEvent e){
					if (!disk.isTop())return;
					Tower nextTower = null;
					int nextTowerIndex = -1;
					for (int i = 0; i < 3; i++){
						if (i == disk.towerIndex()) continue;
						Rectangle towerBounds = towers[i].getBounds();
						int mouseX = disk.getX() + e.getX();
						int mouseY = disk.getY() + e.getY();
						if (towerBounds.x <= mouseX && mouseX <= towerBounds.x + towerBounds.width && towerBounds.y <= mouseY && mouseY <= towerBounds.y + towerBounds.height){
							nextTower = towers[i];
							nextTowerIndex = i;
							break;
						}
					}
					int towerHeight = totalDisk() * 30 + 50;
					int yGame = (MyGraphics.SCREEN_SIZE.height - towerHeight - 30) / 2;
					if (nextTower != null){
						if (nextTower.topDisk() != null && nextTower.topDisk().diskDiameter() < disk.diskDiameter()){
							towers[disk.towerIndex()].topDisk(disk);
							disk.relocateDisk(towers[disk.towerIndex()].getBounds().x + 1, yGame + towerHeight + 15);
						}else {
							if (nextTower.topDisk() != null)
								nextTower.topDisk().isTop(false);
							if (indexOfInferiorDiskOf(disk) != -1) 
								disks.get(indexOfInferiorDiskOf(disk)).isTop(true);
							disk.towerIndex(nextTowerIndex);
							inferiorDisk[indexOfDisk(disk)] = nextTower.topDisk();
							nextTower.topDisk(disk);
							disk.relocateDisk(nextTower.getBounds().x + 1, yGame + towerHeight + 15);
							incrementMoves();
						}
					}else {
						towers[disk.towerIndex()].topDisk(disk);
						disk.relocateDisk(towers[disk.towerIndex()].getBounds().x + 1, yGame + towerHeight + 15);
					}
					repaintScreen(null);
				}
			});

			disk.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if (!disk.isTop())return;
					int newX = disk.getX() + e.getX() - disk.mouseX();
					int newY = disk.getY() + e.getY() - disk.mouseY();
					Dimension size = disk.getPreferredSize();
					disk.setBounds(newX, newY, size.width, size.height);
					disk.getParent().repaint();
				}
			});
        }

		repaintScreen(null);

        mainPanel.revalidate();
        mainPanel.repaint();

        // Start the game timer
        startTimer();
    }

    private void startTimer() {
        elapsedSeconds = 0;
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    elapsedSeconds++;
                    int minutes = elapsedSeconds / 60;
                    int seconds = elapsedSeconds % 60;
                    timerLabel.setText(String.format("Time %02d:%02d", minutes, seconds));
					movesLabel.setText("Moves " + moveCount);
                });
            }
        }, 1000, 1000);
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    private void incrementMoves() {
        moveCount++;
		movesLabel.setText("Moves " + moveCount);
		checkWinCondition();
    }

    private void checkWinCondition() {
        if (towers[2].isCompleteTower(disks.size())) {
            for (int i = 0; i < disks.size(); i++) disks.get(i).isTop(false);
			stopTimer();
            displayWinScreen();
        }
    }
	
    private void displayWinScreen() {
		screen = 3;
		remove(mainPanel);
		mainPanel.removeAll();
		if (disks != null) disks.clear();
		for(int i = 0; i < 3; i++) towers[i] = null;
		mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setOpaque(true);
		mainPanel.setBackground(new Color(215, 196, 183));
		mainPanel.setBounds(0, 0, this.getPreferredSize().width, this.getPreferredSize().height);
		add(mainPanel);
		mainPanel.repaint();
		System.out.println("you win");
		paintCloseBtn();

        // Etiqueta principal: "YOU WIN"
        JLabel winLabel = new JLabel("YOU WIN", SwingConstants.CENTER);
        winLabel.setFont(new Font("Arial", Font.BOLD, 36));
		
        // Etiqueta Moves
        JLabel winMovesLabel = new JLabel("Moves: " + moveCount, SwingConstants.CENTER);
        winMovesLabel.setFont(new Font("Arial", Font.PLAIN, 24));
		
        // Etiqueta Time
		int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        JLabel winTimeLabel = new JLabel(String.format("Time %02d:%02d", minutes, seconds), SwingConstants.CENTER);
        winTimeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
		
        // Botón Main Menu
        RoundedBtn winBtn = new RoundedBtn("Main Menu", 120, 40, 15, new Color(154, 205, 50), Color.DARK_GRAY);
        winBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        winBtn.setFocusPainted(false);
        winBtn.addActionListener(_ -> {
			stopTimer();
			moveCount = 0;
			elapsedSeconds = 0;
			if (disks != null) disks.clear();
			for (int i = 0; i < 3; i++) towers[i] = null;
			for (int i = 0; i < 7; i++) inferiorDisk[i] = null;
			displayStartScreen();
        });
		
		int componentsHeight = winLabel.getPreferredSize().height + winBtn.getPreferredSize().height + winMovesLabel.getPreferredSize().height + winTimeLabel.getPreferredSize().height + 110;
		int xComponents = (SCREEN_SIZE.width - 600) / 2;
		int yComponents = (SCREEN_SIZE.height - componentsHeight) / 2;
		int padding = 0;
		
		
		winLabel.setBounds(xComponents, yComponents + padding, 600, winLabel.getPreferredSize().height);
		padding += winLabel.getPreferredSize().height + 50;
		winMovesLabel.setBounds(xComponents, yComponents + padding, 600, winMovesLabel.getPreferredSize().height);
		padding += winMovesLabel.getPreferredSize().height + 20;
		winTimeLabel.setBounds(xComponents, yComponents + padding, 600, winTimeLabel.getPreferredSize().height);
		padding += winTimeLabel.getPreferredSize().height + 40;
        winBtn.setBounds((SCREEN_SIZE.width - winBtn.getPreferredSize().width) / 2, yComponents + padding, 120, 40);
		
		mainPanel.add(winLabel);		
		mainPanel.add(winMovesLabel);
		mainPanel.add(winTimeLabel);
		mainPanel.add(winBtn);
    }

    public int totalDisk() {
        return disks.size();
    }

	public Disk diskByDiameter(int diskDiameter){
		for (int i = 0; i < totalDisk(); i++) if (disks.get(i).diskDiameter() == diskDiameter) return disks.get(i);
		return null;
	}

	public Disk inferiorDiskOf(Disk disk){
		int index = indexOfInferiorDiskOf(disk);
		return index == -1 ? null : disks.get(index);
	}

	public int indexOfInferiorDiskOf(Disk disk){
		int index = (difficulty*2+1) - (disk.diskDiameter() - 180) / 20;
		if (inferiorDisk[index] == null) return -1;
		return indexOfDisk(inferiorDisk[index]);
	}

	public int indexOfDisk(Disk disk){
		int index = (difficulty*2+1) - (disk.diskDiameter() - 180) / 20;
		return index;
	}
}
