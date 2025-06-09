package me.lyamray.odysseyCombat.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;

@UtilityClass
public class CombatMessages {

    public Component combatGetHitMessage(String attacker) {

        String message = "<color:#8f7c7e>[<b><gradient:#ff0000:#ad2532>Odyssey Combat</gradient></b><color:#8f7c7e>] " +
                "<gradient:#da2525:#c43636>Je bent geraakt door " + attacker + "! Log niet uit.</gradient></color>";

        return ChatUtils.color(message);
    }

    public Component combatHitMessage(String hitted) {

        String message = "<color:#8f7c7e>[<b><gradient:#ff0000:#ad2532>Odyssey Combat</gradient></b><color:#8f7c7e>] " +
                "<gradient:#da2525:#c43636>Je hebt " + hitted + " geraakt! Log niet uit.</gradient></color>";

        return ChatUtils.color(message);
    }
}
