import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * The Tower class represents a graphical tower component within a game or visualization.
 * This component handles the visual representation and interaction with disks.
 */
public class Tower extends JPanel {
	private Disk topDisk = null;
	private final Color bgColor;
	private final int towerDepth;
	private final int towerDiameter;
	private final int towerHeight;
	private final MyGraphics frame;
	private static final double SINE = 0.4375;

    /**
     * Constructs a new Tower with the specified parameters.
     *
     * @param bgColor      the background color of the tower.
     * @param towerDiameter the diameter of the tower.
     * @param towerHeight   the height of the tower.
     * @param towerX        the x-coordinate position of the tower.
     * @param towerY        the y-coordinate position of the tower.
     * @param frame         the graphical frame context in which the tower resides.
     */
    public Tower(Color bgColor, int towerDiameter, int towerHeight, int towerX, int towerY, MyGraphics frame) {
        this.bgColor = bgColor;
		this.towerDiameter = towerDiameter;
		this.towerHeight = towerHeight;
		this.frame = frame;

        this.towerDepth = (int) ((double) towerDiameter * SINE);

		setPreferredSize(new Dimension(towerDiameter, towerHeight + towerDepth));
		setBounds(towerX - 1, towerY, towerDiameter + 2, towerHeight + towerDepth + 1);
		setOpaque(false);
	}

    /**
     * Paints the tower component.
     *
     * @param g the Graphics object used for painting.
     */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(bgColor);
		g2.fillOval(0, 0, towerDiameter, towerDepth);

        if (topDisk == null) {
			g2.fillOval(0, towerHeight, towerDiameter, towerDepth);
            g2.fillRect(0, towerDepth / 2, towerDiameter, towerHeight);
        } else {
			g2.fillOval(0, topDisk.getY() + topDisk.diskDepth() / 2 - getY() - towerDepth / 2, towerDiameter, towerDepth);
            g2.fillRect(0, towerDepth / 2, towerDiameter, topDisk.getY() + topDisk.diskDepth() / 2 - getY() - towerDepth / 2);
		}
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
        g2.drawArc(1, 0, towerDiameter - 1, towerDepth, 0, -180);

		g2.dispose();
	}

    /**
     * Determines whether the given point (x, y) is inside the tower.
     *
     * @param x the x-coordinate to check.
     * @param y the y-coordinate to check.
     * @return true if the point is inside the tower, false otherwise.
     */
	@Override
    public boolean contains(int x, int y) {
		return true;
	}

    /**
     * Sets the top disk on the tower and repaints the component.
     *
     * @param topDisk the top disk to set on the tower.
     */
    public void topDisk(Disk topDisk) {
		this.topDisk = topDisk;
		repaint();
	}

    /**
     * Gets the top disk currently on the tower.
     *
     * @return the top disk on the tower.
     */
    public Disk topDisk() {
		return this.topDisk;
	}

    /**
     * Checks whether the tower is complete with the specified total number of disks.
     *
     * @param totalDisk the total number of disks that should be on the tower.
     * @return true if the tower is complete, false otherwise.
     */
    public boolean isCompleteTower(int totalDisk) {
		Disk disk = topDisk;
		int counter = 0;
        while (disk != null) {
			disk = frame.inferiorDiskOf(disk);
			counter++;
		}
		return counter == totalDisk;
	}
}

