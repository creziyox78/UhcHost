package fr.lastril.uhchost.modes.bleach.roles.shinigamis;

import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.RolePacket;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.npc.NPC;
import fr.lastril.uhchost.tools.API.npc.NPCInteractEvent;
import fr.lastril.uhchost.tools.API.npc.NPCManager;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class KisukeUrahara extends Role implements ShinigamiRole, RoleListener, RolePacket {

    private final int REGENERATION_DELAY = 60;
    private long lastCombat;
    private int hitCount = 0;
    private final Map<NPC, Integer> npcIntegerMap = new HashMap<>();

    public KisukeUrahara() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        long delayLastCombat = System.currentTimeMillis() - this.lastCombat;
        if (delayLastCombat > REGENERATION_DELAY * 1000) {
            if(!player.hasPotionEffect(PotionEffectType.REGENERATION)) player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false));
        } else {
            for(PotionEffect effect : player.getActivePotionEffects()){
                if(effect.getType().getId() == 10){
                    if(effect.getAmplifier() < 1){
                        if(player.hasPotionEffect(PotionEffectType.REGENERATION)) player.removePotionEffect(PotionEffectType.REGENERATION);
                    }
                }
            }
        }
    }



    @EventHandler
    public void onPlayerInteractWithSword(PlayerInteractEvent event){
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        NPCManager npcManager = main.getNpcManager();
        Action action = event.getAction();
        Property skin = npcManager.getPlayerTextures(player);
        if(playerManager.hasRole() && playerManager.getRole() instanceof KisukeUrahara){
            if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK){
                if(bleachPlayerManager.canUsePower()){
                    if(event.getItem() != null && event.getItem().getType().name().contains("SWORD")){
                        if(playerManager.getRoleCooldownClone() <= 0){
                            NPC npc = npcManager.addNPC("clone", player.getDisplayName(), player.getLocation().clone(), skin, new NPCInteractEvent() {
                                @Override
                                public void onClick(Player player, NPC npc) {
                                    UhcHost.debug("Clicked on " + npc.getName());
                                    npc.damageNPC();
                                    npcIntegerMap.put(npc, npcIntegerMap.get(npc) + 1);
                                    if(npcIntegerMap.get(npc) == 3){
                                        npc.killNPC();
                                        npcManager.deleteNPC("clone");
                                        npcIntegerMap.remove(npc);
                                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous venez de tuer un clone de ce joueur.");
                                    }
                                }

                                @Override
                                public void onLeftClick(Player player, NPC npc) {

                                }

                                @Override
                                public void onRightClick(Player player, NPC npc) {

                                }
                            });
                            npc.itemInHandNPC(player.getInventory().getItemInHand());
                            npc.helmetNPC(player.getInventory().getHelmet());
                            npc.chestplateNPC(player.getInventory().getChestplate());
                            npc.leggingsNPC(player.getInventory().getLeggings());
                            npc.bootsNPC(player.getInventory().getBoots());
                            npc.sendNPC();

                            npcIntegerMap.put(npc, 0);
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                npcIntegerMap.remove(npc);
                                npcManager.deleteNPC("clone");
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVotre clone a disparu naturellement.");
                            }, 20*60*2);
                            playerManager.setRoleCooldownClone(60*3);

                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vous avez posés un clone de vous même");
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownClone()));
                        }
                    }
                }

            }
        }
    }

    @Override
    public void giveItems(Player player) {

    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    private net.minecraft.server.v1_8_R3.ItemStack getNmsItem(ItemStack item){
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagList ench = new NBTTagList();
        ench.add(new NBTTagString(Enchantment.DURABILITY.getName()));
        ench.add(new NBTTagInt(1));
        tag.set("ench", ench);
        nmsItem.setTag(tag);
        return nmsItem;
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Kisuke Urahara";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(event.getDamager() instanceof Player){
                Player damager = (Player) event.getDamager();
                PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
                if(damagerManager.hasRole() && damagerManager.getRole() instanceof KisukeUrahara){
                    KisukeUrahara kisukeUrahara = (KisukeUrahara) damagerManager.getRole();
                    kisukeUrahara.setLastCombat(System.currentTimeMillis());
                }
                if(playerManager.hasRole() && playerManager.getRole() instanceof KisukeUrahara){
                    KisukeUrahara kisukeUrahara = (KisukeUrahara) playerManager.getRole();
                    kisukeUrahara.setLastCombat(System.currentTimeMillis());
                }
            }
            if(event.getDamager() instanceof Projectile){
                Projectile projectile = (Projectile) event.getDamager();
                if(projectile.getShooter() instanceof Player){
                    if(playerManager.hasRole() && playerManager.getRole() instanceof KisukeUrahara){
                        KisukeUrahara kisukeUrahara = (KisukeUrahara) playerManager.getRole();
                        kisukeUrahara.setLastCombat(System.currentTimeMillis());
                    }
                }
            }
        }
    }

    public void setLastCombat(long lastCombat) {
        this.lastCombat = lastCombat;
    }
}
