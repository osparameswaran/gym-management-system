import javax.swing.SwingUtilities;

public class GymManagementSystem {
    @SuppressWarnings("Convert2Lambda")
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    private static void createAndShowGUI() {
        try {
            System.out.println("Testing database connection...");
            if (DatabaseConnection.getConnection() != null) {
                System.out.println("Database connection successful!");
            } else {
                System.out.println("Database connection failed!");
                return;
            }
            
            GymGUI gymGUI = new GymGUI();
            gymGUI.setVisible(true);
            System.out.println("Gym Management System started successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start Gym Management System: " + e.getMessage());
        }
    }
}