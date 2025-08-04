package com.example.randomstatus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStatusMod implements ModInitializer {
    private static final int TICK_INTERVAL = 2400; // 2 minutes (20 ticks per second * 60 seconds * 2 minutes)
    private int tickCounter = 0;
    private final Random random = new Random();
    private final List<StatusEffect> statusEffects = new ArrayList<>();

    @Override
    public void onInitialize() {
        // Initialize the list of status effects
        for (StatusEffect effect : Registry.STATUS_EFFECT) {
            if (effect != null) {
                statusEffects.add(effect);
            }
        }

        // Register a server tick event
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;
            if (tickCounter >= TICK_INTERVAL) {
                tickCounter = 0;
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    applyRandomStatusEffect(player);
                }
            }
        });
    }

    private void applyRandomStatusEffect(ServerPlayerEntity player) {
        if (statusEffects.isEmpty()) return;
        StatusEffect randomEffect = statusEffects.get(random.nextInt(statusEffects.size()));
        player.addStatusEffect(new StatusEffectInstance(randomEffect, 2400, 0)); // 2 minutes duration
    }
}