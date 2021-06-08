package dev.gegy.roles.mixin.mute;

import com.mojang.brigadier.Command;
import dev.gegy.roles.PlayerRoles;
import dev.gegy.roles.api.PlayerRoleSource;
import dev.gegy.roles.api.RoleReader;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private static void execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Text message, CallbackInfoReturnable<Integer> ci) {
        Entity entity = source.getEntity();
        if (entity instanceof PlayerRoleSource && entity instanceof ServerPlayerEntity) {
            RoleReader roles = ((PlayerRoleSource) entity).getPlayerRoles();
            if (roles.test(PlayerRoles.MUTE)) {
                PlayerRoles.sendMuteFeedback((ServerPlayerEntity) entity);
                ci.setReturnValue(Command.SINGLE_SUCCESS);
            }
        }
    }
}
