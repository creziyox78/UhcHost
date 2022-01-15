package fr.lastril.uhchost.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdMaj implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("§8§m--------------------------------------------------§r\n" +
                    "§b   Mise à jour Naruto UHC§b  \n" +
                    " \n "+
                    "§aNeji / Hinata : \n" +
                    "- Cooldown du Hakke baissé à 15 minutes (au lieu de 20 minutes)\n" +
                    "- Cooldown du Byakugan baissé à 5 minutes (au lieu de 20 minutes)\n" +
                    "Sakura : \n" +
                    "- Ne perd plus de coeur quand Sasuke meurt\n" +
                    "- Ne connaît plus l'identité de Sasuke quand ce dernier avait tué quelqu'un\n" +
                    "Minato : \n" +
                    "- Cooldown de l'Arc augmenté à 45 secondes (au lieu de 15 secondes)\n" +
                    "- Cooldown du TP augmenté à 10 minutes (au lieu de 5 minutes)\n" +
                    "Jiraya : \n" +
                    "- Cooldown du Senjutsu baissé à 20 minutes (au lieu de 30 minutes)\n" +
                    "Asuma : \n" +
                    "- Temps d'éxecution du pouvoir \"Nuées Ardentes\" réduit à 1 seconde (au lieu de 3 secondes)\n" +
                    "Tsunade :\n" +
                    "- Cooldown du \"Byakugô\" (Regen 5) réduit à 20 minutes (au lieu de 30 minutes)\n" +
                    "Kakashi :\n" +
                    "- Cooldown du Kamui (TP quelqu'un dans le Kamui) réduit à 20 minutes (au lieu de 30 minutes)\n" +
                    "- Cooldown du Kamui (se TP dans le Kamui) réduit à 10 minutes (au lieu de 15 minutes)\n" +
                    " \n" +
                    "§5Orochimaru : \n" +
                    "- Ajout de l'effet Speed 1 permanent\n" +
                    "- Suppression de l'effet Résistance 2 en dessous de 4 coeurs.\n" +
                    "Kimimaro : \n" +
                    "- Connaît Orochimaru\n" +
                    "Jirobo :\n" +
                    "- Connaît Orochimaru\n" +
                    "Tayuya : \n" +
                    "- Connaît Orochimaru\n" +
                    "Ukon :\n" +
                    "- Connaît Orochimaru\n" +
                    "Sakon :\n" +
                    "- Connaît Orochimaru\n" +
                    "Kidomaru :\n" +
                    "- Suppression de l'effet Weakness 1 permanent\n" +
                    " \n " +
                    "§cItachi :\n" +
                    "- Ne perd plus 2 coeurs quand Sasuke meurt\n" +
                    "- Suppression de l'effet Weakness quand Sasuke meurt\n" +
                    "Kisame :\n" +
                    "- Suppression de l'effet Résistance permanent\n" +
                    " \n " +
                    "§eGaara :\n" +
                    "- Ajout de l'effet Force 1 permanent\n" +
                    " \n " +
                    "§9Kamui :\n" +
                    "- Temps dans le Kamui réduit à 2 minutes au lieu de 5 minutes (Kakashi / Obito)\n" +
                    "Jump de sable :\n" +
                    "- Temps dans le jump réduit à 2 minutes au lieu de 5 minutes \n" +
                    "§8§m--------------------------------------------------§r");
        }
        return false;
    }
}
