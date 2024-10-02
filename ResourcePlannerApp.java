import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ResourcePlannerApp extends JFrame implements ActionListener {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JButton salleButton, professeurButton, groupeButton, menuButton;

    public ResourcePlannerApp() {
        super("Resource Planner App");
        setLayout(new BorderLayout());

        // Panel d'accueil
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Veuillez choisir le type à modifier");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 20, 20, 20);
        homePanel.add(label, gbc);

        salleButton = new JButton("Salle");
        salleButton.setFont(new Font("Arial", Font.PLAIN, 18));
        salleButton.setPreferredSize(new Dimension(200, 50));
        salleButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        homePanel.add(salleButton, gbc);

        professeurButton = new JButton("Professeur");
        professeurButton.setFont(new Font("Arial", Font.PLAIN, 18));
        professeurButton.setPreferredSize(new Dimension(200, 50));
        professeurButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 1;
        homePanel.add(professeurButton, gbc);

        groupeButton = new JButton("Groupe Etudiant");
        groupeButton.setFont(new Font("Arial", Font.PLAIN, 18));
        groupeButton.setPreferredSize(new Dimension(200, 50));
        groupeButton.addActionListener(this);
        gbc.gridx = 2;
        gbc.gridy = 1;
        homePanel.add(groupeButton, gbc);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(homePanel, "Home");

        cardPanel.add(new SallePanel(this), "Salle");
        cardPanel.add(new ProfesseurPanel(this), "Professeur");
        cardPanel.add(new GroupePanel(this), "Groupe Etudiant");

        add(cardPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == salleButton) {
            cardLayout.show(cardPanel, "Salle");
        } else if (e.getSource() == professeurButton) {
            cardLayout.show(cardPanel, "Professeur");
        } else if (e.getSource() == groupeButton) {
            cardLayout.show(cardPanel, "Groupe Etudiant");
        }
    }


    public void showHomePage() {
        cardLayout.show(cardPanel, "Home");
    }

    public static void main(String[] args) {
        ResourcePlannerApp app = new ResourcePlannerApp();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(800, 600);
        app.setVisible(true);
    }
}
