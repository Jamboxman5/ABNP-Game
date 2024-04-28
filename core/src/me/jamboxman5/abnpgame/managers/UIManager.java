package me.jamboxman5.abnpgame.managers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.screen.ScreenInfo;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

import javax.swing.plaf.ViewportUI;

public class UIManager {

    static Sprite WeaponHudOverlay;

    public static void setupElements() {
        Texture t = new Texture(Gdx.files.internal("ui/elements/WeaponHudOverlay.PNG"));
        WeaponHudOverlay = new Sprite(t);
    }

    public static void drawWeaponHud(SpriteBatch batch, ShapeRenderer shape, ABNPGame game, OrthographicCamera camera) {
        Weapon activeWeapon = game.getPlayer().getWeaponLoadout().getActiveWeapon();

        float width = 300;
        float height = 120;
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

        shape.begin(ShapeRenderer.ShapeType.Filled);


        shape.setColor((float)(100.0/255.0), 0f, 0f, .6f);
//        shape.rect(x, y-height ,  width, height);
        drawRoundRect(shape, ShapeRenderer.ShapeType.Filled, new Color((float)(100.0/255.0), 0f, 0f,0.6f), new Rectangle(x, y-height, width, height), 8);

        shape.end();
        batch.end();
        batch.begin();

        if (activeWeapon instanceof Firearm) {

            Firearm activeFirearm = (Firearm) activeWeapon;
            String ammo = activeFirearm.getLoadedAmmo() + " / " + activeFirearm.getAmmoCount();

            x = Fonts.getXForRightAlignedText((int) (camera.viewportWidth - 30), ammo, Fonts.INFOFONT, .6f);
            y = y - height + 25;

            Fonts.drawScaled(Fonts.INFOFONT, .6f, ammo, batch,x, y);
            x = camera.viewportWidth - 10 - width;
            Fonts.drawScaled(Fonts.INFOFONT, .6f, activeFirearm.getName(), batch,x, y);
        }



        x = camera.viewportWidth - 20 - (width/2);
        y = camera.viewportHeight - 75;
        weaponIMG.setCenter(x, y);
        weaponIMG.draw(batch);


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


}
