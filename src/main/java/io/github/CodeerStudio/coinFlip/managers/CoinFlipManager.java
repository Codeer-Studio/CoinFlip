package io.github.CodeerStudio.coinFlip.managers;

import io.github.CodeerStudio.coinFlip.CoinFlip;
import io.github.CodeerStudio.coinFlip.api.VaultAPI;
import io.github.CodeerStudio.coinFlip.data.CoinFlipData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CoinFlipManager {

    private final Map<String, CoinFlipData> activeCoinFlips = new HashMap<>();
    private final Map<String, BukkitTask> timeoutTasks = new HashMap<>();
    private final Random random = new Random();
    private final MoneyManager moneyManager = new MoneyManager();
    private final CoinFlip plugin;

    public CoinFlipManager(CoinFlip plugin) {
        this.plugin = plugin;
    }

    public void addCoinFlip(String key, CoinFlipData coinFlipData) {
        this.activeCoinFlips.put(key, coinFlipData);

        // Schedule a timeout task
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (activeCoinFlips.containsKey(key)) {
                cancelCoinFlip(key);
                Player inviter = coinFlipData.getInviter();
                inviter.sendMessage("Your coinflip has been canceled due to no response.");
                Player target = coinFlipData.getTargetPlayer();
                if (target.isOnline()) {
                    target.sendMessage("The coinflip invitation from " + inviter.getName() + " has expired.");
                }
            }
        }, 15 * 20L); // 15 seconds, 20 ticks per second

        timeoutTasks.put(key, task);
    }

    public void removeCoinFlip(String key) {
        activeCoinFlips.remove(key);

        // Cancel and remove the timeout task if it exists
        BukkitTask task = timeoutTasks.remove(key);
        if (task != null) {
            task.cancel();
        }
    }

    public void cancelCoinFlip(String key) {
        removeCoinFlip(key);
    }

    public boolean hasActiveCoinFlip(String key) {
        return activeCoinFlips.containsKey(key);
    }

    public CoinFlipData getActiveCoinFlip(String key) {
        return activeCoinFlips.get(key);
    }

    public String createCoinFlip(Player inviter, Player targetPlayer, double amount) {

        // Ensure the inviter does not already have an active coinflip
        if (hasActiveCoinFlip(inviter.getName())) {
            return "You already have an active coinflip!";
        }

        // Ensure the target player is not already in an active coinflip
        if (hasActiveCoinFlip(targetPlayer.getName())) {
            return "The player you invited is already in a coinflip.";
        }

        // Create and store the coinflip
        CoinFlipData coinFlipData = new CoinFlipData(inviter, amount, targetPlayer);
        addCoinFlip(inviter.getName(), coinFlipData);

        return null; // Null indicates success
    }


    public String resolveCoinFlip(String inviterName, Player targetPlayer) {
        CoinFlipData coinFlipData = getActiveCoinFlip(inviterName);

        // Determine if the target is valid
        if (coinFlipData == null || !coinFlipData.getTargetPlayer().equals(targetPlayer)) {
            return "No active coinflip found for this player or you were not invited.";
        }


        // Players involved
        Player inviter = coinFlipData.getInviter();
        double amount = coinFlipData.getAmount();

        if (!moneyManager.hasEnoughMoney(inviter, amount)) {
            return inviter.getName() + " does not have enough money";
        }

        if (!moneyManager.hasEnoughMoney(targetPlayer, amount)) {
            return targetPlayer.getName() + " does not have enough money";
        }

        moneyManager.withdrawMoney(inviter, amount);
        moneyManager.withdrawMoney(targetPlayer, amount);


        // Determine the result
        String result = random.nextBoolean() ? "heads" : "tails";

        // Winner and loser
        Player winner = result.equals("heads") ? inviter : targetPlayer;
        Player loser = winner.equals(inviter) ? targetPlayer : inviter;

        moneyManager.depositMoney(winner, amount * 2);

        // Broadcast results
        Bukkit.broadcastMessage(inviter.getName() + " and " + targetPlayer.getName() + " are flipping a coin for " + amount);
        Bukkit.broadcastMessage("The coin landed on " + result + "!");
        winner.sendMessage("You won the coinflip and earned " + amount);
        loser.sendMessage("You lost the coinflip and lost " + amount);

        // Remove the coinflip
        removeCoinFlip(inviterName);

        return null; // Null means success
    }
}
