package fr.lastril.uhchost.tools;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class I18n {

	private static Lang lang;

	public static Lang getLang() {
		return lang;
	}

	public static void setLang(Lang lang) {
		I18n.lang = lang;
	}

	public static void supportTranslate(Plugin plugin, Lang lang) {
		if (!(new File(plugin.getDataFolder(), "lang")).exists())
			(new File(plugin.getDataFolder(), "lang")).mkdir();
		File f = new File(plugin.getDataFolder() + File.separator + "lang",
				"lang." + lang.getCode().toLowerCase() + ".yml");
		if (f == null || !f.exists()) {
			plugin.getLogger().warning("Le fichier lang." + lang.getCode().toLowerCase() + ".yml n'existe pas ! Creation en cours...");
			try {
				f.createNewFile();
				FileUtils.copyInputStreamToFile(plugin.getResource("lang." + lang.getCode().toLowerCase() + ".yml"), f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (f.exists()) {
			try {
				YamlConfiguration yamlConfiguration1 = YamlConfiguration.loadConfiguration(f);
				File configFile = new File("temp.yml");
				FileUtils.copyInputStreamToFile(plugin.getResource("lang." + lang.getCode().toLowerCase() + ".yml"),configFile);
				YamlConfiguration yamlConfiguration2 = YamlConfiguration.loadConfiguration(configFile);
				for (String s : yamlConfiguration2.getKeys(true)) {
					if (yamlConfiguration1.get(s) == null) {
						Bukkit.getConsoleSender().sendMessage(
								ChatColor.RED + "Translation file : " + f.getName() + " has receive new path : " + s);
						yamlConfiguration1.set(s, yamlConfiguration2.get(s));
						yamlConfiguration1.save(f);
					}
				}
				if(yamlConfiguration1.contains("scenarios.gotohell.message")) {
					if (yamlConfiguration1.getString("scenarios.gotohell.message").equalsIgnoreCase("&5(&6Go To Hell&5)&6Si tu ne sors pas du nether, tu prendras des d[,]gats toutes les 10 secondes !")) {
						yamlConfiguration1.set("scenarios.gotohell.message","&5(&6Go To Hell&5)&6Si tu ne vas pas dans le nether, tu prendras des d[,]gats toutes les 10 secondes !");
						yamlConfiguration1.save(f);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(f);
		for (String l : yamlConfiguration.getRoot().getKeys(true))
			lang.getWords().put(l, yamlConfiguration.getString(l));
	}

	public static String tl(String key, String... values) {
		if (lang.getWords().containsKey(key)) {
			String text = lang.getWords().get(key);
			MessageFormat messageFormat = new MessageFormat(text);
			text = text.replaceAll("\\{\\D*?\\}", "\\[$1\\]");
			return messageFormat.format(values).replaceAll("\\[,\\]", "é").replaceAll("\\(,\\)", "è")
					.replaceAll("\\],\\[", "ê").replaceAll("\\|,\\|", "ô").replaceAll("\\_,\\_", "î")
					.replaceAll("\\#,\\#", "'").replaceAll("\\+,\\+", "à").replaceAll("\\),\\(", "ç")
					.replaceAll("\\-,\\-", "ñ").replaceAll("\\*,\\*", "â").replaceAll("&", "§");
		}
		return key;
	}

}
