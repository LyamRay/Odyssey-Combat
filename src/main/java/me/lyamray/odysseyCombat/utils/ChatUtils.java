package me.lyamray.odysseyCombat.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@UtilityClass
public class ChatUtils {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public Component color(String message) {
        return miniMessage.deserialize(message);
    }

    public String componentToString (Component component) {
        return miniMessage.serialize(component);
    }
}