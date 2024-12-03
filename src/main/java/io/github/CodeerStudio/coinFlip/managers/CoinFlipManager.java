package io.github.CodeerStudio.coinFlip.managers;

import io.github.CodeerStudio.coinFlip.data.CoinFlipData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CoinFlipManager {

    private final Map<String, CoinFlipData> activeCoinFlips = new HashMap<>();
    private final Random random = new Random();

    public void addCoinFlip(String string, CoinFlipData coinFlipData) {
        this.activeCoinFlips.put(string, coinFlipData);
    }

    public void removeCoinFlip(String string, CoinFlipData coinFlipData) {
        this.activeCoinFlips.remove(string, coinFlipData);
    }

    public boolean getCoinFlipKey(String key) {
        if (activeCoinFlips.containsKey(key)) {
            return true;
        }
        return false;
    }

    public CoinFlipData getActiveCoinFlip(String key) {
        if (activeCoinFlips.get(key) != null) {
            return activeCoinFlips.get(key);
        }

        return null;
    }
}
