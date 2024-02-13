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
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.util.Fonts;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;

public class UIManager {

    static Sprite WeaponHudOverlay;

    public static void setupElements() {
        Texture t = new Texture(Gdx.files.internal("ui/elements/WeaponHudOverlay.PNG"));
        WeaponHudOverlay = new Sprite(t);
    }

    public static void drawWeaponHud(SpriteBatch batch, ABNPGame game, OrthographicCamera camera) {
        Weapon activeWeapon = game.getPlayer().getWeaponLoadout().getActiveWeapon();

        float width = camera.viewportWidth;
        float height = camera.viewportHeight;
        float x = width - 300;
        float y = height - 150;

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

        ShapeRenderer shape = new ShapeRenderer();
        shape.setAutoShapeType(true);
        shape.begin();

        shape.rect(width-10, height-10, 20,20);
        weaponIMG.setCenter(x, y);
        weaponIMG.draw(game.batch);

        if (activeWeapon instanceof Firearm) {

            Firearm activeFirearm = (Firearm) activeWeapon;
            String ammo = activeFirearm.getLoadedAmmo() + " / " + activeFirearm.getAmmoCount();
            x = (int) Fonts.getXForRightAlignedText(Gdx.graphics.getWidth() - 30, ammo, Fonts.INFOFONT, .75f);
            y = height + 5;
            shape.setColor(Color.RED);
            shape.rect(x-200,y,20,20);
            Fonts.drawScaled(Fonts.INFOFONT, .75f, ammo, game.batch,x-200, y);
            x = Gdx.graphics.getWidth() - 10 - width;
            Fonts.drawScaled(Fonts.INFOFONT, .75f, activeFirearm.getName(), game.batch,x, y);

        }
    }

}
