package fr.lastril.uhchost.tools.API.inventory.crafter;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class TaskUpdate extends BukkitRunnable {

    private Consumer<TaskUpdate> consumer;
 
    public TaskUpdate(Consumer<TaskUpdate> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        consumer.accept(this);
    }
}
