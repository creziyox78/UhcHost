package fr.lastril.uhchost.scenario;

import org.bukkit.Material;

import fr.lastril.uhchost.scenario.scenarios.BestPvE;
import fr.lastril.uhchost.scenario.scenarios.BleedingSweets;
import fr.lastril.uhchost.scenario.scenarios.Blitz;
import fr.lastril.uhchost.scenario.scenarios.BloodDiamond;
import fr.lastril.uhchost.scenario.scenarios.Bookception;
import fr.lastril.uhchost.scenario.scenarios.BowSwap;
import fr.lastril.uhchost.scenario.scenarios.CatEyes;
import fr.lastril.uhchost.scenario.scenarios.CutClean;
import fr.lastril.uhchost.scenario.scenarios.Diamondless;
import fr.lastril.uhchost.scenario.scenarios.DoubleOres;
import fr.lastril.uhchost.scenario.scenarios.FastMelting;
import fr.lastril.uhchost.scenario.scenarios.GoToHell;
import fr.lastril.uhchost.scenario.scenarios.Goldless;
import fr.lastril.uhchost.scenario.scenarios.GoneFishin;
import fr.lastril.uhchost.scenario.scenarios.HasteyBoy;
import fr.lastril.uhchost.scenario.scenarios.Horseless;
import fr.lastril.uhchost.scenario.scenarios.InfiniteEnchanter;
import fr.lastril.uhchost.scenario.scenarios.NoBow;
import fr.lastril.uhchost.scenario.scenarios.NoCleanUp;
import fr.lastril.uhchost.scenario.scenarios.NoEnchant;
import fr.lastril.uhchost.scenario.scenarios.NoFall;
import fr.lastril.uhchost.scenario.scenarios.NoFood;
import fr.lastril.uhchost.scenario.scenarios.OnlyOneWinner;
import fr.lastril.uhchost.scenario.scenarios.RKE;
import fr.lastril.uhchost.scenario.scenarios.RandomCraft;
import fr.lastril.uhchost.scenario.scenarios.RandomLoot;
import fr.lastril.uhchost.scenario.scenarios.Regen;
import fr.lastril.uhchost.scenario.scenarios.Rodless;
import fr.lastril.uhchost.scenario.scenarios.SkyHigh;
import fr.lastril.uhchost.scenario.scenarios.SuperHeroes;
import fr.lastril.uhchost.scenario.scenarios.Swordless;
import fr.lastril.uhchost.scenario.scenarios.Timber;
import fr.lastril.uhchost.scenario.scenarios.TimeBomb;
import fr.lastril.uhchost.scenario.scenarios.TripleOres;
import fr.lastril.uhchost.scenario.scenarios.VanillaPlus;

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
