package fr.lastril.uhchost.scenario;

import fr.lastril.uhchost.scenario.scenarios.*;
import org.bukkit.Material;

public enum Scenarios {
	
	BESTPVE(new BestPvE()),
	BLEEDINGSWEETS(new BleedingSweets()),
	BLITZ(new Blitz()),
	BLOODIAMOND(new BloodDiamond()),
	BOOKCEPTION(new Bookception()),
	BOWSWAP(new BowSwap()),
	CATEEYES(new CatEyes()),
	CUTCLEAN(new CutClean()),
	DIAMONDLESS(new Diamondless()),
	DIAMONDLIMIT(new DiamondLimit()),
	DOUBLEORES(new DoubleOres()),
	FASTMELTING(new FastMelting()),
	GOLDLESS(new Goldless()),
	GONEFISHIN(new GoneFishin()),
	GOTOHELL(new GoToHell()),
	HASTEYBOY(new HasteyBoy()),
	HORSELESS(new Horseless()),
	INFINITEENCHANTER(new InfiniteEnchanter()),
	NOBOW(new NoBow()),
	NOCLEANUP(new NoCleanUp()),
	NOENCHANT(new NoEnchant()),
	NOFALL(new NoFall()),
	NOFOOD(new NoFood()),
	ONLYONEWINNER(new OnlyOneWinner()),
	RANDOMCRAFT(new RandomCraft()),
	RANDOMLOOT(new RandomLoot()),
	REGEN(new Regen()),
	RKE(new RKE()),
	RODLESS(new Rodless()),
	SKYHIGH(new SkyHigh()),
	SUPERHEROES(new SuperHeroes()),
	SWORDLESS(new Swordless()),
	TIMBER(new Timber()),
	TIMEBOMB(new TimeBomb()),
	TRIPLEORES(new TripleOres()),
	;

	private Scenario scenario;

	Scenarios(Scenario scenario) {
		this.scenario = scenario;
	}

	public Scenario getScenario() {
		return this.scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public static Scenarios getByMaterial(Material type) {
		for (Scenarios scenarios : values()) {
			if (scenarios.getScenario().getType() == type)
				return scenarios;
		}
		return null;
	}

	public static Scenarios getByName(String name) {
		for (Scenarios scenarios : values()) {
			if (scenarios.getScenario().getName().equalsIgnoreCase(name))
				return scenarios;
		}
		return null;
	}

}
