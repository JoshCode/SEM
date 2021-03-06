package nl.joshuaslik.tudelft.SEM.control.gameObjects.pickup.powerup.player.playerMods;

import static org.junit.Assert.*;
import nl.joshuaslik.tudelft.SEM.control.gameObjects.pickup.powerup.player.IPlayerModifier;
import nl.joshuaslik.tudelft.SEM.control.gameObjects.pickup.powerup.player.PlayerBaseModifier;

import org.junit.Test;

/**
 * Test the player speed up class.
 * @author Faris
 */
public class PlayerSpeedUpTest {

    private final IPlayerModifier ipm = new PlayerBaseModifier();
    IPlayerModifier speed = new PlayerSpeedUp().decorate(ipm);

    /**
     * Test the getMoveSpeedMultiplier method.
     */
    @Test
    public void testGetMoveSpeedMultiplier() {
        assertEquals(1.3, speed.getMoveSpeedMultiplier(), 0.001);
    }

    /**
     * Test the getProjectileSpeedMultiplier method.
     */
    @Test
    public void testGetProjectileSpeedMultiplier() {
        assertEquals(1, speed.getProjectileSpeedMultiplier(), 0.001);
    }

    /**
     * Test the getProjectileSpikeDelay method.
     */
    @Test
    public void testGetProjectileSpikeDelay() {
        assertEquals(0, speed.getProjectileSpikeDelay());
    }
}
