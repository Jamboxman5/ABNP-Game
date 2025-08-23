package me.jamboxman5.abnpgame.screen.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;

public class EquipMenuScreen implements Screen, InputProcessor {

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
    private float scrolled = 0;
    private float scrollDiff = 0;
    private float maxScroll = 0;
    private float scrollMargin = 0;

    public EquipMenuScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Black_Background.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);

    }

    @Override
    public void show() {
        getButtons();
        Gdx.input.setInputProcessor(this);

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
        scrollDiff += scrolled;

        if (scrollDiff < 0) {
            scrolled = scrolled - scrollDiff;
            scrollDiff = 0;
        }

        float lowerBound = Math.max(0, maxScroll - (Settings.screenHeight) + scrollMargin*2);
        if (scrollDiff > lowerBound) {
            scrolled = scrolled - (scrollDiff - lowerBound);
            scrollDiff = lowerBound;
        }

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
        for (Button button : buttons) {
            if (button != backButton1) {
                button.reposition(0, (int) scrolled);
            }
        }

//        System.out.println(scrollDiff + " / " + maxScroll);

        if (scrolled > 0) scrolled -= 1;
        if (scrolled < 0) scrolled += 1;


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

        WeaponLoadout loadout = game.getPlayer().getWeaponLoadout();

        float margin = Settings.hudMargin * Settings.guiScale;
        float width = Settings.screenWidth/2 - (Settings.hudMargin*2);
        float height = Settings.screenHeight - margin * 4;

        float x = margin;
        float y = margin * 3;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setColor(new Color((75f/255f),0f,0f, .6f));
        renderer.rect(x, y, width, height);
        renderer.end();

        x = (float) (x + (width/2.0));
        y = (float) (y + ((height/6.0) * 5.0));

        batch.begin();
        selectedWeapon.getHudSprite().setScale(.2f * Settings.guiScale * (Settings.screenWidth/2560f));
        selectedWeapon.getHudSprite().setCenter(x, y);
        selectedWeapon.getHudSprite().draw(batch);

        Fonts.drawScaled(Fonts.SELECTIONFONT,
                1f,
                selectedWeapon.getName(),
                batch,
                Fonts.getXForCenteredText((int) x, selectedWeapon.getName(), Fonts.SELECTIONFONT),
                y - 100);

        Fonts.drawScaled(Fonts.SELECTIONFONT,
                1f,
                "Equipped: " + loadout.getEquippedWeapons().contains(selectedWeapon, false),
                batch,
                margin * 2,
                y - 200);

        batch.end();

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
        scrollMargin = Settings.screenWidth - (x + buttonWidth);
        int startY = (int) (Settings.screenHeight - scrollMargin - buttonHeight);
        Array<Weapon> weapons = game.getPlayer().getWeaponLoadout().getOwnedWeapons();

        scrolled = 0;
        scrollDiff = 0;

        buttons = new Button[weapons.size + 1];

        int i = 0;
        for (final Weapon weapon : weapons) {

            if (maxScroll > 0) maxScroll += Settings.hudMargin;
            maxScroll += buttonHeight;

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
        scrolled += (amountY * 10 * Settings.guiScale);
        if (scrolled > 0) scrolled = Math.min(scrolled, 20);
        if (scrolled < 0) scrolled = Math.max(scrolled, -20);

        return false;
    }
}
