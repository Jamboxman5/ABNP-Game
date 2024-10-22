package me.jamboxman5.abnpgame.screen.ui.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.managers.MenuManager;
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.screen.ui.elements.Button;
import me.jamboxman5.abnpgame.screen.ScreenInfo;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Sounds;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;
    private final int alignX = Gdx.graphics.getWidth() - 40;
    private final int spacer = 70;
    private long lastButton = System.currentTimeMillis();

    private String title = "ABNP:";
    private String subTitle = "Zombie Assault";

    OrthographicCamera camera;

    public Button[] buttons;
    public Button campaignButton;
    public Button arcadeButton;
    public Button quitButton;
    public Button activeButton;

    public MainMenuScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Menu_Background_0.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
    }

    @Override
    public void show() {
        if (!Sounds.AMBIENCE.isPlaying()) {
            Sounds.AMBIENCE.setLooping(true);
            Sounds.AMBIENCE.play();
        }

        getButtons(game, game.uiShapeRenderer);
        Pixmap pixmap = new Pixmap(Gdx.files.internal("ui/cursor/Cursor_Pointer_Full.png"));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursor);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0, 0f, 1);

        camera.update();
        game.uiCanvas.setProjectionMatrix(camera.combined);

//        Fonts.TITLEFONT.draw(game.batch, "ABNP:", 20, ScreenInfo.HEIGHT - 20);
//        Fonts.drawScaled(Fonts.SUBTITLEFONT, .85f, "Zombie Assault", game.batch,20, ScreenInfo.HEIGHT - Fonts.TITLEFONT.getScaleY() - Fonts.SUBTITLEFONT.getScaleY() - 220);

        update();
        draw();


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

    public void draw() {
        drawBKG(game.uiCanvas);
        drawTitle(game.uiCanvas);
        drawButtons(game.uiShapeRenderer, game.uiCanvas);
    }

    private void drawBKG(SpriteBatch batch) {
        batch.begin();
        batch.draw(menuBKG, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    private void drawTitle(SpriteBatch batch) {

        batch.begin();

        int x = 60;
        int y = Gdx.graphics.getHeight() - 220;


        Fonts.drawScaled(Fonts.TITLEFONT, 1f, title, batch, x, y + Fonts.getTextHeight(title, Fonts.TITLEFONT, 1f));

        y -= 90;

        Fonts.drawScaled(Fonts.SUBTITLEFONT, .841f, subTitle, batch, x, y+ Fonts.getTextHeight(title, Fonts.SUBTITLEFONT, 1f));

        batch.end();

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

    private void drawButtons(ShapeRenderer renderer, SpriteBatch batch) {
        for (Button b : buttons) {
            b.draw(batch, renderer, (activeButton == b));
        }
    }

    private void getButtons(ABNPGame gp, ShapeRenderer renderer) {
        buttons = new Button[3];
        int x = (int) (alignX - Fonts.getTextWidth("Campaign", Fonts.BUTTONFONT, 1f));
        int y = spacer*(buttons.length);
        campaignButton = new Button(x, y, "Campaign", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        x = (int) (alignX - Fonts.getTextWidth("Arcade", Fonts.BUTTONFONT, 1f));
        y -= spacer;
        arcadeButton = new Button(x, y, "Arcade", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        x = (int) (alignX - Fonts.getTextWidth("Quit", Fonts.BUTTONFONT, 1f));
        y -= spacer;
        quitButton = new Button(x, y, "Quit", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);

        buttons[0] = campaignButton;
        buttons[1] = arcadeButton;
        buttons[2] = quitButton;

        campaignButton.setAction(new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new MapSelectMenuScreen(game));
                old.dispose();

            }
        });

        arcadeButton.setAction(new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new ArcadeMenuScreen(game));
                old.dispose();

            }
        });

        quitButton.setAction(new Runnable() {
            @Override
            public void run() {
                System.exit(0);

            }
        });

    }


}
