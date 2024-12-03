package io.github.CodeerStudio.coinFlip.data;

import org.bukkit.entity.Player;

public class CoinFlipData {
    private final Player inviter;
    private final double amount;
    private final Player targetPlayer;

    public CoinFlipData(Player inviter, double amount, Player targetPlayer) {
        this.inviter = inviter;
        this.amount = amount;
        this.targetPlayer = targetPlayer;
    }

    public Player getInviter() {
        return inviter;
    }

    public double getAmount() {
        return amount;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }
}
