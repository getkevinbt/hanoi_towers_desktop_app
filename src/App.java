public class App {
    public static void main(String[] args) throws Exception {
		java.awt.EventQueue.invokeLater(() -> {
			new MyGraphics().setVisible(true);
		});
    }
}