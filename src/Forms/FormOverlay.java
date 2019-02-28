/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import Classes.MyRobot;
import Classes.OBufferedImage;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Ikikay
 */
public class FormOverlay extends javax.swing.JFrame {

    int xMouse;
    int yMouse;
    MyRobot theRobot;
    OBufferedImage theTarget;
    OBufferedImage theAction;
    OBufferedImage theActionInProgress;

    public FormOverlay(OBufferedImage target, OBufferedImage action, OBufferedImage actionInProgress) {
        theTarget = target;
        theAction = action;
        theActionInProgress = actionInProgress;

        setTitle("Overlay");							// Applique le titre à la fenêtre
        setBounds(25, 25, 1024, 768);						// Fait une fenêtre de largeur x hauteur avec 25 de marge
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);				// Ferme l'application si il n'y à plus de fenêtre
        setUndecorated(true);							// Supprime la barre et les contours de la fenêtre
        setLocationRelativeTo(null);						// Centre la fenêtre
        JPanel panel = new JPanel();						// Création d'un JPanel

        setLayout(new GridBagLayout());						// Création d'un Layaout de tipe GridBag
        GridBagConstraints gbC = new GridBagConstraints();			// Le gbC va définir la position et la taille des éléments
        gbC.anchor = GridBagConstraints.NORTHWEST;				// Met les items en haut à gauche
        gbC.fill = GridBagConstraints.BOTH;					// Prend toute la place diponible en hauteur et en largeur
        gbC.insets = new Insets(5, 5, 5, 5);					// insets défini la marge entre les composant new Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite) */

        gbC.gridx = 0;
        gbC.gridy = 0;
        JButton bHide = new JButton("Hide");					// Créer un bouton "Hide"
        bHide.setSize(100, 50);							// de taille 100x50
        panel.add(bHide, gbC);							// ajoute ce bouton, au panel
        bHide.addActionListener((event) -> {					// Créer une " "micro fonction" " lorsque quelque chose se passe sur le bouton
            //Actions lors des cliques sur le bouton 
            setBackground(new Color(0, 0, 0, 0));				// Rend la fenêtre transparente
        });
        gbC.gridx = 1;
        gbC.gridy = 0;
        JButton bShow = new JButton("Show");					// Créer un bouton "Show"
        bShow.setSize(100, 50);							// de taille 100x50
        panel.add(bShow, gbC);							// ajoute ce bouton, au panel
        bShow.addActionListener((event) -> {					// Créer une " "micro fonction" " lorsque quelque chose se passe sur le bouton
            //Actions lors des cliques sur le bouton 
            setBackground(new Color(0, 0, 0, 255));				// Enlève la transparence de la fenêtre
        });
        gbC.gridx = 2;
        gbC.gridy = 0;
        JButton bAnnuler = new JButton("Annuler");				// Créer un bouton "Annuler"
        bAnnuler.setSize(100, 50);						// de taille 100x50
        panel.add(bAnnuler, gbC);						// ajoute ce bouton, au panel
        bAnnuler.addActionListener((event) -> {					// Créer une " "micro fonction" " lorsque quelque chose se passe sur le bouton
            //Actions lors des cliques sur le bouton 
            theRobot.interrupt();
        });
        gbC.gridx = 3;
        gbC.gridy = 0;
        JButton bValider = new JButton("Valider");				// Créer un bouton "Valider"
        bValider.setSize(100, 50);						// de taille 100x50
        panel.add(bValider, gbC);						// ajoute ce bouton, au panel
        bValider.addActionListener((event) -> {					// Créer une " "micro fonction" " lorsque quelque chose se passe sur le bouton
            //Actions lors des cliques sur le bouton 
            theRobot = new MyRobot(this.getX(), this.getY(), this.getWidth(), this.getHeight(), theTarget, theAction, theActionInProgress);
            theRobot.start();
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // Enregistre la position du clique, afin d'éviter une futur téléportation de la fenêtre
                xMouse = arg0.getX();
                yMouse = arg0.getY();
            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent arg0) {

            }

            @Override
            public void mouseDragged(MouseEvent arg0) {
                int x = arg0.getXOnScreen();
                int y = arg0.getYOnScreen();

                System.out.println("Pointeur : " + x + ", " + y);
                // Déplace la fenêtre à la position de la souris
                // - xMouse et yMouse afin d'éviter la "téléportation" de la fenêtre
                setLocation(x - xMouse, y - yMouse);
            }
        });

        setContentPane(panel);							// Ajoute le JPanel (panel) au JFrame
        setBackground(new Color(0, 0, 0, 255));					// Rend la fenêtre transparente
        setVisible(true);							// Rend la fenêtre visible
    }
}
