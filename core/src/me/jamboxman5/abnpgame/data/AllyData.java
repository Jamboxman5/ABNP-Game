package me.jamboxman5.abnpgame.data;

import com.badlogic.gdx.math.Vector2;
import com.google.gson.JsonObject;
import me.jamboxman5.abnpgame.entity.mob.npc.Ally;
import me.jamboxman5.abnpgame.entity.mob.player.Player;
import me.jamboxman5.abnpgame.main.ABNPGame;
import me.jamboxman5.abnpgame.weapon.WeaponLoadout;

public class AllyData {

    public static Ally loadFromJson(JsonObject allyOBJ) {
        String name = allyOBJ.get("name").getAsString();
        int exp = allyOBJ.get("experience").getAsInt();
        Ally ally = new Ally(ABNPGame.getInstance(), name);
        WeaponLoadout allyWeapons = WeaponData.generateWeapons(allyOBJ.get("weaponLoadout").getAsJsonObject());
        ally.setWeaponLoadout(allyWeapons);
        ally.setExp(exp);
        return ally;
    }

    public static JsonObject convertToJson(Ally ally) {
        JsonObject allyOBJ = new JsonObject();
        allyOBJ.addProperty("name", ally.getName());
        allyOBJ.addProperty("experience", ally.getExp());
        allyOBJ.add("weaponLoadout", WeaponData.convertToJson(ally.getWeaponLoadout()));
        return allyOBJ;
    }


}
