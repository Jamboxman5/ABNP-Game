package me.jamboxman5.abnpgame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.managers.UIManager;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Sounds;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public class GameScreen implements Screen, InputProcessor {
    final ABNPGame game;

    private final OrthographicCamera camera;
    private Viewport viewport;

    private final Vector3 touchPos = new Vector3();
    ShapeRenderer shape;

    public GameScreen(final ABNPGame game) {
        this.game = game;

        shape = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(ScreenInfo.WIDTH, ScreenInfo.HEIGHT, camera);

        camera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
        Gdx.input.setInputProcessor(this);

        game.getMapManager().setMap("Verdammtenstadt");
        UIManager.setupElements();
    }

    @Override
    public void show() {
        Sounds.AMBIENCE.stop();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to clear are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        if (camera.zoom > .75f) camera.zoom = .75f;
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);
        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        shape.begin(ShapeRenderer.ShapeType.Line);
        draw();
        game.batch.end();
        shape.end();

        update();

        // process user input
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        }
        game.getMapManager().getDebugRenderer().render(game.getMapManager().getWorld(), camera.combined);
        game.getMapManager().getWorld().step(1/60f, 6, 2);
    }

    private void draw() {
        game.getMapManager().draw(game.batch);
        game.getPlayer().draw(game.batch);
        drawUI();
    }

    private void drawUI() {

        UIManager.drawWeaponHud(game.batch, game, camera);

    }

    private void update() {
        game.getPlayer().update();
    }

    @Override
    public void resize(int width, int height) {
        ScreenInfo.WIDTH = width;
        ScreenInfo.HEIGHT = height;
        game.batch.setProjectionMatrix(camera.combined);

//        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }

    public void setZoom(float newZoom) { camera.zoom = newZoom; }
    public float getZoom() { return camera.zoom; }

    public void zoomIn() {
        setZoom(getZoom()+.002f);
    }
    public void zoomOut() {
        setZoom(getZoom()-.005f);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY >= 1) game.getPlayer().getWeaponLoadout().nextWeapon();
        if (amountY <= -1) game.getPlayer().getWeaponLoadout().previousWeapon();
        return false;
    }
}
