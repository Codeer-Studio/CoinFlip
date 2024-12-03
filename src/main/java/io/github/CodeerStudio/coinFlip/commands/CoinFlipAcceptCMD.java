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
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length < 1) {
            player.sendMessage("Usage: /coinflipaccept <player>");
            return true;
        }

        String inviterName = strings[0];
        Player inviter = Bukkit.getPlayer(inviterName);

        if (inviter == null || !inviter.isOnline()) {
            player.sendMessage("The specified player is not online or does not exist.");
            return true;
        }

        // Check if the inviter has an active coinflip
        if (!coinFlipManager.getCoinFlipKey(inviterName)) {
            player.sendMessage("No active coinflip found from " + inviterName + ".");
            return true;
        }

        CoinFlipData coinFlipData = coinFlipManager.getActiveCoinFlip(inviterName);

        if (!coinFlipData.getTargetPlayer().equals(player)) {
            player.sendMessage("You were not invited to this coinflip.");
            return true;
        }

        // Randomize the outcome
        String result = new java.util.Random().nextBoolean() ? "heads" : "tails";

        Bukkit.broadcastMessage(inviterName + " and " + player.getName() + " are flipping a coin for " + coinFlipData.getAmount() + " coins!");
        Bukkit.broadcastMessage("The coin landed on " + result + "!");

        // Determine winner and loser
        Player winner = result.equals("heads") ? inviter : player;
        Player loser = winner.equals(inviter) ? player : inviter;

        // Notify players of the outcome
        winner.sendMessage("You won the coinflip and earned " + coinFlipData.getAmount() + " coins!");
        loser.sendMessage("You lost the coinflip and lost " + coinFlipData.getAmount() + " coins.");

        // TODO: Integrate with your economy plugin to handle transactions here.

        // Clean up the coinflip
        coinFlipManager.removeCoinFlip(inviterName, coinFlipData);

        return true;
    }
}
