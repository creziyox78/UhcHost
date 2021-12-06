package fr.lastril.uhchost.modes;

import fr.lastril.uhchost.modes.classic.ClassicMode;
import fr.lastril.uhchost.modes.ds.DemonSlayer;
import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;

public enum Modes {

    CLASSIC("§fClassique", "", new ClassicMode(), new QuickItem(Material.IRON_SWORD).setName("§fClassique")
            .setLore("",
                    "§7Equipez-vous et soyez le dernier joueur",
                    "§7en vie dans ce mode de jeu en difficulté",
                    "§7ultra-hardcore.",
                    ""), true),
    LG("§cLoup-Garou", "", new LoupGarouMode(),
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY4ZDQzMTI5MzliYjMxMTFmYWUyOGQ2NWQ5YTMxZTc3N2Y4ZjJjOWZjNDI3NTAxY2RhOGZmZTNiMzY3NjU4In19fQ==")
                    .setLore("",
                            "§7Auteur:§b Setrox§7 et§b Lapin",
                            "§8Version : V10",
                            "",
                            "§7Infiltration, enquête et trahison sont maîtres",
                            "§7dans ce mode de jeu stratégique opposant",
                            "§aVillageois§7 et §cLoups-Garous§7.",
                            "",
                            "§6§k!§r §eClique droit§7 pour configurer le mode de jeu.")
                    .setName("§cLoup-Garou"), true),
    DS("§6Demon Slayer", "", new DemonSlayer(),
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTA5MzIzMmFlYTQ5NjAwYjUwOTJmYzE4MmUwZDVjZTg5OTlmNzgwNDVkZDdiZGEyM2M2NWNjYTZmY2Y1Y2Y2MCJ9fX0=")
                    .setLore("",
                            "§7Auteur:§b ALEXQLF§7 et§b Timouh",
                            "§8Version : V3",
                            "",
                            "§7Mode de jeu basé sur le manga/animé du",
                            "§7même nom opposant §3Slayers§7",
                            "§7et§c Démons§7.",
                            "",
                            "§6§k!§r §eClique droit§7 pour configurer le mode de jeu.")
                    .setName("§6Demon Slayer"), false),

    AOT("§eAOT", "", new DemonSlayer(),
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
                    .setName("§eAOT"), false),


    CMS("§fChainsaw Man", "", new DemonSlayer(),
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
                    .setName("§fChainsaw Man"), false),

    BLEACH("§3Bleach", "", null,
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
                            "",
                            "§6§k!§r §eClique droit§7 pour configurer le mode de jeu.")
                    .setName("§3Bleach"), false),
    /*NARUTO_V2("§6Naruto V2", "", new NarutoV2(),
        Items.createHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiYTYzMzQ0ZjQ5ZGQxYzRmNTQ4OGU5MjZiZjNkOWUyYjI5OTE2YTZjNTBkNjEwYmI0MGE1MjczZGM4YzgyIn19fQ=="
                , "§cBientôt", 1, null)),*/
    SOON_1("§cBientôt", "", null,
            new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiYTYzMzQ0ZjQ5ZGQxYzRmNTQ4OGU5MjZiZjNkOWUyYjI5OTE2YTZjNTBkNjEwYmI0MGE1MjczZGM4YzgyIn19fQ==")
                    .setLore("")
                    .setName("§cSoon"), false);


    private final String name, headHash;
    private final Mode mode;
    private final QuickItem item;
    private final boolean available;

    Modes(String name, String headHash, Mode mode, QuickItem item, boolean available) {
        this.name = name;
        this.headHash = headHash;
        this.mode = mode;
        this.item = item;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public String getHeadHash() {
        return headHash;
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
}
