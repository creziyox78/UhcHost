package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.solo.LoupGarouBlanc;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class VilainPetitLoup extends Role implements LGRole {

	private final List<PlayerManager> loupGarouList = new ArrayList<>();

	public VilainPetitLoup() {
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
		super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60*1, 0, false, false), When.AT_KILL);
	}

	@Override
	public String getSkullValue() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGViNDFlMzA5ZDgyOTUyZjVmNGE5MDhiZjE2OGU4NDhjODE3NmU1ZjA0ODdlNTc2YmMzMjE5ZWQwNjQ0ZTZlNyJ9fX0=";
	}

	@Override
	public String getRoleName() {
		return "Vilain Petit Loup";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void giveItems(Player player) {}

	@Override
	public void onNight(Player player) {}

	@Override
	public void onDay(Player player) {}

	@Override
	public void onNewEpisode(Player player) {

	}

	@Override
	public void afterRoles(Player player) {
		player.sendMessage(sendList());
	}

	@Override
	public String sendList() {
		String list = Messages.LOUP_GAROU_PREFIX.getPrefix() + "Voici la liste entière des Loups-Garous : \n";
		for (PlayerManager joueur : main.gameManager.getLoupGarouManager().getJoueursWithCamps(Camps.LOUP_GAROU)) {
			loupGarouList.add(joueur);
		}
		for(PlayerManager joueur : main.gameManager.getLoupGarouManager().getJoueursWithRole(LoupGarouBlanc.class)){
			loupGarouList.add(joueur);
		}
		int numberOfElements = loupGarouList.size();
		for (int i = 0; i < numberOfElements; i++) {
			int index = UhcHost.getRANDOM().nextInt(loupGarouList.size());
			list += "§c- " + loupGarouList.get(index).getPlayerName() + "\n";
			loupGarouList.remove(index);
		}
		return list;
	}

	@Override
	public void onNewDay(Player player) {}

	@Override
	public void checkRunnable(Player player) {}

	@Override
	public ItemsCreator getItem() {
		return null;
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§4"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0=");
	}

	@Override
	public Camps getCamp() {
		return Camps.LOUP_GAROU;
	}

}
