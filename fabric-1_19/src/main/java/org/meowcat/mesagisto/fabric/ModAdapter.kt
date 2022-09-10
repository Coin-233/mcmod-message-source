package org.meowcat.mesagisto.fabric

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent

import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.meowcat.mesagisto.fabric.impl.ChatImpl
import org.mesagisto.mcmod.ModEntry
import java.util.concurrent.CompletableFuture

val logger: Logger = LogManager.getLogger("mesagisto")

class ModAdapter : ModInitializer {
  private lateinit var server: MinecraftServer
  override fun onInitialize() {
    ServerLifecycleEvents.SERVER_STARTED.register {
      server = it
      ChatImpl.server = it
      ModEntry.onEnable()
    }
    ServerMessageDecoratorEvent.EVENT.register(
      ServerMessageDecoratorEvent.CONTENT_PHASE
    ) { player: ServerPlayerEntity?, component: Text ->
      if (player == null) return@register CompletableFuture.completedFuture(component)
      ChatImpl.deliverChatEvent(player, component)
      CompletableFuture.completedFuture(component)
    }
    ServerLifecycleEvents.SERVER_STOPPING.register {
      ModEntry.onDisable()
    }
  }
}
