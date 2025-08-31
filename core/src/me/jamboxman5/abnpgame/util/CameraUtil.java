package me.jamboxman5.abnpgame.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraUtil {

    public static Vector2 getWorldPosScreenCenterDisplacement(PerspectiveCamera camera, float canvasHeight, float screenX, float screenY) {

        float worldPerPixel = (2f * (camera.position.y - canvasHeight) * (float)Math.tan(Math.toRadians(camera.fieldOfView) / 2f))
                / Gdx.graphics.getHeight();

        float dxPixels = screenX - Gdx.graphics.getWidth() / 2f;
        float dzPixels = screenY - Gdx.graphics.getHeight() / 2f;

// Screen Y is inverted, so flip sign if needed
        float dxWorld = dxPixels * worldPerPixel;
        float dzWorld = dzPixels * worldPerPixel;

        return new Vector2(dxWorld, dzWorld);

    }

}
