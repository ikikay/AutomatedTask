/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedtask.Form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Ikikay
 */
public class FormFenetre {

    public FormFenetre() {
	JFrame fenetre = new JFrame();						// Création d'un JFrame
	fenetre.setTitle("Menu");						// Applique le titre à la fenêtre
	fenetre.setBounds(25, 25, 100, 100);					// Fait une fenêtre de largeur x hauteur avec 25 de marge
	fenetre.setDefaultCloseOperation(DISPOSE_ON_CLOSE);			// Ferme l'application si il n'y à plus de fenêtre
	fenetre.setLocationRelativeTo(null);					// Centre la fenêtre
	JPanel panel = new JPanel();						// Création d'un JPanel
	panel.setLayout(new GridBagLayout());					// Création d'un Layaout de tipe GridBag
	GridBagConstraints gbC = new GridBagConstraints();			// Le gbC va définir la position et la taille des éléments
	gbC.fill = GridBagConstraints.BOTH;					// Prend toute la place diponible en hauteur et en largeur
	gbC.insets = new Insets(5, 5, 5, 5);					// insets défini la marge entre les composant new Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite) */

	//Panel Boutons Articles
	JPanel panelOverlay = new JPanel();					// Création d'un JPanel
	panelOverlay.setLayout(new GridBagLayout());				// Création d'un Layaout de tipe GridBag
	GridBagConstraints gbCA = new GridBagConstraints();			// Le gbC va définir la position et la taille des éléments
	gbCA.fill = GridBagConstraints.BOTH;					// Prend toute la place diponible en hauteur et en largeur
	gbCA.insets = new Insets(5, 5, 5, 5);

	gbC.gridx = 1;
	gbC.gridy = 0;
	panel.add(panelOverlay, gbC);						// Ajoute un panel "panelOverlay" à "panel" en position x = 1, Y = 0

	//Création des boutons
	gbCA.gridx = 0;
	gbCA.gridy = 0;
	JButton bAloeVeroRecolter = new JButton("Aloe Vero : Récolter");	// Créer un bouton de récolte d'Aloe Vero
	bAloeVeroRecolter.setSize(100, 50);					// de taille 100x50
	panelOverlay.add(bAloeVeroRecolter, gbCA);				// ajoute ce bouton, au panel
	//Action Boutons
	bAloeVeroRecolter.addActionListener((event) -> {			// Créer une "micro fonction" " lorsque quelque chose se passe sur le bouton
	    //Actions lors des cliques sur le bouton
	    BufferedImage target, action, actionInProgress;
	    try {
		target = ImageIO.read(new File("aloeVero.bmp"));
		action = ImageIO.read(new File("recolter.bmp"));
		actionInProgress = ImageIO.read(new File("recolteInProgress.bmp"));
		FormOverlay formOverlay = new FormOverlay(target, action, actionInProgress);// Ouvre une fenetre formOverlay
	    } catch (IOException ex) {
		Logger.getLogger(FormFenetre.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    //fenetre.dispose();						// Ferme la fenetre
	});
	gbCA.gridx = 1;
	JButton bAloeVeroCouper = new JButton("Aloe Vero : Couper");		// Créer un bouton couper Aloe Vero
	bAloeVeroCouper.setSize(100, 50);					// de taille 100x50
	panelOverlay.add(bAloeVeroCouper, gbCA);				// ajoute ce bouton, au panel
	//Action Boutons
	bAloeVeroCouper.addActionListener((event) -> {				// Créer une "micro fonction" " lorsque quelque chose se passe sur le bouton
	    //Actions lors des cliques sur le bouton
	    BufferedImage target, action, actionInProgress;
	    try {
		target = ImageIO.read(new File("aloeVero.bmp"));
		action = ImageIO.read(new File("couper.bmp"));
		actionInProgress = ImageIO.read(new File("recolteInProgress.bmp"));
		FormOverlay formOverlay = new FormOverlay(target, action, actionInProgress);// Ouvre une fenetre formOverlay
	    } catch (IOException ex) {
		Logger.getLogger(FormFenetre.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    //fenetre.dispose();						// Ferme la fenetre
	});
	gbCA.gridx = 2;
	JButton bTest = new JButton("TEST");					// Créer un bouton de TEST
	bTest.setSize(100, 50);							// de taille 100x50
	panelOverlay.add(bTest, gbCA);						// ajoute ce bouton, au panel
	//Action Boutons
	bTest.addActionListener((event) -> {					// Créer une "micro fonction" " lorsque quelque chose se passe sur le bouton
	    //Actions lors des cliques sur le bouton
	    BufferedImage target, action, actionInProgress;
	    try {
		target = ImageIO.read(new File("test.bmp"));
		action = ImageIO.read(new File("couper.bmp"));
		actionInProgress = ImageIO.read(new File("recolteInProgress.bmp"));
		FormOverlay formOverlay = new FormOverlay(target, action, actionInProgress);// Ouvre une fenetre formOverlay
	    } catch (IOException ex) {
		Logger.getLogger(FormFenetre.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    //fenetre.dispose();						// Ferme la fenetre
	});

	fenetre.add(panel);							// Ajoute le JPanel (panel) au JFrame (fenetre)
	fenetre.pack();								// Pack les éléments
	fenetre.setVisible(true);						// Rend la fenêtre visible
    }
}
