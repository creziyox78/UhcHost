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
    LG_AMNESIQUE(LoupGarouAmnesique.class),
    LG_ANONYME(LoupGarouAnonyme.class),
    LG_ALPHA(LoupGarouAlpha.class),
    LG_FEUTRE(LoupGarouFeutre.class),
    LG_PERFIDE(LoupGarouPerfide.class),
    LG_LUNAIRE(LoupGarouLunaire.class), //TODO BE TESTED
    LG_GRIMEUR(LoupGarouGrimeur.class),
    LG_JUMEAU(LoupGarouJumeau.class),
    LV_MAUDITE(LouveMaudite.class),
    VPL(VilainPetitLoup.class),

    /*
     * CAMP DU VILLAGE
     */
    ANCIEN(Ancien.class),
    CHAMAN(Chaman.class),
    CHASSEUR(Chasseur.class),
    CITOYEN(Citoyen.class),
    CORBEAU(Corbeau.class),
    CUPIDON(Cupidon.class),
    DETECTIVE(Detective.class),
    ENFANT_SAUVAGE(EnfantSauvage.class),
    MINEUR(Mineur.class),
    MONTREUR_DOURS(MontreurDours.class),
    PETITE_FILLE(PetiteFille.class),
    PRETRESSE(Pretresse.class),
    RENARD(Renard.class),
    SALVATEUR(Salvateur.class),
    SOEUR(Soeur.class),
    SORCIERE(Sorciere.class),
    TRUBLION(Trublion.class),
    VOYANTE(Voyante.class),
    VOYANTE_BAVARDE(VoyanteBavarde.class),
    VILLAGEOIS(Villageois.class),

    /*
     * CAMP DES NEUTRES
     */

    VOLEUR(Voleur.class),
    CHIEN_LOUP(ChienLoup.class),
    /*
     * CAMP DES SOLOS
     */
    IMITATEUR(Imitateur.class),
    ANGE(Ange.class),
    LGB(LoupGarouBlanc.class),
    ASSASSIN(Assassin.class),
    ;


    private Class<? extends Role> role;

    LGRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }
}
