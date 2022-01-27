package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
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
			p.addPotionEffect(new PotionEffect(e.getPotionEffectType(), 999999, 1));
			p.sendMessage(I18n.tl("scenarios.superheroes.message", e.toString()));
		});
	}

	public enum PositiveEffect {
		STRENGHT(PotionEffectType.INCREASE_DAMAGE, 1), RESITANCE(PotionEffectType.DAMAGE_RESISTANCE, 2),
		JUMPBOOST(PotionEffectType.JUMP, 4), REGENERATION(PotionEffectType.REGENERATION, 1),
		SWIFTNESS(PotionEffectType.SPEED, 2), FIRERESISTANCE(PotionEffectType.FIRE_RESISTANCE, 2);

		private PotionEffectType potionEffectType;

		private int rarity;

		PositiveEffect(PotionEffectType potionEffectType, int rarity) {
			this.potionEffectType = potionEffectType;
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

		public PotionEffectType getPotionEffectType() {
			return this.potionEffectType;
		}

		public void setPotionEffectType(PotionEffectType potionEffectType) {
			this.potionEffectType = potionEffectType;
		}

		public int getRarity() {
			return this.rarity;
		}

		public void setRarity(int rarity) {
			this.rarity = rarity;
		}
	}

}
