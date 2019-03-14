/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import Classes.Chrono;
import static automatedtask.AutomatedTask.*;

/**
 *
 * @author Ikikay
 */
public class Recolteur extends Thread {

    protected volatile boolean running = true;
    public Rectangle overlayArea;
    public NamedBufferedImage target;
    public NamedBufferedImage action;
    public NamedBufferedImage actionInProgress;

    public Recolteur(int x, int y, int width, int height, NamedBufferedImage theTarget, NamedBufferedImage theAction, NamedBufferedImage theActionInProgress) {
	this.overlayArea = new Rectangle(x, y, width, height);
	this.target = theTarget;
	this.action = theAction;
	this.actionInProgress = theActionInProgress;
    }

    public void close() {
	System.out.println("Demande d'éxtinction");

	running = false;
    }

    public void run() {
	while (running) {
	    try {
		System.out.println("");
		Robot robot = new Robot();

		System.out.println("Screen de la zone");
		// Screen la zone de l'Overlay
		//BufferedImage capturedOverlayArea = robot.createScreenCapture(new Rectangle(this.overlayArea));
		//BufferedImage capturedOverlayArea = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

		Point actionInProgressPosition = searchExactly(actionInProgress);
		if ((actionInProgressPosition.getX() != 0)
			&& (actionInProgressPosition.getY() != 0)) {

		    System.out.println("Action en cours, reboot");
		    this.sleep(500);

		} else {

		    System.out.println("Aucune action en cours, Ok pour recherche de ressources");
		    Point targetPosition = searchDoubtfully(target);

		    if ((targetPosition.getX() == 0)
			    && (targetPosition.getY() == 0)) {

			System.out.println("Aucune ressource trouvé, reboot");
			this.sleep(500);

		    } else {

			System.out.println("Clique droit sur la ressource");
			robot.mouseMove((int) targetPosition.getX(), (int) targetPosition.getY());
			robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);         // Clique droit
			this.sleep(250);
			robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);       // Relache le clique droit
			this.sleep(250);
			System.out.println("Déplacement de la souris pour suppression overlay");
			robot.mouseMove((int) targetPosition.getX() + 150, (int) targetPosition.getY() + 150);
			this.sleep(250);

			Point actionPosition = searchExactly(action);

			if ((actionPosition.getX() == 0)
				&& (actionPosition.getY() == 0)) {

			    System.out.println("Overlay d'action introuvable ... Reboot");
			    this.sleep(500);

			} else {

			    System.out.println("Clique gauche sur l'action demandé");
			    robot.mouseMove((int) actionPosition.getX(), (int) actionPosition.getY());
			    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);     // Clique sur l'action
			    this.sleep(100);                                    // Attends avant de relacher le click
			    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);   // Relache le clique
			} // Action Position
		    } // Ressource Position
		} // Action In progress Position

	    } catch (AWTException ex) {
		Logger.getLogger(Recolteur.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (InterruptedException ex) {
		Logger.getLogger(Recolteur.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public Point searchExactly(NamedBufferedImage cible) {
	try {
	    Robot robot = new Robot();
	    System.out.println("Screen de nouveau la zone, pour rechercher " + cible.getName());
	    BufferedImage capturedOverlayArea = robot.createScreenCapture(new Rectangle(this.overlayArea));
	    System.out.println("Scanning en cours, recherche de : " + cible.getName());

	    // Parcours les colonnes de la zone
	    for (int y = 0; y < capturedOverlayArea.getHeight(); y++) {
		// Parcours les lignes de la zone
		for (int x = 0; x < capturedOverlayArea.getWidth(); x++) {

		    // Matches sur true à la base, pour faire au moins une fois la boucle
		    boolean matches = true;
		    // (SI MATCH) vérifie les autres pixels d'après le modèle :
		    // Parcours les colonnes du modèle
		    for (int y2 = 0; y2 < cible.getImage().getHeight() && matches; y2++) {
			// Parcours les lignes du modèle
			for (int x2 = 0; x2 < cible.getImage().getWidth() && matches; x2++) {

			    // Vérifie le matching
			    if (cible.getImage().getRGB(x2, y2) != capturedOverlayArea.getRGB(x + x2, y + y2)) {
				matches = false;
			    }

			}
		    }

		    if (matches) {
			int realX = x + margeLeft;
			int realY = y + margeTop;

			System.out.println(cible.getName() + " trouvé en position " + realX + ", " + realY);
			return new Point(realX, realY);
		    }

		}

	    }
	    System.out.println(cible.getName() + " non trouvé");
	    return new Point(0, 0);

	} catch (AWTException ex) {
	    Logger.getLogger(Recolteur.class.getName()).log(Level.SEVERE, null, ex);
	    return new Point(0, 0);
	}
    }

    /**
     * Finds the a region in one image that best matches another, smaller,
     * image.
     */
    public Point searchDoubtfully(NamedBufferedImage cible) {
	try {
	    Chrono chrono = new Chrono();
	    chrono.start();

	    Robot robot = new Robot();
	    System.out.println("Screen de nouveau la zone, pour rechercher " + cible.getName());
	    BufferedImage capturedOverlayArea = robot.createScreenCapture(new Rectangle(this.overlayArea));
	    System.out.println("Scanning en cours, recherche de : " + cible.getName());

	    int widthArea = capturedOverlayArea.getWidth();
	    int heightArea = capturedOverlayArea.getHeight();
	    int widthTarget = target.getImage().getWidth();
	    int heightTarget = target.getImage().getHeight();
	    assert (widthTarget <= widthArea && heightTarget <= heightArea);
	    // will keep track of best position found
	    int bestX = 0;
	    int bestY = 0;
	    double lowestDiff = Double.POSITIVE_INFINITY;
	    // brute-force search through whole image (slow...)
	    for (int x = 0; x < widthArea - widthTarget; x++) {
		for (int y = 0; y < heightArea - heightTarget; y++) {
		    double comp = compareImages(capturedOverlayArea.getSubimage(x, y, widthTarget, heightTarget), target.getImage());
		    if (comp < lowestDiff) {
			bestX = x;
			bestY = y;
			lowestDiff = comp;
		    }
		}
	    }
	    // output similarity measure from 0 to 1, with 0 being identical
	    DecimalFormat formatDouble = new DecimalFormat(".##");
	    double pourcentage = 1.0;
	    pourcentage = pourcentage - lowestDiff;
	    pourcentage = pourcentage * 100;
	    //System.out.println(lowestDiff);
	    System.out.println(target.getName() + " trouvé en position " + bestX + ", " + bestY + " avec : " + formatDouble.format(pourcentage) + "%.");
	    // return best location
	    if (pourcentage <= 80.00) {
		bestX = 0;
		bestY = 0;
	    }

	    chrono.stop();
	    chrono.printSec();

	    int realX = bestX + margeLeft;
	    int realY = bestY + margeTop;
	    return new Point(realX, realY);
	} catch (AWTException ex) {
	    Logger.getLogger(Recolteur.class.getName()).log(Level.SEVERE, null, ex);
	    System.out.println("ECHEC");

	    return new Point(0, 0);
	}
    }

    /**
     * Determines how different two identically sized regions are.
     */
    public double compareImages(BufferedImage area, BufferedImage target) {
	assert (target.getHeight() == area.getHeight() && target.getWidth() == area.getWidth());
	double variation = 0.0;
	for (int x = 0; x < target.getWidth(); x++) {
	    for (int y = 0; y < target.getHeight(); y++) {

		double compareARGB = compareARGB(area.getRGB(x, y), target.getRGB(x, y));
		variation += compareARGB / Math.sqrt(3);

	    }
	}
	return variation / (target.getWidth() * target.getHeight());
    }

    /**
     * Calculates the difference between two ARGB colours
     * (BufferedImage.TYPE_INT_ARGB).
     */
    public double compareARGB(int rgbArea, int rgbTarget) {
	double redArea = ((rgbArea >> 16) & 0xFF) / 255.0;
	double redTarget = ((rgbTarget >> 16) & 0xFF) / 255.0;

	double greenArea = ((rgbArea >> 8) & 0xFF) / 255.0;
	double greenTarget = ((rgbTarget >> 8) & 0xFF) / 255.0;

	double blueArea = (rgbArea & 0xFF) / 255.0;
	double blueTarget = (rgbTarget & 0xFF) / 255.0;

	double alphaArea = ((rgbArea >> 24) & 0xFF) / 255.0;
	double alphaTarget = ((rgbTarget >> 24) & 0xFF) / 255.0;
	// if there is transparency, the alpha values will make difference smaller
	return alphaArea * alphaTarget * Math.sqrt(0
		+ (redArea - redTarget) * (redArea - redTarget)
		+ (greenArea - greenTarget) * (greenArea - greenTarget)
		+ (blueArea - blueTarget) * (blueArea - blueTarget));
    }

}
