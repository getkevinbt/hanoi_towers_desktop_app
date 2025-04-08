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

public class Disk extends JPanel{
	private boolean isTop = true;
	private final Color color;
	private final int diskDepth;
	private final int diskDiameter;
	private final int diskHeight;
	private final MyGraphics frame;
	private final Shape clickableShape;
	private int mouseX, mouseY, towerIndex = 0;
	public static final double COSINE = 0.795269879146;
	public static final double SINE = 0.4375;

	public Disk(Color color, int diskHeight, int diskDiameter, MyGraphics frame) {
		this.color = color;
		this.diskHeight = diskHeight;
		this.diskDiameter = diskDiameter;
		this.diskDepth = (int)((double)diskDiameter*SINE);
		this.frame = frame;

		setCursor(new Cursor(Cursor.HAND_CURSOR));

		setPreferredSize(new Dimension(diskDiameter, diskDepth + diskHeight));
		setOpaque(false);

		Area area = new Area(new Ellipse2D.Double(0, 0, diskDiameter, diskDepth));
        area.add(new Area(new Rectangle2D.Double(0, diskDepth / 2, diskDiameter, diskHeight)));
        area.add(new Area(new Ellipse2D.Double(0, diskHeight, diskDiameter, diskDepth)));
        clickableShape = area;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(color);
		g2.fillOval(0, 0, diskDiameter, diskDepth);
		g2.fillOval(0, diskHeight, diskDiameter, diskDepth);
		g2.fillRect(0, diskDepth/2, diskDiameter, diskHeight);
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		g2.drawArc(0, 0, diskDiameter, diskDepth, 0, -180);

		g2.dispose();
	}

	public void relocateDisk(int towerAxis, int inferiorBound){
		Dimension size = getPreferredSize();
		int x = towerAxis + 32 - size.width / 2;
		Disk inferiorDisk = frame.inferiorDiskOf(this);
		int y = inferiorDisk == null ? inferiorBound - diskHeight - (diskDepth / 2) : inferiorDisk.getY() - (int)((double)diskHeight*COSINE) - (inferiorDisk.diskDepth() - diskDepth) / 2;
		setBounds(x, y, size.width+2, size.height);
	}

	@Override
	public boolean contains(int x, int y){
		return clickableShape.contains(x, y);
	}

	public int diskDepth(){
		return this.diskDepth;
	}

	public void isTop(boolean isTop){
		if (isTop)
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.isTop = isTop;
	}
	
	public boolean isTop() {
		return isTop;
	}

    public int diskDiameter() {
        return diskDiameter;
    }

	public int mouseX() {
		return mouseX;
	}

	public void mouseX(int mouseX) {
		this.mouseX = mouseX;
	}
	public int mouseY() {
		return mouseY;
	}

	public void mouseY(int mouseY) {
		this.mouseY = mouseY;
	}

    public int towerIndex() {
        return towerIndex;
    }

    public void towerIndex(int towerIndex) {
        this.towerIndex = towerIndex;
    }
}
