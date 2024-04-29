package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Fonts;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class MenuManager {

    Texture menuBKG;
    ABNPGame game;
    public MenuManager(ABNPGame game) {
        this.game = game;
        menuBKG = new Texture(Gdx.files.internal("ui/bkg/Menu_Background_0.png"));

    }

    public void draw() {
        drawMainMenu(game.uiCanvas, game.shapeRenderer);
    }

    private void drawMainMenu(SpriteBatch batch, ShapeRenderer renderer) {
        renderer.setColor(new Color((50/255),0,0, 0));
        batch.draw(menuBKG, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        String title = "ABNP:";
        String subTitle = "Zombie Assault";

        int x = 60;
        int y = Gdx.graphics.getHeight() - 220;


        Fonts.drawScaled(Fonts.TITLEFONT, 1f, title, batch, x, y + Fonts.getTextHeight(title, Fonts.TITLEFONT));

        y -= 90;

        Fonts.drawScaled(Fonts.SUBTITLEFONT, .841f, subTitle, batch, x, y+ Fonts.getTextHeight(title, Fonts.SUBTITLEFONT));


        x = (int) Fonts.getXForRightAlignedText(Gdx.graphics.getWidth() - 40,"Singleplayer", Fonts.SELECTIONFONT, .5f);
        y = Gdx.graphics.getHeight()/2 - 140;
        int spacer = 70;

        Fonts.drawScaled(Fonts.SELECTIONFONT, .5f, "Singleplayer", batch, x, y);

//        if (menuIndex == 0) {
//
//            Utilities.drawStringShadow(g2, ">", x-60, y);
//
//            g2.setColor(Color.white);
//            g2.drawString(">", x-60, y);
//        }
        y-=spacer;

        x = (int) Fonts.getXForRightAlignedText(Gdx.graphics.getWidth() - 40,"Multiplayer", Fonts.SELECTIONFONT, .5f);

        Fonts.drawScaled(Fonts.SELECTIONFONT, .5f, "Multiplayer", batch, x, y);

//        if (menuIndex == 1) {
//            Utilities.drawStringShadow(g2, ">", x-60, y);
//
//            g2.setColor(Color.white);
//            g2.drawString(">", x-60, y);
//        }

        y-= spacer;
        x = (int) Fonts.getXForRightAlignedText(Gdx.graphics.getWidth() - 40,"Quit Game", Fonts.SELECTIONFONT, .5f);

        Fonts.drawScaled(Fonts.SELECTIONFONT, .5f, "Quit Game", batch, x, y);
//        if (menuIndex == 2) {
//            Utilities.drawStringShadow(g2, ">", x-60, y);
//
//            g2.setColor(Color.white);
//            g2.drawString(">", x-60, y);
//        }

    }

}
