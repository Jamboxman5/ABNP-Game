package me.jamboxman5.legacyofgoku.map;

public class LegacyGateway {

    private LegacyMap map1;
    private LegacyMap map2;

    public LegacyGateway(LegacyMap map1, LegacyMap map2) {
        this.map1 = map1;
        this.map2 = map2;
    }

    public void teleport() {
        if (LegacyMapManager.getActiveMap().equals(map1)) LegacyMapManager.setActiveMap(map2);
        if (LegacyMapManager.getActiveMap().equals(map2)) LegacyMapManager.setActiveMap(map1);
    }

}
