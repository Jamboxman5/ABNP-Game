package me.jamboxman5.abnpgame.script;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import me.jamboxman5.abnpgame.data.DataManager;
import me.jamboxman5.abnpgame.entity.zombie.Zombie;
import me.jamboxman5.abnpgame.entity.zombie.ZombieNormal;
import me.jamboxman5.abnpgame.entity.zombie.ZombieRunner;
import me.jamboxman5.abnpgame.entity.zombie.ZombieTank;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.managers.UIManager;

public class BasicScript extends MissionScript {



    @Override
    public void run() {
        try {
            Sound winSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx/menu/Win.wav"));

            final ABNPGame game = ABNPGame.getInstance();

            gameOver = false;

            Vector2[] spawnPoints = game.getMapManager().getActiveMap().getZombieSpawns();
            UIManager.pushBufferMessage("Prepare for the first wave!");
            Thread.sleep(10000);
            UIManager.pushBufferMessage("Begin!");

            for (int i = 0; i < 10; i++) {
                zombiesRemaining = (50 - i) + game.getMapManager().entities.size;
                Thread.sleep(2000);
                Zombie zombie = new ZombieTank(game, spawnPoints[spawnCounter]);
                game.getMapManager().addEntity(zombie);
                lastSpawn = System.currentTimeMillis();
                spawnCounter++;
                if (spawnCounter >= spawnPoints.length) spawnCounter = 0;
            }

            while (game.getMapManager().entities.size > 0) {
                zombiesRemaining = game.getMapManager().entities.size;
                Thread.sleep(3000);
            }
            zombiesRemaining = game.getMapManager().entities.size;

            winSound.play();
            UIManager.pushBufferMessage("Prepare for the next wave!");
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 150; i++) {
                        game.getPlayer().healBy(.1f, false);
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
                zombiesRemaining = 2*(50 - i) + game.getMapManager().entities.size;
                Thread.sleep(2000);
                Zombie zombie2 = new ZombieNormal(game, spawnPoints[spawnCounter].cpy().add(new Vector2(40,40)));
                Zombie zombie3 = new ZombieTank(game, spawnPoints[spawnCounter].cpy().add(new Vector2(-40,-40)));
                game.getMapManager().addEntity(zombie2);
                game.getMapManager().addEntity(zombie3);
                lastSpawn = System.currentTimeMillis();
                spawnCounter++;
                if (spawnCounter >= spawnPoints.length) spawnCounter = 0;
            }

            while (game.getMapManager().entities.size > 0) {
                zombiesRemaining = game.getMapManager().entities.size;
                Thread.sleep(3000);
            }
            zombiesRemaining = game.getMapManager().entities.size;

            winSound.play();
            UIManager.pushBufferMessage("Prepare for the final wave!");
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 30; i++) {
                        game.getPlayer().healBy(.5, false);
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
                zombiesRemaining = 3*(50 - i) + game.getMapManager().entities.size;
                Thread.sleep(2000);
                Zombie zombie = new ZombieNormal(game, spawnPoints[spawnCounter]);
                Zombie zombie2 = new ZombieRunner(game, spawnPoints[spawnCounter].cpy().add(new Vector2(60,60)));
                Zombie zombie3 = new ZombieTank(game, spawnPoints[spawnCounter].cpy().add(new Vector2(-60,-60)));                        game.getMapManager().addEntity(zombie);
                game.getMapManager().addEntity(zombie);
                game.getMapManager().addEntity(zombie2);
                game.getMapManager().addEntity(zombie3);
                lastSpawn = System.currentTimeMillis();
                spawnCounter++;
                if (spawnCounter >= spawnPoints.length) spawnCounter = 0;
            }

            while (game.getMapManager().entities.size > 0) {
                zombiesRemaining = game.getMapManager().entities.size;
                Thread.sleep(3000);
            }
            zombiesRemaining = game.getMapManager().entities.size;

            winSound.play();
            UIManager.pushBufferMessage("Congratulations! You win!");
            game.getMapManager().clearMap();
            DataManager.save(game.getPlayer());
            gameOver = true;


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
