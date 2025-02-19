package net.additionz;

import com.mojang.blaze3d.systems.RenderSystem;

import net.additionz.block.JukeBoxEntityRenderer;
import net.additionz.block.render.ChunkLoaderRenderer;
import net.additionz.block.screen.ChunkLoaderScreen;
import net.additionz.misc.FletchingScreen;
import net.additionz.network.AdditionClientPacket;
import net.additionz.network.packet.ExperiencePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class AdditionClient implements ClientModInitializer {

    private static final Identifier ORE_TEXTURE = Identifier.of("additionz:textures/gui/ore_icon.png");
    private static final Identifier TELEPORT_BARS_TEXTURE = Identifier.of("additionz:textures/gui/teleport_bars.png");

    private static int spyglassUsage = 0;

    @Override
    public void onInitializeClient() {
        AdditionClientPacket.init();
        HandledScreens.register(AdditionMain.FLETCHING, FletchingScreen::new);
        HandledScreens.register(AdditionMain.CHUNK_LOADER_SCREEN_HANDLER, ChunkLoaderScreen::new);
        BlockEntityRendererFactories.register(BlockEntityType.JUKEBOX, JukeBoxEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(AdditionMain.CHUNK_LOADER, RenderLayer.getCutout());
        BlockEntityRendererFactories.register(AdditionMain.CHUNK_LOADER_ENTITY, ChunkLoaderRenderer::new);

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                if (client.player.isUsingItem()) {
                    if (AdditionMain.CONFIG.teleport_scroll && client.player.getActiveItem().isOf(AdditionMain.TELEPORT_SCROLL)) {
                        int useTime = (int) (client.player.getItemUseTime() / 200f * 182.0f);

                        context.getMatrices().push();
                        context.getMatrices().translate(0.0f, 0.0f, 51.0f);
                        context.drawTexture(TELEPORT_BARS_TEXTURE, context.getScaledWindowWidth() / 2 - 91, context.getScaledWindowHeight() - 57, 0, 0, 182, 5);
                        if (useTime > 0) {
                            context.drawTexture(TELEPORT_BARS_TEXTURE, context.getScaledWindowWidth() / 2 - 91, context.getScaledWindowHeight() - 57, 0, 5, useTime, 5);
                        }
                        context.getMatrices().pop();
                    } else if (!renderOreIcon(context, client) && spyglassUsage != 0) {
                        spyglassUsage = 0;
                    }
                }
            }
        });
    }

    private static boolean renderOreIcon(DrawContext context, MinecraftClient client) {
        if (AdditionMain.CONFIG.eagle_eyed_enchantment && client.player.isUsingSpyglass() && (client.player.experienceLevel > 0 || client.player.isCreative())
                && client.player.getActiveItem().hasEnchantments()
                && client.player.getActiveItem().getEnchantments().getEnchantments().stream().anyMatch(entry -> entry.matchesId(AdditionMain.EAGLE_EYED_ENCHANTMENT.getRegistry()))) {
            HitResult hit = client.player.raycast(128, 0, false);
            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            if (hit.getType() == HitResult.Type.BLOCK) {
                for (int k = -1; k < 2; k++) {
                    for (int i = -1; i < 2; i++) {
                        for (int u = -1; u < 2; u++) {
                            BlockPos otherPos = pos.up(k).north(i).east(u);
                            if ((client.world.getBlockState(otherPos).getBlock() instanceof ExperienceDroppingBlock || client.world.getBlockState(otherPos).getBlock() instanceof RedstoneBlock
                                    || client.world.getBlockState(otherPos).isOf(Blocks.ANCIENT_DEBRIS))) {
                                context.getMatrices().push();
                                context.getMatrices().translate(0.0f, 0.0f, 51.0f);
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                                context.drawTexture(ORE_TEXTURE, (client.getWindow().getScaledWidth() / 2), (client.getWindow().getScaledHeight() / 2) - 16, 0.0F, 0.0F, 16, 16, 16, 16);
                                context.getMatrices().pop();
                                if (spyglassUsage == 0) {
                                    ClientPlayNetworking.send(new ExperiencePacket(1));
                                }
                                spyglassUsage++;
                                return true;
                            }

                        }
                    }
                }
            }
        }
        return false;

    }

}
