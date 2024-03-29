package fr.lastril.uhchost.modes;

import fr.lastril.uhchost.modes.bleach.BleachMode;
import fr.lastril.uhchost.modes.classic.ClassicMode;
import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.modes.sm.SlaveMarketMode;
import fr.lastril.uhchost.modes.tpg.TaupeGunMode;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;

public enum Modes {

    CLASSIC("§a§lClassique", "",new ClassicMode(), new QuickItem(Material.IRON_SWORD).setName("§a§lClassique")
            .setLore("",
                    "§7Equipez-vous et soyez le dernier joueur",
                    "§7en vie dans ce mode de jeu en difficulté",
                    "§7ultra-hardcore.",
                    ""), true),
    LG("§4§lLoup-Garou","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY4ZDQzMTI5MzliYjMxMTFmYWUyOGQ2NWQ5YTMxZTc3N2Y4ZjJjOWZjNDI3NTAxY2RhOGZmZTNiMzY3NjU4In19fQ==" ,new LoupGarouMode(),
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY4ZDQzMTI5MzliYjMxMTFmYWUyOGQ2NWQ5YTMxZTc3N2Y4ZjJjOWZjNDI3NTAxY2RhOGZmZTNiMzY3NjU4In19fQ==")
                    .setLore("",
                            "§7Auteur:§b Setrox§7 et§b Lapin",
                            "§8Version : V10",
                            "",
                            "§7Infiltration, enquête et trahison sont maîtres",
                            "§7dans ce mode de jeu stratégique opposant",
                            "§aVillageois§7 et §cLoups-Garous§7.",
                            "")
                    .setName("§4§lLoup-Garou"), true),

        TAUPEGUN("§e§lTaupe Gun", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNjYzIzYWM3ZTRiZTUxZDg4NGM2MjAwNzZkYTlhOTRiMGJiZTAxN2MzYTc1MzA0MDMzYzA2YmQ4ZjQwZjc0In19fQ==", new TaupeGunMode(),
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNjYzIzYWM3ZTRiZTUxZDg4NGM2MjAwNzZkYTlhOTRiMGJiZTAxN2MzYTc1MzA0MDMzYzA2YmQ4ZjQwZjc0In19fQ==")
                    .setLore("",
                            "§7Auteur:§b Bergarsm (adapté par Fukano)",
                            "§8Version : V1",
                            "",
                            "§7Dans chacunes des équipes, il y a",
                            "§7un traître. Ces derniers doivent",
                            "§7gagner ensemble, à moins qu'un ultime",
                            "§7traître se cache parmis eux.",
                            "")
                    .setName("§e§lTaupe Gun"), true),

    SM("§b§lSlave Market", "",new SlaveMarketMode(),
            new QuickItem(Material.DIAMOND)
                    .setLore("",
                            "§7Acheter vos propres coéquipiers",
                            "§7en échange de§b diamants§7 ou",
                            "§7découvrez votre valeur aux yeux",
                            "§7des autres.",
                            "")
                    .setName("§b§lSlave Market"), false),
    /*AOT("§eAOT", "", null,
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGUzNWIxN2M5YmUyMDQ3NGJmZGE0Yjg0NGYwZTg2NzAyYTBkOGFkNTgzZDE5MDQ2OTNlYmQ4ZjA0MGFiOWJiYiJ9fX0=")
                    .setLore("",
                            "§7Auteur:§b Dragiox§7,§b Kyrutoo§7,§b Rremis§7 et§b _Mqrco",
                            "§8Version : V3",
                            "",
                            "§7Mode de jeu basé sur le manga/animé du",
                            "§7même nom opposant §aSoldats§7,",
                            "§cTitans§7 et§9 Shifter.",
                            "",
                            "§6§k!§r §eClique droit§7 pour configurer le mode de jeu.")
                    .setName("§eAOT"), true),


    CMS("§fChainsaw Man", "", null,
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTllMDFiM2EzNWFlM2JlNDQ4M2I2ZGY2OWI3MDcwYmQ2ZGM3NWIzOTlkN2UyZWJiYzdiODg0MDMzMmY3YjNhMCJ9fX0=")
                    .setLore("",
                            "§7Auteur:§b Dragiox§7 et §b Kyrutoo§7",
                            "§8Version : V1",
                            "",
                            "§7Affrontez les autres joueurs en scellant",
                            "§7des pactes avec des démons qui vous ",
                            "§7apporteront des pouvoirs extraordinaires.",
                            "",
                            "§6§k!§r §eClique droit§7 pour configurer le mode de jeu.")
                    .setName("§fChainsaw Man"), true),


        YUGIOH("§eYu-Gi-Oh", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc0Y2E5YThkNjMxNGNkZmU2ZDU2MTEyODlhZDZmNTNkNTJmMzZkMGU4MGExODAwOGNkN2EzZjMzNzZkOGJkOSJ9fX0=",null,
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc0Y2E5YThkNjMxNGNkZmU2ZDU2MTEyODlhZDZmNTNkNTJmMzZkMGU4MGExODAwOGNkN2EzZjMzNzZkOGJkOSJ9fX0=")
                    .setLore("",
                            "§7Auteur:§b NewMew",
                            "§8Version : V1",
                            "",
                            "§7Ouvrez des packs de cartes",
                            "§7et rassemblez les parties d'Exodia.",
                            "",
                            "§6§k!§r §eClique droit§7 pour configurer le mode de jeu.")
                    .setName("§3Yu-Gi-Oh"), true),
 */
    NARUTO("§6§lNaruto: Next Gen", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFmOTFlOGViY2ZmNThlNzZmNTk3OWJjNjZmMzc3MzZjZjIxNmQ0ZGQzZjQwYWMxMzVkMGIxMDAwM2FjYWJkYiJ9fX0=",null,
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmFmOTFlOGViY2ZmNThlNzZmNTk3OWJjNjZmMzc3MzZjZjIxNmQ0ZGQzZjQwYWMxMzVkMGIxMDAwM2FjYWJkYiJ9fX0=")
                    .setLore("",
                            "§7Auteur:§b NETSPEED",
                            "§8Version : V3",
                            "",
                            "§7§oArrive bientôt",
                            "")
                    .setName("§6§lNaruto: Next Gen")
            , false),

    BLEACH("§9§lBleach", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDUzYjRmZjMyZTRkOTEyYWQ1ODk1YjZjMzdhMzUyZjYxYWY5ZTQxZDI0N2E4NzliNWY0OWE2MzUyZmM4NiJ9fX0=",new BleachMode(),
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDUzYjRmZjMyZTRkOTEyYWQ1ODk1YjZjMzdhMzUyZjYxYWY5ZTQxZDI0N2E4NzliNWY0OWE2MzUyZmM4NiJ9fX0=")
                    .setLore("",
                            "§7Auteur:§b Artyx§7,§b Hiirauh§7 et§b Exyleuh",
                            "§8Version : V1",
                            "",
                            "§7Mondes parallèles, invocations et portails",
                            "§7bouleverserons la partie où §6Shinigamis",
                            "§7et§3 Arrancars§7 s'affronteront dans",
                            "§7une bataille sans merci.",
                            "")
                    .setName("§9§lBleach"), false),


    /*DEATHNOTE("§9§lDeath Note", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNjYzIzYWM3ZTRiZTUxZDg4NGM2MjAwNzZkYTlhOTRiMGJiZTAxN2MzYTc1MzA0MDMzYzA2YmQ4ZjQwZjc0In19fQ==", null,
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNjYzIzYWM3ZTRiZTUxZDg4NGM2MjAwNzZkYTlhOTRiMGJiZTAxN2MzYTc1MzA0MDMzYzA2YmQ4ZjQwZjc0In19fQ==")
                    .setLore("",
                            "§7Auteur:§b Paulo§7 et§b Korso",
                            "§8Version : V4",
                            "",
                            "§cKira :§7 \"Je créerai un monde sans",
                            "§7criminels où j'en serai le dieu !\"",
                            "",
                            "§9L :§7 \"§cKira§7, j'ignore comment tu tues",
                            "§7tes victimes mais sache que je t'enverrai",
                            "§7moi-même à l'échafaud !\"",
                            "")
                    .setName("§9§lDeath Note"), false),*/


    SOON_1("§c§lBientôt", "",null,
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiYTYzMzQ0ZjQ5ZGQxYzRmNTQ4OGU5MjZiZjNkOWUyYjI5OTE2YTZjNTBkNjEwYmI0MGE1MjczZGM4YzgyIn19fQ==")
                    .setLore("")
                    .setName("§c§lSoon"), false);


    private final String name, texture;
    private final Mode mode;
    private final QuickItem item;
    private final boolean available;

    Modes(String name, String texture, Mode mode, QuickItem item, boolean available) {
        this.name = name;
        this.mode = mode;
        this.item = item;
        this.available = available;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public Mode getMode() {
        return mode;
    }

    public QuickItem getItem() {
        return item;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getTexture() {
        return texture;
    }
}
