package org.meowcat.mesagisto.fabric.mixin;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import org.meowcat.mesagisto.client.Logger;
import org.meowcat.mesagisto.fabric.impl.ChatImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * From architectury/architectury-api
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {
  @Shadow
  public ServerPlayer player;

  @Shadow
  public abstract void disconnect(Component component);

  @Inject(method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V",
          at = @At(value = "INVOKE",
                  target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"),
          locals = LocalCapture.CAPTURE_FAILHARD)
  private void handleChat(TextFilter.FilteredText message, CallbackInfo ci, String string, String string2, Component filtered, Component raw) {
    ChatImpl.INSTANCE.deliverChatEvent(player, new TextComponent(string));
  }
}