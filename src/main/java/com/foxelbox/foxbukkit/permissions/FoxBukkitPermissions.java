/**
 * This file is part of FoxBukkitPermissions.
 *
 * FoxBukkitPermissions is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FoxBukkitPermissions is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FoxBukkitPermissions.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.foxelbox.foxbukkit.permissions;

import com.foxelbox.dependencies.config.Configuration;
import com.foxelbox.dependencies.redis.RedisManager;
import com.foxelbox.dependencies.threading.SimpleThreadCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FoxBukkitPermissions extends JavaPlugin implements Listener {
	Configuration configuration;
	RedisManager redisManager;
	FoxBukkitPermissionHandler handler;

	private StringBuilder makeMessageBuilder() {
		return new StringBuilder("\u00a75[FBP] \u00a7f");
	}

	public FoxBukkitPermissionHandler getHandler() {
		return handler;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Utils.patchPlayer(this, event.getPlayer());
	}

	@Override
	public void onEnable() {
		handler = new FoxBukkitPermissionHandler(this);
		configuration = new Configuration(getDataFolder());
		redisManager = new RedisManager(new SimpleThreadCreator(), configuration);

		handler.load();

		getServer().getPluginManager().registerEvents(this, this);

		getServer().getPluginCommand("reloadpermissions").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
				handler.reload();
				commandSender.sendMessage(makeMessageBuilder().append("Permissions reloaded").toString());
				return true;
			}
		});

		for(Player player : getServer().getOnlinePlayers()) {
			Utils.patchPlayer(this, player);
		}
	}
}
