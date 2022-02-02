package fr.lastril.uhchost.modes.naruto.v2.roles.taka;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.JubiItem;
import fr.lastril.uhchost.modes.naruto.v2.items.swords.KubikiribochoSword;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Orochimaru;
import fr.lastril.uhchost.modes.naruto.v2.roles.zabuza.Zabuza;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Suigetsu extends Role implements NarutoV2Role, RoleListener {

	private boolean invisible = false, orochimaruDead, itachiDead;

	private int chance = 10;

	public Suigetsu() {
		super.addRoleToKnow(Orochimaru.class);
	}

	@Override
	public void checkRunnable(Player player) {
		super.checkRunnable(player);
		Block block = player.getLocation().getBlock();
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(joueur.hasRole() && joueur.isAlive()) {
			if(joueur.getRole() instanceof Suigetsu) {
				Suigetsu suigestu = (Suigetsu) joueur.getRole();
				if (block.isLiquid() || block.getLocation().add(0, 1, 0).getBlock().isLiquid()) {
					if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
						player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					if (player.hasPotionEffect(PotionEffectType.WATER_BREATHING))
						player.removePotionEffect(PotionEffectType.WATER_BREATHING);
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 1, false, false));
					player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20 * 5, 0, false, false));
					EntityEquipment equipement = player.getEquipment();
					if (equipement.getHelmet() == null && equipement.getChestplate() == null && equipement.getLeggings() == null
							&& equipement.getBoots() == null) {
						player.addPotionEffect(
								new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
						suigestu.setInvisible(true);
					} else {
						suigestu.setInvisible(false);
						if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
							player.removePotionEffect(PotionEffectType.INVISIBILITY);
					}

				} else {
					suigestu.setInvisible(false);
					if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
						player.removePotionEffect(PotionEffectType.INVISIBILITY);
					if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
						player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 5, 0, false, false));
				}
			}
		}
		
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if (joueur.hasRole()) {
				if (joueur.getRole() instanceof Suigetsu) {
					Suigetsu suigestu = (Suigetsu) joueur.getRole();
					if (!suigestu.isInvisible()) {
						UhcHost.debug("Not invisible Suigestu");
						int value = UhcHost.getRANDOM().nextInt(100);
						UhcHost.debug("Value Suegestu: " + value);
						if (value <= 10) {
							event.setCancelled(true);
						}
					} else {
						event.setCancelled(true);
					}

				}
			}
			if (event.getDamager() instanceof Player) {
				Player damager = (Player) event.getDamager();
				PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
				if (damagerJoueur.hasRole()) {
					if (damagerJoueur.getRole() instanceof Suigetsu) {
						Suigetsu suigestu = (Suigetsu) damagerJoueur.getRole();
						UhcHost.debug("Damager Suigestu");
						if (suigestu.isInvisible()) {
							UhcHost.debug("Invisible Suigestu");
							event.setCancelled(true);
							return;
						}
						int pourcentage = UhcHost.getRANDOM().nextInt(100);
						if(pourcentage <= chance){
							if(damager.getHealth() + event.getFinalDamage() >= damager.getMaxHealth())
								damager.setHealth(damager.getMaxHealth());
							else
								damager.setHealth(damager.getHealth() + event.getFinalDamage());
						}
					}
				}
			} else if (event.getDamager() instanceof Projectile) {
				if (joueur.hasRole()) {
					if (joueur.getRole() instanceof Suigetsu) {
						Suigetsu suigestu = (Suigetsu) joueur.getRole();
						if(suigestu.isInvisible()) {
							event.setCancelled(true);
						}
					}
				}

			}

		}

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerShoot(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUniqueId());
			if (joueur.getRole() instanceof Suigetsu) {
				event.setCancelled(invisible);
			}
		}
	}

	@EventHandler
	public void Craft(CraftItemEvent event) {
		ItemStack result = event.getRecipe().getResult();
		if(result.getType() == Material.NETHER_STAR){
			if(result.isSimilar(new JubiItem(main).toItemStack())){
				if (super.getPlayer() != null) {
					Player jugoPlayer = super.getPlayer();
					main.getPlayerManager(jugoPlayer.getUniqueId()).setCamps(Camps.SHINOBI);
					jugoPlayer.sendMessage("§5Jûbi§e vient d'être invoqué. Vous gagnez désormais avec les§a Shinobis.");
				}
			}
		}
	}

	@Override
	public void onPlayerDeath(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(main.getGamemanager().getModes() != Modes.NARUTO) return;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		if (joueur.hasRole()) {
			if(joueur.getRole() instanceof Zabuza){
				if(super.getPlayer() != null){
					Player suigetsu = super.getPlayer();
					suigetsu.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§3Zabuza§e vient de mourir. Vous recevez donc l'épée§3 Kubikiribôchô§e ainsi que de ses propriétés.");
					main.getInventoryUtils().giveItemSafely(suigetsu, new KubikiribochoSword().toItemStack());
				}
			}
			if (joueur.getRole() instanceof Orochimaru) {
				if (super.getPlayer() != null) {
					if(itachiDead){
						Player sasukePlayer = super.getPlayer();
						itachiDead = true;
						main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
						sasukePlayer.sendMessage("§cVous gagnez avec l'§cAkatsuki car Itachi et Orochimaru sont morts. Voici l'identité de Nagato et de Sasuke: ");
						for (PlayerManager nagato : narutoV2Manager.getPlayerManagersWithRole(Nagato.class)) {
							sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
						}
						for (PlayerManager sasuke :narutoV2Manager.getPlayerManagersWithRole(Sasuke.class)) {
							sasukePlayer.sendMessage("§6 - Sasuke : " + sasuke.getPlayerName());
						}
					} else {
						orochimaruDead = true;
						Player sasukePlayer = super.getPlayer();
						main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.TAKA);
						sasukePlayer.sendMessage("§5Orochimaru§e vient de mourir, vous gagnez uniquement avec votre camp. Voici l'identité de Sasuke: ");
						for (PlayerManager sasuke : narutoV2Manager.getPlayerManagersWithRole(Sasuke.class)) {
							sasukePlayer.sendMessage("§6 - Sasuke : " + sasuke.getPlayerName());
						}
					}

				}
			}else if (joueur.getRole() instanceof Itachi) {
				if (super.getPlayer() != null) {
					if (orochimaruDead) {
						Player sasukePlayer = super.getPlayer();
						itachiDead = true;
						main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
						sasukePlayer.sendMessage("§cItachi§e vient de mourir, vous gagnez avec l'§cAkatsuki. Voici l'identité de Nagato: ");
						for (PlayerManager nagato : narutoV2Manager.getPlayerManagersWithRole(Nagato.class)) {
							sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
						}
					} else {
						itachiDead = true;
					}
				}
			}

		}
	}

	@Override
	public void giveItems(Player player) {
		Livre livre = new Livre(Enchantment.DEPTH_STRIDER, 3);
		main.getInventoryUtils().giveItemSafely(player, livre.toItemStack());
	}

	@Override
	protected void onNight(Player player) {

	}

	@Override
	protected void onDay(Player player) {

	}

	@Override
	public void onNewEpisode(Player player) {

	}

	@Override
	public void onNewDay(Player player) {

	}

	@Override
	public QuickItem getItem() {
		return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgzODgxZDMzYWZmYTliZTZlMTAwODJjOTE4NTJmMDViMWQ2MjgzMTU1ODI2ODgyNmY2OWYzNzE5YTNjODQ3MSJ9fX0=")
				.setName(getCamp().getCompoColor() + getRoleName());
	}

	@Override
	public Camps getCamp() {
		return Camps.TAKA;
	}

	@Override
	public String getRoleName() {
		return "Suigetsu";
	}

	@Override
	public String getDescription() {
		return "\n§7Son but est de gagner avec le camp de §6Taka§7.\n" +
				" \n " +
				"§7§lItems :\n" +
				" \n " +
				"§7• Il dispose d’un livre Depth Strider 3. \n" +
				" \n " +
				"§7§lParticularités :\n" +
				" \n " +
				"§7• Il dispose de l’effet §9Résistance 1§7 permanent.\n" +
				" \n " +
				"§7• Il dispose de 10% de chance qu’un coup ne lui mette pas de dégâts.\n" +
				" \n " +
				"§7• Dans l’eau, il dispose de l’effet §9Résistance 2§7, il peut respirer sous l’eau et s’il enlève son armure, il peut se mettre en invisible et une fois invisible, il ne peut recevoir de dégâts venant d’un joueur et il ne peut infliger de dégâts.\n" +
				" \n " +
				"§7• Son camp se retrouvera souvent allié avec un autre camp, c’est pour cela qu’au début de la partie, son camp fait partie du camp d’§5Orochimaru§7. Si §5Orochimaru§7 vient à mourir alors son camp ne sera plus allié avec personne. Si §cItachi§7 vient à mourir (après la mort d’§5Orochimaru§7), son camp s’alliera avec l’§cAkatsuki§7 et donc il connaîtra l’identité de §cNagato§7. Si §dTobi§7 et §dMadara§7 rassemble les 9 biju pour former §6Jûbi§7, il s’alliera au camp §aShinobi§7.\n" +
				" \n " +
				"§7• Il connaît l’identité d’§5Orochimaru§7.\n" +
				" \n " +
				"§7• A la mort d’§5Orochimaru§7, il obtient l’identité de §6Sasuke§7.\n" +
				" \n " +
				"§7• A la mort de §bZabuza§7, il obtient l’épée de celui-ci nommée “§8Kubikiribôchô§7” (voir rôle Zabuza sur le doc).\n" +
				" \n " +
				"§7• Il dispose de la nature de Chakra : §bSuiton";
	}

	@Override
	public void onPlayerUsedPower(PlayerManager joueur) {

	}

	@Override
	public Chakra getChakra() {
		return Chakra.SUITON;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}
	
	@Override
	public void afterRoles(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		joueur.setCamps(Camps.OROCHIMARU);
	}
}
