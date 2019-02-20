/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedtask;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ikikay
 */
public class MyRobot extends Thread {

    public Rectangle overlayArea;
    public BufferedImage target;
    public BufferedImage action;
    public BufferedImage actionInProgress;

    public MyRobot(int x, int y, int width, int height, BufferedImage theTarget, BufferedImage theAction, BufferedImage theActionInProgress) {
	this.overlayArea = new Rectangle(x, y, width, height);
	this.target = theTarget;
	this.action = theAction;
	this.actionInProgress = theActionInProgress;
    }

    public void run() {
	try {
	    Robot robot = new Robot();

	    while (true) {
		System.out.println("Scanning en cours");

		// Screen la zone de l'Overlay
		BufferedImage capturedOverlayArea = robot.createScreenCapture(new Rectangle(this.overlayArea));

		// Parcours les lignes de la zone
		for (int x = 0; x < overlayArea.getWidth(); x++) {
		    // Parcours les colonnes de la zone
		    for (int y = 0; y < overlayArea.getHeight(); y++) {

			// Matches sur true à la base, pour faire au moins une fois la boucle
			boolean matches = true;
			// (SI MATCH) vérifie les autres pixels d'après le modèle :
			// Parcours les lignes du modèle
			for (int x2 = 0; x2 < target.getWidth() && matches; x2++) {
			    //for (int x2 = 0; x2 < action.getWidth() && matches; x2++) {
			    // Parcours les colonnes du modèle
			    for (int y2 = 0; y2 < target.getHeight() && matches; y2++) {
				//for (int y2 = 0; y2 < action.getHeight() && matches; y2++) {

				// Vérifie le matching
				if (target.getRGB(x2, y2) != capturedOverlayArea.getRGB(x + x2, y + y2)) {
				    //if (action.getRGB(x2, y2) != capturedOverlayArea.getRGB(x + x2, y + y2)) {
				    matches = false;
				}

			    }
			}
			if (matches) {
			    // Si on passe ici, c'est qu'il y à MATCH complet ! 
			    // TODO ACTION			    
			    // Draw rectangle ici

			    System.out.println("Trouvé en position " + x + ", " + y);
			    if (!isActioneInProgress()) {
				robot.mouseMove(x, y);				// Déplace la fenêtre sur la cible
				robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);	// Clique droit
				robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);// Relache le clique droit
				robot.mouseMove(x - 250, y - 250);		// Déplace la souris pour supprimer l'overlay de la cible
				this.sleep(2000);				// Attends 2 seconde //TODO adapter ici
				clickOnAction();				// Clique sur l'action
			    }
			}

		    }
		}

	    }
	} catch (AWTException ex) {
	    Logger.getLogger(MyRobot.class.getName()).log(Level.SEVERE, null, ex);
	} catch (InterruptedException ex) {
	    Logger.getLogger(MyRobot.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public boolean isActioneInProgress() throws AWTException {
	System.out.println("Recherche d'action en cours");
	Robot robot = new Robot();

	// Screen la zone de l'Overlay
	BufferedImage capturedOverlayArea = robot.createScreenCapture(new Rectangle(this.overlayArea));

	// Parcours les lignes de la zone
	for (int x = 0; x < overlayArea.getWidth(); x++) {
	    // Parcours les colonnes de la zone
	    for (int y = 0; y < overlayArea.getHeight(); y++) {

		// Matches sur true à la base, pour faire au moins une fois la boucle
		boolean matches = true;
		// (SI MATCH) vérifie les autres pixels d'après le modèle :
		// Parcours les lignes du modèle
		for (int x2 = 0; x2 < actionInProgress.getWidth() && matches; x2++) {
		    // Parcours les colonnes du modèle
		    for (int y2 = 0; y2 < actionInProgress.getHeight() && matches; y2++) {

			// Vérifie le matching
			if (actionInProgress.getRGB(x2, y2) != capturedOverlayArea.getRGB(x + x2, y + y2)) {
			    matches = false;
			}

		    }
		}
		if (matches) {
		    System.out.println("Action en cours !");
		    return true;
		}

	    }
	}
	System.out.println("Aucune Action en cours");
	return false;
    }

    public void clickOnAction() throws AWTException {
	System.out.println("Recherche du bouton d'action");
	Robot robot = new Robot();

	// Screen la zone de l'Overlay
	BufferedImage capturedOverlayArea = robot.createScreenCapture(new Rectangle(this.overlayArea));

	// Parcours les lignes de la zone
	for (int x = 0; x < overlayArea.getWidth(); x++) {
	    // Parcours les colonnes de la zone
	    for (int y = 0; y < overlayArea.getHeight(); y++) {

		// Matches sur true à la base, pour faire au moins une fois la boucle
		boolean matches = true;
		// (SI MATCH) vérifie les autres pixels d'après le modèle :
		// Parcours les lignes du modèle
		for (int x2 = 0; x2 < action.getWidth() && matches; x2++) {
		    // Parcours les colonnes du modèle
		    for (int y2 = 0; y2 < action.getHeight() && matches; y2++) {

			// Vérifie le matching
			if (action.getRGB(x2, y2) != capturedOverlayArea.getRGB(x + x2, y + y2)) {
			    matches = false;
			}

		    }
		}
		if (matches) {
		    System.out.println("Aucune action en cours, click sur l'action demandé");
		    robot.mouseMove(x, y);					// Déplace la souris sur l'action
		    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);		// Clique sur l'action
		    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);		// Relache le clique
		}

	    }
	}

    }

}
