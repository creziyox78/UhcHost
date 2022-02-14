package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class Villageois extends Role implements LGRole {

    private final int randomKit = UhcHost.getRANDOM().nextInt(6);

    @Override
    public void giveItems(Player player) {
        giveKit(player);
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Simple Villageois";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE5NTUxNjhlZjUzZjcxMjBjMDg5ZGFmZTNlNmU0MzdlOTUyNDA1NTVkOGMzYWNjZjk0NGQ2YzU2Yjc0MDQ3NSJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    public void giveKit(Player player){
        switch (randomKit){
            case 0:
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVoici votre kit: Mineur");
                main.getInventoryUtils().giveItemSafely(player,new QuickItem(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 2, true).toItemStack());
                break;
            case 1:
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVoici votre kit: Forgeron");
                main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.ANVIL).toItemStack());
                main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.EXP_BOTTLE, 20).toItemStack());
                break;
            case 2:
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVoici votre kit: Archer");
                main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.ARROW, 64).toItemStack());
                main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.STRING, 6).toItemStack());
                break;
            case 3:
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVoici votre kit: Meurtrier");
                main.getInventoryUtils().giveItemSafely(player, new Livre(Enchantment.DAMAGE_ALL, 3).toItemStack());
                break;
            case 4:
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVoici votre kit: Libraire");
                main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.BOOK, 8 ).toItemStack());
                main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.EXP_BOTTLE, 20).toItemStack());
                break;
            case 5:
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVoici votre kit: Armurier");
                main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).toItemStack());
                break;
        }
    }

}
