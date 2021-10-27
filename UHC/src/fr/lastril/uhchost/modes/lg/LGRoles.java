package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.modes.lg.roles.lg.*;
import fr.lastril.uhchost.modes.lg.roles.solo.Assassin;
import fr.lastril.uhchost.modes.lg.roles.solo.Imitateur;
import fr.lastril.uhchost.modes.lg.roles.solo.LoupGarouBlanc;
import fr.lastril.uhchost.modes.lg.roles.solo.Voleur;
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
    LG_ANONYME(LoupGarouAnonyme.class),
    LG_ALPHA(LoupGarouAlpha.class),
    LG_FEUTRE(LoupGarouFeutre.class), //TODO BE TESTED (Adaptation Voyante / Voyante bavarde)
    LG_PERFIDE(LoupGarouPerfide.class), //TODO BE TESTED
    LG_LUNAIRE(LoupGarouLunaire.class), //TODO BE TESTED
    LG_GRIMEUR(LoupGarouGrimeur.class),
    LG_JUMEAU(LoupGarouJumeau.class),
    LV_MAUDITE(LouveMaudite.class),
    VPL(VilainPetitLoup.class), //TODO BE TESTED

    /*
     * CAMP DU VILLAGE
     */
    ANCIEN(Ancien.class), //TODO BE TESTED AND MAKE (Adaptation Sorcier / IDPL)
    CHAMAN(Chaman.class),
    CHASSEUR(Chasseur.class),
    CITOYEN(Citoyen.class),
    CORBEAU(Corbeau.class),
    CUPIDON(Cupidon.class), //TODO BE TESTED
    DETECTIVE(Detective.class),
    ENFANT_SAUVAGE(EnfantSauvage.class), //TODO BE TESTED
    GARDE(Garde.class),
    MINEUR(Mineur.class), //FINISHED
    MONTREUR_DOURS(MontreurDours.class), //TODO BE TESTED
    PETITE_FILLE(PetiteFille.class), //TODO BE TESTED
    PRETRESSE(Pretresse.class),
    RENARD(Renard.class), //TODO BE TESTED
    SALVATEUR(Salvateur.class), //TODO BE TESTED
    SOEUR(Soeur.class), //TODO BE TESTED
    SORCIERE(Sorciere.class), //TODO BE TESTED
    TRUBLION(Trublion.class),
    VOYANTE(Voyante.class), //TODO BE TESTED
    VOYANTE_BAVARDE(VoyanteBavarde.class), //TODO BE TESTED
    VILLAGEOIS(Villageois.class), //FINISHED

    /*
     * CAMP DES NEUTRES
     */

    VOLEUR(Voleur.class), //TODO BE TESTED
    CHIEN_LOUP(ChienLoup.class),
    /*
     * CAMP DES SOLOS
     */
    IMITATEUR(Imitateur.class),
    ANGE(Ange.class),
    LGB(LoupGarouBlanc.class), //TODO BE TESTED
    ASSASSIN(Assassin.class), //FINISHED TODO Place Heads
    ;


    private final Class<? extends Role> role;

    LGRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }
}
