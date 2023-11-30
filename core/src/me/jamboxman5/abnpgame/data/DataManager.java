package me.jamboxman5.abnpgame.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.jamboxman5.abnpgame.entity.player.Player;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

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

        JsonObject weaponsOBJ = playerOBJ.get("weaponLoadout").getAsJsonObject();
        JsonArray weaponsArr = weaponsOBJ.get("weapons").getAsJsonArray();

        Array<Weapon> weapons = new Array<>();
        for (int i = 0; i < weaponsArr.size(); i++) {
            JsonObject weaponOBJ = weaponsArr.get(i).getAsJsonObject();
            Weapon.WeaponType type = Weapon.WeaponType.valueOf(weaponOBJ.get("type").getAsString());
            Weapon weapon = Weapon.getByType(type);
            JsonArray modsArr = weaponOBJ.get("mods").getAsJsonArray();
            WeaponModLoadout loadout = new WeaponModLoadout();
            for (int j = 0; j < modsArr.size(); j++) {
                JsonObject modOBJ = modsArr.get(j).getAsJsonObject();
                WeaponMod.ModType modType = WeaponMod.ModType.valueOf(modOBJ.get("type").getAsString());
                loadout.addMod(WeaponMod.getByType(modType));
            }
            weapon.setMods(loadout);
            weapons.add(weapon);
        }
        WeaponLoadout loadout = new WeaponLoadout(weapons);
        Player player = new Player(ABNPGame.getInstance(), name);
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
            shiftSave(json, localPlayerPath);
            return loadLocalPlayer();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void save(Player p) {
        JsonObject playerOBJ = new JsonObject();
        JsonObject weaponLoadoutOBJ = new JsonObject();
        JsonArray weaponsArr = new JsonArray();

        for (Weapon weapon : p.getWeaponLoadout().getWeapons()) {
            JsonObject weaponOBJ = new JsonObject();
            weaponOBJ.addProperty("type", weapon.getType().toString());
            if (weapon instanceof Firearm) {
                Firearm firearm = (Firearm) weapon;
                weaponOBJ.addProperty("ammo", firearm.getAmmoType().toString());
            }
            JsonArray modsArr = new JsonArray();
            for (WeaponMod mod : weapon.getModLoadout().getMods()) {
                JsonObject modOBJ = new JsonObject();
                modOBJ.addProperty("type", mod.getType().toString());
                modsArr.add(modOBJ);
            }
            weaponOBJ.add("mods", modsArr);
            weaponsArr.add(weaponOBJ);
        }

        weaponLoadoutOBJ.add("weapons", weaponsArr);

        playerOBJ.addProperty("username", p.getUsername());
        playerOBJ.addProperty("money", 0);
        playerOBJ.addProperty("experience", 0);
        playerOBJ.add("weaponLoadout", weaponLoadoutOBJ);
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
    private static String load(InputStream is) throws IOException {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
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
