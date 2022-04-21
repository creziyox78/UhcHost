package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.modes.bleach.roles.arrancars.Aizen;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Stark;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.*;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.*;
import fr.lastril.uhchost.modes.roles.Role;

public enum BleachRoles {

    /*
     * SHINIGAMIS
     */

    //Soul Society
    YAMAMOTO(Yamamoto.class), //TODO FIX NO ABSO
    SOIFON(SoiFon.class), //FINISHED
    OMAEDA(Omaeda.class), //TODO FIX LEFT CLICK ROD
    KIRA(Kira.class), //FINISHED
    UNOHANA(Unohana.class), //FINISHED
    ISANE(Isane.class), //FINISHED
    HINAMORI(Hinamori.class), //FINISHED
    BYAKUYA(Byakuya.class), //TODO BE TESTED
    RENJI(Renji.class), //FINISHED
    KOMAMURA(Komamura.class),
    IBA(Iba.class), //FINISHED
    KYORAKU(Kyoraku.class), //FINISHED
    NANAO(Nanao.class), //FINISHED
    HISAGI(Hisagi.class), //FINISHED
    TOSHIROHITSUGAYA(ToshiroHitsugaya.class), //FINISHED
    RANGIKUMATSUMOTO(RangikuMatsumoto.class), //FINISHED
    KENPACHIZARAKI(KenpachiZaraki.class), //FINISHED
    YACHIRU(Yachiru.class), //FINISHED
    MAYURI(Mayuri.class), //TODO FIX WITHER
    NEMU(Nemu.class), //FINISHED
    JUSHIROUKITAKE(JushiroUkitake.class), //TODO ADAPTION WITH CERO
    IKKAKU(Ikkaku.class), //FINISHED

    //Ichigo Groups

    ICHIGOKUROSAKI(IchigoKurosaki.class), //TODO ADAPTION WITH AIZEN, FIX CHARGE
    INOUEORIHIME(InoueOrihime.class), //TODO BE TESTED
    SADO(Sado.class), //TODO BE TESTED
    ISHIDA(Ishida.class), //TODO BE TESTED
    RUKIA(Rukia.class), //TODO FIX FULL POWER
    KISUKEURAHARA(KisukeUrahara.class), //TODO BE TESTED
    YORUICHI(Yoruichi.class), //TODO BE TESTED AND FIX 2 MINUTES DAMAGE
    NELLIEL(Nelliel.class),




    /*
     * ARRANCARS
     */

    AIZEN(Aizen.class),


    //ESPADA
    STARK(Stark.class),

    ;

    private final Class<? extends Role> role;

    BleachRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }

}
