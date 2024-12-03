package io.github.CodeerStudio.coinFlip.api;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultAPI {

    private static Economy economy = null;

    public static boolean setUpEconomy(JavaPlugin plugin) {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Vault is not installed or enabled!");
            return false; // Return false if Vault is not available
        }

        // Get the economy provider from Vault
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().severe("No economy provider found!");
            return false; // Return false if no economy provider is found
        }

        // Set the economy instance
        economy = rsp.getProvider();
        plugin.getLogger().info("Economy provider found: " + economy.getName());
        return true; // Return true if the economy provider is successfully found
    }

    public static Economy getEconomy() {
        return economy;
    }

}
