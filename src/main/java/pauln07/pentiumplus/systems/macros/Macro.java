 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.macros;

import pauln07.pentiumplus.utils.misc.ISerializable;
import pauln07.pentiumplus.utils.misc.Keybind;
import pauln07.pentiumplus.utils.misc.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pauln07.pentiumplus.PentiumPlus.mc;

public class Macro implements ISerializable<Macro> {
    public String name = "";
    public List<String> messages = new ArrayList<>(1);
    public Keybind keybind = Keybind.none();

    public void addMessage(String command) {
        messages.add(command);
    }

    public void removeMessage(int i) {
        messages.remove(i);
    }

    public boolean onAction(boolean isKey, int value) {
        if (keybind.matches(isKey, value) && mc.currentScreen == null) {
            for (String command : messages) {
                mc.player.sendChatMessage(command);
            }

            return true;
        }

        return false;
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        // General
        tag.putString("name", name);
        tag.put("keybind", keybind.toTag());

        // Messages
        NbtList messagesTag = new NbtList();
        for (String message : messages) messagesTag.add(NbtString.of(message));
        tag.put("messages", messagesTag);

        return tag;
    }

    @Override
    public Macro fromTag(NbtCompound tag) {
        name = tag.getString("name");

        if (tag.contains("key")) keybind.set(true, tag.getInt("key"));
        else keybind.fromTag(tag.getCompound("keybind"));

        messages = NbtUtils.listFromTag(tag.getList("messages", 8), NbtElement::asString);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Macro macro = (Macro) o;
        return Objects.equals(name, macro.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
