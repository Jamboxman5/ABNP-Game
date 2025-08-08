package me.jamboxman5.abnpgame.event.entity;

import me.jamboxman5.abnpgame.entity.Entity;
import me.jamboxman5.abnpgame.event.Event;
import me.jamboxman5.abnpgame.net.packets.Packet02Move;

public class MoveEvent implements Event {

    Entity entity;

    public MoveEvent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void fire() {
        new Packet02Move(entity.getID(), entity.getWorldX(), entity.getWorldY(), entity.getRotation());
    }

}
