/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.joshuaslik.tudelft.SEM.control.gameObjects;

/**
 * Interface to be implemented by all objects which should be updated by the GameLoop.
 *
 * @author faris
 */
public interface IUpdateable {

    /**
     * Update the object.
     *
     * @param nanoFrameTime nanoFrameTime the framerate (nanoseconds/frame)
     */
    void update(final long nanoFrameTime);
}
