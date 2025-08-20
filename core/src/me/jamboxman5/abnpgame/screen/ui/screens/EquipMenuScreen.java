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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ui.elements.Button;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;
import me.jamboxman5.abnpgame.weapon.Weapon;

import java.util.Set;

public class EquipMenuScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;

    private long lastButton = System.currentTimeMillis();

    private String title = "ABNP:";
    private String subTitle = "Zombie Assault";

    OrthographicCamera camera;

    public Button[] buttons;

    public Button backButton1;
    public Button activeButton;

    private Weapon selectedWeapon;

    private int move = 800;

    public EquipMenuScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Black_Background.png"));

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
        game.uiShapeRenderer.setProjectionMatrix(camera.combined);
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

    public void draw() {

        drawBKG(game.uiCanvas);
        drawButtons(game.uiCanvas, game.uiShapeRenderer);
        drawSelectedWeapon(game.uiCanvas, game.uiShapeRenderer);
    }

    public void drawSelectedWeapon(SpriteBatch batch, ShapeRenderer renderer) {
        if (selectedWeapon == null) return;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setColor(new Color((75f/255f),0f,0f, .6f));
        renderer.rect(Settings.hudMargin, 140, Settings.screenWidth/2 - (Settings.hudMargin*2), Settings.screenHeight - Settings.hudMargin - 140);
        renderer.end();

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

        int buttonWidth = (int) (300*Settings.guiScale);
        int buttonHeight = (int) (150*Settings.guiScale);
        int x = (int) ((Settings.screenWidth/4f)*3f - (buttonWidth/2f));
        int startY = (int) (Settings.screenHeight - Settings.hudMargin - buttonHeight);

        Array<Weapon> weapons = game.getPlayer().getWeaponLoadout().getWeapons();

        buttons = new Button[weapons.size + 1];

        int i = 0;
        for (final Weapon weapon : weapons) {

            Button button = new Button(x, startY, buttonWidth, buttonHeight, weapon.getHudSprite(), .15f * Settings.guiScale);
            startY -= buttonHeight;
            startY -= Settings.hudMargin;

            button.setAction(new Runnable() {
                @Override
                public void run() {
                    selectedWeapon = weapon;
                }
            });

            buttons[i] = button;
            i++;

        }

        int y = 80;
        x = 40;
        backButton1 = new Button(x, y - 25, "< Main Menu", Fonts.BUTTONFONT, 1f);
        buttons[i] = backButton1;

        Runnable backAction = new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new MainMenuScreen(game));
                old.dispose();
            }
        };

        backButton1.setAction(backAction);

    }
}
