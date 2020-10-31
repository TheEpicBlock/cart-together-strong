package nl.theepicblock.carttogetherstrong.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import nl.theepicblock.carttogetherstrong.Cart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartEntityRenderer.class)
public abstract class MinecartEntityRendererMixin<T extends AbstractMinecartEntity> extends EntityRenderer<T> {
    protected MinecartEntityRendererMixin(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected boolean hasLabel(T entity) {
        return true;
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(T abstractMinecartEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        Vec3d v = abstractMinecartEntity.getVelocity();
        Text t = Text.of(String.format("%s, %s, %s | %s", v.x, v.y, v.z, ((Cart)abstractMinecartEntity).getMovingDirection().getName()));
        this.renderLabelIfPresent(abstractMinecartEntity, t, matrixStack, vertexConsumerProvider, i);
    }
}
