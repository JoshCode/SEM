/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.joshuaslik.tudelft.SEM.control.viewController;

import static org.junit.Assert.assertTrue;

import nl.joshuaslik.tudelft.SEM.JavaFxJUnit4ClassRunner;
import nl.joshuaslik.tudelft.SEM.Launcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;

/**
 * Test the game controller class with an integration test.
 * @author Faris
 */
@RunWith(JavaFxJUnit4ClassRunner.class)
public class GameControllerITest {

    /**
     * Make sure system.exit() won't actually stop the application.
     */
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    /**
     * Controller of the main menu.
     */
    private GameViewController controller;

    /**
     * Make sure the main menu is always loaded first and set the right controller.
     */
    @Before
    public final void startGame() {
        MainMenuViewController.loadView();
        ((MainMenuViewController) Launcher.getController()).firePlayButton();
        controller = (GameViewController) Launcher.getController();
        assert controller != null;
    }

    /**
     * Test of handleQuitButton method, of class MainMenuViewController. System.exit(0) will be called,
     * because we are using the "ExpectedSystemExit" rule, we expect a runtime exception.
     *
     * @throws java.lang.InterruptedException ignore.
     */
    @Test(expected = RuntimeException.class)
    public void testHandleQuitButton() throws InterruptedException {
        exit.expectSystemExitWithStatus(0);
        controller.fireQuitButton();
    }

    /**
     * Test of handleMainMenuButton method, of class GameViewController.
     */
    @Test
    public void testHandleMainMenuButton() {
        controller.fireMainMenuButton();
        assertTrue(Launcher.getController() instanceof MainMenuViewController);
    }
}
