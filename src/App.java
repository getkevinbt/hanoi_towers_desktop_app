public class App {
    /**
     * Entry point of the application. Initializes the GUI window.
     *
     * @param args command-line arguments
     * @throws Exception if an error occurs during the creation of the GUI
     */
    public static void main(String[] args) throws Exception {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
		java.awt.EventQueue.invokeLater(() -> {
			new MyGraphics().setVisible(true);
		});
    }
}