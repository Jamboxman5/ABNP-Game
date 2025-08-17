package me.jamboxman5.abnpgame.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.jamboxman5.abnpgame.entity.mob.player.Player;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;
import me.jamboxman5.abnpgame.weapon.WeaponType;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class DataManager {

    static private final int shiftKey = 1;
    static final String dataPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/ABNPGame/data/";
    static final String playerDataFile = "playerData.abnp";
    static final String localPlayerPath =  dataPath + playerDataFile;

    public static Player loadLocalPlayer() {

        String jsonString = null;
        try {
            InputStream is = new FileInputStream(localPlayerPath);
            jsonString = shiftLoad(is);
            return loadPlayer(jsonString);
        } catch (IOException e) {
            return generateNewPlayerData();
        }



    }
    private static Player loadPlayer(String json) {
        JsonObject playerOBJ = JsonParser.parseString(json).getAsJsonObject();

        String name = playerOBJ.get("username").getAsString();
        int money = playerOBJ.get("money").getAsInt();
        int exp = playerOBJ.get("experience").getAsInt();
        String uuid = playerOBJ.get("uuid").getAsString();

        JsonObject weaponsOBJ = playerOBJ.get("weaponLoadout").getAsJsonObject();
        WeaponLoadout loadout = WeaponData.generateWeapons(weaponsOBJ);

        Player player = new Player(ABNPGame.getInstance(), name, uuid);
        player.setWeaponLoadout(loadout);
        player.setMoney(money);
        player.setExp(exp);
        return player;
    }
    @SuppressWarnings("NewApi")
    private static Player generateNewPlayerData() {
        try {
            Files.createDirectories(Paths.get(dataPath));
            InputStream defaultInput = Gdx.files.internal("data/player/defaultPlayerData.json/").read();
            assert defaultInput != null;
            String json = load(defaultInput);
            JsonObject playerOBJ = JsonParser.parseString(json).getAsJsonObject();
            playerOBJ.addProperty("uuid", UUID.randomUUID().toString());
            shiftSave(playerOBJ.toString(), localPlayerPath);
            return loadLocalPlayer();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void save(Player p) {
        JsonObject playerOBJ = new JsonObject();


        playerOBJ.addProperty("username", p.getUsername());
        playerOBJ.addProperty("uuid", p.getID());
        playerOBJ.addProperty("money", p.getMoney());
        playerOBJ.addProperty("experience", p.getExp());
        playerOBJ.add("weaponLoadout", WeaponData.convertToJson(p.getWeaponLoadout()));
        playerOBJ.add("purchasedWeapons", new JsonArray());

        shiftSave(playerOBJ.toString(), localPlayerPath);
//        try {
//            FileWriter writer = new FileWriter(localPlayerPath);
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            gson.toJson(playerOBJ, writer);
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    private static String shiftLoad(InputStream is) throws IOException {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        StringBuilder shiftedContents = new StringBuilder(result);
        for (int i = 0; i < shiftedContents.length(); i ++) {
            shiftedContents.setCharAt(i, (char) (shiftedContents.charAt(i) - shiftKey));
        }
        return shiftedContents.toString();
    }
    protected static String load(InputStream is) throws IOException {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    protected static void save(String jsonString, String path) throws IOException {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    private static void shiftSave(String jsonString, String path) {
        StringBuilder fileContents = new StringBuilder(jsonString);
        for (int i = 0; i < fileContents.length(); i ++) {
            fileContents.setCharAt(i, (char) (fileContents.charAt(i) + shiftKey));
        }
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(fileContents.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

}
