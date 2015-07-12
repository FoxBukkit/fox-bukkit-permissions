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

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.*;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public class FoxBukkitPermissibleBase extends PermissibleBase {
	private final Player ply;

	private final FoxBukkitPermissionHandler handler;

	public FoxBukkitPermissibleBase(Player ply, FoxBukkitPermissions plugin) {
		super(ply);
		this.handler = plugin.handler;
		this.ply = ply;
		recalculatePermissions();
	}

	private boolean isBaseOp() {
		return ply.isOp();
	}

	@Override
	public boolean isOp() {
		return isBaseOp() || hasPermission("bukkit.base.op");
	}

	@Override
	public void setOp(boolean arg0) {
		ply.setOp(arg0);
		recalculatePermissions();
	}

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
    	recalculatePermissions();
    	return null;
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
    	recalculatePermissions();
    	return null;
    }

    public void removeAttachment(PermissionAttachment attachment) {
    	recalculatePermissions();
    }
    
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
    	recalculatePermissions();
        return null;
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
    	recalculatePermissions();
    	return null;
    }

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return new HashSet<>();
	}

	@Override
	public boolean hasPermission(String arg0) {
		return handler.has(ply, arg0.toLowerCase());
	}

	@Override
	public boolean hasPermission(Permission arg0) {
		return hasPermission(arg0.getName());
	}

	@Override
	public boolean isPermissionSet(String arg0) {
		return true;
	}

	@Override
	public boolean isPermissionSet(Permission arg0) {
		return true;
	}

	@Override
	public void recalculatePermissions() {

	}
}
