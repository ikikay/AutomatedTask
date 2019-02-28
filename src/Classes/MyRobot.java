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
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ikikay
 */
public class MyRobot extends Thread {

    public Rectangle overlayArea;
    public OBufferedImage targetRessource;
    public OBufferedImage action;
    public OBufferedImage actionInProgress;

    public MyRobot(int x, int y, int width, int height, OBufferedImage theTarget, OBufferedImage theAction, OBufferedImage theActionInProgress) {
        this.overlayArea = new Rectangle(x, y, width, height);
        this.targetRessource = theTarget;
        this.action = theAction;
        this.actionInProgress = theActionInProgress;
    }

    public void run() {
        while (true) {
            try {

                Robot robot = new Robot();

                System.out.println("Screen de la zone");
                // Screen la zone de l'Overlay
                OBufferedImage capturedOverlayArea = (OBufferedImage) robot.createScreenCapture(new Rectangle(this.overlayArea));

                Point actionInProgressPosition = search(capturedOverlayArea, actionInProgress);
                if ((actionInProgressPosition.getX() != 0)
                        && (actionInProgressPosition.getY() != 0)) {

                    System.out.println("Action en cours, reboot");
                    this.sleep(500);
                    break;

                } else {

                    System.out.println("Aucune action en cours, Ok pour recherche de ressources");
                    Point ressourcePosition = search(capturedOverlayArea, targetRessource);

                    if ((ressourcePosition.getX() == 0)
                            && (ressourcePosition.getY() == 0)) {

                        System.out.println("Aucune ressource trouvé, reboot");
                        this.sleep(500);
                        break;

                    } else {

                        System.out.println("Clique droit sur la ressource");
                        robot.mouseMove((int) ressourcePosition.getX(), (int) ressourcePosition.getY());
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);         // Clique droit
                        this.sleep(250);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);       // Relache le clique droit
                        System.out.println("Déplacement de la souris pour suppression overlay");
                        robot.mouseMove((int) ressourcePosition.getX() + 150, (int) ressourcePosition.getY() + 150);
                        this.sleep(250);

                        Point actionPosition = search(capturedOverlayArea, action);

                        if ((actionPosition.getX() != 0)
                                && (actionPosition.getY() != 0)) {

                            System.out.println("Overlay d'action introuvable ... Reboot");
                            this.sleep(500);
                            break;

                        } else {

                            System.out.println("Clique gauche sur l'action demandé");
                            robot.mouseMove((int) actionPosition.getX(), (int) actionPosition.getY());
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);     // Clique sur l'action
                            this.sleep(100);                                    // Attends avant de relacher le click
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);   // Relache le clique
                            
                            System.out.println("Patiente 2 secondes pour récolte + déplacement");
                            this.sleep(2000); 

                        } // Action Position
                    } // Ressource Position
                } // Action In progress Position

            } catch (AWTException ex) {
                Logger.getLogger(MyRobot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Point search(OBufferedImage researchZone, OBufferedImage target) {
        System.out.println("Scanning en cours, recherche de : " + target.getName());
        Point position = new Point();

        // Parcours les colonnes de la zone
        for (int y = 0; y < researchZone.getHeight(); y++) {
            // Parcours les lignes de la zone
            for (int x = 0; x < researchZone.getWidth(); x++) {

                // Matches sur true à la base, pour faire au moins une fois la boucle
                boolean matches = true;
                // (SI MATCH) vérifie les autres pixels d'après le modèle :
                // Parcours les colonnes du modèle
                for (int y2 = 0; y2 < target.getHeight() && matches; y2++) {
                    // Parcours les lignes du modèle
                    for (int x2 = 0; x2 < target.getWidth() && matches; x2++) {

                        // Vérifie le matching
                        if (target.getRGB(x2, y2) != researchZone.getRGB(x + x2, y + y2)) {
                            matches = false;
                        }

                    }
                }
                
                if (matches) {
                    System.out.println(target.getName() + " trouvé en position " + x + ", " + y);
                    position.setLocation(x, y);
                    return position;
                }

            }
        }
        System.out.println(target.getName() + " non trouvé");
        return new Point(0, 0);
    }

}
