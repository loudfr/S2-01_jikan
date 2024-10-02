import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SallePanel extends JPanel {

    private JPanel p1, p2;


    /**
     * Construit un nouvel objet SallePanel avec l'objet ResourcePlannerApp donné.
     *
     * @param app l'objet ResourcePlannerApp à utiliser pour afficher la page d'accueil
     */
    public SallePanel(ResourcePlannerApp app) {
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
        //fin bordure du haut

        // panneau central
        p1 = new JPanel(new BorderLayout());
        add(p1, BorderLayout.CENTER);

        JLabel salle = new JLabel("Vous modifiez salles de cours");
        salle.setFont(new Font("Arial", Font.BOLD, 28));
        salle.setHorizontalAlignment(salle.CENTER);
        p1.add(salle, BorderLayout.NORTH);

        p2 = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel text = new JLabel("Modifier dans la catégorie :");
        text.setFont(new Font("Arial", Font.CENTER_BASELINE, 25));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(text, gbc);

        String[] items_categorie = {"Cours", "TDs", "TPs", "Autres"};
        JComboBox<String> cat = new JComboBox<>(items_categorie);
        cat.setFont(new Font("Arial", Font.PLAIN, 18)); // Agrandir la taille du texte
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(cat, gbc);

        JLabel ajouterLabel = new JLabel(", la salle :");
        ajouterLabel.setFont(new Font("Arial", Font.CENTER_BASELINE, 25));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(ajouterLabel, gbc);

        JComboBox<String> itemsComboBox = new JComboBox<>();
        itemsComboBox.setFont(new Font("Arial", Font.PLAIN, 18)); // Agrandir la taille du texte
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        p2.add(itemsComboBox, gbc);

        cat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCategory = (String) cat.getSelectedItem(); // Récupère le type de cours sélectionné
                selectedCategory = selectedCategory.toLowerCase(); // Convertit en minuscules

                // Chemin du dossier correspondant au type de cours
                String folderPath = "jikan/data/jikan2324/rooms/" + selectedCategory;

                // Liste pour stocker les noms de fichiers .txt
                List<String> fileNames = new ArrayList<>();

                // Récupère les noms de fichiers .txt dans le dossier
                File folder = new File(folderPath);
                File[] files = folder.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".txt");
                    }
                });

                // Ajoute les noms de fichiers à la liste
                if (files != null) {
                    for (File file : files) {
                        String fileName = file.getName();
                        // Enlève l'extension .txt du nom de fichier
                        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
                        fileNames.add(nameWithoutExtension);
                    }
                }

                // Met à jour les choix dans le JComboBox
                itemsComboBox.removeAllItems();
                for (String fileName : fileNames) {
                    itemsComboBox.addItem(fileName);
                }
            }
        });

        // Définition des items pour chaque catégorie
        String[] items_cours = {"Amphi", "SCons", "SC1", "SConf", "SExt", "SC3", "SC2", "SRinfo", "Amph", "SCrz"};
        String[] items_tds = {"SCC", "TD1", "TD2", "TD2", "TD2", "Bibli", "TD1"};
        String[] items_tps = {"IG", "Mul", "TR", "Res", "Tim", "IN", "Alab", "Guy", "Ada", "Mae", "Tur", "SII1"};
        String[] items_autres = {"Nancy", "Epinal", "M21s", "EnPres", "EnPres"};

        // Action lors de la sélection d'une catégorie  
        cat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCategory = (String) cat.getSelectedItem();
                itemsComboBox.removeAllItems();
                // affiche les bons items en fonction du premier choix de la combobox
                if (selectedCategory.equals("Cours")) {
                    for (String item : items_cours) {
                        itemsComboBox.addItem(item);
                    }
                } else if (selectedCategory.equals("TDs")) {
                    for (String item : items_tds) {
                        itemsComboBox.addItem(item);
                    }
                } else if (selectedCategory.equals("TPs")) {
                    for (String item : items_tps) {
                        itemsComboBox.addItem(item);
                    }
                } else if (selectedCategory.equals("Autres")) {
                    for (String item : items_autres) {
                        itemsComboBox.addItem(item);
                    }
                }
                
                
            }
        });

        p1.add(p2, BorderLayout.CENTER);

        // Ajout du panneau pour le bouton "Nouveau"
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton nouveauButton = new JButton("Nouveau");
        nouveauButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Vider le panneau principal et le panneau central avant d'ajouter les nouveaux composants
                bottomPanel.removeAll();
                p2.removeAll();
        
                // Création d'un nouveau panneau avec les composants spécifiés
                JPanel pNouveau = new JPanel(new GridBagLayout());
                GridBagConstraints gbcNew = new GridBagConstraints();
                gbcNew.insets = new Insets(10, 10, 10, 10);

                JLabel CreationNv = new JLabel("Vous créez une nouvelle salle.");
                CreationNv.setFont(new Font("Arial", Font.BOLD, 18));
                gbcNew.gridx = 0;
                gbcNew.gridy = 0;
                gbcNew.anchor = GridBagConstraints.WEST;
                pNouveau.add(CreationNv, gbcNew);
        
                JLabel titleLabelNew = new JLabel("Salle de :");
                titleLabelNew.setFont(new Font("Arial", Font.PLAIN, 18));
                gbcNew.gridx = 0;
                gbcNew.gridy = 1;
                gbcNew.anchor = GridBagConstraints.WEST;
                pNouveau.add(titleLabelNew, gbcNew);
        
                JComboBox<String> catNew = new JComboBox<>(items_categorie);
                gbcNew.gridx = 1;
                gbcNew.gridy = 1;
                gbcNew.anchor = GridBagConstraints.WEST;
                pNouveau.add(catNew, gbcNew);
        
                JLabel nomAbregeLabel = new JLabel("De nom abrégé :");
                nomAbregeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                gbcNew.gridx = 0;
                gbcNew.gridy = 2;
                gbcNew.anchor = GridBagConstraints.WEST;
                pNouveau.add(nomAbregeLabel, gbcNew);
        
                JTextField nomAbregeTextField = new JTextField(15);
                gbcNew.gridx = 1;
                gbcNew.gridy = 2;
                gbcNew.anchor = GridBagConstraints.WEST;
                pNouveau.add(nomAbregeTextField, gbcNew);
        
                JLabel couleurLabel = new JLabel("Et de couleur :");
                couleurLabel.setFont(new Font("Arial", Font.PLAIN, 18));
                gbcNew.gridx = 0;
                gbcNew.gridy = 3;
                gbcNew.anchor = GridBagConstraints.WEST;
                pNouveau.add(couleurLabel, gbcNew);
        
                JButton choisirCouleurButton = new JButton("Choisir Couleur");
                choisirCouleurButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Ouverture d'une boîte de dialogue de sélection de couleur
                        Color selectedColor = JColorChooser.showDialog(null, "Choisir une couleur", Color.BLACK);
                        // Traitement de la couleur sélectionnée ici (par exemple, mise à jour de l'affichage)
                        if (selectedColor != null) {
                            choisirCouleurButton.setBackground(selectedColor);
                            choisirCouleurButton.setOpaque(true);
                        }
                    }
                });
                gbcNew.gridx = 1;
                gbcNew.gridy = 3;
                gbcNew.anchor = GridBagConstraints.WEST;
                pNouveau.add(choisirCouleurButton, gbcNew);
        
                // bouton ok dans le panneau de base
                JButton okButtonNouveau = new JButton("OK");
                okButtonNouveau.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Récupère les informations saisies par l'utilisateur
                        String newRoomName = nomAbregeTextField.getText(); // Nom de la nouvelle salle
                        Color newColor = choisirCouleurButton.getBackground(); // Couleur de la nouvelle salle
                        String selectedCategory = (String) catNew.getSelectedItem(); // Catégorie de la nouvelle salle
                        selectedCategory = selectedCategory.toLowerCase(); // Convertit en minuscules

                        // Construit le contenu du fichier .txt
                        String fileContent = "Couleur " + newColor.getRed() + " " + newColor.getGreen() + " " + newColor.getBlue() + "\n";
                        fileContent += "Info \"Salle de cours " + newRoomName + ", rdc\"";

                        // Chemin complet du nouveau fichier à créer
                        String filePath = "jikan/data/jikan2324/rooms/" + selectedCategory + "/" + newRoomName + ".txt";
                        File file = new File(filePath);

                        // Vérifie si le fichier existe déjà
                        if (file.exists()) {
                            // Affiche un message d'avertissement si le fichier existe déjà
                            JOptionPane.showMessageDialog(null, "Le fichier existe déjà.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                        } else {
                            // Crée un nouveau fichier .txt et écrit le contenu
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                                writer.write(fileContent);
                                // Affiche un message de succès si la création du fichier est réussie
                                JOptionPane.showMessageDialog(null, "Le fichier a été créé avec succès.");
                            } catch (IOException ex) {
                                // Affiche un message d'erreur si la création du fichier échoue
                                JOptionPane.showMessageDialog(null, "Erreur lors de la création du fichier.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                ex.printStackTrace();
                            }
                        }
                        // Réinitialiser le panneau après sauvegarde
                        bottomPanel.removeAll();
                        bottomPanel.add(nouveauButton);
                        bottomPanel.revalidate();
                        bottomPanel.repaint();
                    }
                });
                gbcNew.gridx = 0;
                gbcNew.gridy = 4;
                gbcNew.gridwidth = 2;
                gbcNew.anchor = GridBagConstraints.CENTER;
                pNouveau.add(okButtonNouveau, gbcNew);
        
                // Ajouter pNouveau au panneau central
                p2.add(pNouveau);
                p2.revalidate();
                p2.repaint();
            }
        });
        bottomPanel.add(nouveauButton);
        
    // Ajout du panneau pour le bouton "OK"
    JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // Vider le panneau central avant d'ajouter les nouveaux composants
            bottomPanel.removeAll();
            p2.removeAll();
    
            // Création d'un nouveau panneau avec les composants spécifiés
            JPanel pModif = new JPanel(new GridBagLayout());
            GridBagConstraints gbcModif = new GridBagConstraints();
            gbcModif.insets = new Insets(10, 10, 10, 10);

            JLabel modifSalleJLabel = new JLabel("Vous modifiez ... .");
            modifSalleJLabel.setFont(new Font("Arial", Font.BOLD, 18));
    
            JLabel nomLabel = new JLabel("Nom:");
            nomLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            gbcModif.gridx = 0;
            gbcModif.gridy = 0;
            gbcModif.anchor = GridBagConstraints.WEST;
            pModif.add(nomLabel, gbcModif);
    
            JTextField nomTextField = new JTextField(15);
            gbcModif.gridx = 1;
            gbcModif.gridy = 0;
            gbcModif.anchor = GridBagConstraints.WEST;
            pModif.add(nomTextField, gbcModif);
    
            JLabel couleurLabel = new JLabel("Couleur:");
            couleurLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            gbcModif.gridx = 0;
            gbcModif.gridy = 1;
            gbcModif.anchor = GridBagConstraints.WEST;
            pModif.add(couleurLabel, gbcModif);
    
            JButton choisirCouleurButton = new JButton("Choisir Couleur");
            choisirCouleurButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Ouverture d'une boîte de dialogue de sélection de couleur
                    Color selectedColor = JColorChooser.showDialog(null, "Choisir une couleur", Color.BLACK);
                    // Traitement de la couleur sélectionnée ici (par exemple, mise à jour de l'affichage)
                    if (selectedColor != null) {
                        choisirCouleurButton.setBackground(selectedColor);
                        choisirCouleurButton.setOpaque(true);
                    }
                }
            });
            gbcModif.gridx = 1;
            gbcModif.gridy = 1;
            gbcModif.anchor = GridBagConstraints.WEST;
            pModif.add(choisirCouleurButton, gbcModif);
    
            // Bouton supprimer
            JButton supprimerButton = new JButton("Supprimer");
            supprimerButton.setBackground(Color.RED);
            supprimerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Récupère le nom du fichier sélectionné dans le JComboBox
                    String selectedFileName = (String) itemsComboBox.getSelectedItem();
                    String selectedCategory = (String) cat.getSelectedItem();
                    selectedCategory = selectedCategory.toLowerCase();

                    // Chemin complet du fichier à supprimer
                    String filePath = "jikan/data/jikan2324/rooms/" + selectedCategory + "/" + selectedFileName + ".txt";
                    File file = new File(filePath);

                    // Vérifie si le fichier existe avant de le supprimer
                    if (file.exists()) {
                        if (file.delete()) {
                            // Affiche un message de succès si la suppression est réussie
                            JOptionPane.showMessageDialog(null, "Le fichier a été supprimé avec succès.");
                        } else {
                            // Affiche un message d'erreur si la suppression échoue
                            JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du fichier.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Affiche un message d'avertissement si le fichier n'existe pas
                        JOptionPane.showMessageDialog(null, "Le fichier n'existe pas.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                    }
                    p2.remove(pModif);
                    p2.revalidate();
                    p2.repaint();
                }
            });
            gbcModif.gridx = 0;
            gbcModif.gridy = 2;
            gbcModif.anchor = GridBagConstraints.CENTER;
            pModif.add(supprimerButton, gbcModif);
    
            JButton okButtonModifier = new JButton("OK");
            okButtonModifier.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String actualRoomName = (String) itemsComboBox.getSelectedItem(); // Récupère le nom actuel de la salle
                    String newRoomName = nomTextField.getText(); // Récupère le nouveau nom de la salle
                    Color newColor = choisirCouleurButton.getBackground(); // Récupère la nouvelle couleur
                    String selectedCategory = (String) cat.getSelectedItem(); // Récupère le type de cours sélectionné

                    // Convertit selectedCategory en minuscules
                    selectedCategory = selectedCategory.toLowerCase();

                    // Chemin du fichier de l'ancienne salle
                    String oldFilePath = "jikan/data/jikan2324/rooms/" + selectedCategory + "/" + actualRoomName + ".txt";
                    File oldFile = new File(oldFilePath);

                    // Chemin du fichier pour la nouvelle salle
                    String newFilePath = "jikan/data/jikan2324/rooms/" + selectedCategory + "/" + newRoomName + ".txt";
                    File newFile = new File(newFilePath);

                    // Renomme le fichier si le nouveau nom est différent de l'ancien nom
                    if (!newRoomName.equals(actualRoomName)) {
                        if (oldFile.exists()) {
                            oldFile.renameTo(newFile);
                        }
                    }

                    // Met à jour le contenu du fichier avec les nouvelles informations
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                        writer.write("Couleur " + newColor.getRed() + " " + newColor.getGreen() + " " + newColor.getBlue());
                        writer.newLine();
                        writer.write("Info \"Salle de cours " + newRoomName + ", rdc\"");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        // Gérer les erreurs d'écriture dans le fichier
                    }

                    p2.remove(pModif);
                    p2.revalidate();
                    p2.repaint();
                }
            });
            gbcModif.gridx = 1;
            gbcModif.gridy = 2;
            gbcModif.anchor = GridBagConstraints.CENTER;
            pModif.add(okButtonModifier, gbcModif);
    
            // Ajouter pModif au panneau central
            p2.add(pModif);
            p2.revalidate();
            p2.repaint();
        }
    });
    bottomPanel.add(okButton);
    
    add(bottomPanel, BorderLayout.SOUTH);
    }
}
