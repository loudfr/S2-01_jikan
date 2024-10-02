import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

public class ProfesseurPanel extends JPanel {

    private JLabel detailLabel;
    private JPanel comboAndDetailPanel;
    private JPanel detailPanel;
    private HashMap<String, String> professorsName = new HashMap<>();
    private JCheckBox extCheckBox;
    private JCheckBox permCheckBox;
    private JCheckBox voisinsCheckBox;
    private JButton colorButton;
    private Color selectedColor = new Color(180, 140, 228); // Default color

    public ProfesseurPanel(ResourcePlannerApp app) {
        setLayout(new BorderLayout());

        // Panneau supérieur avec bouton Menu et titre
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 18));
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                app.showHomePage();
            }
        });
        topPanel.add(menuButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Modificateur de script pour Jikan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Panneau central avec étiquettes et combobox
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel modifyLabel = new JLabel("Vous modifiez professeurs", SwingConstants.CENTER);
        modifyLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Augmenter la taille de la police
        modifyLabel.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(modifyLabel);

        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.Y_AXIS));
        
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel chooseTypeLabel = new JLabel("Choisir le type de professeur à modifier");
        chooseTypeLabel.setFont(new Font("Arial", Font.PLAIN, 22)); // Augmenter la taille de la police
        labelPanel.add(chooseTypeLabel);
        comboPanel.add(labelPanel);

        comboAndDetailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] professorTypes = {"Extérieurs", "Permanents", "Voisins", "Creation"};
        JComboBox<String> professorTypeComboBox = new JComboBox<>(professorTypes);
        professorTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        professorTypeComboBox.setPreferredSize(new Dimension(150, 30)); // Définir une taille préférée plus petite
        comboAndDetailPanel.add(professorTypeComboBox);

        detailPanel = new JPanel(new GridLayout(0, 1));
        detailLabel = new JLabel("Sélectionnez un type de professeur pour afficher les détails...", SwingConstants.CENTER);
        detailLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Augmenter la taille de la police
        detailPanel.add(detailLabel);
        comboAndDetailPanel.add(detailPanel);

        comboPanel.add(comboAndDetailPanel);
        centerPanel.add(comboPanel);
        
        professorTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
                String selectedType = (String) comboBox.getSelectedItem();
                updateDetailPanel(selectedType);
            }
        });

        add(centerPanel, BorderLayout.CENTER);
    }

    private String readFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateDetailPanel(String professorType) {
        detailPanel.removeAll();
        professorsName.clear();

        switch (professorType) {
            case "Extérieurs":
                detailLabel.setText("Vous avez choisi de modifier les professeurs extérieurs...");
                loadProfessors("data/jikan2324/teachers/Ext/aCharger.txt", "data/jikan2324/teachers/Ext/");
                break;
            case "Permanents":
                detailLabel.setText("Vous avez choisi de modifier les professeurs permanents...");
                loadProfessors("data/jikan2324/teachers/Perm/aCharger.txt", "data/jikan2324/teachers/Perm/");
                break;
            case "Voisins":
                detailLabel.setText("Vous avez choisi de modifier les professeurs voisins...");
                loadProfessors("data/jikan2324/teachers/Voisins/aCharger.txt", "data/jikan2324/teachers/Voisins/");
                break;
            case "Creation":
                detailLabel.setText("Vous avez choisi de créer un nouveau professeur...");
                createProfessorForm();
                break;
            default:
                detailLabel.setText("Sélectionnez un type de professeur pour le modifier...");
                break;
        }
        detailPanel.add(detailLabel);
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private void loadProfessors(String listPath, String detailsPath) {
        detailPanel.add(detailLabel);
        String content = readFile(listPath);
        if (content != null) {
            String[] lines = content.split("\n");
            for (int i = 1; i < lines.length; i++) {
                String details = readFile(detailsPath + lines[i] + ".txt");
                if (details != null) {
                    String profName = details.split("\n")[1].substring(6, details.split("\n")[1].length() - 1);
                    professorsName.put(profName, lines[i]);
                }
            }
            JComboBox<String> comboBox = new JComboBox<>(professorsName.keySet().toArray(new String[0]));
            detailPanel.add(comboBox);

            JButton modifyButton = new JButton("Modifier professeur");
            detailPanel.add(modifyButton);

            JButton deleteButton = new JButton("Supprimer professeur");
            deleteButton.setBackground(Color.RED);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteProfessor(comboBox, detailsPath);
                }
            });
            detailPanel.add(deleteButton);
        }
    }

    private void deleteProfessor(JComboBox<String> comboBox, String detailsPath) {
        int res = JOptionPane.showConfirmDialog(null, "Voulez-vous sauvegarder", "Attention", JOptionPane.YES_NO_CANCEL_OPTION);
        if (res == JOptionPane.NO_OPTION) {
            return;
        }
        String selectedProf = (String) comboBox.getSelectedItem();
        String profId = professorsName.get(selectedProf);
        String path = detailsPath + profId + ".txt";
        String newFile = readFile(detailsPath + "aCharger.txt").replace(profId + "\n", "");

        try (FileWriter fw = new FileWriter(detailsPath + "aCharger.txt")) {
            fw.write(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (new File(path).delete()) {
            JOptionPane.showMessageDialog(null, "Professeur supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du professeur", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createProfessorForm() {
        JLabel nameLabel = new JLabel("Prenom puis Nom du professeur:");
        JTextField nameField = new JTextField(20);

        extCheckBox = new JCheckBox("Extérieurs");
        permCheckBox = new JCheckBox("Permanents");
        voisinsCheckBox = new JCheckBox("Voisins");

        colorButton = new JButton("Choisir Couleur");
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choisir Couleur", selectedColor);
                if (newColor != null) {
                    selectedColor = newColor;
                    colorButton.setBackground(selectedColor);
                }
            }
        });

        JButton createButton = new JButton("Créer Professeur");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fullName = nameField.getText();
                String[] nameParts = fullName.split(" ");
                if (nameParts.length < 2) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer un nom complet (prénom et nom de famille)", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String firstName = nameParts[0];
                String lastName = nameParts[1];
                if (lastName.length() < 3) {
                    JOptionPane.showMessageDialog(null, "Le nom de famille doit contenir au moins trois lettres", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String selectedType = getSelectedType();
                if (selectedType == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un type de professeur", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                createProfessor(firstName, lastName, selectedType, selectedColor);
            }
        });

        detailPanel.add(nameLabel);
        detailPanel.add(nameField);
        detailPanel.add(extCheckBox);
        detailPanel.add(permCheckBox);
        detailPanel.add(voisinsCheckBox);
        detailPanel.add(colorButton);
        detailPanel.add(createButton);
    }

    private String getSelectedType() {
        if (extCheckBox.isSelected()) {
            return "Ext";
        } else if (permCheckBox.isSelected()) {
            return "Perm";
        } else if (voisinsCheckBox.isSelected()) {
            return "Voisins";
        }
        return null;
    }

    private void createProfessor(String firstName, String lastName, String type, Color color) {
        String fileName = firstName.charAt(0) + lastName.substring(0, 3);
        String newProfFilePath = "data/jikan2324/teachers/" + type + "/" + fileName + ".txt";
        String newProfContent = String.format("Couleur %d %d %d\nInfo \"%s %s\"", color.getRed(), color.getGreen(), color.getBlue(), firstName, lastName);

        try (FileWriter fw = new FileWriter(newProfFilePath)) {
            fw.write(newProfContent);
            String aChargerPath = "data/jikan2324/teachers/" + type + "/aCharger.txt";
            try (FileWriter fwACharger = new FileWriter(aChargerPath, true)) {
                fwACharger.write(fileName + "\n");
            }
            JOptionPane.showMessageDialog(null, "Professeur créé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de la création du professeur", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}

