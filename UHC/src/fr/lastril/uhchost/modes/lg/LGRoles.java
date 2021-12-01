package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.modes.lg.roles.lg.*;
import fr.lastril.uhchost.modes.lg.roles.solo.*;
import fr.lastril.uhchost.modes.lg.roles.village.*;
import fr.lastril.uhchost.modes.lg.roles.village.voyante.Voyante;
import fr.lastril.uhchost.modes.lg.roles.village.voyante.VoyanteBavarde;
import fr.lastril.uhchost.modes.roles.Role;

public enum LGRoles {


    /*
     * CAMP DES LOUPS
     */
    LG(LoupGarou.class), //FINISHED
    INFECT(InfectPereDesLoups.class), //FINISHED
    LG_AMNESIQUE(LoupGarouAmnesique.class), //TODO BE TESTED
    LG_ALPHA(LoupGarouAlpha.class),
    LG_FEUTRE(LoupGarouFeutre.class), //FINISHED
    LG_PERFIDE(LoupGarouPerfide.class), //FINISHED
    LG_LUNAIRE(LoupGarouLunaire.class), //FINISHED
    LG_GRIMEUR(LoupGarouGrimeur.class), //FINISHED
    LG_CRAINTIF(LoupGarouCraintif.class), //FINISHED
    LG_JUMEAU(LoupGarouJumeau.class),
    LV_MAUDITE(LouveMaudite.class),
    VPL(VilainPetitLoup.class), //TODO BE TESTED
    GML(GrandMechantLoup.class),
    LG_MYSTIQUE(LoupGarouMystique.class),
    LG_VENGEUR(LoupGarouVengeur.class),

    /*
     * CAMP DU VILLAGE
     */
    ANCIEN(Ancien.class), //FINISHED
    CHAMAN(Chaman.class),
    CHASSEUR(Chasseur.class), //FINISHED
    CITOYEN(Citoyen.class),
    CORBEAU(Corbeau.class),
    CUPIDON(Cupidon.class), //TODO BE TESTED
    DETECTIVE(Detective.class), //FINISHED
    ENFANT_SAUVAGE(EnfantSauvage.class), //FINISHED
    GARDE(Garde.class), //TODO BE TESTED
    MONTREUR_DOURS(MontreurDours.class), //FINISHED
    PETITE_FILLE(PetiteFille.class), //TODO LG CHAT SEE
    PRETRESSE(Pretresse.class), //TODO BE TESTED
    RENARD(Renard.class), //FINISHED
    SALVATEUR(Salvateur.class), //FINISHED
    SOEUR(Soeur.class), //FINISHED
    SORCIERE(Sorciere.class), //FINISHED
    VOYANTE(Voyante.class), //FINISHED
    VOYANTE_BAVARDE(VoyanteBavarde.class), //FINISHED
    VILLAGEOIS(Villageois.class), //FINISHED

    /*
     * CAMP DES NEUTRES
     */

    CHIEN_LOUP(ChienLoup.class), //TODO BE TESTED
    TRUBLION(Trublion.class), //TODO BE TESTED
    VOLEUR(Voleur.class), //TODO BE TESTED
    /*
     * CAMP DES SOLOS
     */
    IMITATEUR(Imitateur.class), //TODO BE TESTED
    ANGE(Ange.class), //TODO BE TESTED
    LGB(LoupGarouBlanc.class), //FINISHED
    ASSASSIN(Assassin.class), //FINISHED
    ;


    private final Class<? extends Role> role;

    LGRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }
}
