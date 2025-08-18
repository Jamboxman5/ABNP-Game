package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.jamboxman5.abnpgame.entity.mob.player.Player;
import me.jamboxman5.abnpgame.entity.mob.player.Survivor;
import me.jamboxman5.abnpgame.entity.mob.zombie.Zombie;
import me.jamboxman5.abnpgame.entity.prop.pickup.Pickup;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public class UIManager {

    static float margin = Settings.hudMargin;
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
        int y = Gdx.graphics.getHeight()/2;

        batch.begin();

        for (int i = 0; i < msgBuffer.size; i++) {

            Fonts.drawScaled(Fonts.INFOFONT, .8f, msgBuffer.get(i), batch, margin + 2*Settings.guiScale, y);
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

        float width = 300 * Settings.guiScale;
        float height = 120 * Settings.guiScale;
        float x = Settings.screenWidth - margin - width;
        float y = Settings.screenHeight-margin;
        Rectangle bounds = new Rectangle(x, y-height, width, height);

        Sprite weaponIMG = activeWeapon.getHudSprite();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shape.setColor((float)(100.0/255.0), 0f, 0f, .6f);
        drawRoundRect(shape, ShapeRenderer.ShapeType.Filled, new Color((100f/255f), 0f, 0f,0.6f), bounds, 8);
        shape.end();

        batch.begin();

        if (activeWeapon instanceof Firearm) {

            Firearm activeFirearm = (Firearm) activeWeapon;
            String ammo = activeFirearm.getLoadedAmmo() + " / " + activeFirearm.getAmmoCount();

            x = Fonts.getXForRightAlignedText((int) (Settings.screenWidth - margin - (10*Settings.guiScale)), ammo, Fonts.INFOFONT, .55f * Settings.guiScale);
            y = y - height + (25*Settings.guiScale);

            Fonts.drawScaled(Fonts.INFOFONT, .55f * Settings.guiScale, ammo, batch,x, y);
            x = Settings.screenWidth - margin - width + (15*Settings.guiScale);
            Fonts.drawScaled(Fonts.INFOFONT, .55f * Settings.guiScale, activeFirearm.getName(), batch,x, y);
        }



        x = bounds.x + bounds.width/2f;
        y = bounds.y + 15 + bounds.height/2f;
        weaponIMG.setCenter(x, y);
        float nativeScale = weaponIMG.getScaleX();
        weaponIMG.setScale(Settings.guiScale*nativeScale);
        weaponIMG.draw(batch);
        weaponIMG.setScale(nativeScale);

        batch.end();

    }

    public static void drawRadar(SpriteBatch batch, ShapeRenderer shape, ABNPGame game) {

        float side = 140 * Settings.guiScale;
        Vector2 center = new Vector2(margin + (side/2f), margin + (side/2f));
        Rectangle mapBounds = new Rectangle(margin+4, margin+4, side-8, side-8);

        Sprite pointer;

        shape.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        shape.setColor(0f, (float)(50.0/255.0), 0f, .6f);
        shape.rect(margin, margin, side, side);

        float zoomFactor = .1f;

        // draw map entities
        MapManager map = game.getMapManager();
        Player player = game.getPlayer();
        for (Pickup p : map.getPickups()) {
            Vector2 displacement = p.getPosition().cpy().sub(player.getPosition()).scl(zoomFactor).add(center);
            if (mapBounds.contains(displacement)) {
                shape.setColor(Color.YELLOW);
                shape.circle(displacement.x, displacement.y, 3*Settings.guiScale);
                shape.setColor(1f, 1f, (220f/255f), 1f);
                shape.circle(displacement.x, displacement.y, 2*Settings.guiScale);
            }

        }

        for (Zombie z : map.getZombies()) {
            Vector2 displacement = z.getPosition().cpy().sub(player.getPosition()).scl(zoomFactor).add(center);
            if (mapBounds.contains(displacement)) {
                shape.setColor(Color.RED);
                shape.circle(displacement.x, displacement.y, 3*Settings.guiScale);
                shape.setColor(1f, (220f/255f), (220f/255f), 1f);
                shape.circle(displacement.x, displacement.y, 2*Settings.guiScale);
            }

        }

        for (Survivor s : map.getSurvivors()) {
            Vector2 displacement = s.getPosition().cpy().sub(player.getPosition()).scl(zoomFactor).add(center);
            if (mapBounds.contains(displacement)) {
                shape.setColor(Color.BLUE);
                shape.circle(displacement.x, displacement.y, 3*Settings.guiScale);
                shape.setColor((220f/255f), (220f/255f), 1f, 1f);
                shape.circle(displacement.x, displacement.y, 2*Settings.guiScale);
            }
        }

        shape.setColor(Color.WHITE);
        shape.circle(center.x, center.y, 2*Settings.guiScale);
        shape.setColor(Color.GREEN);
        shape.setAutoShapeType(true);
        shape.set(ShapeRenderer.ShapeType.Line);
        shape.rect(mapBounds.x, mapBounds.y, mapBounds.width, mapBounds.height);
        shape.setColor(Color.WHITE);
        shape.line(center, center.cpy().add(new Vector2(1, 0).rotateRad((float) player.getAdjustedRotation()).scl(10f*Settings.guiScale)));
        shape.end();

        batch.begin();

//        if (activeWeapon instanceof Firearm) {
//
//            Firearm activeFirearm = (Firearm) activeWeapon;
//            String ammo = activeFirearm.getLoadedAmmo() + " / " + activeFirearm.getAmmoCount();
//
//            x = Fonts.getXForRightAlignedText((int) (camera.viewportWidth - (30*Settings.guiScale)), ammo, Fonts.INFOFONT, .55f * Settings.guiScale);
//            y = y - height + (25*Settings.guiScale);
//
//            Fonts.drawScaled(Fonts.INFOFONT, .55f * Settings.guiScale, ammo, batch,x, y);
//            x = camera.viewportWidth - width - 20 + (15*Settings.guiScale);
//            Fonts.drawScaled(Fonts.INFOFONT, .55f * Settings.guiScale, activeFirearm.getName(), batch,x, y);
//        }
//
//
//
//        x = camera.viewportWidth - 20 - (width/2);
//        y = camera.viewportHeight - (65*Settings.guiScale);
//        weaponIMG.setCenter(x, y);
//        float nativeScale = weaponIMG.getScaleX();
//        weaponIMG.setScale(Settings.guiScale/2.5f);
//        weaponIMG.draw(batch);
//        weaponIMG.setScale(nativeScale);
//
        batch.end();

    }

    public static void drawHealthBar(SpriteBatch batch, ShapeRenderer renderer, Player player) {

        int width = (int) (300 * Settings.guiScale);
        int height = (int) (30 * Settings.guiScale);
        int weight = 2;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setColor((float)(100.0/255.0), 0f, 0f, .6f);
        renderer.rect(margin, Gdx.graphics.getHeight()-margin-height, width, height);
        renderer.setColor(Color.RED);
        renderer.rect(margin, Gdx.graphics.getHeight()-margin-height, width * player.getHealthRatio(), height);

        renderer.setColor((float)(140.0/255.0), (float)(100.0/255.0), 0f, .6f);
        renderer.rect(margin, Gdx.graphics.getHeight()-(margin)-(height*2)-10, width, height);
        renderer.setColor(Color.ORANGE);
        renderer.rect(margin, Gdx.graphics.getHeight()-(margin)-(height*2)-10, width * player.getStaminaRatio(), height);


        renderer.setColor(Color.WHITE);
        renderer.setAutoShapeType(true);
        Gdx.gl.glLineWidth(weight);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.rect(margin, Gdx.graphics.getHeight()-margin-height, width, height);
        renderer.rect(margin, Gdx.graphics.getHeight()-(margin)-(height*2)-10, width, height);

        renderer.end();
        
        batch.begin();
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, "HP: " + (int)player.getHealth() + "/" + (int)player.getMaxHealth(), batch,margin + (5*Settings.guiScale), Gdx.graphics.getHeight()-margin-height + (5*Settings.guiScale) + Fonts.getTextHeight("/", Fonts.INFOFONT,.4f * Settings.guiScale));
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, "Stamina: " + (int)player.getStamina() + "/" + (int)player.getMaxStamina(), batch,margin + (5*Settings.guiScale), Gdx.graphics.getHeight()-margin-(height*2)-10 + (5*Settings.guiScale) + Fonts.getTextHeight("/", Fonts.INFOFONT,.4f * Settings.guiScale));
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, "Money: $" + player.getMoney(), batch,margin + 2*Settings.guiScale, Gdx.graphics.getHeight()-(margin*2)-(height*2) - (5*Settings.guiScale) - Fonts.getTextHeight("/", Fonts.INFOFONT,.4f * Settings.guiScale));
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
        int y = (int) (screenHeight - (180*Settings.guiScale));
        int spacer = 30;
        int x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);
        //
        debugTXT = "World Y: " + String.format("%,.2f", game.getPlayer().getWorldY());
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);

        //
        debugTXT = "Player Rotation: " + String.format("%,.2f", Math.toDegrees(game.getPlayer().getAdjustedRotation()));
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);

        //

        if (game.getMousePointer() != null) {
            debugTXT = "Mouse X: " + String.format("%,.2f", game.getMousePointer().x);
            y-=spacer;
            x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//            Utilities.drawStringShadow(g2, debugTXT, x, y);
//            g2.drawString(debugTXT, x, y);
            Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);

            //
            debugTXT = "Mouse Y: " + String.format("%,.2f", game.getMousePointer().y);
            y-=spacer;
            x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//            Utilities.drawStringShadow(g2, debugTXT, x, y);
//            g2.drawString(debugTXT, x, y);
            Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);
            //
            debugTXT = "Target X: " + String.format("%,.2f", game.getWorldMousePointer().x);
            y-=spacer;
            x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//            Utilities.drawStringShadow(g2, debugTXT, x, y);
//            g2.drawString(debugTXT, x, y);
            Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);
            //
            debugTXT = "Target Y: " + String.format("%,.2f", game.getWorldMousePointer().y);
            y-=spacer;
            x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//            Utilities.drawStringShadow(g2, debugTXT, x, y);
//            g2.drawString(debugTXT, x, y);
            Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);

        }

        //
        debugTXT = "Active Projectiles: " + game.getMapManager().projectiles.size;
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);

        debugTXT = "Active Entities: " + game.getMapManager().entities.size;
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);

        //
        debugTXT = "Frame Time: " + delta + "ms";
        y-=spacer;
        x = (int) Fonts.getXForRightAlignedText(screenWidth-30, debugTXT, Fonts.INFOFONT, .4f * Settings.guiScale);
//        Utilities.drawStringShadow(g2, debugTXT, x, y);
//        g2.drawString(debugTXT, x, y);
        Fonts.drawScaled(Fonts.INFOFONT, .4f * Settings.guiScale, debugTXT, spriteBatch, x, y);

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
