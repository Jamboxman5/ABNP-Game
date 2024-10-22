package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.jamboxman5.abnpgame.entity.player.Player;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.GameScreen;
import me.jamboxman5.abnpgame.screen.ScreenInfo;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

import javax.swing.plaf.ViewportUI;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class UIManager {

    static Sprite WeaponHudOverlay;
    static float guiScale = (1920f/(Gdx.graphics.getWidth()/.9f));
    static Array<String> msgBuffer = new Array<>();
    static int messageBufferCounter = 0;
    public static float fadeOut = 0f;

//    public static void setupElements() {
//        Texture t = new Texture(Gdx.files.internal("ui/elements/WeaponHudOverlay.PNG"));
//        WeaponHudOverlay = new Sprite(t);
//    }

    public static void drawMessageBuffer(SpriteBatch batch) {
        if (msgBuffer.size == 0) return;
        messageBufferCounter++;
        int x = 20;
        int y = Gdx.graphics.getHeight()/2;

        batch.begin();

        for (int i = 0; i < msgBuffer.size; i++) {

            Fonts.drawScaled(Fonts.INFOFONT, .8f, msgBuffer.get(i), batch, x, y);
            y += 40;

        }
        if (messageBufferCounter > 360) {
            msgBuffer.removeIndex(msgBuffer.size-1);
            messageBufferCounter = 0;
        }

        batch.end();
    }

    public static void pushBufferMessage(String msg) {
        msgBuffer.insert(0, msg);
    }

    public static void drawWeaponHud(SpriteBatch batch, ShapeRenderer shape, ABNPGame game, OrthographicCamera camera) {
        Weapon activeWeapon = game.getPlayer().getWeaponLoadout().getActiveWeapon();

        float width = 300 * guiScale;
        float height = 120 * guiScale;
        float x = ScreenInfo.WIDTH - 20 - width;
        float y = ScreenInfo.HEIGHT-20;

//        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f);
//        Composite old = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
//        g2.setColor(new Color(100,0,0));
//        g2.setStroke(new BasicStroke(4));
//        g2.setComposite(comp);
//        g2.fillRoundRect(x, y, width, height,8,8);
//        g2.setComposite(old);
//        g2.setColor(Color.white);
//        g2.drawRoundRect(x, y, width, height, 8,8);

        Sprite weaponIMG = activeWeapon.getHudSprite();


//        shape.rect(x, y-height ,  width, height);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shape.setColor((float)(100.0/255.0), 0f, 0f, .6f);
        drawRoundRect(shape, ShapeRenderer.ShapeType.Filled, new Color((float)(100.0/255.0), 0f, 0f,0.6f), new Rectangle(x, y-height, width, height), 8);
        shape.end();

        batch.begin();

        if (activeWeapon instanceof Firearm) {

            Firearm activeFirearm = (Firearm) activeWeapon;
            String ammo = activeFirearm.getLoadedAmmo() + " / " + activeFirearm.getAmmoCount();

            x = Fonts.getXForRightAlignedText((int) (camera.viewportWidth - (30*guiScale)), ammo, Fonts.INFOFONT, .55f * guiScale);
            y = y - height + (25*guiScale);

            Fonts.drawScaled(Fonts.INFOFONT, .55f * guiScale, ammo, batch,x, y);
            x = camera.viewportWidth - width - 20 + (15*guiScale);
            Fonts.drawScaled(Fonts.INFOFONT, .55f * guiScale, activeFirearm.getName(), batch,x, y);
        }



        x = camera.viewportWidth - 20 - (width/2);
        y = camera.viewportHeight - (65*guiScale);
        weaponIMG.setCenter(x, y);
        float nativeScale = weaponIMG.getScaleX();
        weaponIMG.setScale(guiScale/2.5f);
        weaponIMG.draw(batch);
        weaponIMG.setScale(nativeScale);

        batch.end();

    }

    public static void drawHealthBar(SpriteBatch batch, ShapeRenderer renderer, Player player) {

        int width = 300;
        int height = 40;
        int margin = 30;
        int weight = 2;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setColor((float)(100.0/255.0), 0f, 0f, .6f);
        renderer.rect(margin, Gdx.graphics.getHeight()-margin-height, width, height);
        renderer.setColor(Color.RED);
        renderer.rect(margin, Gdx.graphics.getHeight()-margin-height, width * player.getHealthRatio(), height);
        renderer.setColor(Color.WHITE);
        renderer.setAutoShapeType(true);
        Gdx.gl.glLineWidth(weight);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.rect(margin, Gdx.graphics.getHeight()-margin-height, width, height);

        renderer.end();

        batch.begin();
        Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, "HP: " + (int)player.getHealth() + "/" + (int)player.getMaxHealth(), batch,margin + (5*guiScale), Gdx.graphics.getHeight()-margin-height + (5*guiScale) + Fonts.getTextHeight("/", Fonts.INFOFONT,.4f * guiScale));
        Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, "Money: $" + player.getMoney(), batch,margin + (5*guiScale), Gdx.graphics.getHeight()-margin-height - (5*guiScale) - Fonts.getTextHeight("/", Fonts.INFOFONT,.4f * guiScale));
        batch.end();

    }

    public static void drawRoundRect(ShapeRenderer shape, ShapeRenderer.ShapeType type, Color color, Rectangle bounds, int angle) {
        shape.setColor(color);
        shape.set(type);
        shape.rect(bounds.x + angle, bounds.y, bounds.width - (angle*2), bounds.height);
        shape.rect(bounds.x, bounds.y + angle, angle, bounds.height - (angle*2));
        shape.rect(bounds.x + bounds.width - angle, bounds.y + angle, angle, bounds.height - (angle*2));
        shape.arc(bounds.x + angle, bounds.y + angle, angle, 270, -90, 20);
        shape.arc(bounds.x + bounds.width - angle, bounds.y + bounds.height - angle, angle, 90, -90, 20);
        shape.arc(bounds.x + bounds.width - angle, bounds.y + angle, angle, 0, -90, 20);
        shape.arc(bounds.x + angle, bounds.y + bounds.height - angle, angle, 90, 90, 20);


    }

    public static void drawDebugInfo(ABNPGame game, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, float delta) {

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        spriteBatch.begin();

        String debugTXT = "World X: " + String.format("%,.2f", game.getPlayer().getWorldX());
        int y = (int) (screenHeight - (180*guiScale));
        int spacer = 30;
        int x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);
        //
        debugTXT = "World Y: " + String.format("%,.2f", game.getPlayer().getWorldY());
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);

        //
        debugTXT = "Player Rotation: " + String.format("%,.2f", Math.toDegrees(game.getPlayer().getAngleToCursor()));
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);

        //

        if (game.getMousePointer() != null) {
            debugTXT = "Mouse X: " + String.format("%,.2f", game.getMousePointer().x);
            y-=spacer;
            x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//            Utilities.drawStringShadow(g2, debugTXT, x, y);
//            g2.drawString(debugTXT, x, y);
            Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);

            //
            debugTXT = "Mouse Y: " + String.format("%,.2f", game.getMousePointer().y);
            y-=spacer;
            x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//            Utilities.drawStringShadow(g2, debugTXT, x, y);
//            g2.drawString(debugTXT, x, y);
            Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);
            //
            debugTXT = "Target X: " + String.format("%,.2f", game.getWorldMousePointer().x);
            y-=spacer;
            x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//            Utilities.drawStringShadow(g2, debugTXT, x, y);
//            g2.drawString(debugTXT, x, y);
            Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);
            //
            debugTXT = "Target Y: " + String.format("%,.2f", game.getWorldMousePointer().y);
            y-=spacer;
            x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//            Utilities.drawStringShadow(g2, debugTXT, x, y);
//            g2.drawString(debugTXT, x, y);
            Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);

        }

        //
        debugTXT = "Active Projectiles: " + game.getMapManager().projectiles.size;
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);

        debugTXT = "Active Entities: " + game.getMapManager().entities.size;
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);

        //
        debugTXT = "Frame Time: " + delta + "ms";
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * guiScale, debugTXT, spriteBatch, x, y);

        spriteBatch.end();

    }


    public static boolean drawFadeOut(ShapeRenderer shape) {

        shape.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shape.setColor(1f,1f,1f, fadeOut);
        shape.rect(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shape.end();

        fadeOut += .005f;
        if (fadeOut >= 1) {
            fadeOut = 0f;
            return true;
        }
        return false;

    }
}
