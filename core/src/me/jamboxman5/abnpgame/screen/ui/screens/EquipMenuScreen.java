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
import me.jamboxman5.abnpgame.screen.ui.elements.button.Button;
import me.jamboxman5.abnpgame.screen.ui.elements.button.ButtonSprite;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;

public class EquipMenuScreen implements Screen, InputProcessor {

    Texture menuBKG;
    final ABNPGame game;

    private long lastButton = System.currentTimeMillis();

    OrthographicCamera camera;

    public Array<Button> buttons;
    public Array<Button> scrollWeaponButtons;
    public Array<Button> equippedWeaponButtons;

    public Button backButton1;
    public Button activeButton;

    private Weapon selectedWeapon;

    private float scrolled = 0;
    private float scrollDiff = 0;
    private float maxScroll = 0;
    private int scrollStartX;

    private int scrollMargin;
    private int scrollerY;
    private int infoY;
    private int infoHeight;
    private int infoWidth;
    private int invWidth;
    private int scrollButtonHeight;
    private int scrollButtonWidth;
    private int invButtonHeight;
    private int invButtonWidth;


    private SpriteBatch spriteBatch;
    private ShapeRenderer shapes;

    public EquipMenuScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Black_Background.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);
        shapes = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        scrollMargin = (int) (Settings.hudMargin * 2);

        scrollButtonHeight = (Settings.screenHeight/4);
        scrollButtonWidth = (scrollButtonHeight*2);

        scrollStartX = scrollMargin;
        scrollerY = (int) (Settings.screenHeight * (1.5/16.0));

        infoY = (int) ((Settings.screenHeight * (7.0/16.0)));
        infoHeight = (int) ((Settings.screenHeight * (9.0/16.0)) - scrollMargin);

        invButtonHeight = (infoHeight - (scrollMargin * 2)) / 3;
        invButtonWidth = invButtonHeight * 2;

        invWidth = (invButtonWidth * 2) + scrollMargin;

        infoWidth = (int) (Settings.screenWidth - (scrollMargin * 3) - invWidth);



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
        scrollDiff += scrolled;

        if (scrollDiff < 0) {
            scrolled = scrolled - scrollDiff;
            scrollDiff = 0;
        }

        float lowerBound = Math.max(0, maxScroll - (Settings.screenWidth) + scrollMargin*2);
        if (scrollDiff > lowerBound) {
            scrolled = scrolled - (scrollDiff - lowerBound);
            scrollDiff = lowerBound;
        }

        updateActiveButton(game.getMousePointer());
        if (Gdx.input.isTouched()) {
            if (activeButton != null && (System.currentTimeMillis() - lastButton > 200)) {
                Sounds.MENUSELECT.play(Settings.sfxVolume);
                activeButton.press();
                lastButton = System.currentTimeMillis();
            }
        }

        for (Button button : buttons) {
            if (button != backButton1) {
                button.reposition((int) -scrolled, 0);
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
        if (scrollWeaponButtons.size == 0) {
            batch.begin();
            int x = (int) Fonts.getXForCenteredText(Settings.screenWidth / 2, "All Weapons Equipped.", Fonts.INFOFONT);
            Fonts.drawScaled(Fonts.INFOFONT, 1f, "All Weapons Equipped.", batch, x, scrollerY + (scrollButtonHeight / 2f));
            batch.end();
        } else {
            for (Button b : scrollWeaponButtons) {
                b.draw(batch, renderer, (activeButton == b));
            }
        }

        for (Button b : equippedWeaponButtons) {
            b.draw(batch, renderer, (activeButton == b));
        }
    }

    public void draw() {

        drawBKG(spriteBatch);
        drawMenuBgBlocks(spriteBatch, shapes);
        drawButtons(spriteBatch, shapes);
        drawSelectedWeapon(spriteBatch, shapes);
    }

    public void drawMenuBgBlocks(SpriteBatch batch, ShapeRenderer renderer) {

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setColor(new Color((10f/255f),(10f/255f),(10f/255f), .9f));

        //Selected Weapon Box Shadow
        renderer.rect(scrollMargin- 10, infoY - 10, infoWidth + 20, infoHeight + 20);

        //Weapon Inventory Box Shadow
        renderer.rect(Settings.screenWidth - scrollMargin - invWidth - 10, infoY - 10, invWidth + 20, infoHeight + 20);

        renderer.setColor(new Color((60f/255f),(10f/255f),(10f/255f), 1f));

        //Selected Weapon Box
        renderer.rect(scrollMargin, infoY, infoWidth, infoHeight);

        //Weapon Inventory Box
        renderer.rect(Settings.screenWidth - scrollMargin - invWidth, infoY, invWidth, infoHeight);

        renderer.end();

    }

    public void drawSelectedWeapon(SpriteBatch batch, ShapeRenderer renderer) {
        if (selectedWeapon == null) return;

        WeaponLoadout loadout = game.getPlayer().getWeaponLoadout();

        float margin = Settings.hudMargin * Settings.guiScale;

        float x = scrollMargin;

        x = (float) (x + (infoWidth/2.0));
        float y = (float) (infoY + ((infoHeight/6.0) * 5.0));

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
        for (Button b : scrollWeaponButtons) {
            if (b.contains(p))  {
                if (activeButton != b) {
                    activeButton = b;
                    Sounds.MENUSCROLL.play(Settings.sfxVolume);
                }
                return;
            }
        }

        for (Button b : equippedWeaponButtons) {
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


        Array<Weapon> weapons = game.getPlayer().getWeaponLoadout().getUnequippedWeapons();

        scrolled = 0;
        scrollDiff = 0;

        buttons = new Array<>();
        scrollWeaponButtons = new Array<>();
        equippedWeaponButtons = new Array<>();

        int startX = scrollMargin;

        if (weapons.size == 1) {
            startX = (Settings.screenWidth / 2) - (scrollButtonWidth / 2);
        } else if (weapons.size == 2) {
            startX = (int) ((Settings.screenWidth / 2) - (scrollMargin / 2) - (scrollButtonWidth));
        } else if (weapons.size == 3) {
            startX = (int) ((Settings.screenWidth / 2) - (scrollButtonWidth / 2) - (scrollButtonWidth) - (scrollMargin));
        }

        for (final Weapon weapon : weapons) {

            if (maxScroll > 0) maxScroll += scrollMargin;
            maxScroll += scrollButtonWidth;

            ButtonSprite button = new ButtonSprite(startX, scrollerY, scrollButtonWidth, scrollButtonHeight, weapon.getHudSprite(), .15f * Settings.guiScale);
            startX += scrollButtonWidth;
            startX += scrollMargin;

            button.setAction(new Runnable() {
                @Override
                public void run() {
                    selectedWeapon = weapon;
                }
            });
            button.setSpriteDropShadow(true);
            button.setDropShadowOffset(6);
            button.setDropShadowOpacity(.3f);

            scrollWeaponButtons.add(button);

        }

        weapons = game.getPlayer().getWeaponLoadout().getEquippedWeapons();

        int i = 0;
        int j = 0;
        for (final Weapon weapon : weapons) {

            int x = (int) (Settings.screenWidth - (scrollMargin * 2) - (invButtonWidth * 2));
            int y = (int) (Settings.screenHeight - (scrollMargin) - (invButtonHeight));
            y -= (int) (j * (invButtonHeight + scrollMargin));
            if (i % 2 != 0) {
                x += (int) (invButtonWidth + (scrollMargin));
                i = 0;
                j++;
            } else {
                i++;
            }

            ButtonSprite button = new ButtonSprite(x, y, invButtonWidth, invButtonHeight, weapon.getHudSprite(), .1f * Settings.guiScale);
            button.setFill(false);
            button.setSpriteDropShadow(true);
            button.setAction(new Runnable() {
                @Override
                public void run() {
                    selectedWeapon = weapon;
                }
            });
            button.setDropShadowOpacity(.6f);

            equippedWeaponButtons.add(button);

        }

        int y = 80;
        int x = 40;

        backButton1 = new Button(x, y - 25, "< Back", Fonts.BUTTONFONT, 1f);
        buttons.add(backButton1);

        Runnable backAction = new Runnable() {
            @Override
            public void run() {
                Screen old = game.getScreen();
                game.setScreen(new ArcadeMenuScreen(game));
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
        if (scrolled > 0) scrolled = Math.min(scrolled, 40);
        if (scrolled < 0) scrolled = Math.max(scrolled, -40);

        return false;
    }
}
