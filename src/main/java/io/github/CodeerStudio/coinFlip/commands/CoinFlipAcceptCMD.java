package io.github.CodeerStudio.coinFlip.commands;

import io.github.CodeerStudio.coinFlip.data.CoinFlipData;
import io.github.CodeerStudio.coinFlip.managers.CoinFlipManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoinFlipAcceptCMD implements CommandExecutor {

    private final CoinFlipManager coinFlipManager;

    public CoinFlipAcceptCMD(CoinFlipManager coinFlipManager) {
        this.coinFlipManager = coinFlipManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length < 1) {
            player.sendMessage("Usage: /coinflipaccept <player>");
            return true;
        }

        String inviterName = args[0];

        // Validate inviter
        Player inviter = Bukkit.getPlayer(inviterName);
        if (inviter == null || !inviter.isOnline()) {
            player.sendMessage("The specified player is not online or does not exist.");
            return true;
        }

        // Resolve the coinflip
        String errorMessage = coinFlipManager.resolveCoinFlip(inviterName, player);
        if (errorMessage != null) {
            player.sendMessage(errorMessage);
        }

        return true;
    }
}
