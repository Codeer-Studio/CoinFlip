package io.github.CodeerStudio.coinFlip.managers;

import io.github.CodeerStudio.coinFlip.api.VaultAPI;
import io.github.CodeerStudio.coinFlip.data.CoinFlipData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CoinFlipManager {

    private final Map<String, CoinFlipData> activeCoinFlips = new HashMap<>();
    private final Random random = new Random();
    private final MoneyManager moneyManager = new MoneyManager();

    public void addCoinFlip(String key, CoinFlipData coinFlipData) {
        this.activeCoinFlips.put(key, coinFlipData);
    }

    public void removeCoinFlip(String key) {
        this.activeCoinFlips.remove(key);
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

        if (moneyManager.hasEnoughMoney(inviter, amount)) {
            return inviter.getName() + " does not have enough money";
        }

        if (moneyManager.hasEnoughMoney(targetPlayer, amount)) {
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
        Bukkit.broadcastMessage(inviter.getName() + " and " + targetPlayer.getName() + " are flipping a coin for " + amount + " coins!");
        Bukkit.broadcastMessage("The coin landed on " + result + "!");
        winner.sendMessage("You won the coinflip and earned " + amount + " coins!");
        loser.sendMessage("You lost the coinflip and lost " + amount + " coins.");

        // Remove the coinflip
        removeCoinFlip(inviterName);

        return null; // Null means success
    }
}
