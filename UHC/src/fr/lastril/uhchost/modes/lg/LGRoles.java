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
    LG_AMNESIQUE(LoupGarouAmnesique.class),
    LG_ALPHA(LoupGarouAlpha.class), //FINISHED
    LG_FEUTRE(LoupGarouFeutre.class), //FINISHED
    LG_PERFIDE(LoupGarouPerfide.class), //FINISHED
    LG_LUNAIRE(LoupGarouLunaire.class), //FINISHED
    LG_GRIMEUR(LoupGarouGrimeur.class), //FINISHED
    LG_CRAINTIF(LoupGarouCraintif.class), //FINISHED
    LG_PISTEUR(LoupGarouPisteur.class), //FINISHED
    LG_JUMEAU(LoupGarouJumeau.class),
    LV_MAUDITE(LouveMaudite.class),
    VPL(VilainPetitLoup.class), //FINISHED
    GML(GrandMechantLoup.class), //FINISHED
    LG_MYSTIQUE(LoupGarouMystique.class), //FINISHED
    LG_VENGEUR(LoupGarouVengeur.class), //FINISHED
    LG_HYBRIDE(LoupGarouHybride.class),
    LG_DOMINANT(LoupGarouDominant.class), //TODO BE TESTED
    LG_TIMIDE(LoupGarouTimide.class), //TODO BE TESTED

    /*
     * CAMP DU VILLAGE
     */
    ANCIEN(Ancien.class), //FINISHED
    BUCHERON(Bucheron.class), //FINISHED
    CHAMAN(Chaman.class), //FINISHED
    CHASSEUR(Chasseur.class), //FINISHED
    CHEF_DU_VILLAGE(ChefDuVillage.class), //TODO BE TESTED
    CITOYEN(Citoyen.class),
    CORBEAU(Corbeau.class),
    CUPIDON(Cupidon.class), //FINISHED
    DETECTIVE(Detective.class), //FINISHED
    ENFANT_SAUVAGE(EnfantSauvage.class), //FINISHED
    ERMITE(Ermite.class), //FINISHED
    GARDE(Garde.class), //FINISHED
    IDIOT_DU_VILLAGE(IdiotDuVillage.class), //FINISHED
    IVROGNE_DU_VILLAGE(IvrogneDuVillage.class), //FINISHED
    INDIGENE(Indigene.class), //FINISHED
    MONTREUR_DOURS(MontreurDours.class), //FINISHED
    PETITE_FILLE(PetiteFille.class), //FINISHED
    PRETRESSE(Pretresse.class), //FINISHED
    RENARD(Renard.class), //FINISHED
    REVENANT(Revenant.class), //TODO BE TESTED
    SALVATEUR(Salvateur.class), //FINISHED
    SOEUR(Soeur.class), //FINISHED
    SORCIERE(Sorciere.class), //FINISHED
    VOYANTE(Voyante.class), //FINISHED
    VOYANTE_BAVARDE(VoyanteBavarde.class), //FINISHED
    TRAPPEUR(Trappeur.class), //FINISHED
    VILLAGEOIS(Villageois.class), //FINISHED

    /*
     * CAMP DES NEUTRES
     */

    CHIEN_LOUP(ChienLoup.class), //FINISHED
    TRUBLION(Trublion.class), //FINISHED
    VOLEUR(Voleur.class), //FINISHED
    REBELLE(Rebelle.class),
    RIVAL(Rival.class), //FINISHED
    /*
     * CAMP DES SOLOS
     */
    IMITATEUR(Imitateur.class), //FINISHED
    ANGE(Ange.class), //FINISHED
    LGB(LoupGarouBlanc.class), //FINISHED
    ASSASSIN(Assassin.class), //FINISHED
    CHASSEUR_DE_PRIME(ChasseurDePrime.class), //FINISHED
    NECROMENCIEN(Necromancien.class), //FINISHED
    ;


    private final Class<? extends Role> role;

    LGRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }
}
