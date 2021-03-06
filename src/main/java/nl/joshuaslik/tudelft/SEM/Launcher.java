/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.joshuaslik.tudelft.SEM;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import nl.joshuaslik.tudelft.SEM.control.viewController.GameplayChoicesViewController;
import nl.joshuaslik.tudelft.SEM.control.viewController.IpopupController;
import nl.joshuaslik.tudelft.SEM.control.viewController.IviewController;
import nl.joshuaslik.tudelft.SEM.sound.MusicLoop;
import nl.joshuaslik.tudelft.SEM.utility.GameLog;

/**
 * The launcher of the application.
 *
 * @author faris
 */
public class Launcher extends Application {

    public static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    public static final double GRAVITY = 700;
    public static final double ENERGY = GRAVITY * SCREEN_HEIGHT; // E = .5v2 + gh
    private static final BorderPane BP = new BorderPane();
    private static Stage stage;

    // These controllers are only intended to be used for testing purposes:
    private static IviewController controller;
    private static IpopupController popupController;
    private static final Object LOCK = new Object();
    private static boolean initialized = false;
    private static boolean hideViewForTesting = false;

    /**
     * Start up the game.
     */
    @Override
    public void start(final Stage primaryStage) {
        GameLog.constructor();
        IviewController mainV = loadView(getClass().getResource("/data/gui/pages/MainMenu.fxml"));
        Scene scene = new Scene(BP);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        scaleToScreenSize(BP);
        if (!hideViewForTesting) {
            MusicLoop.getInstance().start();
            primaryStage.show();
        }
        Launcher.stage = primaryStage;
        synchronized (LOCK) {
            initialized = true;
        }
        if (!hideViewForTesting) {
            GameplayChoicesViewController.loadPopup(mainV);
        }
    }

    /**
     * Load the fxml file for the screen.
     *
     * @param fxmlURL URL of the FXML file.
     * @return the controller of the loaded view.
     */
    public static IviewController loadView(final URL fxmlURL) {
        GameLog.addInfoLog("Load view: " + fxmlURL.getPath());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlURL);
        try {
            Pane pane = loader.load();
            IviewController res = ((IviewController) loader.getController());
            res.start(BP.getScene());
            BP.setCenter(pane);
            controller = res;
            return res;
        }
        catch (IOException ex) {
            GameLog.addErrorLog("Failed to load fxml file: " + fxmlURL.toString());
            GameLog.addErrorLog(ex.getMessage());
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, 
                    "Failed to load fxml file: " + fxmlURL.toString(), ex);
            return null;
        }
    }

    /**
     * Load a popup screen.
     *
     * @param mainViewController the controller of the current view.
     * @param fxmlURL the URL of the FXML file of the popup.
     */
    public static void loadPopup(final IviewController mainViewController, final URL fxmlURL) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlURL);
        mainViewController.setButtonsDisabled(true);
        try {
            Pane pane = loader.load();
            PopupControl popup = new PopupControl();
            popup.getScene().setRoot(pane);
            popup.show(stage);
            IpopupController popupContrl = (IpopupController) loader.getController();
            popupContrl.setPopupControl(popup);
            popupContrl.setMainViewController(mainViewController);
            Launcher.popupController = popupContrl;
        }
        catch (IOException ex) {
            GameLog.addErrorLog("Failed to load fxml file: " + fxmlURL.toString());
            GameLog.addErrorLog(ex.getMessage());
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE,
                    "Failed to load fxml file: " + fxmlURL.toString(), ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * Get the controller of the last loaded view. FOR TESTING PURPSOSES ONLY!
     *
     * @return the view controller
     */
    public static IviewController getController() {
        return controller;
    }

    /**
     * Get the controller of the last loaded popup. FOR TESTING PURPSOSES ONLY!
     *
     * @return the popup controller
     */
    public static IpopupController getPopupController() {
        return popupController;
    }

    /**
     * Wait till the initial view is initialized. FOR TESTING PURPSOSES ONLY!
     */
    public static void waitTillInitialized() {
        while (true) {
            synchronized (LOCK) {
                if (initialized) {
                    break;
                }
            }
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Hide the view for integration testing. FOR TESTING PURPSOSES ONLY!
     *
     * @param hideViewForTesting if the view must be hidden
     */
    public static void setHideViewForTesting(boolean hideViewForTesting) {
        Launcher.hideViewForTesting = hideViewForTesting;
    }

    /**
     * Scale the size of the given stage.
     *
     * @param contentPane the stage to resize
     */
    private void scaleToScreenSize(final Pane contentPane) {
        Scene scene = contentPane.getScene();
        final double initWidth = 1920;
        final double initHeight = 1080;
        final double ratio = initWidth / initHeight;

        SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio,
                initHeight, initWidth, contentPane);
        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);
    }

    /**
     * When the user resizes the screen the method changed() in this class will be called.
     */
    private static class SceneSizeChangeListener implements ChangeListener<Number> {

        private final Scene scene;
        private final double ratio;
        private final double initHeight;
        private final double initWidth;
        private final Pane contentPane;

        /**
         * Create a scene size listener.
         *
         * @param scene the scene.
         * @param ratio the initial ratio.
         * @param initHeight the initial height.
         * @param initWidth the initial width
         * @param contentPane the pane to listen to.
         */
        public SceneSizeChangeListener(final Scene scene, final double ratio,
                final double initHeight, final double initWidth, final Pane contentPane) {
            this.scene = scene;
            this.ratio = ratio;
            this.initHeight = initHeight;
            this.initWidth = initWidth;
            this.contentPane = contentPane;
        }

        /**
         * Listen for size changes and change the scene accordingly.
         *
         * @param observableValue the value being observed
         * @param oldValue old size
         * @param newValue new size
         */
        @Override
        public void changed(final ObservableValue<? extends Number> observableValue,
                final Number oldValue, final Number newValue) {
            final double newWidth = scene.getWidth();
            final double newHeight = scene.getHeight();
            double scaleFactor = newWidth / newHeight > ratio
                    ? newHeight / initHeight
                    : newWidth / initWidth;
            Scale scale = new Scale(scaleFactor, scaleFactor);
            scale.setPivotX(0);
            scale.setPivotY(0);
            scene.getRoot().getTransforms().setAll(scale);
            contentPane.setPrefWidth(newWidth / scaleFactor);
            contentPane.setPrefHeight(newHeight / scaleFactor);
            BP.autosize();
        }
    }
}
