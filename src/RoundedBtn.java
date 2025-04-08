import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.JButton;

public class RoundedBtn extends JButton {
	private final int R;
	private final boolean isCircular;

	public RoundedBtn(String text, int width, int height, int cornerRadius, Color bgColor, Color fgColor) {
		super(text);
		this.R = cornerRadius;
		this.isCircular = (width == height && cornerRadius == width/2);

		setContentAreaFilled(false); // Makes the background transparent for custom painting
		setFocusPainted(false); // Removes focus border
		setBorderPainted(false); // We will draw our own border
		setBackground(bgColor); // Set button background color
		setForeground(fgColor); // Set text color

		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);

		setMargin(new Insets(4, 4, 4, 4));

		setLayout(new GridBagLayout());
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());

		if (isCircular){
			g2.fillOval(0, 0, getWidth(), getHeight());
		}else{
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), R, R);
		}

		Icon icon = getIcon();
		if (icon != null){
			int x = (getWidth() - icon.getIconWidth()) / 2;
			int y = (getHeight() - icon.getIconHeight()) / 2;
			icon.paintIcon(this, g, x, y);
		}else{
			super.paintComponent(g);
		}


		g2.dispose();
	}
	@Override
	public boolean contains(int x, int y) {
		final int W = getWidth();
		final int H = getHeight();

		if (R <= x && x <= W - R && R <= y && y <= H - R){ //center
			return true;
		} else if (0 <= x && x <= R && 0 <= y && y <= R) { // top left
			return Math.pow(x - R, 2) + Math.pow(y - R, 2) <= R * R;
		} else if (0 <= x && x <= R && H - R <= y && y <= H ){ // bottom left
			return Math.pow(x - R, 2) + Math.pow(H - R - y, 2) <= R * R;
		} else if (W - R <= x && x <= W && 0 <= y && y <= R){ // top right
			return Math.pow(W - R - x, 2) + Math.pow(y - R, 2) <= R * R;
		} else if (W - R <= x && x <= W && H - R <= y && y <= H){ // bottom right
			return Math.pow(W - R - x, 2) + Math.pow(H - R - y, 2) <= R * R;
		} else if (R < x && x < W - R && 0 <= y && y < R){ // top side
			return true;
		} else if (0 <= x && x < R && R < y && y < H - R){ // left side
			return true;
		} else if (W - R < x && x < W && R < y && y < H - R){ // right side
			return true;
		} else if (R < x && x < W - R && H - R < y && y < H){ // bottom side
			return true;
		}

		return false;
	}
}