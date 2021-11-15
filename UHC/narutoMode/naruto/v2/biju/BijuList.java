package fr.lastril.uhchost.modes.naruto.v2.biju;

import fr.lastril.uhchost.modes.naruto.v2.biju.bijus.*;

public enum BijuList {

    MATATABI(MatatabiBiju.class),
    ISOBU(IsobuBiju.class),
    SONGOKU(SonGokuBiju.class),
    KOKUO(KokuoBiju.class),
    SAIKEN(SaikenBiju.class),
    CHOMEI(ChomeiBiju.class);

    Class<? extends Biju> biju;

    BijuList(Class<? extends Biju> class1) {
        this.biju = class1;
    }

    public Class<? extends Biju> getBiju() {
        return biju;
    }

    public Biju getBijuObject() throws InstantiationException, IllegalAccessException {
        return biju.newInstance();
    }

}
