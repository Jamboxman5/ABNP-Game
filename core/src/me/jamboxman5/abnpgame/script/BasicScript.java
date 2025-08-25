package me.jamboxman5.abnpgame.script;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.mob.npc.Ally;
import me.jamboxman5.abnpgame.entity.mob.zombie.Zombie;
import me.jamboxman5.abnpgame.entity.mob.zombie.ZombieNormal;
import me.jamboxman5.abnpgame.entity.mob.zombie.ZombieRunner;
import me.jamboxman5.abnpgame.entity.mob.zombie.ZombieTank;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.entity.prop.pickup.PickupWeapon;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.managers.UIManager;
import me.jamboxman5.abnpgame.util.Settings;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.firearms.rifle.RifleM4A1;

import java.util.Random;

public class BasicScript extends MissionScript {



    @Override
    public void run() {
        try {
            Sound winSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/menu/Win.wav"));

            final ABNPGame game = ABNPGame.getInstance();
            Ally ally = new Ally(game, "Sarge");
            ally.setPosition(game.getMapManager().getActiveMap().getPlayerSpawn().cpy());
//            game.getMapManager().addAlly(ally);

            gameOver = false;

            Vector3[] spawnPoints = game.getMapManager().getActiveMap().getZombieSpawns();
            UIManager.pushBufferMessage("Prepare for the first wave!");
            Thread.sleep(10000);
            UIManager.pushBufferMessage("Begin!");

            Firearm toDrop = new RifleM4A1();
            toDrop.setAmmo(game.getPlayer().getWeaponLoadout().getAmmo(Ammo.AmmoType.StandardAmmo));

            game.getMapManager().addEntity(new PickupWeapon(toDrop, new Vector3(0, 0, 0), 90));

            for (int i = 0; i < 10; i++) {
                zombiesRemaining = (50 - i) + game.getMapManager().entities.size;
                Thread.sleep(2000);

                Zombie zombie = new ZombieTank(game, getRandomSpawnPoint(spawnPoints));
                game.getMapManager().addEntity(zombie);
                lastSpawn = System.currentTimeMillis();
            }

            while (game.getMapManager().getZombies().size > 0) {
                zombiesRemaining = game.getMapManager().getZombies().size;
                Thread.sleep(3000);
            }
            zombiesRemaining = game.getMapManager().getZombies().size;

            winSound.play(Settings.sfxVolume);
            UIManager.pushBufferMessage("Prepare for the next wave!");
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 150; i++) {
                        game.getPlayer().healBy(.5f, false);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }.start();
            Thread.sleep(15000);

            UIManager.pushBufferMessage("Begin!");

            for (int i = 0; i < 10; i++) {
                zombiesRemaining = 2*(50 - i) + game.getMapManager().getZombies().size;
                Thread.sleep(2000);
                Zombie zombie2 = new ZombieNormal(game, getRandomSpawnPoint(spawnPoints));
                Zombie zombie3 = new ZombieTank(game, getRandomSpawnPoint(spawnPoints));
                game.getMapManager().addEntity(zombie2);
                game.getMapManager().addEntity(zombie3);
                lastSpawn = System.currentTimeMillis();
            }

            while (game.getMapManager().getZombies().size > 0) {
                zombiesRemaining = game.getMapManager().entities.size;
                Thread.sleep(3000);
            }
            zombiesRemaining = game.getMapManager().getZombies().size;

            winSound.play(Settings.sfxVolume);
            UIManager.pushBufferMessage("Prepare for the final wave!");
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 30; i++) {
                        game.getPlayer().healBy(.5f, false);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }.start();
            Thread.sleep(15000);
            UIManager.pushBufferMessage("Begin!");

            for (int i = 0; i < 10; i++) {
                zombiesRemaining = 3*(50 - i) + game.getMapManager().getZombies().size;
                Thread.sleep(2000);
                Zombie zombie = new ZombieNormal(game, getRandomSpawnPoint(spawnPoints));
                Zombie zombie2 = new ZombieRunner(game, getRandomSpawnPoint(spawnPoints));
                Zombie zombie3 = new ZombieTank(game, getRandomSpawnPoint(spawnPoints));
                game.getMapManager().addEntity(zombie);
                game.getMapManager().addEntity(zombie2);
                game.getMapManager().addEntity(zombie3);
                lastSpawn = System.currentTimeMillis();
            }

            while (game.getMapManager().getZombies().size > 0) {
                zombiesRemaining = game.getMapManager().getZombies().size;
                Thread.sleep(3000);
            }
            zombiesRemaining = game.getMapManager().getZombies().size;

            winSound.play(Settings.sfxVolume);
            UIManager.pushBufferMessage("Congratulations! You win!");
            game.getMapManager().clearMap();
            DataManager.save(game.getPlayer());
            game.getPlayer().heal();
            gameOver = true;


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    private Vector3 getRandomSpawnPoint(Vector3[] points) {
        int idx = new Random().nextInt(points.length);
        return points[idx].cpy();
    }

    private Vector2 getRandomSpawnPoint(Vector2[] points, Vector2 exclude) {
        int idx = new Random().nextInt(points.length);
        while (points[idx].equals(exclude)) idx = new Random().nextInt(points.length);
        return points[idx].cpy();
    }

}
