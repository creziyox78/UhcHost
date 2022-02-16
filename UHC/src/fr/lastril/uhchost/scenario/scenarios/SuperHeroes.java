package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SuperHeroes extends Scenario {

	public SuperHeroes() {
		super("Super Heroes",
				Arrays.asList(I18n.tl("scenarios.superheroes.lore")),
				Material.INK_SACK, (byte) 4);
	}

	@EventHandler
	public void onGameStart(GameStartEvent event) {
		event.getPlayers().forEach(p -> {
			PositiveEffect e = PositiveEffect.getRandomEffect();
			for(PotionEffect potionEffect : e.getPotionEffect()){
				p.addPotionEffect(potionEffect);
			}
			p.sendMessage(I18n.tl("scenarios.superheroes.message", e.toString()));
		});
	}

	@EventHandler
	public void onDamageFall(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(player.hasPotionEffect(PotionEffectType.JUMP) && event.getCause() == EntityDamageEvent.DamageCause.FALL)
				event.setCancelled(true);
		}
	}

	public enum PositiveEffect {
		STRENGHT(Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false)), 1), RESISTANCE(Arrays.asList(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false)), 1),
		JUMPBOOST(Arrays.asList(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 3, false, false), new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false)), 1),
		SPEED(Arrays.asList(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false), new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, false, false)), 1),
		;

		private List<PotionEffect> potionEffect;

		private int rarity;

		PositiveEffect(List<PotionEffect> potionEffect, int rarity) {
			this.potionEffect = potionEffect;
			this.rarity = rarity;
		}

		public static PositiveEffect getRandomEffect() {
			List<PositiveEffect> effects = new ArrayList<>();
			for (PositiveEffect positiveEffect : values()) {
				for (int i = 0; i < positiveEffect.getRarity(); i++)
					effects.add(positiveEffect);
			}
			return effects.get((new Random()).nextInt(effects.size()));
		}

		public List<PotionEffect> getPotionEffect() {
			return potionEffect;
		}

		public void setPotionEffect(List<PotionEffect> potionEffect) {
			this.potionEffect = potionEffect;
		}

		public int getRarity() {
			return this.rarity;
		}

		public void setRarity(int rarity) {
			this.rarity = rarity;
		}
	}

}
