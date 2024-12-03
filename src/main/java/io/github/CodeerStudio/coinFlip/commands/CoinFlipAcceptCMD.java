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
        CoinFlipData coinFlipData = coinFlipManager.getActiveCoinFlip(inviterName);

        if (coinFlipData == null) {
            player.sendMessage("No active coinflip found from " + inviterName + ".");
            return true;
        }

        if (!coinFlipData.getTargetPlayer().equals(player)) {
            player.sendMessage("You were not invited to this coinflip.");
            return true;
        }

        // Resolve coinflip
        String result = coinFlipManager.resolveCoinFlip(inviterName, player);
        if (result != null) {
            player.sendMessage(result);
        }

        return true;
    }
}
