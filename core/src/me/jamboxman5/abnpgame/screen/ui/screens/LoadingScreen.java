package me.jamboxman5.abnpgame.screen.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.abnpgame.entity.mob.zombie.Zombie;
import me.jamboxman5.abnpgame.entity.prop.pickup.Pickup;
import me.jamboxman5.abnpgame.entity.prop.pickup.PickupWeapon;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.map.Map;
import me.jamboxman5.abnpgame.map.maps.*;
import me.jamboxman5.abnpgame.net.packets.PacketMap;
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.screen.ui.elements.Button;
import me.jamboxman5.abnpgame.script.BasicScript;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;
import me.jamboxman5.abnpgame.weapon.firearms.pistol.Pistol1911;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;
import me.jamboxman5.abnpgame.weapon.firearms.shotgun.ShotgunWinchester12;

public class LoadingScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;
    OrthographicCamera camera;

    private final String title = "Loading...";
    private final int alignX = Gdx.graphics.getWidth() - 40;
    private final int spacer = 70;
    private long lastButton = System.currentTimeMillis();

    private float progress;
    private long loadedTime = 0;
    private boolean spritesLoaded = false;

    public LoadingScreen(final ABNPGame game) {
        this.game = game;
        progress = 0;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Menu_Background_1.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0, 0f, 1);

        camera.update();
        game.uiCanvas.setProjectionMatrix(camera.combined);


        update();
        draw();
    }

    @Override
    public void resize(int width, int height) {

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
        menuBKG.dispose();
    }



    public void update() {
        progress = game.getAssetManager().getProgress();

        if (game.getAssetManager().update() && loadedTime == 0) loadedTime = System.currentTimeMillis();
        if (loadedTime > 0 && !spritesLoaded) {
            loadSprites();
            game.generatePlayer();
            spritesLoaded = true;
        }
        if (loadedTime != 0 && (System.currentTimeMillis() - loadedTime) > 200) {
            game.setScreen(new MainMenuScreen(game));
            game.disposeScreen(this);
        }

    }

    public void drawBKG(SpriteBatch batch) {
        batch.begin();
        batch.draw(menuBKG, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }


    public void drawTitle(SpriteBatch batch) {
        batch.begin();

        int x = 60;
        int y = Gdx.graphics.getHeight() - 220;

        Fonts.drawScaled(Fonts.TITLEFONT, .6f, title, batch, x, y + Fonts.getTextHeight(title, Fonts.TITLEFONT, 1f));
//
//        y -= 90;
//        Fonts.drawScaled(Fonts.SUBTITLEFONT, .841f, subTitle, batch, x, y+ Fonts.getTextHeight(title, Fonts.SUBTITLEFONT, 1f));
        batch.end();
    }

    public void draw() {

        drawBKG(game.uiCanvas);
        drawTitle(game.uiCanvas);
        drawProgress(game.shapeRenderer);



    }

    private void loadSprites() {
        Zombie.loadSprites(game.getAssetManager());
        RifleM4A1.loadSprites(game.getAssetManager());
        Pistol1911.loadSprites(game.getAssetManager());
        ShotgunWinchester12.loadSprites(game.getAssetManager());
        PickupWeapon.loadSprites(game.getAssetManager());
        Pickup.loadSprites(game.getAssetManager());
        Sounds.loadSounds(game.getAssetManager());
        Map.loadMaps(game.getAssetManager());
        Sounds.updateVolumes();

    }

    public void drawProgress(ShapeRenderer renderer) {

        int width = 900;
        int height = 100;
        int margin = 100;
        int weight = 4;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setColor((float)(100.0/255.0), 0f, 0f, .6f);
        renderer.rect((Gdx.graphics.getWidth()/2f) - (width/2f), margin, width, height);
        renderer.setColor(Color.RED);
        renderer.rect((Gdx.graphics.getWidth()/2f) - (width/2f), margin, width * progress, height);



        renderer.setColor(Color.WHITE);
        renderer.setAutoShapeType(true);
        Gdx.gl.glLineWidth(weight);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.rect((Gdx.graphics.getWidth()/2f) - (width/2f), margin, width, height);

        renderer.end();
    }

    private Runnable getMapSelectButtonAction(final Map selected) {
        return new Runnable() {
            @Override
            public void run() {
                if (game.isMultiplayer()) {
                    PacketMap packet = new PacketMap();
                    packet.type = selected.getMapType();
                    game.getClientManager().sendPacketTCP(packet);
                } else {
                    Screen old = game.getScreen();
                    game.setScreen(new GameScreen(game, selected, new BasicScript()));
                    old.dispose();
                }
            }
        };
    }

}
