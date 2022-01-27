package fr.lastril.uhchost.modes.roles;


import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.WorldState;
import fr.lastril.uhchost.game.tasks.TaskManager;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.function.Consumer;

public abstract class Role implements RoleDescription {

    protected final UhcHost main = UhcHost.getInstance();
    private final Map<PotionEffect, When> effects;
    private final Map<Integer, Consumer<Player>> timeEvent;
    private final List<Class<? extends Role>> roleToKnow;
    private final TaskManager taskManager = new TaskManager(main);
    public UUID player;

    public Role() {
        this.effects = new HashMap<>();
        this.timeEvent = new HashMap<>();
        this.roleToKnow = new ArrayList<>();
    }

    public Role addEffect(PotionEffect key) {
        return this.addEffect(key, When.START);
    }

    public Role addEffect(PotionEffect key, When value) {
        effects.put(key, value);
        return this;
    }

    public Role addTimeEvent(int time, Consumer<Player> event) {
        this.timeEvent.put(time, event);
        return this;
    }

    public Role addRoleToKnow(Class<? extends Role>... roles) {
        roleToKnow.addAll(Arrays.asList(roles));
        return this;
    }

    public Role addRoleToKnow(Class<? extends Role> role) {
        roleToKnow.add(role);
        return this;
    }

    protected void givePermanentEffects(Player player) {
        effects.entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> player.addPotionEffect(e.getKey()));
    }

    public void stuff(Player player) {
        this.player = player.getUniqueId();
        this.giveItems(player);
        if(WorldState.isWorldState(WorldState.JOUR)){
            day(player);
        } else {
            night(player);
        }
        this.givePermanentEffects(player);
    }

    public void day(Player player) {
        effects.entrySet().stream().filter(e -> e.getValue() == When.NIGHT).forEach(e -> player.removePotionEffect(e.getKey().getType()));
        effects.entrySet().stream().filter(e -> e.getValue() == When.DAY).forEach(e -> player.addPotionEffect(e.getKey()));
        this.onDay(player);
    }

    public void night(Player player) {
        effects.entrySet().stream().filter(e -> e.getValue() == When.DAY).forEach(e -> player.removePotionEffect(e.getKey().getType()));
        effects.entrySet().stream().filter(e -> e.getValue() == When.NIGHT).forEach(e -> player.addPotionEffect(e.getKey()));
        this.onNight(player);
    }

    public void sendDescription(Player player) {
        /*player.sendMessage("§7§m------------------------------------------");
        player.sendMessage("§7Vous êtes " + this.getCamp().getCompoColor() + "" + this.getRoleName() + "§7.");
        player.sendMessage("§7§m------------------------------------------");*/
        player.sendMessage(this.getDescription() != null ? "§7" + this.getDescription() : "§c§lDESCRIPTION NULL");
        if (!this.getRoleToKnow().isEmpty()) {
            for (Class<? extends Role> roleToKnow : this.getRoleToKnow()) {
				if(!UhcHost.getInstance().gameManager.getModes().getMode().getModeManager().getPlayerManagersWithRole(roleToKnow).isEmpty()) {
					try {
						player.sendMessage( "§eLe(s) joueur(s) possédant le rôle §6" + roleToKnow.newInstance().getRoleName() + "§e :");
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
					for (PlayerManager PlayerManagerHasRole : UhcHost.getInstance().gameManager.getModes().getMode().getModeManager().getPlayerManagersWithRole(roleToKnow)) {
						player.sendMessage("§6- " + PlayerManagerHasRole.getPlayerName());
					}
				}
            }
        }
        if (sendList() != null) {
            player.sendMessage(sendList());
        }
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 10);
    }

    public void onKill(OfflinePlayer player, Player killer) {
        effects.entrySet().stream().filter(e -> e.getValue() == When.AT_KILL).forEach(e -> killer.addPotionEffect(e.getKey()));
    }

    public void onDamage(Player damager, Player target) {
    }

    public void onPlayerDeath(Player player) {
    }



    public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
    }

    public void afterRoles(Player player) {
    }

    public void onRessurect(Player ressurecter) {
    }

    /* PUBLIC FOR REGIVE ITEMS */
    public abstract void giveItems(Player player);

    protected abstract void onNight(Player player);

    protected abstract void onDay(Player player);

    public abstract void onNewEpisode(Player player);

    public abstract void onNewDay(Player player);

    public void checkRunnable(Player player) {
        //UhcHost.getInstance().getPlayerManager(player.getUniqueId()).removeCooldowns();
        int timer = taskManager.getCount();
        this.timeEvent.entrySet().stream().filter(e -> e.getKey() == timer).forEach(e -> e.getValue().accept(player));
    }

    public abstract QuickItem getItem();

    public abstract Camps getCamp();

    public UUID getPlayerId() {
        return this.player;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.player);
    }

    public Map<PotionEffect, When> getEffects() {
        return this.effects;
    }

    public List<Class<? extends Role>> getRoleToKnow() {
        return this.roleToKnow;
    }

    public String sendList() {
        return null;
    }

    public void setPlayerID(UUID uniqueId) {
        this.player = uniqueId;
    }
}
