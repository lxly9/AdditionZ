// package net.additionz.mixin.client;

// import org.spongepowered.asm.mixin.Mixin;

// import net.fabricmc.api.EnvType;
// import net.fabricmc.api.Environment;
// import net.minecraft.client.render.entity.EntityRendererFactory.Context;
// import net.minecraft.client.render.VertexConsumerProvider;
// import net.minecraft.client.render.entity.MobEntityRenderer;
// import net.minecraft.client.render.entity.SpiderEntityRenderer;
// import net.minecraft.client.render.entity.model.SpiderEntityModel;
// import net.minecraft.client.util.math.MatrixStack;
// import net.minecraft.entity.mob.SpiderEntity;
// import net.minecraft.util.math.Direction;
// import net.minecraft.util.math.RotationAxis;

// @Environment(EnvType.CLIENT)
// @Mixin(SpiderEntityRenderer.class)
// public abstract class SpiderEntityRendererMixin<T extends SpiderEntity> extends MobEntityRenderer<T, SpiderEntityModel<T>> {

//     public SpiderEntityRendererMixin(Context context, SpiderEntityModel<T> entityModel, float f) {
//         super(context, entityModel, f);
//     }

//     @Override
//     public void render(T mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
//         for (int u = 0; u < 4; u++) {
//             if (mobEntity.getWorld().getBlockState(mobEntity.getBlockPos().offset(Direction.fromHorizontal(u))).isSolidBlock(mobEntity.getWorld(),
//                     mobEntity.getBlockPos().offset(Direction.fromHorizontal(u)))) {
//                 matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0f));
//                 matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(climbingAngle(u)));
//                 break;
//             }
//         }
//         super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
//     }

//     private float climbingAngle(int u) {
//         if (u == 0) {
//             return 270.0f;
//         } else if (u == 1) {
//             return 180.0f;
//         } else if (u == 2) {
//             return 90.0f;
//         }
//         return 0.0f;
//     }

// }
