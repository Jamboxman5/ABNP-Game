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

        Array<Weapon> weapons = new Array<>();

        for (int i = 0; i < weaponsArr.size(); i++) {
            JsonObject weaponOBJ = weaponsArr.get(i).getAsJsonObject();
            WeaponType type = WeaponType.valueOf(weaponOBJ.get("type").getAsString());
            Weapon weapon = Weapon.getByType(type);
            JsonArray modsArr = weaponOBJ.get("mods").getAsJsonArray();
            WeaponModLoadout loadout = new WeaponModLoadout();
            for (int j = 0; j < modsArr.size(); j++) {
                JsonObject modOBJ = modsArr.get(j).getAsJsonObject();
                WeaponMod.ModType modType = WeaponMod.ModType.valueOf(modOBJ.get("type").getAsString());
                loadout.addMod(WeaponMod.getByType(modType));
            }
            weapon.setMods(loadout);
            if (weapon instanceof Firearm) {
                Firearm arm = (Firearm) weapon;
                arm.setLoadedAmmo(weaponOBJ.get("loaded").getAsInt());
            }
            weapons.add(weapon);
        }

        JsonArray ammoArr = weaponsOBJ.get("ammo").getAsJsonArray();

        Array<Ammo> ammos = new Array<>();

        for (int i = 0; i < ammoArr.size(); i++) {
            JsonObject ammoOBJ = ammoArr.get(i).getAsJsonObject();
            Ammo ammo = Ammo.getByType(Ammo.AmmoType.valueOf(ammoOBJ.get("type").getAsString()));
            ammo.setCount(ammoOBJ.get("count").getAsInt());
            ammos.add(ammo);
        }
        return new WeaponLoadout(weapons, ammos);
    }

}
