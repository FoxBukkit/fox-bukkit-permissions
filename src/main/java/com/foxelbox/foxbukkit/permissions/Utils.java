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

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static UUID CONSOLE_UUID = UUID.nameUUIDFromBytes("[CONSOLE]".getBytes());

    private static final Class<?> craftHumanEntityClass;
    static {
        try {
            String packageName = null;
            Pattern packagePattern = Pattern.compile("^(org\\.bukkit\\.craftbukkit\\.v[0-9_R]+)(\\.|$)");
            for(Package p : Package.getPackages()) {
                String pName = p.getName();
                Matcher m = packagePattern.matcher(pName);
                if(m.matches()) {
                    packageName = m.group(1);
                    break;
                }
            }
            craftHumanEntityClass = Class.forName(packageName + ".entity.CraftHumanEntity");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void patchPlayer(FoxBukkitPermissions plugin, Player player) {
        plugin.getServer().getPluginManager().subscribeToPermission(Server.BROADCAST_CHANNEL_USERS, player);
        try {
            Utils.setPrivateValue(craftHumanEntityClass, player, "perm", new FoxBukkitPermissibleBase(player, plugin));
        } catch (Exception e) {
            e.printStackTrace();
            player.kickPlayer("You != CraftHumanEntity");
        }
    }

    public static UUID getCommandSenderUUID(CommandSender commandSender) {
        if(commandSender instanceof Player) {
            return ((Player) commandSender).getUniqueId();
        }
        if(commandSender instanceof ConsoleCommandSender) {
            return CONSOLE_UUID;
        }
        return UUID.nameUUIDFromBytes(("[CSUUID:" + commandSender.getClass().getName() + "]").getBytes());
    }

    public static String getCommandSenderDisplayName(CommandSender commandSender) {
        if(commandSender instanceof Player) {
            return ((Player) commandSender).getDisplayName();
        }
        return commandSender.getName();
    }

    public static void setPrivateValue(Class<?> instanceclass, Object instance, String field, Object value) {
        try
        {
            Field field_modifiers = Field.class.getDeclaredField("modifiers");
            field_modifiers.setAccessible(true);

            Field f = instanceclass.getDeclaredField(field);
            int modifiers = field_modifiers.getInt(f);

            if ((modifiers & Modifier.FINAL) != 0)
                field_modifiers.setInt(f, modifiers & ~Modifier.FINAL);

            f.setAccessible(true);
            f.set(instance, value);
        }
        catch (Exception e) {
            System.err.println("Could not set field \"" + field + "\" of class \"" + instanceclass.getCanonicalName() + "\" because \"" + e.getMessage() + "\"");
        }
    }
}
