package fr.lastril.uhchost.modes.naruto.v2.roles.taka;


import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.commands.CmdIzanagi;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.GenjutsuItem;
import fr.lastril.uhchost.modes.naruto.v2.items.RinneganItem;
import fr.lastril.uhchost.modes.naruto.v2.items.SusanoItem;
import fr.lastril.uhchost.modes.naruto.v2.items.biju.JubiItem;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru.Orochimaru;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Sasuke extends Role implements NarutoV2Role, SusanoItem.SusanoUser, CmdIzanagi.IzanagiUser, RoleCommand, RoleListener, GenjutsuItem.GenjutsuUser {

	private boolean orochimaruDead, itachiDead, killedItachi;
	
	private boolean hasUsedIzanagi;
	
	private boolean hasUsedIzanami;

	private boolean killedUchiwa;
	
	private int tsukuyomiUses;

	private IzanamiGoal izanamiGoal;
	
    public Sasuke(){
        super.addRoleToKnow(Orochimaru.class);
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }
   
    @Override
    public void giveItems(Player player) {
    	main.getInventoryUtils().giveItemSafely(player, new SusanoItem(main).toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new RinneganItem(main).toItemStack());
    }
    
    @EventHandler
    public void onKillItachi(PlayerKillEvent event) {
    	Player killer = event.getKiller();
    	Player player = event.getDeadPlayer();
    	PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
    	PlayerManager killerJoueur = main.getPlayerManager(killer.getUniqueId());
    	if(joueur.hasRole() && killerJoueur.hasRole()) {
    		if(joueur.getRole() instanceof Itachi && killerJoueur.getRole() instanceof Sasuke) {
    			setKilledItachi(true);
    			killer.setMaxHealth(killer.getMaxHealth() + (2D*3D));
    			killer.setHealth(killer.getHealth() + (2D*3D));
    			killer.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous venez de tuer Itachi. Vous recevez 3 coeurs supplémentaires ainsi que la possibilité d'utilisé l'épée Susano d'Itachi.");
    		}
    	}
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

    }

    @Override
	public QuickItem getItem() {
		return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFlYmNhMzcyOWJhOTgwOTM1OTg4OGNiNDY4MTM1YmI0OTNjOGQxOGM5OGI5NWMxNTdlZTk5MDQyOWExMCJ9fX0=")
				.setName(getCamp().getCompoColor() + getRoleName());
	}

    @Override
    public Camps getCamp() {
        return Camps.TAKA;
    }

    @Override
    public String getRoleName() {
        return "Sasuke";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
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
						sasukePlayer.sendMessage("§cVous gagnez avec l'§cAkatsuki car Itachi et Orochimaru sont morts. Voici l'identité de Nagato et des membres de Taka: ");

						for (PlayerManager nagato : narutoV2Manager.getPlayerManagersWithRole(Nagato.class)) {
							sasukePlayer.sendMessage("§c - Nagato : " + nagato.getPlayerName());
						}
						for (PlayerManager sasuke : narutoV2Manager.getPlayerManagersWithRole(Jugo.class)) {
							sasukePlayer.sendMessage("§6 - Jugo : " + sasuke.getPlayerName());
						}
						for (PlayerManager sasuke : narutoV2Manager.getPlayerManagersWithRole(Karin.class)) {
							sasukePlayer.sendMessage("§6 - Karin : " + sasuke.getPlayerName());
						}
						for (PlayerManager sasuke : narutoV2Manager.getPlayerManagersWithRole(Suigetsu.class)) {
							sasukePlayer.sendMessage("§6 - Suigetsu : " + sasuke.getPlayerName());
						}
					} else {
						orochimaruDead = true;
						Player sasukePlayer = super.getPlayer();
						main.getPlayerManager(sasukePlayer.getUniqueId()).setCamps(Camps.TAKA);
						sasukePlayer.sendMessage("§5Orochimaru§e vient de mourir, vous gagnez uniquement avec votre camp. Voici les joueurs dans votre camp: ");
						for(PlayerManager joueurs : narutoV2Manager.getPlayerManagersWithCamps(getCamp())) {
							sasukePlayer.sendMessage("§6 - " + joueurs.getPlayerName() + " : " + joueurs.getRole().getRoleName());
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
						for (PlayerManager nagato : narutoV2Manager.getPlayerManagersWithRole(Nagato.class)) {
							sasukePlayer.sendMessage("§c - " + nagato.getPlayerName());
						}
					} else {
						itachiDead = true;
					}
				}
			}
				
		}
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

	@Override
	public Chakra getChakra() {
		return Chakra.KATON;
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
	
	@Override
	public void afterRoles(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		joueur.setCamps(Camps.OROCHIMARU);
		player.setMaxHealth(player.getMaxHealth() + (2D*5D));
		player.setHealth(player.getMaxHealth());

		Bukkit.getScheduler().runTaskTimer(main, () -> {
			if(!killedUchiwa && hasUsedIzanami){
				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, false, false));
			}
		}, 0, 20*60);
	}

	@Override
	public List<ModeSubCommand> getSubCommands() {
		return Arrays.asList(new CmdIzanagi(main));
	}

	@Override
	public boolean hasUsedIzanagi() {
		return hasUsedIzanagi;
	}

	@Override
    public void setHasUsedIzanagi(boolean hasUsedIzanagi) {
        this.hasUsedIzanagi = hasUsedIzanagi;
    }

	public boolean isKilledItachi() {
		return killedItachi;
	}

	public void setKilledItachi(boolean killedItachi) {
		this.killedItachi = killedItachi;
	}

	@Override
    public void useTsukuyomi() {
        tsukuyomiUses++;
    }

	@Override
    public int getTsukuyomiUses() {
        return tsukuyomiUses;
    }

	@Override
    public void useIzanami() {
        hasUsedIzanami = true;
    }

    @Override
    public boolean hasUsedIzanami() {
        return hasUsedIzanami;
    }

	@Override
	public boolean hasKilledUchiwa() {
		return killedUchiwa;
	}

	@Override
	public boolean isCompleteIzanami() {
		return false;
	}

	@Override
	public void setCompleteIzanami(boolean completeIzanami) {

	}

	@Override
	public IzanamiGoal getIzanamiGoal() {
		return izanamiGoal;
	}

	@Override
	public void setIzanamiGoal(IzanamiGoal izanamiGoal) {
		this.izanamiGoal = izanamiGoal;
	}

	public void setTsukuyomiUses(int tsukuyomiUses) {
		this.tsukuyomiUses = tsukuyomiUses;
	}

	public boolean isHasUsedIzanami() {
		return hasUsedIzanami;
	}

	public void setHasUsedIzanami(boolean hasUsedIzanami) {
		this.hasUsedIzanami = hasUsedIzanami;
	}
	
}
