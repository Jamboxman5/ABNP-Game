package me.jamboxman5.abnpgame.screen.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
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

import java.io.IOException;

public class LobbyScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;
    OrthographicCamera camera;

    private final String title = "Lobby";
    private final int alignX = Gdx.graphics.getWidth() - 40;
    private final int spacer = 70;
    private long lastButton = System.currentTimeMillis();

    public Button[] buttons;
    public Button start;
    public Button back;
    public Button activeButton;

    private boolean dispose = false;

    public LobbyScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Menu_Background_1.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);
    }

    public void markForDisposal() {
        dispose = true;
    }

    @Override
    public void show() {
        getButtons();
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
        updateActiveButton(game.getMousePointer());
        if (Gdx.input.isTouched()) {
            if (activeButton != null && (System.currentTimeMillis() - lastButton > 500)) {
                Sounds.MENUSELECT.play(Settings.sfxVolume);
                activeButton.press();
                lastButton = System.currentTimeMillis();
            }
        }
        if (dispose) dispose();
    }

    public void drawBKG(SpriteBatch batch) {
        batch.begin();
        batch.draw(menuBKG, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    public void drawButtons(SpriteBatch batch, ShapeRenderer renderer) {
        for (Button b : buttons) {
            if (b == start && !game.isHosting()) continue;
            b.draw(batch, renderer, (activeButton == b));
        }
    }

    public void drawTitle(SpriteBatch batch) {
        batch.begin();

        int x = 60;
        int y = Gdx.graphics.getHeight() - 220;

        Fonts.drawScaled(Fonts.TITLEFONT, .6f, title, batch, x, y + Fonts.getTextHeight(title, Fonts.TITLEFONT, 1f));

        int spacer = 60;
        y -= 200;

        for (String s : game.getConnectedPlayers()) {

            Fonts.drawScaled(Fonts.SELECTIONFONT, 1f, game.getConnectedPlayerName(s), batch, x, y + Fonts.getTextHeight(title, Fonts.TITLEFONT, 1f));
            y -= spacer;
        }

//
//        y -= 90;
//        Fonts.drawScaled(Fonts.SUBTITLEFONT, .841f, subTitle, batch, x, y+ Fonts.getTextHeight(title, Fonts.SUBTITLEFONT, 1f));
        batch.end();
    }

    public void draw() {

        drawBKG(game.uiCanvas);
        drawTitle(game.uiCanvas);
        drawButtons(game.uiCanvas, game.uiShapeRenderer);

    }

    private void getButtons() {
        buttons = new Button[2];
        int x = (int) (alignX - Fonts.getTextWidth("Start", Fonts.BUTTONFONT, 1f));
        int y = spacer*(buttons.length-1);
        start = new Button(x, y, "Start", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);

        x = Gdx.graphics.getWidth() - alignX;
        back = new Button(x, y, "Back", Fonts.BUTTONFONT, Button.TextAlign.LEFT);

        buttons[0] = back;
        buttons[1] = start;

                back.setAction(new Runnable() {
                    @Override
                    public void run() {
                        Screen old = game.getScreen();
                        game.setScreen(new MainMenuScreen(game));
                        old.dispose();
                        if (game.isMultiplayer()) try {
                            game.closeMultiplayerGame();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        start.setAction(new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new MapSelectMenuScreen(game));
                old.dispose();

            }
        });


    }

    public void updateActiveButton(Vector2 p) {
        for (Button b : buttons) {
            if (b.contains(p))  {
                if (activeButton != b) {
                    activeButton = b;
                    Sounds.MENUSCROLL.play(Settings.sfxVolume);
                }
                return;
            }
        }
        activeButton = null;
    }

}
