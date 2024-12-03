package io.github.CodeerStudio.coinFlip;

import io.github.CodeerStudio.coinFlip.api.VaultAPI;
import io.github.CodeerStudio.coinFlip.commands.CoinFlipAcceptCMD;
import io.github.CodeerStudio.coinFlip.commands.CoinFlipCMD;
import io.github.CodeerStudio.coinFlip.managers.CoinFlipManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoinFlip extends JavaPlugin {


    @Override
    public void onEnable() {

        if (!VaultAPI.setUpEconomy(this)) {
            getLogger().severe("Disabling plugin due to missing Vault economy!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("CoinFlip Plugin has been enabled!");

        CoinFlipManager coinFlipManager = new CoinFlipManager();

        getCommand("coinflip").setExecutor(new CoinFlipCMD(coinFlipManager));
        getCommand("coinflipaccept").setExecutor(new CoinFlipAcceptCMD(coinFlipManager));

    }

    @Override
    public void onDisable() {
        getLogger().info("CoinFlip Plugin has been disabled!");
    }
}
