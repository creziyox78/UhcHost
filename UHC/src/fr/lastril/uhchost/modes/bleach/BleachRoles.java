package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.modes.bleach.roles.arrancars.Aizen;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.*;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.*;
import fr.lastril.uhchost.modes.roles.Role;

public enum BleachRoles {

    /*
     * SHINIGAMIS
     */

    //Soul Society
    YAMAMOTO(Yamamoto.class), //TODO FIX NO ABSO
    SOIFON(SoiFon.class), //TODO ITEM "Suzumebachi"
    OMAEDA(Omaeda.class), //FINISHED
    KIRA(Kira.class), //FINISHED
    UNOHANA(Unohana.class), //FINISHED
    ISANE(Isane.class), //FINISHED
    HINAMORI(Hinamori.class), //FINISHED
    BYAKUYA(Byakuya.class), //TODO BE TESTED
    RENJI(Renji.class), //FINISHED
    KOMAMURA(Komamura.class),
    IBA(Iba.class), //TODO FIX close to wall
    KYORAKU(Kyoraku.class), //TODO ITEM "Kageoni"
    NANAO(Nanao.class), //FINISHED
    HISAGI(Hisagi.class), //TODO FIX switch item
    TOSHIROHITSUGAYA(ToshiroHitsugaya.class), //TODO ITEM "Ryusenka"
    RANGIKUMATSUMOTO(RangikuMatsumoto.class), //FINISHED
    KENPACHIZARAKI(KenpachiZaraki.class), //TODO BE TESTED
    YACHIRU(Yachiru.class), //TODO BE TESTED
    MAYURI(Mayuri.class),
    NEMU(Nemu.class),
    JUSHIROUKITAKE(JushiroUkitake.class),
    IKKAKU(Ikkaku.class),

    ICHIGOKUROSAKI(IchigoKurosaki.class),
    INOUEORIHIME(InoueOrihime.class),
    SADO(Sado.class),
    ISHIDA(Ishida.class),
    RUKIA(Rukia.class),
    KISUKEURAHARA(KisukeUrahara.class),
    YORUICHI(Yoruichi.class),
    NELLIEL(Nelliel.class),


    //Ichigo Groups

    /*
     * ARRANCARS
     */
    AIZEN(Aizen.class),
    ;

    private final Class<? extends Role> role;

    BleachRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }

}
