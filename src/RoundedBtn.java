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

/**
 * A custom JButton with rounded corners or a circular shape.
 */
public class RoundedBtn extends JButton {
    private final int R; // Radius for rounded corners
    private final boolean isCircular; // Flag to check if the button should be circular
    /**
     * Constructor for creating a rounded button.
     *
     * @param text        the text to be displayed on the button
     * @param width       the width of the button
     * @param height      the height of the button
     * @param cornerRadius the radius of the button corners
     * @param bgColor     the background color of the button
     * @param fgColor     the foreground (text) color of the button
     */
	public RoundedBtn(String text, int width, int height, int cornerRadius, Color bgColor, Color fgColor) {
		super(text);
		this.R = cornerRadius;
        this.isCircular = (width == height && cornerRadius == width / 2);

        // Button appearance settings
		setContentAreaFilled(false); // Makes the background transparent for custom painting
        setFocusPainted(false); // Removes the focus border
        setBorderPainted(false); // We will draw our custom border
		setBackground(bgColor); // Set button background color
		setForeground(fgColor); // Set text color

        // Set button dimensions
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);

        // Set button margin and layout
		setMargin(new Insets(4, 4, 4, 4));
		setLayout(new GridBagLayout());
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Set hand cursor on hover
	}

    /**
     * Custom paint method to draw the button with rounded corners or circular shape.
     *
     * @param g the Graphics object to protect
     */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(getBackground());

        // Draw the button shape based on its form
        if (isCircular) {
			g2.fillOval(0, 0, getWidth(), getHeight());
        } else {
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), R, R);
		}

        // Draw the icon if available
		Icon icon = getIcon();
        if (icon != null) {
			int x = (getWidth() - icon.getIconWidth()) / 2;
			int y = (getHeight() - icon.getIconHeight()) / 2;
			icon.paintIcon(this, g, x, y);
        } else {
			super.paintComponent(g);
		}

		g2.dispose();
	}

    /**
     * Checks if a point is contained within the button's shape.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @return true if the point is within the shape, else false
     */
	@Override
	public boolean contains(int x, int y) {
		final int W = getWidth();
		final int H = getHeight();

        // Determine if the point is within the bounds of the button shape
        if (R <= x && x <= W - R && R <= y && y <= H - R) { // center
			return true;
		} else if (0 <= x && x <= R && 0 <= y && y <= R) { // top left
			return Math.pow(x - R, 2) + Math.pow(y - R, 2) <= R * R;
        } else if (0 <= x && x <= R && H - R <= y && y <= H) { // bottom left
			return Math.pow(x - R, 2) + Math.pow(H - R - y, 2) <= R * R;
        } else if (W - R <= x && x <= W && 0 <= y && y <= R) { // top right
			return Math.pow(W - R - x, 2) + Math.pow(y - R, 2) <= R * R;
        } else if (W - R <= x && x <= W && H - R <= y && y <= H) { // bottom right
			return Math.pow(W - R - x, 2) + Math.pow(H - R - y, 2) <= R * R;
        } else if (R < x && x < W - R && 0 <= y && y < R) { // top side
			return true;
        } else if (0 <= x && x < R && R < y && y < H - R) { // left side
			return true;
        } else if (W - R < x && x < W && R < y && y < H - R) { // right side
			return true;
        } else if (R < x && x < W - R && H - R < y && y < H) { // bottom side
			return true;
		}

		return false;
	}
}