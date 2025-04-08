import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class Tower extends JPanel {
	private Disk topDisk = null;
	private final Color bgColor;
	private final int towerDepth;
	private final int towerDiameter;
	private final int towerHeight;
	private final MyGraphics frame;
	private static final double SINE = 0.4375;

	public Tower(Color bgColor, int towerDiameter, int towerHeight, int towerX, int towerY, MyGraphics frame){
        this.bgColor = bgColor;
		this.towerDiameter = towerDiameter;
		this.towerHeight = towerHeight;
		this.frame = frame;

		this.towerDepth = (int)((double)towerDiameter * SINE);

		setPreferredSize(new Dimension(towerDiameter, towerHeight + towerDepth));
		setBounds(towerX - 1, towerY, towerDiameter + 2, towerHeight + towerDepth + 1);
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Suavizado de bordes

		g2.setColor(bgColor);
		g2.fillOval(0, 0, towerDiameter, towerDepth);

		if (topDisk == null){	
			g2.fillOval(0, towerHeight, towerDiameter, towerDepth);
			g2.fillRect(0, towerDepth/2, towerDiameter, towerHeight);
		}else {
			g2.fillOval(0, topDisk.getY() + topDisk.diskDepth() / 2 - getY() - towerDepth / 2, towerDiameter, towerDepth);
			g2.fillRect(0, towerDepth/2, towerDiameter, topDisk.getY() + topDisk.diskDepth() / 2 - getY() - towerDepth / 2 );
		}
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		g2.drawArc(1, 0, towerDiameter-1, towerDepth, 0, -180);

		g2.dispose();
	}

	@Override
	public boolean contains(int x, int y){
		return true;
	}

	public void topDisk(Disk topDisk){
		this.topDisk = topDisk;
		repaint();
	}

	public Disk topDisk(){
		return this.topDisk;
	}

	public boolean isCompleteTower(int totalDisk){
		Disk disk = topDisk;
		int counter = 0;
		while (disk != null){
			disk = frame.inferiorDiskOf(disk);
			counter++;
		}
		return counter == totalDisk;
	}
}
