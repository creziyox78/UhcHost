package fr.lastril.uhchost.modes.bleach.gui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.KisukeUrahara;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.PotionItem;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class BenihimePlayerGui extends IQuickInventory {

    private final KisukeUrahara kisukeUrahara;
    private final PlayerManager playerManager, targetManager;

    public BenihimePlayerGui(PlayerManager targetManager, PlayerManager playerManager, KisukeUrahara kisukeUrahara) {
        super("Benihime", 9*1);
        this.kisukeUrahara = kisukeUrahara;
        this.playerManager = playerManager;
        this.targetManager = targetManager;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("benihime", taskUpdate -> {
            inv.setItem(new QuickItem(new PotionItem(PotionType.INSTANT_DAMAGE).toItemStack(1)).setName("§cRetirer 2 coeurs pendant 3 minutes").toItemStack(), onClick -> {
                Player target = targetManager.getPlayer();
                PlayerManager playerManager = UhcHost.getInstance().getPlayerManager(onClick.getPlayer().getUniqueId());
                if(target != null) {
                    target.setHealth(target.getHealth() - 2D*2D);
                    Bukkit.getScheduler().runTaskLater(UhcHost.getInstance(), () -> target.setHealth(target.getHealth() + 2D*2D), 3*60*20L);
                    onClick.getPlayer().closeInventory();
                    playerManager.setRoleCooldownBenihime(3*60);
                    kisukeUrahara.addBenehimeUsage();
                    onClick.getPlayer().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous venez de retirer 2 coeurs à §e" + target.getName() + "§7 pendant 3 minutes.");
                    target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous venez de perdre 2 coeurs à cause de §e" + onClick.getPlayer().getName() + "§7 pendant 3 minutes.");

                }
            },0);
            inv.setItem(new QuickItem(new PotionItem(PotionType.SLOWNESS).toItemStack(1)).setName("§7Infliger Slowness 2 pendant 20 secondes").toItemStack(), onClick -> {
                Player target = targetManager.getPlayer();
                if(target != null) {
                    if(target.hasPotionEffect(PotionEffectType.SLOW))
                        target.removePotionEffect(PotionEffectType.SLOW);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*20, 1, false, false));
                    onClick.getPlayer().closeInventory();
                    playerManager.setRoleCooldownBenihime(3*60);
                    kisukeUrahara.addBenehimeUsage();
                    onClick.getPlayer().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous avez infligé Slowness à §e" + target.getName() + "§7.");
                    target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous avez reçu Slowness par §e" + onClick.getPlayer().getName() + "§7.");
                }
            },1);
            inv.setItem(new QuickItem(new PotionItem(PotionType.INSTANT_HEAL).toItemStack(1)).setName("§dSoigner 7 coeurs instantanément").toItemStack(), onClick -> {
                Player target = targetManager.getPlayer();
                if(target != null) {
                    if(target.getHealth()  + 7D*2D > target.getMaxHealth()){
                        target.setHealth(target.getMaxHealth());
                    } else {
                        target.setHealth(target.getHealth() + 7D*2D);
                    }
                    onClick.getPlayer().closeInventory();
                    playerManager.setRoleCooldownBenihime(3*60);
                    kisukeUrahara.addBenehimeUsage();
                    onClick.getPlayer().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous venez de soigner §d7 coeurs §7de §d" + target.getName() + "§7.");
                    target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7" + onClick.getPlayer().getName() + " vous a soigné §d7 coeurs §7.");
                }
            },2);
            inv.setItem(new QuickItem(new PotionItem(PotionType.INSTANT_HEAL).toItemStack(1)).setName("§7Donne§c Force 1§7 pendant 1 minute 30 secondes").toItemStack(), onClick -> {
                Player target = targetManager.getPlayer();
                if(target != null) {
                    if(target.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                        target.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 90*20, 0, false, false));
                    onClick.getPlayer().closeInventory();
                    playerManager.setRoleCooldownBenihime(3*60);
                    kisukeUrahara.addBenehimeUsage();
                    onClick.getPlayer().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7" + target.getName() + "§7 a reçu l'effet §cForce§7 pendant §c1§7 minute §c30§7 secondes");
                    target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7" + onClick.getPlayer().getName() + "§7 vous a donné l'effet §cForce§7 pendant §c1§7 minute §c30§7 secondes");
                }
            },3);
            inv.setItem(new QuickItem(Material.NETHER_STAR).setName("§eInverser sa position avec le joueur ciblé").toItemStack(), onClick -> {
                Player target = targetManager.getPlayer();
                if(target != null) {
                    ClassUtils.switchPlayersLocation(onClick.getPlayer(), target);
                    onClick.getPlayer().closeInventory();
                    playerManager.setRoleCooldownBenihime(3*60);
                    kisukeUrahara.addBenehimeUsage();
                    onClick.getPlayer().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous avez inversé votre position avec §c" + target.getName());
                    target.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7" + onClick.getPlayer().getName() + "a inversé sa position avec vous.");
                }
            },4);

        });
    }
}
