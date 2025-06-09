package me.lyamray.odysseyCombat.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class CombatMessages {


    public void combatGetHitMessage(String attacker, Player hitted) {

        String message = "<color:#8f7c7e>[<b><gradient:#ff0000:#ad2532>Odyssey Combat</gradient></b><color:#8f7c7e>] " +
                "<gradient:#da2525:#c43636>Je bent geraakt door " + attacker + "! Log niet uit.</gradient></color>";
        hitted.sendMessage(ChatUtils.color(message));
    }

    public void combatHitMessage(String hitted, Player attacker) {

        String message = "<color:#8f7c7e>[<b><gradient:#ff0000:#ad2532>Odyssey Combat</gradient></b><color:#8f7c7e>] " +
                "<gradient:#da2525:#c43636>Je hebt " + hitted + " geraakt! Log niet uit.</gradient></color>";

        attacker.sendMessage(ChatUtils.color(message));
    }
}
