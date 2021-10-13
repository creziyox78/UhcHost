package fr.lastril.uhchost.scenario;

import fr.lastril.uhchost.scenario.scenarios.*;
import org.bukkit.Material;

public enum Scenarios {
	
	BESTPVE((Scenario) new BestPvE()),
	BLEEDINGSWEETS((Scenario) new BleedingSweets()),
	BLITZ((Scenario) new Blitz()),
	BLOODIAMOND((Scenario) new BloodDiamond()),
	BOOKCEPTION((Scenario) new Bookception()),
	BOWSWAP((Scenario) new BowSwap()),
	CATEEYES((Scenario) new CatEyes()),
	CUTCLEAN((Scenario) new CutClean()),
	DIAMONDLESS((Scenario) new Diamondless()),
	DIAMONDLIMIT((Scenario) new DiamondLimit()),
	DOUBLEORES((Scenario) new DoubleOres()),
	FASTMELTING((Scenario) new FastMelting()),
	GOLDLESS((Scenario) new Goldless()),
	GONEFISHIN((Scenario) new GoneFishin()),
	GOTOHELL((Scenario) new GoToHell()),
	HASTEYBOY((Scenario) new HasteyBoy()),
	HORSELESS((Scenario) new Horseless()),
	INFINITEENCHANTER((Scenario) new InfiniteEnchanter()),
	NOBOW((Scenario) new NoBow()),
	NOCLEANUP((Scenario) new NoCleanUp()),
	NOENCHANT((Scenario) new NoEnchant()),
	NOFALL((Scenario) new NoFall()),
	NOFOOD((Scenario) new NoFood()),
	ONLYONEWINNER((Scenario) new OnlyOneWinner()),
	RANDOMCRAFT((Scenario) new RandomCraft()),
	RANDOMLOOT((Scenario) new RandomLoot()),
	REGEN((Scenario) new Regen()),
	RKE((Scenario) new RKE()),
	RODLESS((Scenario) new Rodless()),
	SKYHIGH((Scenario) new SkyHigh()),
	SUPERHEROES((Scenario) new SuperHeroes()),
	SWORDLESS((Scenario) new Swordless()),
	TIMBER((Scenario) new Timber()),
	TIMEBOMB((Scenario) new TimeBomb()),
	TRIPLEORES((Scenario) new TripleOres()),
	VANILLAPLUS((Scenario) new VanillaPlus()),
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
