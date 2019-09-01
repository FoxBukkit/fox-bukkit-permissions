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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMap extends ConcurrentHashMap<String, String> {
    public static CacheMap create(String name) {
        return new CacheMap();
    }

    private Set<OnChangeHook> hooks = new HashSet<>();
    private final Object onChangeLock = new Object();

    public interface OnChangeHook {
        /**
         * Entry change delegate functional interface
         * @param key Key of changed entry
         * @param value Value of changed entry (null if removed)
         */
        void onEntryChanged(String key, String value);
    }

    public void addOnChangeHook(OnChangeHook hook)  {
        synchronized (onChangeLock) {
            this.hooks.add(hook);
        }
    }

    private void triggerChange(String key, String value) {
        synchronized (onChangeLock) {
            for (OnChangeHook hook : hooks) {
                hook.onEntryChanged(key, value);
            }
        }
    }

    @Override
    public String put(String key, String value) {
        String res = super.put(key, value);
        triggerChange(key, value);
        return res;
    }

    @Override
    public String remove(Object key) {
        String res = super.remove(key);
        triggerChange(key.toString(), null);
        return res;
    }

    @Override
    public void clear() {
        Set<String> allKeys = new HashSet<>(this.keySet());
        super.clear();
        for (String key : allKeys)  {
            triggerChange(key, null);
        }
    }
}
