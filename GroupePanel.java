import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Classe GroupePanel qui représente un panneau d'interface graphique pour la modification
 * et la gestion des groupes d'étudiants dans l'application ResourcePlannerApp.
 */
public class GroupePanel extends JPanel {
    private JLabel contenufichierLabel;
    private JComboBox<String> comboPromo;

    /**
     * Constructeur de la classe GroupePanel.
     * 
     * @param app Instance de l'application ResourcePlannerApp.
     */
    public GroupePanel(ResourcePlannerApp app) {
        setLayout(new BorderLayout());

        // Panneau supérieur pour le titre et le bouton Menu
        JPanel topPanel = new JPanel(new BorderLayout());

        // Bouton Menu
        JButton menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 18));
        menuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                app.showHomePage();
            }
        });
        topPanel.add(menuButton, BorderLayout.WEST);

        // Titre
        JLabel letitreLabel = new JLabel("Modificateur de script pour Jikan", SwingConstants.CENTER);
        letitreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(letitreLabel, BorderLayout.CENTER);

        // Panneau principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Label "Vous modifiez étudiants"
        JLabel label = new JLabel("Vous Modifiez étudiants");
        label.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(label, gbc);

        // Label "Choisir le type de promotion à modifier"
        JLabel typePromoJLabel = new JLabel("Choisir le type de promotion à Afficher :");
        typePromoJLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(typePromoJLabel, gbc);

        // ComboBox pour sélectionner le type de promotion
        String[] options = { "Info 1", "Info 2", "Info 3" };
        comboPromo = new JComboBox<>(options);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(comboPromo, gbc);

        // Label "Voici les informations :"
        JLabel nomPromoJLabel = new JLabel("Voici les informations :");
        nomPromoJLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(nomPromoJLabel, gbc);

        // JLabel pour afficher le contenu du fichier
        refreshComboBox();
        contenufichierLabel = new JLabel();
        contenufichierLabel.setVerticalAlignment(SwingConstants.TOP);
        contenufichierLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(contenufichierLabel);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(scrollPane, gbc);

        // Panneau pour les boutons
        JPanel lesbouttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));

        // Boutton Supprimer
        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.setBackground(Color.RED);
        lesbouttonsPanel.add(supprimerButton);

        // ActionListener pour le bouton "Supprimer"
        supprimerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createDeleteFileDialog();
            }
        });

        // Bouton Modifier
        JButton modifierButton = new JButton("Modifier");
        lesbouttonsPanel.add(modifierButton);

        // ActionListener pour le bouton "Modifier"
        modifierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createEditFileDialog();
            }
        });

        // Bouton Ajouter
        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.setBackground(Color.GREEN);
        lesbouttonsPanel.add(ajouterButton);

        // ActionListener pour le bouton "Ajouter"
        ajouterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAddFileDialog();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(lesbouttonsPanel, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Initialiser avec le contenu du premier fichier
        updateContent("jikan/data/jikan2324/students/Info/Info1.txt");

        // Ajouter un ActionListener à la JComboBox pour changer le contenu du JLabel
        comboPromo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) comboPromo.getSelectedItem();
                if (selectedOption != null) {
                    String filePath = "jikan/data/jikan2324/students/Info/" + selectedOption.replace(" ", "") + ".txt";
                    updateContent(filePath);
                }
            }
        });
    }

    /**
     * Lit le contenu d'un fichier texte et le retourne sous forme de chaîne de caractères.
     * 
     * @param path Le chemin du fichier à lire.
     * @return Le contenu du fichier sous forme de chaîne de caractères, ou null en cas d'erreur.
     */
    private String readFile(String path) {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            byte[] fileContent = dis.readAllBytes();
            return new String(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Met à jour le JLabel en fonction du fichier sélectionné.
     * 
     * @param filePath Le chemin du fichier à lire.
     */
    private void updateContent(String filePath) {
        String content = readFile(filePath);
        if (content != null) {
            contenufichierLabel.setText("<html>" + content.replace("\n", "<br>") + "</html>");
        } else {
            contenufichierLabel.setText("Erreur lors de la lecture du fichier.");
        }
    }

    /**
     * Met à jour les fichiers disponibles pour la JComboBox.
     */
    private void refreshComboBox() {
        File folder = new File("jikan/data/jikan2324/students/Info/");
        File[] files = folder.listFiles();
        if (files != null) {
            comboPromo.removeAllItems();
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".txt")) {
                        fileName = fileName.substring(0, fileName.length() - 4); // Supprimer l'extension ".txt"
                    }
                    comboPromo.addItem(fileName);
                }
            }
        }
    }

    /**
     * Remplit une JComboBox avec les noms des fichiers disponibles.
     * 
     * @param comboBox La JComboBox à remplir.
     */
    private void remplirComboBox(JComboBox<String> comboBox) {
        File folder = new File("jikan/data/jikan2324/students/Info/");
        File[] files = folder.listFiles();
        if (files != null) {
            comboBox.removeAllItems();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    comboBox.addItem(file.getName().substring(0, file.getName().length() - 4));
                }
            }
        }
    }

    /**
     * Crée et affiche une fenêtre de dialogue pour supprimer un fichier.
     */
    private void createDeleteFileDialog() {
        // Création de la fenêtre de suppression de fichier
        JFrame deleteFileDialog = new JFrame("Supprimer une Promotion");
        deleteFileDialog.setSize(300, 200);
        deleteFileDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteFileDialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JComboBox<String> fileComboBox = new JComboBox<>();
        remplirComboBox(fileComboBox); // Remplir la combobox avec les fichiers disponibles
        fileComboBox.setPreferredSize(new Dimension(200, 25)); // Définition de la taille de la combobox
        panel.add(fileComboBox, BorderLayout.NORTH);

        // Création du JLabel avec le texte d'avertissement
        JLabel warningLabel = new JLabel("Attention cette action est irréversible !", JLabel.CENTER);
        panel.add(warningLabel, BorderLayout.CENTER); // Ajout du JLabel au panneau, au-dessus de la JComboBox

        JLabel statusLabel = new JLabel("", JLabel.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        // Création du panneau pour les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2)); // GridLayout pour aligner les boutons horizontalement

        // Bouton Annuler
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFileDialog.dispose(); // Ferme la fenêtre de suppression
            }
        });
        buttonPanel.add(cancelButton); // Ajoute le bouton "Annuler" au panneau des boutons

        // Bouton Supprimer
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFileName = (String) fileComboBox.getSelectedItem();
                if (selectedFileName != null) {
                    String filePath = "jikan/data/jikan2324/students/Info/" + selectedFileName + ".txt";
                    File fileToDelete = new File(filePath);
                    if (fileToDelete.exists()) {
                        if (fileToDelete.delete()) {
                            statusLabel.setText("Suppression effectuée."); // Message de succès
                            // Mettre à jour la Combobox après la suppression
                            remplirComboBox(fileComboBox);
                            // Mettre à jour la Combobox présente sur la page de base "Groupe"
                            refreshComboBox();

                            // Supprimer le nom du fichier supprimé de aCharger.txt
                            String indexFilePath = "jikan/data/jikan2324/students/Info/aCharger.txt";
                            try {
                                // Lire toutes les lignes de aCharger.txt
                                List<String> lines = Files.readAllLines(Paths.get(indexFilePath));
                                // Supprimer la ligne contenant le nom du fichier supprimé
                                lines.remove(selectedFileName);
                                // Réécrire toutes les lignes dans aCharger.txt
                                Files.write(Paths.get(indexFilePath), lines);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null,
                                        "Erreur lors de la mise à jour de aCharger.txt.", "Erreur",
                                        JOptionPane.ERROR_MESSAGE);
                            }

                            // Créer la fenêtre modale de confirmation de suppression
                            JOptionPane.showMessageDialog(null,
                                    "Fichier \"" + selectedFileName + "\" supprimé avec succès.", "Suppression",
                                    JOptionPane.INFORMATION_MESSAGE);
                            deleteFileDialog.dispose();
                        } else {
                            statusLabel.setText("Erreur lors de la suppression du fichier.");
                        }
                    } else {
                        statusLabel.setText("Le fichier sélectionné n'existe pas.");
                    }
                } else {
                    statusLabel.setText("Aucun fichier sélectionné.");
                }
            }
        });

        buttonPanel.add(deleteButton); // Ajoute le bouton "Supprimer" au panneau des boutons

        panel.add(buttonPanel, BorderLayout.SOUTH); // Ajoute le panneau des boutons au bas du panneau principal

        deleteFileDialog.add(panel);
        deleteFileDialog.setVisible(true);
    }

    /**
     * Crée et affiche une fenêtre de dialogue pour modifier un fichier.
     */
    private void createEditFileDialog() {
        // Créer une nouvelle fenêtre pour modifier un fichier
        JFrame editFileDialog = new JFrame("Modifier un fichier");
        editFileDialog.setSize(400, 250); // Augmentation de la taille de la fenêtre
        editFileDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFileDialog.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JComboBox<String> fileComboBox = new JComboBox<>();
        remplirComboBox(fileComboBox); // Remplir la combobox avec les fichiers disponibles
        fileComboBox.setPreferredSize(new Dimension(300, 25)); // Définition de la taille de la combobox
        panel.add(fileComboBox, BorderLayout.NORTH);

        // Zone de texte pour modifier le contenu du fichier
        JTextArea textArea = new JTextArea(10, 30); // Augmentation de la taille de la zone de texte
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Création du panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // Bouton "Annuler"
        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editFileDialog.dispose(); // Fermer la fenêtre de modification
            }
        });
        buttonPanel.add(cancelButton);

        // Bouton "Enregistrer"
        JButton confirmButton = new JButton("Enregistrer");
        confirmButton.setBackground(Color.GREEN);
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFileName = (String) fileComboBox.getSelectedItem();
                if (selectedFileName != null) {
                    String filePath = "jikan/data/jikan2324/students/Info/" + selectedFileName + ".txt";
                    // Écrire le contenu modifié dans le fichier
                    try (PrintWriter writer = new PrintWriter(filePath)) {
                        writer.print(textArea.getText());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(editFileDialog,
                                "Erreur lors de l'enregistrement des modifications.", "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    refreshComboBox();
                    JOptionPane.showMessageDialog(editFileDialog,
                            "Modifications enregistrées avec succès.", "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    editFileDialog.dispose();
                }
            }
        });
        buttonPanel.add(confirmButton);

        // Ajouter le panneau des boutons en bas de la fenêtre
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Charger le contenu du fichier sélectionné dans la zone de texte
        fileComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFileName = (String) fileComboBox.getSelectedItem();
                if (selectedFileName != null) {
                    String filePath = "jikan/data/jikan2324/students/Info/" + selectedFileName + ".txt";
                    String fileContent = readFile(filePath);
                    if (fileContent != null) {
                        textArea.setText(fileContent);
                    } else {
                        textArea.setText("Erreur lors de la lecture du fichier.");
                    }
                }
            }
        });

        editFileDialog.add(panel);
        editFileDialog.setVisible(true);
    }

    /**
     * Crée et affiche une fenêtre de dialogue pour ajouter un nouveau fichier.
     */
    private void createAddFileDialog() {
        // Demander à l'utilisateur le nom du nouveau fichier
        String nomFichier = JOptionPane.showInputDialog(null, "Nom du nouveau fichier:",
                "Ajouter une Promotion", JOptionPane.PLAIN_MESSAGE);

        // Vérifier si l'utilisateur a annulé la saisie
        if (nomFichier != null) {
            if (!nomFichier.trim().isEmpty()) {
                // Vérifier si le fichier existe déjà
                File folder = new File("jikan/data/jikan2324/students/Info/");
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().equals(nomFichier + ".txt")) {
                            JOptionPane.showMessageDialog(GroupePanel.this,
                                    "Un fichier avec le même nom existe déjà.", "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                            return; // Sortir de la méthode si le fichier existe déjà
                        }
                    }
                }

                // Le fichier n'existe pas encore, continuer avec l'ajout
                // Créer un nouveau JDialog
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(GroupePanel.this),
                        "Ajouter un nouveau fichier", true);
                dialog.setLayout(new BorderLayout());

                // Panneau principal avec GridBagLayout
                JPanel contentPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.fill = GridBagConstraints.BOTH;

                // Label en haut à gauche
                JLabel label = new JLabel(
                        "<html><b>Instructions:</b><br><br>1. La Couleur.<br>Format : 'Couleur' + 'Code couleur RVB'<br><br>2. Intitulé de Promotion.<br> Exemple: Info 'DUT INFO, 1e année'.<br><br>3. 'Partition' + Plus Petit Commun Multiple des tailles des Groupes.<br> Exemple : Partition 12<br><br>4 et +. Les Groupes.<br> Exemple : Groupe 2 info1x info1y<br> Format : 'Groupe' 'NombredeGroupes' 'NomGroupe1' 'NomGroupe 2'</html>");
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 1;
                gbc.gridheight = 1;
                gbc.weightx = 0.0;
                gbc.weighty = 0.0;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                contentPanel.add(label, gbc);

                // Bouton pour afficher les codes couleurs RVB
                JButton couleurRVBButton = new JButton("Codes Couleurs RVB");
                couleurRVBButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Créer une nouvelle fenêtre pour afficher les codes couleurs RVB
                        JFrame colorsFrame = new JFrame("Codes Couleurs RVB");
                        colorsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fermer la fenêtre sans
                                                                                       // quitter l'application

                        // Créer un panneau pour organiser les labels
                        JPanel labelsPanel = new JPanel();
                        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS)); // Organiser les composants
                                                                                             // verticalement

                        // Lire le contenu du fichier Couleurs.txt et créer un JLabel pour chaque ligne
                        try (BufferedReader reader = new BufferedReader(new FileReader("Couleurs.txt"))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                JLabel label = new JLabel(line);
                                label.setAlignmentX(Component.LEFT_ALIGNMENT); // Aligner les labels à gauche
                                labelsPanel.add(label); // Ajouter chaque label dans le panneau
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erreur lors de la lecture du fichier.",
                                    "Erreur", JOptionPane.ERROR_MESSAGE);
                        }

                        // Ajouter le panneau contenant les labels à la fenêtre
                        colorsFrame.add(labelsPanel);

                        // Ajuster la taille de la fenêtre en fonction du contenu
                        colorsFrame.pack();

                        // Centrer la fenêtre sur l'écran
                        colorsFrame.setLocationRelativeTo(null);

                        // Rendre la fenêtre visible
                        colorsFrame.setVisible(true);
                    }
                });
                // Définir la taille du bouton
                couleurRVBButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Réduire la taille de la police
                gbc.gridx = 0;
                gbc.gridy = 1; // Modifier la position en fonction de votre mise en page
                gbc.gridwidth = 1; // Prendre une seule cellule de largeur
                gbc.gridheight = 1; // Prendre une seule cellule de hauteur
                gbc.weightx = 0.0; // Ne pas étendre horizontalement
                gbc.weighty = 0.0; // Ne pas étendre verticalement
                gbc.anchor = GridBagConstraints.NORTHWEST; // Aligner en haut à gauche
                couleurRVBButton.setBackground(Color.GRAY);
                couleurRVBButton.setForeground(Color.WHITE);
                contentPanel.add(couleurRVBButton, gbc);

                // Zone de texte pour le contenu du fichier à droite
                JTextArea textArea = new JTextArea(20, 40); // 20 lignes, 40 colonnes
                textArea.setText("Ecrivez ici...");
                JScrollPane scrollPane = new JScrollPane(textArea);
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridwidth = 1;
                gbc.gridheight = 2;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                gbc.anchor = GridBagConstraints.CENTER;
                contentPanel.add(scrollPane, gbc);

                // Ajouter un bouton "Annuler" en bas à gauche
                JButton cancelButton = new JButton("Annuler");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose(); // Fermer le dialogue sans effectuer d'action
                    }
                });
                gbc.gridx = 0; // Modifier la colonne pour placer le bouton "Annuler" à gauche
                gbc.gridy = 2; // Modifier la ligne pour placer le bouton "Annuler" en bas
                gbc.anchor = GridBagConstraints.SOUTHWEST; // Aligner le bouton "Annuler" en bas à gauche
                contentPanel.add(cancelButton, gbc); // Ajouter le bouton "Annuler" avec les contraintes gbc

                // Ajouter le bouton "Confirmer" en bas à droite
                JButton confirmButton = new JButton("Confirmer");
                confirmButton.setBackground(Color.GREEN);
                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Récupérer le contenu du texte de la zone de texte
                        String contenuFichier = textArea.getText();
                        // Créer le chemin complet du nouveau fichier
                        String filePath = "jikan/data/jikan2324/students/Info/" + nomFichier + ".txt";

                        // Écrire le contenu dans le nouveau fichier
                        try (PrintWriter writer = new PrintWriter(filePath)) {
                            writer.println(contenuFichier);
                            writer.flush();
                            writer.close();

                            // Mettre à jour la JComboBox avec le nouveau fichier ajouté
                            comboPromo.addItem(nomFichier);
                            refreshComboBox();

                            // Ajouter le nom du nouveau fichier à la suite dans aCharger.txt
                            String indexFilePath = "jikan/data/jikan2324/students/Info/aCharger.txt";
                            try (PrintWriter indexWriter = new PrintWriter(
                                    new FileWriter(indexFilePath, true))) {
                                indexWriter.println(nomFichier); // Ajouter le nom du fichier à la ligne suivante
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(GroupePanel.this,
                                        "Erreur lors de l'écriture dans le fichier aCharger.txt.", "Erreur",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(GroupePanel.this,
                                    "Erreur lors de la création du fichier.", "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        JOptionPane.showMessageDialog(null,
                                    "Fichier \"" + nomFichier + "\" ajouté avec succès.", "Suppression",
                                    JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    }
                });

                gbc.gridx = 1; // Modifier la colonne pour placer le bouton "Confirmer" à droite
                gbc.gridy = 2; // Modifier la ligne pour placer le bouton "Confirmer" en bas
                gbc.anchor = GridBagConstraints.SOUTHEAST; // Aligner le bouton "Confirmer" en bas à droite
                contentPanel.add(confirmButton, gbc); // Ajouter le bouton "Confirmer" avec les contraintes gbc

                // Ajouter le panneau de contenu au dialogue
                dialog.add(contentPanel, BorderLayout.CENTER);

                // Ajuster la taille du dialogue en fonction du contenu
                dialog.pack();

                // Centrer le dialogue sur le panneau GroupePanel
                dialog.setLocationRelativeTo(GroupePanel.this);

                // Rendre le dialogue visible
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(GroupePanel.this, "Nom de fichier invalide.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}