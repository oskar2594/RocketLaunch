package de.pogs.rl.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.pogs.rl.utils.CameraShake;

/**
 * RocketCamera
 */
public class RocketCamera extends OrthographicCamera {


    public RocketCamera() {
        super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.zoom = 1f;
    }


    /**
     * Kameragröße bei Veränderung der Bildschrimgröße anpassen
     */
    public void resize(int width, int height) {
        this.viewportHeight = height;
        this.viewportWidth = width;

        this.zoom = 1f;
    }

    private float beforeZoom = 0;

    public void refresh(float delta) {
        // Position der Kamera auf den Spieler setzen
        this.position.set(GameScreen.INSTANCE.player.getPosition().getX(),
                GameScreen.INSTANCE.player.getPosition().getY(), 0);

        // Kamera Wackeln ausführen
        Vector2 shake = CameraShake.getShake();
        this.translate(shake);


        // Zoom Faktor ermitteln & setzen
        float playerSpeed = GameScreen.INSTANCE.player.getSpeed();
        float maxSpeed = GameScreen.INSTANCE.player.getMaxSpeed();
        float zoom = (float) easeInOut((playerSpeed / maxSpeed)) * 0.18f + 0.9f;
        this.zoom = Math.min(zoom, 1.1f);


        // Kamera Wackeln setzen
        if (zoom > .9f && GameScreen.INSTANCE.player.isAccelerating()) {
            CameraShake.activate((zoom - .95f) * 20);
        } else {
            CameraShake.deactivate();
        }

        // Trigger CameraZoomm Event
        if (beforeZoom != this.zoom) {
            GameScreen.INSTANCE.resizeZoom(
                    (int) (Gdx.graphics.getWidth() * GameScreen.INSTANCE.camera.zoom),
                    (int) (Gdx.graphics.getHeight() * GameScreen.INSTANCE.camera.zoom));
            beforeZoom = this.zoom;
        }
        this.update();
    }

    private double easeInOut(double number) {
        if (number < 0.5) {
            return 4 * Math.pow(number, 3);
        } else {
            return (number - 1) * (2 * number - 2) * (2 * number - 2) + 1;
        }

    }

}
