package fr.lastril.uhchost.modes.naruto.v2.roles.taka;

import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdKaguraShingan;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdMorsure;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KarinItem;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.JubiItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Orochimaru;
import fr.lastril.uhchost.modes.naruto.v2.tasks.KarinTask;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Karin extends Role implements NarutoV2Role, RoleCommand {
	
	private boolean orochimaruDead, itachiDead, useMorsure;
	
	private PlayerManager tracked;
	
	private final Map<PlayerManager, Integer> joueurUnknow;
	
	private int timer = 60*10;
	
	public Karin() {
		super.addRoleToKnow(Orochimaru.class);
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
		this.joueurUnknow = new HashMap<>();
	}
	
	@Override
	public void afterRoles(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		joueur.setCamps(Camps.OROCHIMARU);
		new KarinTask(main, this).runTaskTimer(main, 0, 20);
	}

	@EventHandler
	public void Craft(CraftItemEvent event) {
		ItemStack result = event.getRecipe().getResult();
		if(result.getType() == Material.NETHER_STAR){
			if(result.isSimilar(new JubiItem(main).toItemStack())){
				if (super.getPlayer() != null) {
					Player jugoPlayer = super.getPlayer();
					main.getPlayerManager(jugoPlayer.getUniqueId()).setCamps(Camps.SHINOBI);
					jugoPlayer.sendMessage("§5Jûbi§e vient d'être invoqué. Vous gagnez désormais avec les§a Shinobis.");
				}
			}
		}

	}

	@Override
	public void onPlayerDeath(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
		NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
		if (joueur.hasRole()) {
			if (joueur.getRole() instanceof Orochimaru) {
				if (super.getPlayer() != null) {
					if(itachiDead){
						Player sasukePlayer = super.getPlayer();
						itachiDead = true;
						main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
						sasukePlayer.sendMessage("§cVous gagnez avec l'§cAkatsuki car Itachi et Orochimaru sont morts. Voici l'identité de Nagato et de Sasuke: ");
						for (PlayerManager nagato : narutoV2Manager.getPlayerManagersWithRole(Nagato.class)) {
							sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
						}
						for (PlayerManager sasuke : narutoV2Manager.getPlayerManagersWithRole(Sasuke.class)) {
							sasukePlayer.sendMessage("§6 - Sasuke : " + sasuke.getPlayerName());
						}
					} else {
						orochimaruDead = true;
						Player sasukePlayer = super.getPlayer();
						main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.TAKA);
						sasukePlayer.sendMessage("§5Orochimaru§e vient de mourir, vous gagnez uniquement avec votre camp. Voici l'identité de Sasuke: ");
						for (PlayerManager sasuke : narutoV2Manager.getPlayerManagersWithRole(Sasuke.class)) {
							sasukePlayer.sendMessage("§6 - Sasuke : " + sasuke.getPlayerName());
						}
					}

				}
			}else if (joueur.getRole() instanceof Itachi) {
				if (super.getPlayer() != null) {
					if (orochimaruDead) {
						Player sasukePlayer = super.getPlayer();
						itachiDead = true;
						main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.AKATSUKI);
						sasukePlayer.sendMessage("§cItachi§e vient de mourir, vous gagnez avec l'§cAkatsuki. Voici l'identité de Nagato: ");
						for (PlayerManager nagato :narutoV2Manager.getPlayerManagersWithRole(Nagato.class)) {
							sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
						}
					} else {
						itachiDead = true;
					}
				}
			}

		}
	}
	
    @Override
    public void giveItems(Player player) {
    	main.getInventoryUtils().giveItemSafely(player, new KarinItem(main).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {
    	PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
    	if(joueur.hasRole()) {
    		if(joueur.getRole() instanceof Karin) {
    			Karin karin = (Karin) joueur.getRole();
    			karin.setUseMorsure(false);
    		}
    	}
    }

    @Override
	public QuickItem getItem() {
		return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmM3NjY5Yjk5NmY2MDUwZDVkYWE0YmM2NmViZDc3YWIzMTU1ODE4YzA3NzI1MDZmNjM0N2YwYzk4NDM1YjQxOCJ9fX0=")
				.setName(getCamp().getCompoColor() + getRoleName());
	}

    @Override
    public Camps getCamp() {
        return Camps.TAKA;
    }

    @Override
    public String getRoleName() {
        return "Karin";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

	@Override
	public Chakra getChakra() {
		return null;
	}

	public boolean isOrochimaruDead() {
		return orochimaruDead;
	}

	public void setOrochimaruDead(boolean orochimaruDead) {
		this.orochimaruDead = orochimaruDead;
	}

	public boolean isItachiDead() {
		return itachiDead;
	}

	public void setItachiDead(boolean itachiDead) {
		this.itachiDead = itachiDead;
	}

	public Map<PlayerManager, Integer> getJoueurUnknow() {
		return joueurUnknow;
	}

	public int getTimer() {
		return timer;
	}

	public boolean isUseMorsure() {
		return useMorsure;
	}

	public void setUseMorsure(boolean useMorsure) {
		this.useMorsure = useMorsure;
	}

	@Override
	public List<ModeSubCommand> getSubCommands() {
		return Arrays.asList(new CmdMorsure(main), new CmdKaguraShingan(main));
	}

	public PlayerManager getTracked() {
		return tracked;
	}

	public void setTracked(PlayerManager tracked) {
		this.tracked = tracked;
	}
	
	
}
