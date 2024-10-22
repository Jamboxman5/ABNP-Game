package me.jamboxman5.abnpgame.screen.ui.screens;

import com.badlogic.gdx.Game;
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
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.screen.ScreenInfo;
import me.jamboxman5.abnpgame.screen.ui.elements.Button;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Sounds;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MapSelectMenuScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;
    OrthographicCamera camera;

    private final String title = "Select a Map:";
    private final int alignX = Gdx.graphics.getWidth() - 40;
    private final int spacer = 70;
    private long lastButton = System.currentTimeMillis();

    public Button[] buttons;
    public Button verdammtenstadt;
    public Button blackIsle;
    public Button karnivale;
    public Button airbase;
    public Button farmHouse;
    public Button back;
    public Button activeButton;


    public MapSelectMenuScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Menu_Background_1.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
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
                Sounds.MENUSELECT.play();
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
        buttons = new Button[6];
        int x = (int) (alignX - Fonts.getTextWidth("Farmhouse", Fonts.BUTTONFONT, 1f));
        int y = spacer*(buttons.length-1);
        farmHouse = new Button(x, y, "Farmhouse", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        x = (int) (alignX - Fonts.getTextWidth("Airbase", Fonts.BUTTONFONT, 1f));
        y -= spacer;
        airbase = new Button(x, y, "Airbase", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        x = (int) (alignX - Fonts.getTextWidth("Karnivale", Fonts.BUTTONFONT, 1f));
        y -= spacer;
        karnivale = new Button(x, y, "Karnivale", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        x = (int) (alignX - Fonts.getTextWidth("Verdammtenstadt", Fonts.BUTTONFONT, 1f));
        y -= spacer;
        verdammtenstadt = new Button(x, y, "Verdammtenstadt", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        x = (int) (alignX - Fonts.getTextWidth("Black Isle", Fonts.BUTTONFONT, 1f));
        y -= spacer;
        blackIsle = new Button(x, y, "Black Isle", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);

        x = Gdx.graphics.getWidth() - alignX;
        back = new Button(x, y, "Back", Fonts.BUTTONFONT, Button.TextAlign.LEFT);

        buttons[0] = back;
        buttons[1] = farmHouse;
        buttons[2] = airbase;
        buttons[3] = karnivale;
        buttons[4] = verdammtenstadt;
        buttons[5] = blackIsle;

                back.setAction(new Runnable() {
                    @Override
                    public void run() {
                        Screen old = game.getScreen();
                        game.setScreen(new MainMenuScreen(game));
                        old.dispose();

                    }
                });

                farmHouse.setAction(getMapSelectButtonAction(new Farmhouse()));
                airbase.setAction(getMapSelectButtonAction(new Airbase()));
                karnivale.setAction(getMapSelectButtonAction(new Karnivale()));
                verdammtenstadt.setAction(getMapSelectButtonAction(new Verdammtenstadt()));
                blackIsle.setAction(getMapSelectButtonAction(new BlackIsle()));

    }

    private Runnable getMapSelectButtonAction(final Map selected) {
        return new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new GameScreen(game, selected));
                old.dispose();
            }
        };
    }

    public void updateActiveButton(Vector2 p) {
        for (Button b : buttons) {
            if (b.contains(p))  {
                if (activeButton != b) {
                    activeButton = b;
                    Sounds.MENUSCROLL.play();
                }
                return;
            }
        }
        activeButton = null;
    }

}
