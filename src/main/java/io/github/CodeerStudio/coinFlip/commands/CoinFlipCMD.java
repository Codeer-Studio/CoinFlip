package io.github.CodeerStudio.coinFlip.commands;

import io.github.CodeerStudio.coinFlip.data.CoinFlipData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CoinFlipCMD implements CommandExecutor {

    private final Map<String, CoinFlipData> activeCoinFlips = new HashMap<>();
    private final Random random = new Random();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length < 2) {
            player.sendMessage("Usage: /coinflip <amount> <person>");
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(strings[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("Please enter a valid amount.");
            return true;
        }

        // Find the target player
        Player targetPlayer = Bukkit.getPlayer(strings[1]);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage("The specified player is not online.");
            return true;
        }

        // Ensure the inviter does not already have an active coinflip
        if (activeCoinFlips.containsKey(player.getName())) {
            player.sendMessage("You already have an active coinflip!");
            return true;
        }

        // Ensure the target player is not already in an active coinflip
        if (activeCoinFlips.containsKey(targetPlayer.getName())) {
            player.sendMessage("The player you invited is already in a coinflip.");
            return true;
        }


        CoinFlipData coinFlipData = new CoinFlipData(player, amount, player);
        activeCoinFlips.put(player.getName(), coinFlipData);

        player.sendMessage("You have invited " + targetPlayer.getName() + " to a coinflip for " + amount + " coins!");
        targetPlayer.sendMessage(player.getName() + " has invited you to a coinflip for " + amount + " coins! Type /coinflip accept " + player.getName() + " to accept.");

        return true;
    }

}
