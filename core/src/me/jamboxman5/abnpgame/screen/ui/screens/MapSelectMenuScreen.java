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
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.screen.ScreenInfo;
import me.jamboxman5.abnpgame.screen.ui.elements.Button;
import me.jamboxman5.abnpgame.util.Fonts;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class MapSelectMenuScreen implements Screen {

    Texture menuBKG;
    final ABNPGame game;
    OrthographicCamera camera;

    private long lastButton = System.currentTimeMillis();

    public Button[] buttons;
    public Button verdammtenstadt;
    public Button blackIsle;
    public Button karnivale;
    public Button airbase;
    public Button farmHouse;
    public Button activeButton;


    public MapSelectMenuScreen(final ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Menu_Background_1.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ScreenInfo.WIDTH, ScreenInfo.HEIGHT);
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

    }



    public void update() {
        updateActiveButton(game.getMousePointer());
        if (Gdx.input.isTouched()) {
            if (activeButton != null && (System.currentTimeMillis() - lastButton > 500)) {
                activeButton.press();
                lastButton = System.currentTimeMillis();
            }
        }
    }

    public void drawBKG(SpriteBatch batch) {

    }

    public void draw() {

        g2.drawImage(bkg, 0, 0, gp.getScreenWidth(), gp.getScreenHeight(), null);

        g2.setFont(Fonts.TITLEFONT.deriveFont(Font.BOLD, 180));
        String title = "Select Map:";

        int x = 60+Utilities.getTextHeight(title, g2)/12;
        int y = 180+Utilities.getTextHeight(title, g2)/12;

        Utilities.drawStringWithShadow(g2, title, Color.white, x, y);

        g2.setFont(Fonts.BUTTONFONT);


        int spacer = 60;
        y = gp.getScreenHeight() - 40;



        for (int i = gp.getMapManager().getMapList().size()-1; i >= 0; i--) {
            Map m = gp.getMapManager().getMapList().get(i);
            x = Utilities.getXForRightAlignedText(gp.getScreenWidth() - 40,m.getName().replace("_", " "), g2);
            Utilities.drawStringShadow(g2, m.getName().replace("_", " "), x, y);
            g2.setColor(Color.white);
            g2.drawString(m.getName().replace("_", " "), x, y);
            if (menuIndex == i) {
                Utilities.drawStringShadow(g2, ">", x - 60, y);
                g2.setColor(Color.white);
                g2.drawString(">", x - 60, y);
            }
            y -= spacer;

        }

        y = gp.getScreenHeight() - 40;
        x = 80;
        Utilities.drawStringShadow(g2, "Main Menu", x, y);
        if (menuIndex == 5) {
            g2.setColor(Color.LIGHT_GRAY);
        } else {
            g2.setColor(Color.white);
        }
        g2.drawString("Main Menu", x, y);
        Utilities.drawStringShadow(g2, ">", x - 40, y);
        g2.setColor(Color.RED);
        g2.drawString(">", x - 40, y);

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
                game.setScreen(new GameScreen(game));
                old.dispose();

            }
        });

    }

    public void updateActiveButton(Vector2 p) {
        for (Button b : buttons) {
            if (b.contains(p))  {
                activeButton = b;
                return;
            }
        }
        activeButton = null;
    }

}
