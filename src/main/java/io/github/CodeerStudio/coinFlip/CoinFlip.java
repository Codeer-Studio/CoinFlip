package io.github.CodeerStudio.coinFlip;

import io.github.CodeerStudio.coinFlip.commands.CoinFlipAcceptCMD;
import io.github.CodeerStudio.coinFlip.commands.CoinFlipCMD;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoinFlip extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("CoinFlip Plugin has been enabled!");

        getCommand("coinflip").setExecutor(new CoinFlipCMD());
        getCommand("coinflipaccept").setExecutor(new CoinFlipAcceptCMD());

    }

    @Override
    public void onDisable() {
        getLogger().info("CoinFlip Plugin has been disabled!");
    }
}
