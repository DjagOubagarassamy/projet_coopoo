// Importations des packages graphiques
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Classe Labyrinthe
public class VueLabyrinthe {
    // Attributs
    private static final int LIGNES = 10;
    private static final int COLONNES = 10;

    // --- Références du Jeu ---
    private static Personne joueur;
    private static JLabel[][] casesCarte = new JLabel[LIGNES][COLONNES];
    private static JLabel infoLabel;

    /**
     * Initialise et affiche l'interface graphique.
     * @param personnage L'objet Personne à contrôler.
     */
    public static void creerEtAfficherGUI(Personne personnage) {
        joueur = personnage;

        // Configuration de la Fenêtre Principale
        JFrame frame = new JFrame("Labyrinthe du Paysan.java - " + joueur.getNom());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 850);
        frame.setLayout(new BorderLayout());

        // 1. Création du Panel de la Carte (CENTER)
        JPanel cartePanel = new JPanel(new GridLayout(LIGNES, COLONNES));
        cartePanel.setBorder(BorderFactory.createTitledBorder("Carte du Labyrinthe"));
        initialiserCarte(cartePanel);

        // 2. Création du Panel de Contrôle (SOUTH)
        JPanel controlPanel = creerPanelControle();

        // 3. Assemblage Final
        frame.add(cartePanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        // Affichage initial du joueur
        mettreAJourAffichage();
    }

    // --- Méthode d'initialisation de la carte (Grille) ---
    private static void initialiserCarte(JPanel cartePanel) {
        for (int y = 0; y < LIGNES; y++) {
            for (int x = 0; x < COLONNES; x++) {
                JLabel caseLaby = new JLabel("", SwingConstants.CENTER);
                caseLaby.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                caseLaby.setOpaque(true);
                caseLaby.setBackground(Color.LIGHT_GRAY);
                casesCarte[y][x] = caseLaby;
                cartePanel.add(caseLaby);
            }
        }
        // Afficher le Trésor (exemple à (9, 9))
        casesCarte[9][9].setText("TRÉSOR");
        casesCarte[9][9].setBackground(new Color(255, 255, 100)); // Jaune clair
    }

    // --- Méthode de création du panneau de boutons ---
    private static JPanel creerPanelControle() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Aligne au centre

        // Création des composants de contrôle
        infoLabel = new JLabel("Initialisation...");
        JButton hautBtn = new JButton("HAUT (4)");
        JButton basBtn = new JButton("BAS (3)");
        JButton gaucheBtn = new JButton("GAUCHE (1)");
        JButton droiteBtn = new JButton("DROITE (2)");

        // Ajout des Listeners aux Boutons
        gaucheBtn.addActionListener(new DeplacementListener(1));
        droiteBtn.addActionListener(new DeplacementListener(2));
        basBtn.addActionListener(new DeplacementListener(3));
        hautBtn.addActionListener(new DeplacementListener(4)); // Notez l'inversion par rapport au code précédent pour correspondre à votre logique (4:HAUT, 3:BAS)

        // Assemblage
        controlPanel.add(infoLabel);
        controlPanel.add(hautBtn);
        controlPanel.add(basBtn);
        controlPanel.add(gaucheBtn);
        controlPanel.add(droiteBtn);

        return controlPanel;
    }

    // --- Mise à Jour de l'Affichage ---

    /**
     * Nettoie la grille et affiche le joueur à sa nouvelle position.
     */
    private static void mettreAJourAffichage() {
        // Nettoyage de l'ancienne position (sauf le Trésor)
        for (int y = 0; y < LIGNES; y++) {
            for (int x = 0; x < COLONNES; x++) {
                if (!casesCarte[y][x].getText().equals("TRÉSOR")) {
                     casesCarte[y][x].setText("");
                     casesCarte[y][x].setBackground(Color.LIGHT_GRAY);
                }
            }
        }

        // Affichage du joueur à la nouvelle position
        int x = joueur.getPosition().getX();
        int y = joueur.getPosition().getY();

        if (y >= 0 && y < LIGNES && x >= 0 && x < COLONNES) {
            // Afficher le joueur. Attention: Y est la ligne, X est la colonne.
            casesCarte[y][x].setText(joueur.getNom());
            casesCarte[y][x].setBackground(Color.CYAN);
        } else {
             // Gérer la sortie des limites (Mort/Fin de partie)
             JOptionPane.showMessageDialog(null, "Vous êtes sorti du labyrinthe et êtes mort de froid !");
             System.exit(0);
        }

        // Mise à jour de l'étiquette d'information
        infoLabel.setText("Classe: " + joueur.getClass().getSimpleName() +
                          " | Pos: (" + x + ", " + y + ")");
    }

    // --- Classe Interne pour gérer les clics des boutons ---
    private static class DeplacementListener implements ActionListener {
        private int directionChoisie;

        public DeplacementListener(int direction) {
            this.directionChoisie = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 1. Appelle la méthode de la classe Personne
            joueur.seDeplacer(directionChoisie);

            // 2. Met à jour l'interface graphique
            mettreAJourAffichage();
        }
    }

    public static void main(String[] args) {
        Personne paysan = new Paysan("Hero");

        // Lancer l'interface graphique
        SwingUtilities.invokeLater(() -> creerEtAfficherGUI(paysan));
    }
}