package io.github.CodeerStudio.coinFlip.data;

import org.bukkit.entity.Player;

public class CoinFlipData {
    private final Player creator;
    private final double amount;
    private final Player targetPlayer;

    public CoinFlipData(Player creator, double amount, Player targetPlayer) {
        this.creator = creator;
        this.amount = amount;
        this.targetPlayer = targetPlayer;
    }

    public Player getCreator() {
        return creator;
    }

    public double getAmount() {
        return amount;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }
}
