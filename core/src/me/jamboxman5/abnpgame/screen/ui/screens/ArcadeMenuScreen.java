package me.jamboxman5.abnpgame.screen.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ui.elements.button.Button;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;

public class ArcadeMenuScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;

    private long lastButton = System.currentTimeMillis();

    private String title = "ABNP:";
    private String subTitle = "Zombie Assault";

    OrthographicCamera camera;

    public Button[] buttons;
    public Button playButton;
    public Button shopButton;
    public Button equipButton;
    public Button unlocksButton;
    public Button backButton1;
    public Button activeButton;

    private int move = 800;

    private SpriteBatch spriteBatch;
    private ShapeRenderer shapes;

    public ArcadeMenuScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Dark_Brown_Background.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);

        shapes = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        getButtons();
        playButton.reposition(-move, 0);
        shopButton.reposition(-move, 0);
        equipButton.reposition(move, 0);
        unlocksButton.reposition(move, 0);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0, 0f, 1);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        shapes.setProjectionMatrix(camera.combined);
        update();
        draw();
    }

    @Override
    public void resize(int width, int height) {
//        camera.setToOrtho(false,width, height);
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
        if (move > 0) {
            playButton.reposition(80, 0);
            shopButton.reposition(80, 0);
            equipButton.reposition(-80, 0);
            unlocksButton.reposition(-80, 0);
            move-=80;
        }
    }

    public void drawBKG(SpriteBatch batch) {
        batch.begin();
        batch.draw(menuBKG, 0, 0, Settings.screenWidth, Settings.screenHeight);
        batch.end();
    }

    public void drawButtons(SpriteBatch batch, ShapeRenderer renderer) {
        for (Button b : buttons) {
            b.draw(batch, renderer, (activeButton == b));
        }
    }

    public void drawPlayerBar(SpriteBatch batch, ShapeRenderer renderer) {


        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setColor(new Color(75f/255f,0,0,.6f));

        float yDown = Settings.screenHeight - Settings.screenHeight/4.5f;
        float height = Settings.screenHeight/4.5f;

        renderer.rect(0, yDown + move, Settings.screenWidth, height);
        renderer.setColor(Color.RED);
        renderer.rectLine(0, yDown + move, Settings.screenWidth, yDown + move, 2f);
        renderer.end();




        batch.begin();
        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, game.getPlayer().getName(), batch, 20, Settings.screenHeight - 20 + move);
        Fonts.drawScaled(Fonts.INFOFONT, .8f, "Money: $" + game.getPlayer().getMoney(), batch, 20, Settings.screenHeight - 80 + move);

        String exp = "EXP: " + game.getPlayer().getExp();
        int x = (int) Fonts.getXForRightAlignedText((int) (20 + Fonts.getTextWidth(game.getPlayer().getName(), Fonts.BUTTONFONT, 1f)), exp, Fonts.INFOFONT, .8f);

        Fonts.drawScaled(Fonts.INFOFONT, .8f, exp, batch, x, Settings.screenHeight - 80 + move);
//
//        int x = 60;
//        int y = Settings.screenHeight - 220;
//
//        Fonts.drawScaled(Fonts.TITLEFONT, .6f, title, batch, x, y + Fonts.getTextHeight(title, Fonts.TITLEFONT, 1f));
//
//        y -= 90;
//        Fonts.drawScaled(Fonts.SUBTITLEFONT, .841f, subTitle, batch, x, y+ Fonts.getTextHeight(title, Fonts.SUBTITLEFONT, 1f));
        batch.end();
    }

    public void draw() {

        drawBKG(spriteBatch);
        drawPlayerBar(spriteBatch, shapes);
        drawButtons(spriteBatch, shapes);

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

    private void getButtons() {

        int buttonWidth = (int) (Settings.screenWidth/3f);
        int buttonXSpace = (int) (Settings.screenWidth/9f);
        int buttonTopSpace = (int) (Settings.screenHeight/4f) + (int) (Settings.screenHeight/3.75f);
        int buttonMidSpace = (int) (Settings.screenHeight/16f);
        int buttonHeight = (int) (Settings.screenHeight/4f);

        buttons = new Button[5];
        playButton = new Button(buttonXSpace, Settings.screenHeight - buttonTopSpace, Settings.screenWidth/3, buttonHeight, "Play", Fonts.BUTTONFONT);
        shopButton = new Button(buttonXSpace, Settings.screenHeight - buttonTopSpace - buttonHeight - buttonMidSpace, buttonWidth, buttonHeight, "Shop", Fonts.BUTTONFONT);
        equipButton = new Button(Settings.screenWidth - buttonXSpace - buttonWidth, Settings.screenHeight - buttonTopSpace, buttonWidth, buttonHeight, "Equip", Fonts.BUTTONFONT);
        unlocksButton = new Button(Settings.screenWidth - buttonXSpace - buttonWidth, Settings.screenHeight - buttonTopSpace - buttonHeight - buttonMidSpace, buttonWidth, buttonHeight, "Unlocks", Fonts.BUTTONFONT);

        buttons[0] = playButton;
        buttons[1] = shopButton;
        buttons[2] = equipButton;
        buttons[3] = unlocksButton;

        int y = 80;
        int x = 40;


        backButton1 = new Button(x, y - 25, "< Main Menu", Fonts.BUTTONFONT, 1f);
        buttons[4] = backButton1;

        Runnable backAction = new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new MainMenuScreen(game));
                old.dispose();
            }
        };

        equipButton.setAction(new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new EquipMenuScreen(game));
                old.dispose();
            }
        });

        backButton1.setAction(backAction);

        playButton.setAction(new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new ArcadeModeSelectScreen(game));
                old.dispose();
            }
        });

    }
}
