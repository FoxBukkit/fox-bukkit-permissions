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
import org.bukkit.plugin.java.JavaPlugin;

public class FoxBukkitPermissions extends JavaPlugin {
	public static FoxBukkitPermissions instance;

	public Configuration configuration;

	public RedisManager redisManager;

	private StringBuilder makeMessageBuilder() {
		return new StringBuilder("\u00a75[FBP] \u00a7f");
	}

	@Override
	public void onEnable() {
		instance = this;
		configuration = new Configuration(getDataFolder());
		redisManager = new RedisManager(new SimpleThreadCreator(), configuration);

		getServer().getPluginManager().registerEvents(new PermissionsPlayerListener(), this);
	}
}
