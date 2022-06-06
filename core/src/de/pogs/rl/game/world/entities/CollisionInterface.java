/**
 * 
 * MIT LICENSE
 * 
 * Copyright 2022 Philip Gilde & Oskar Stanschus
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author Philip Gilde & Oskar Stanschus
 * 
 */
package de.pogs.rl.game.world.entities;

import de.pogs.rl.utils.SpecialMath.Vector2;

/**
 * Interface für elastische Kollisionen mit Asteroiden
 */
public interface CollisionInterface {
    /**
     * Gibt Geschwindigkeit zurück.
     * @return Die aktuelle Geschwindigkeit.
     */
    public Vector2 getVelocity();
    /**
     * Setzt Geschwindigkeit.
     * @param velocity Die neue Geschwindigkeit.
     */
    public void setVelocity(Vector2 velocity);
    /**
     * Gibt Masse zurück.
     * @return Masse.
     */
    public float getMass();
    /**
     * Gibt Position zurück.
     * @return Position.
     */
    public Vector2 getPosition();
}
