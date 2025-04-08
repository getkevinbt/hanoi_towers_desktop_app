import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * The Disk class represents a disk shape component in a graphical application.
 * It defines the properties and behaviors of the disk, such as its dimensions,
 * color, and position on a tower in a graphical UI.
 */
public class Disk extends JPanel {
	private boolean isTop = true; // Indicates if the disk is the topmost on its tower.
	private final Color color; // The color of the disk.
	private final int diskDepth; // The "depth" of the disk for visual appearance.
	private final int diskDiameter; // The diameter of the disk.
	private final int diskHeight; // The height of the disk.
	private final MyGraphics frame; // Reference to the parent component for graphical context.
	private final Shape clickableShape; // The interactive shape area for mouse events.
	private int mouseX, mouseY, towerIndex = 0; // Position and tower details for the disk.
	public static final double COSINE = 0.795269879146; // Constant for angle calculations.
	public static final double SINE = 0.4375; // Constant for angle calculations.

	/**
	 * Constructs a Disk with specified color, height, diameter, and graphical frame reference.
	 *
	 * @param color The color of the disk.
	 * @param diskHeight The height of the disk.
	 * @param diskDiameter The diameter of the disk.
	 * @param frame The parent frame for graphical context.
	 */
	public Disk(Color color, int diskHeight, int diskDiameter, MyGraphics frame) {
		this.color = color;
		this.diskHeight = diskHeight;
		this.diskDiameter = diskDiameter;
        this.diskDepth = (int) ((double) diskDiameter * SINE);
		this.frame = frame;

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setPreferredSize(new Dimension(diskDiameter, diskDepth + diskHeight));
		setOpaque(false);

		Area area = new Area(new Ellipse2D.Double(0, 0, diskDiameter, diskDepth));
        area.add(new Area(new Rectangle2D.Double(0, diskDepth / 2, diskDiameter, diskHeight)));
        area.add(new Area(new Ellipse2D.Double(0, diskHeight, diskDiameter, diskDepth)));
        clickableShape = area;
	}

	/**
	 * Paints the disk component on the screen with its defined visual properties.
	 *
	 * @param g The Graphics context in which to paint.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(color);
		g2.fillOval(0, 0, diskDiameter, diskDepth);
		g2.fillOval(0, diskHeight, diskDiameter, diskDepth);
        g2.fillRect(0, diskDepth / 2, diskDiameter, diskHeight);
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		g2.drawArc(0, 0, diskDiameter, diskDepth, 0, -180);

		g2.dispose();
	}

	/**
	 * Relocates the disk on the graphical interface to the specified tower axis and boundary.
	 *
	 * @param towerAxis The axis position of the tower.
	 * @param inferiorBound The lower boundary for disk placement.
	 */
    public void relocateDisk(int towerAxis, int inferiorBound) {
		Dimension size = getPreferredSize();
		int x = towerAxis + 32 - size.width / 2;
		Disk inferiorDisk = frame.inferiorDiskOf(this);
		int y = (inferiorDisk != null) ? inferiorDisk.getY() - (int) ((double) diskHeight * COSINE) - (inferiorDisk.diskDepth() - diskDepth) / 2 : inferiorBound - diskHeight - (diskDepth / 2);
        setBounds(x, y, size.width + 2, size.height);
	}

	@Override
    public boolean contains(int x, int y) {
		return clickableShape.contains(x, y);
	}

	/**
	 * Returns the computed depth of the disk.
	 *
	 * @return The depth of the disk.
	 */
    public int diskDepth() {
		return this.diskDepth;
	}

	/**
	 * Sets the disk as the topmost or not, changing the cursor appearance accordingly.
	 *
	 * @param isTop True if the disk is the topmost, else false.
	 */
    public void isTop(boolean isTop) {
        setCursor(new Cursor(isTop ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
		this.isTop = isTop;
	}
	
	/**
	 * Checks if the disk is the topmost on its tower.
	 *
	 * @return True if the disk is the topmost, else false.
	 */
	public boolean isTop() {
		return isTop;
	}

	/**
	 * Returns the diameter of the disk.
	 *
	 * @return The disk's diameter.
	 */
    public int diskDiameter() {
        return diskDiameter;
    }

	/**
	 * Returns the X coordinate of the mouse pointer relative to the disk.
	 *
	 * @return The X coordinate of the mouse pointer.
	 */
	public int mouseX() {
		return mouseX;
	}
	

	/**
	 * Updates the stored X coordinate of the mouse pointer.
	 *
	 * @param mouseX The new X coordinate.
	 */
	public void mouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	/**
	 * Returns the Y coordinate of the mouse pointer relative to the disk.
	 *
	 * @return The Y coordinate of the mouse pointer.
	 */
	public int mouseY() {
		return mouseY;
	}

	/**
	 * Updates the stored Y coordinate of the mouse pointer.
	 *
	 * @param mouseY The new Y coordinate.
	 */
	public void mouseY(int mouseY) {
		this.mouseY = mouseY;
	}

	/**
	 * Returns the index of the tower where the disk is placed.
	 *
	 * @return The index of the tower.
	 */
    public int towerIndex() {
        return towerIndex;
    }

	/**
	 * Updates the index of the tower where the disk is positioned.
	 *
	 * @param towerIndex The new tower index.
	 */
    public void towerIndex(int towerIndex) {
        this.towerIndex = towerIndex;
    }
}

