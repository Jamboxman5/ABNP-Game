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
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.screen.ui.elements.Button;
import me.jamboxman5.abnpgame.script.BasicScript;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;

public class MultiplayerModeSelectScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;
    OrthographicCamera camera;

    private final String title = "Select Online Mode:";
    private final int alignX = Gdx.graphics.getWidth() - 40;
    private final int spacer = 70;
    private long lastButton = System.currentTimeMillis();

    public Button[] buttons;

    public Button host;
    public Button join;

    public Button back;

    public Button activeButton;


    public MultiplayerModeSelectScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Menu_Background_1.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);
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
    }

    public void drawBKG(SpriteBatch batch) {
        batch.begin();
        batch.draw(menuBKG, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    public void drawButtons(SpriteBatch batch, ShapeRenderer renderer) {
        for (Button b : buttons) {
            b.draw(batch, renderer, (activeButton == b));
        }
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
        drawButtons(game.uiCanvas, game.uiShapeRenderer);

    }

    private void getButtons() {
        buttons = new Button[3];
        int x = (int) (alignX - Fonts.getTextWidth("Host Game", Fonts.BUTTONFONT, 1f));
        int y = spacer*(buttons.length-1);
        host = new Button(x, y, "Host Game", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        x = (int) (alignX - Fonts.getTextWidth("Join Game", Fonts.BUTTONFONT, 1f));
        y -= spacer;
        join = new Button(x, y, "Join Game", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);


        x = Gdx.graphics.getWidth() - alignX;
        back = new Button(x, y, "Back", Fonts.BUTTONFONT, Button.TextAlign.LEFT);

        buttons[0] = back;
        buttons[1] = host;
        buttons[2] = join;

                back.setAction(new Runnable() {
                    @Override
                    public void run() {
                        Screen old = game.getScreen();
                        game.setScreen(new ArcadeModeSelectScreen(game));
                        old.dispose();

                    }
                });

                host.setAction(new Runnable() {
                    @Override
                    public void run() {
                        Screen old = game.getScreen();
                        game.setupMultiplayerGame(true);
                        game.setScreen(new MapSelectMenuScreen(game));
                        old.dispose();
                    }
                });

        join.setAction(new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setupMultiplayerGame(false);
                game.setScreen(new MapSelectMenuScreen(game));
                old.dispose();
            }
        });

    }

    private Runnable getMapSelectButtonAction(final Map selected) {
        return new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new GameScreen(game, selected, new BasicScript()));
                old.dispose();
            }
        };
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
