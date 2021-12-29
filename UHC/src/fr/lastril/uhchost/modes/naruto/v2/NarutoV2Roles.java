package fr.lastril.uhchost.modes.naruto.v2;

import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.*;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Danzo;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Jugo;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Karin;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Suigetsu;
import fr.lastril.uhchost.modes.naruto.v2.roles.zabuza.Haku;
import fr.lastril.uhchost.modes.naruto.v2.roles.zabuza.Zabuza;
import fr.lastril.uhchost.modes.roles.Role;

public enum NarutoV2Roles {

	/**
	 * SHINOBIS
	 */
	NARUTO(Naruto.class), //WORKER : Maygo FINISHED
	SAKURA(Sakura.class), //WORKER : Maygo FINISHED
	SAI(Sai.class), //WORKER : Maygo FINISHED
	MINATO(Minato.class), //WORKER : Maygo FINISHED
	KAKASHI(Kakashi.class), //WORKER : Maygo FINISHED
	JIRAYA(Jiraya.class), //WORKER : Lastril FINISHED
	TSUNADE(Tsunade.class), //WORKER : Lastril FINISHED
	ASUMA(Asuma.class), //WORKER : Maygo FINISHED
	GAI_MAITO(GaiMaito.class), //WORKER : Lastril FINISHED
	ROCK_LEE(RockLee.class), //WORKER : Lastril FINISHED
	TEN_TEN(TenTen.class), //WORKER : Lastril FINISHED
	NEJI(Neji.class), //WORKER : Lastril FINISHED
	HINATA(Hinata.class), //WORKER : Lastril FINISHED
	KIBA(Kiba.class), //WORKER : Lastril FINISHED
	SHINO(Shino.class), //WORKER : Maygo FINISHED
	INO(Ino.class), //WORKER : Maygo FINISHED
	CHOJI(Choji.class), //WORKER : Lastril FINISHED
	SHIKAMARU(Shikamaru.class), //WORKER : Maygo FINISHED
	TEMARI(Temari.class), //WORKER : Maygo FINISHED
	YONDAIME_RAIKAGE(YondameRaikage.class), //WORKER : Lastril FINISHED
	KILLER_BEE(KillerBee.class), //WORKER : Lastril FINISHED
	KONOHAMARU(Konohamaru.class), //WORKER : Lastril FINISHED

	/**
	 * AKATSUKI
	 */
	DEIDARA(Deidara.class), //WORKER : Maygo FINISHED
	SASORI(Sasori.class), //WORKER : Maygo FINISHED
	HIDAN(Hidan.class), //WORKER : Lastril FINISHED
	KAKUZU(Kakuzu.class), //WORKER : Lastril FINISHED
	KISAME(Kisame.class), //WORKER : Lastril FINISHED
	ITACHI(Itachi.class), //WORKER : Maygo FINISHED
	KONAN(Konan.class), //WORKER : Maygo FINISHED
	NAGATO(Nagato.class), //WORKER : Maygo FINISHED
	ZETSU_NOIR(ZetsuNoir.class), //WORKER : Lastril FINISHED
	ZETSU_BLANC(ZetsuBlanc.class), //WORKER : Maygo FINISHED

	/**
	 * OROCHIMARU
	 */
	OROCHIMARU(Orochimaru.class), //WORKER : Lastril FINISHED
	KABUTO(Kabuto.class), //WORKER : Lastril FINISHED
	KIMIMARO(Kimimaro.class), //WORKER : Maygo FINISHED
	SAKON(Sakon.class), //WORKER : Maygo FINISHED
	UKON(Ukon.class), //WORKER : Maygo  FINISHED
	KIDOMARU(Kidomaru.class), //WORKER : Lastril FINISHED
	TAYUYA(Tayuya.class), //WORKER : Lastril FINISHED
	JIROBO(Jirobo.class), //WORKER : Lastril FINISHED

	/**
	 * TAKA
	 */
	SASUKE(Sasuke.class), //WORKER : Lastril : TODO Sasuke VS Naruto Event
	SUIGETSU(Suigetsu.class), //WORKER : Lastril FINISHED
	KARIN(Karin.class), //WORKER : Lastril FINISHED
	JUGO(Jugo.class), //WORKER : Maygo FINISHED

	/**
	 * JÛBI
	 */

	OBITO(Obito.class), //WORKER : Lastril FINISHED
	MADARA(Madara.class), //WORKER : Maygo FINISHED

	/**
	 * SOLOS
	 */

	DANZO(Danzo.class), //WORKER : Lastril FINISHED
	GAARA(Gaara.class), //WORKER : Lastril FINISHED

	/**
	 *  ZABUZA & HAKU
	 */

	ZABUZA(Zabuza.class), //WORKER : Lastril FINISHED
	HAKU(Haku.class), //WORKER : Lastril FINISHED

	;
	
	
	
	Class<? extends Role> role;

	private NarutoV2Roles(Class<? extends Role> class1) {
		this.role = class1;
	}

	public Class<? extends Role> getRole() {
		return role;
	}

}
