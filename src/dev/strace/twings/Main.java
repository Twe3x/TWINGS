package dev.strace.twings;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev.strace.twings.api.SpigotMcAPI;
import dev.strace.twings.commands.WingsCommand;
import dev.strace.twings.listener.InventoryClickListener;
import dev.strace.twings.listener.PlayerConnectionListener;
import dev.strace.twings.listener.PlayerMoveListener;
import dev.strace.twings.players.CurrentWings;
import dev.strace.twings.players.PlayWings;
import dev.strace.twings.utils.ConfigManager;
import dev.strace.twings.utils.Messages;
import dev.strace.twings.utils.MyColors;
import dev.strace.twings.utils.SendWings;
import dev.strace.twings.utils.WingPreview;
import dev.strace.twings.utils.WingReader;
import dev.strace.twings.utils.WingTemplate;

/**
 * 
 * @author Jason Holweg [STRACE] <b>TWINGS</b><br>
 *         Website: <a>https://strace.dev/</a><br>
 *         GitHub: <a>https://github.com/MrStrace</a><br>
 *         Created: May 31, 2021<br>
 *
 */
public class Main extends JavaPlugin {

	public static Main instance;
	public static Plugin plugin;
	public ConfigManager config;
	public Messages msg;

	@Override
	public void onEnable() {
		// Init plugin
		plugin = this;
		// Init instance
		instance = this;

		// Check Plugin version (is it uptodate?)
		checkVersion();

		// Creates or loads the Config.yml
		config = new ConfigManager("config");

		// Config is getting written (Defaults)
		registerConfig();

		// Init lang.yml
		msg = new Messages().init();
		
		// Init Template.yml (a Example of an Wing)
		new WingTemplate();

		// All Listeners getting enabled.
		registerListener();

		// Init WingReader all Wings getting saved in cached.
		new WingReader().registerWings();

		// Init Wing animation.
		new SendWings().enableAnimated();

		// Wings getting displayed every ticks.
		new PlayWings().playOnPlayers();

		// Enables animated Wings.
		new PlayWings().enableAnimated();

		this.getCommand("wings").setExecutor(new WingsCommand());

		// Players getting there old Wings equipped (Important after reload)
		new CurrentWings().onEnable();

		// Enables the Wing previews
		new WingPreview().enablePreview();
		
		// enablePreview();
		System.out.println("[TWINGS] Enabled!");
	}

	@Override
	public void onDisable() {
		new CurrentWings().onDisable();
	}

	public long reload() {
		long milis = System.currentTimeMillis();
		checkVersion();
		saveConfig();
		reloadConfig();
		// enablePreview();

		long now = System.currentTimeMillis() - milis;
		System.out.println("[PixelStrace's Twings] Reload complete!" + "(" + now + "ms)");
		return now;
	}

	public static void checkVersion() {
		try {
			if (!plugin.getDescription().getVersion().equalsIgnoreCase(new SpigotMcAPI("82088").getVersion())) {
				System.out.println(
						"[PixelStrace's Twings] isn't uptodate! Visit https://www.spigotmc.org/resources/twings-1-16.82088/ to update!");
			} else {
				System.out.println("[PixelStrace's Twings] Plugin is uptodate!");
				System.out.println("[PixelStrace's Twings] Thanks for downloading and using my plugin!");
			}
		} catch (IOException e) {
		}

	}

	private void registerListener() {
		PluginManager pm = Bukkit.getPluginManager();
		;
		pm.registerEvents(new PlayerMoveListener(), this);
		pm.registerEvents(new PlayerConnectionListener(), this);
		pm.registerEvents(new InventoryClickListener(), this);
	}

	private void registerConfig() {
		config.addDefault("Prefix", "&e&lTWings");
		config.addDefault("Wings.showwithperms", false);
		config.addDefault("Wings.updaterate", 3);
		config.addDefault("Menu.title", "%prefix% &9Choose your Wings!");
		config.addDefault("Menu.symbol", "⏹");
		config.addDefault("Menu.creator", "&c&lCreator:&f %creator%");
		config.addDefault("Menu.permissions", "&7unlocked: [%perms%&7]");
		config.addDefault("Menu.preview", true);
		config.addDefault("haspermission", "&aYES");
		config.addDefault("nopermission", "&cNO");
		config.save();
	}

	public String getPrefix() {
		if (this.getConfig().getString("Prefix") == null)
			return "ERROR";
		String prefix = MyColors.format(this.getConfig().getString("Prefix"));
		return prefix;
	}

	public String getConfigString(String path) {
		if (this.getConfig().getString(path) == null)
			return "ERROR";
		String string = MyColors.format(this.getConfig().getString(path));
		return string;
	}

	
	
	public Messages getMsg() {
		return msg;
	}

	public static Main getInstance() {
		return instance;
	}

	public Plugin getPlugin() {
		return plugin;
	}
}