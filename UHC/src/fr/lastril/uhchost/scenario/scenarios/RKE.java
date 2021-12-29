package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Random;

public class RKE extends Scenario {

	public RKE() {
		super("RandomKillEffect", Arrays.asList(I18n.tl("scenarios.rke.lore", ""), I18n.tl("scenarios.rke.lore1", "")),
				Material.POTION);
	}

	@EventHandler
	public void onKill(PlayerKillEvent e) {
		Player player = e.getKiller();
		setEffect(player);
	}

	public void setEffect(Player kill) {
		Random rand = new Random();
		int chance = rand.nextInt(13);
		if (chance == 0) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§b vitesse§a pendant 10 secondes !");
		} else if (chance == 1) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§c force§a pendant 10 secondes !");
		} else if (chance == 2) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet d'§7invisibilit§a pendant 10 secondes !");
		} else if (chance == 3) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§f résistance§a pendant 10 secondes !");
		} else if (chance == 4) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§0 cécit§a pendant 10 secondes !");
		} else if (chance == 5) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§1 poison§a pendant 10 secondes !");
		} else if (chance == 6) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§8 lenteur§a pendant 10 secondes !");
		} else if (chance == 7) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§e faiblesse§a pendant 10 secondes !");
		} else if (chance == 8) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§l naus�e§a pendant 10 secondes !");
		} else if (chance == 9) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§1§l saut§a pendant 10 secondes !");
		} else if (chance == 10) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de�f�l fatigue§a pendant 10 secondes !");
		} else if (chance == 11) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet d'§eabsorption§a pendant 10 secondes !");
		} else if (chance == 12) {
			kill.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 220, 0));
			kill.sendMessage("§7[§fR.K.E.§7]§a Tu viens de recevoir l'effet de§d régéneration§a pendant 10 secondes !");
		}
	}

}
