package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ByakuganItem;
import fr.lastril.uhchost.modes.naruto.v2.items.HakkeItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Neji extends Role implements NarutoV2Role {
	
	private final int DISTANCE = 20;

	private boolean hinataDeath;

	private boolean vanished = false;
	
	public Neji() {
		super.addRoleToKnow(Hinata.class);
	}
	
    @Override
    public void giveItems(Player player) {
    	main.getInventoryUtils().giveItemSafely(player, new ByakuganItem().toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new HakkeItem().toItemStack());
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
	public void checkRunnable(Player player) {
		super.checkRunnable(player);
		if(hinataDeath){
			if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
				player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*4, 0, false, false));
		}
	}

	@Override
	public void onPlayerDeath(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		if (joueur.hasRole()) {
			if(joueur.getRole() instanceof Hinata){
				for(PlayerManager joueurs : narutoV2Manager.getPlayerManagersWithRole(Neji.class)){
					if(joueurs.getPlayer() != null){
						Player neji = joueurs.getPlayer();
						if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
							player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						}
						neji.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
						neji.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aHinata§3 vient de mourir, vous avez§c Force I§3 de façon permanent.");
						hinataDeath = true;
					}
				}
			}
		}
	}
    
    @Override
	public QuickItem getItem() {
		return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
				.setTexture(
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThjM2RiZWI3NDVmMTczZjQ1YjNlM2MzOWU1MzExYzMwMDBiODUxNTgwZGE0OWVjNDFkYzZiN2M2ZDZlMzNiIn19fQ==");
	}

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Neji";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }
    
    @Override
	public void afterRoles(Player player) {
		/*if (!UhcHost.getInstance().getConfiguration().containsRoleInComposition(Hinata.class)) {
			player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aHinata§e ne fait pas partie de la composition. Vous venez de recevoir votre effet.");
			for (Joueur targetJoueur : UhcHost.getInstance().getNarutoManager().getJoueursWithRole(Neji.class)) {
				Neji neji = (Neji) targetJoueur.getRole();
				neji.getEffects().clear();
				neji.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false),
						When.START);
				if (targetJoueur.getPlayer() != null) {
					Player target = targetJoueur.getPlayer();
					if (!target.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
						target.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
					}
				}
			}
		}*/
	}

    @Override
	public void onPlayerUsedPower(PlayerManager joueur) {
		Player player = super.getPlayer();
		Player target = joueur.getPlayer();
		if (player != null && target != null) {
			if (player.getWorld() == target.getWorld()) {
				double distance = player.getLocation().distance(target.getLocation());
				if (distance <= DISTANCE) {
					player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§a" + joueur.getRole().getRoleName()
							+ " vient d'utiliser une technique !");
				}
			}
		}
	}

	@Override
	public Chakra getChakra() {
		return Chakra.DOTON;
	}

	public boolean isVanished() {
		return vanished;
	}

	public void setVanished(boolean vanished) {
		this.vanished = vanished;
	}
}
