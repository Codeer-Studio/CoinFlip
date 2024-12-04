package io.github.CodeerStudio.coinFlip.commands;

import io.github.CodeerStudio.coinFlip.data.CoinFlipData;
import io.github.CodeerStudio.coinFlip.managers.CoinFlipManager;
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

    private final CoinFlipManager coinFlipManager;

    public CoinFlipCMD(CoinFlipManager coinFlipManager) {
        this.coinFlipManager = coinFlipManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player inviter = (Player) sender;

        // Validate arguments
        if (args.length < 2) {
            inviter.sendMessage("Usage: /coinflip <amount> <player>");
            return true;
        }

        // Parse the amount
        double amount;
        try {
            amount = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            inviter.sendMessage("Please enter a valid amount.");
            return true;
        }

        // Get the target player
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            inviter.sendMessage("The specified player is not online.");
            return true;
        }

        // Delegate coinflip creation to the manager
        String errorMessage = coinFlipManager.createCoinFlip(inviter, targetPlayer, amount);
        if (errorMessage != null) {
            inviter.sendMessage(errorMessage);
            return true;
        }

        // Notify both players
        inviter.sendMessage("You have invited " + targetPlayer.getName() + " to a coinflip for " + amount);
        targetPlayer.sendMessage(inviter.getName() + " has invited you to a coinflip for " + amount + " Type /cfaccept " + inviter.getName() + " to accept.");

        return true;
    }
}
