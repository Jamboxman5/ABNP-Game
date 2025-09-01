package me.jamboxman5.abnpgame.screen.ui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import me.jamboxman5.abnpgame.data.SettingsData;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ui.elements.button.Button;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.util.Sounds;

import java.io.IOException;

public class SettingsMenuScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;
    OrthographicCamera camera;

    private final String title = "Settings";
    private final int alignX = Gdx.graphics.getWidth() - 40;
    private final int spacer = 70;
    private long lastButton = System.currentTimeMillis();

    public Button[] buttons;

    public Button activeButton;
    public Button back;
    public Button apply;

    public Button resLeft;
    public Button resRight;

    public Button guiLeft;
    public Button guiRight;

    public Button sfxLeft;
    public Button sfxRight;

    public Button musLeft;
    public Button musRight;

    private int selectedResolution;
    private int selectedGuiScale;
    private int selectedMusVolume;
    private int selectedSfxVolume;


    private SpriteBatch spriteBatch;
    private ShapeRenderer shapes;

    public SettingsMenuScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Menu_Background_1.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Settings.screenWidth, Settings.screenHeight);

        selectedResolution = Settings.resolutions.indexOf(Settings.getResolution(), false);
        selectedGuiScale = Math.round(Settings.guiScale * 10);

        selectedMusVolume = Math.round(Settings.musVolume * 10);
        selectedSfxVolume = Math.round(Settings.sfxVolume * 10);


        shapes = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        getButtons();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0, 0f, 1);

        camera.update();
        shapes.setProjectionMatrix(camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);


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

        drawBKG(spriteBatch);
        drawTitle(spriteBatch);
        drawLabels(spriteBatch);
        drawButtons(spriteBatch, shapes);
        drawValues(spriteBatch);

    }

    public void drawValues(SpriteBatch batch) {
        int half = Settings.screenWidth/2;

        batch.begin();

        int y = Settings.screenHeight/2;
        y += spacer;
        float x = Fonts.getXForCenteredText(half, getTextForResolution(), Fonts.BUTTONFONT);
        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, getTextForResolution(), batch, x, y + Fonts.getTextHeight("/", Fonts.BUTTONFONT, 1f));
        y += spacer;

        x = Fonts.getXForCenteredText(half, getTextForGuiScale(), Fonts.BUTTONFONT);
        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, getTextForGuiScale(), batch, x, y + Fonts.getTextHeight("/", Fonts.BUTTONFONT, 1f));
        y = Settings.screenHeight/2;

        x = Fonts.getXForCenteredText(half, selectedMusVolume + "", Fonts.BUTTONFONT);
        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, selectedMusVolume + "", batch, x, y + Fonts.getTextHeight("/", Fonts.BUTTONFONT, 1f));
        y -= spacer;

        x = Fonts.getXForCenteredText(half, selectedSfxVolume + "", Fonts.BUTTONFONT);
        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, selectedSfxVolume + "", batch, x, y + Fonts.getTextHeight("/", Fonts.BUTTONFONT, 1f));

        batch.end();
    }

    public void drawLabels(SpriteBatch batch) {
        float margin = Settings.hudMargin;
        batch.begin();

        int y = Settings.screenHeight/2;
        y += spacer;
        float x = margin;
        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, "Resolution:", batch, x, y + Fonts.getTextHeight("/", Fonts.BUTTONFONT, 1f));
        y += spacer;

        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, "GUI Scale:", batch, x, y + Fonts.getTextHeight("/", Fonts.BUTTONFONT, 1f));
        y = Settings.screenHeight/2;

        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, "Music Volume" + "", batch, x, y + Fonts.getTextHeight("/", Fonts.BUTTONFONT, 1f));
        y -= spacer;

        Fonts.drawScaled(Fonts.BUTTONFONT, 1f, "SFX Volume" + "", batch, x, y + Fonts.getTextHeight("/", Fonts.BUTTONFONT, 1f));

        batch.end();
    }

    private String getTextForGuiScale() {
        switch(selectedGuiScale) {
            case 10:
                return "Small";
            case 15:
                return "Medium";
            case 20:
                return "Large";
            case 25:
                return "Extra Large";
            default:
                return "INVALID";
        }
    }

    private String getTextForResolution() {
        Vector2 selected = Settings.resolutions.get(selectedResolution);
        return (int) selected.x + " x " + (int) selected.y;
    }

    private void getButtons() {
        buttons = new Button[10];

        int quarter = Settings.screenWidth/4;

        int y = Settings.screenHeight/2;
        y += spacer;
        resLeft = new Button(quarter, y, "<", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        resRight = new Button(quarter*3, y, ">", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        y += spacer;

        guiLeft = new Button(quarter, y, "<", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        guiRight = new Button(quarter*3, y, ">", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        y = Settings.screenHeight/2;

        musLeft = new Button(quarter, y, "<", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        musRight = new Button(quarter*3, y, ">", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        y -= spacer;

        sfxLeft = new Button(quarter, y, "<", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);
        sfxRight = new Button(quarter*3, y, ">", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);


        int x = Gdx.graphics.getWidth() - alignX;
        back = new Button(x, 50, "< Back", Fonts.BUTTONFONT, Button.TextAlign.LEFT);
        apply = new Button((int) Fonts.getXForRightAlignedText(alignX, "Apply", Fonts.BUTTONFONT, 1f), 50, "Apply", Fonts.BUTTONFONT, Button.TextAlign.RIGHT);

        buttons[0] = back;
        buttons[1] = resRight;
        buttons[2] = resLeft;
        buttons[3] = guiRight;
        buttons[4] = guiLeft;
        buttons[5] = musRight;
        buttons[6] = musLeft;
        buttons[7] = sfxRight;
        buttons[8] = sfxLeft;
        buttons[9] = apply;

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

        apply.setAction(new Runnable() {
            @Override
            public void run() {
                Settings.setResolution(Settings.resolutions.get(selectedResolution));
                Settings.guiScale = selectedGuiScale / 10f;
                Settings.musVolume = selectedMusVolume / 10f;
                Settings.sfxVolume = selectedSfxVolume / 10f;
                Sounds.updateVolumes();
                SettingsData.updateSettings();

                Screen old = game.getScreen();
                game.setScreen(new SettingsMenuScreen(game));
                game.disposeScreen(old);
            }
        });

        resRight.setAction(new Runnable() {
            @Override
            public void run() {
                selectedResolution++;
                if (selectedResolution >= Settings.resolutions.size) selectedResolution = 0;
            }
        });

        resLeft.setAction(new Runnable() {
            @Override
            public void run() {
                selectedResolution--;
                if (selectedResolution < 0) selectedResolution = Settings.resolutions.size-1;
            }
        });

        guiRight.setAction(new Runnable() {
            @Override
            public void run() {
                selectedGuiScale+=5;
                if (selectedGuiScale > 25) selectedGuiScale = 10;
            }
        });

        guiLeft.setAction(new Runnable() {
            @Override
            public void run() {
                selectedGuiScale-=5;
                if (selectedGuiScale < 10) selectedGuiScale = 25;
            }
        });

        musRight.setAction(new Runnable() {
            @Override
            public void run() {
                selectedMusVolume++;
                if (selectedMusVolume > 10) selectedMusVolume = 0;
            }
        });

        musLeft.setAction(new Runnable() {
            @Override
            public void run() {
                selectedMusVolume--;
                if (selectedMusVolume < 0) selectedMusVolume = 10;
            }
        });

        sfxRight.setAction(new Runnable() {
            @Override
            public void run() {
                selectedSfxVolume++;
                if (selectedSfxVolume > 10) selectedSfxVolume = 0;
            }
        });

        sfxLeft.setAction(new Runnable() {
            @Override
            public void run() {
                selectedSfxVolume--;
                if (selectedSfxVolume < 0) selectedSfxVolume = 10;
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
