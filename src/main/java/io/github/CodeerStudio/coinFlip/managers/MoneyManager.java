package io.github.CodeerStudio.coinFlip.managers;

import io.github.CodeerStudio.coinFlip.api.VaultAPI;
import org.bukkit.entity.Player;

public class MoneyManager {

    public boolean hasEnoughMoney(Player player, double amount) {
        return VaultAPI.getEconomy().has(player, amount);
    }

    public void withdrawMoney(Player player, double amount) {
        VaultAPI.getEconomy().withdrawPlayer(player, amount);
    }

    public void depositMoney(Player player, double amount) {
        VaultAPI.getEconomy().depositPlayer(player, amount);
    }
}
