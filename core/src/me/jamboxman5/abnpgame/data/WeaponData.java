package me.jamboxman5.abnpgame.data;

import com.badlogic.gdx.utils.Array;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.jamboxman5.abnpgame.entity.projectile.ammo.Ammo;
import me.jamboxman5.abnpgame.weapon.Weapon;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;
import me.jamboxman5.abnpgame.weapon.WeaponType;
import me.jamboxman5.abnpgame.weapon.firearms.Firearm;
import me.jamboxman5.abnpgame.weapon.mods.WeaponMod;
import me.jamboxman5.abnpgame.weapon.mods.WeaponModLoadout;

public class WeaponData {

    public static WeaponLoadout generateWeapons(String json) {
        JsonObject weaponsOBJ = JsonParser.parseString(json).getAsJsonObject();
        return generateWeapons(weaponsOBJ);
    }

    public static WeaponLoadout generateWeapons(JsonObject weaponsOBJ) {
        JsonArray weaponsArr = weaponsOBJ.get("weapons").getAsJsonArray();

        WeaponLoadout loadout = new WeaponLoadout();

        JsonArray ammoArr = weaponsOBJ.get("ammo").getAsJsonArray();
        Array<Ammo> ammos = new Array<>();
        for (int i = 0; i < ammoArr.size(); i++) {
            JsonObject ammoOBJ = ammoArr.get(i).getAsJsonObject();
            ammos.add(generateAmmoFromJson(ammoOBJ));
        }

        loadout.setAmmos(ammos);

        Array<Weapon> ownedWeapons = new Array<>();
        Array<Weapon> equippedWeapons = new Array<>();
        for (int i = 0; i < weaponsArr.size(); i++) {
            JsonObject weaponOBJ = weaponsArr.get(i).getAsJsonObject();
            Weapon weapon = generateWeaponFromJson(weaponOBJ, loadout);
            ownedWeapons.add(weapon);
            if (weaponOBJ.get("equipped").getAsBoolean()) equippedWeapons.add(weapon);
        }

        loadout.setOwnedWeapons(ownedWeapons);
        loadout.setEquippedWeapons(equippedWeapons);

        return loadout;
    }

    protected static Ammo generateAmmoFromJson(JsonObject ammoOBJ) {
        Ammo ammo = Ammo.getByType(Ammo.AmmoType.valueOf(ammoOBJ.get("type").getAsString()));
        ammo.setCount(ammoOBJ.get("count").getAsInt());
        return ammo;
    }

    protected static Weapon generateWeaponFromJson(JsonObject weaponOBJ, WeaponLoadout loadout) {
        WeaponType type = WeaponType.valueOf(weaponOBJ.get("type").getAsString());
        Weapon weapon = Weapon.getByType(type);
        JsonArray modsArr = weaponOBJ.get("mods").getAsJsonArray();
        WeaponModLoadout modLoadout = new WeaponModLoadout();
        for (int j = 0; j < modsArr.size(); j++) {
            JsonObject modOBJ = modsArr.get(j).getAsJsonObject();
            WeaponMod.ModType modType = WeaponMod.ModType.valueOf(modOBJ.get("type").getAsString());
            modLoadout.addMod(WeaponMod.getByType(modType));
        }
        weapon.setMods(modLoadout);
        if (weapon instanceof Firearm) {
            Firearm arm = (Firearm) weapon;
            Ammo.AmmoType ammoType = Ammo.AmmoType.valueOf(weaponOBJ.get("ammoType").getAsString());
            arm.setAmmo(loadout.getAmmo(ammoType));
            arm.setLoadedAmmo(weaponOBJ.get("loaded").getAsInt());
        }
        return weapon;
    }

    protected static JsonObject convertToJson(WeaponLoadout loadout) {
        JsonObject weaponLoadoutOBJ = new JsonObject();
        JsonArray weaponsArr = new JsonArray();

        for (Weapon weapon : loadout.getEquippedWeapons()) {
            weaponsArr.add(convertToJson(weapon));
        }

        JsonArray ammoArray = new JsonArray();
        for (Ammo ammo : loadout.getAmmos()) {
            ammoArray.add(convertToJson(ammo));
        }

        weaponLoadoutOBJ.add("weapons", weaponsArr);
        weaponLoadoutOBJ.add("ammo", ammoArray);

        return weaponLoadoutOBJ;
    }

    protected static JsonObject convertToJson(Weapon weapon) {
        JsonObject weaponOBJ = new JsonObject();
        weaponOBJ.addProperty("type", weapon.getType().toString());
        if (weapon instanceof Firearm) {
            Firearm firearm = (Firearm) weapon;
            weaponOBJ.addProperty("loaded", firearm.getLoadedAmmo());
            weaponOBJ.addProperty("ammoType", firearm.getAmmoType().toString());
        }
        JsonArray modsArr = new JsonArray();
        for (WeaponMod mod : weapon.getModLoadout().getMods()) {
            JsonObject modOBJ = new JsonObject();
            modOBJ.addProperty("type", mod.getType().toString());
            modsArr.add(modOBJ);
        }
        weaponOBJ.add("mods", modsArr);
        return weaponOBJ;
    }

    protected static JsonObject convertToJson(Ammo ammo) {
        JsonObject ammoOBJ = new JsonObject();
        ammoOBJ.addProperty("type", ammo.getType().toString());
        ammoOBJ.addProperty("count", ammo.getAmmoCount());
        return ammoOBJ;
    }

}
