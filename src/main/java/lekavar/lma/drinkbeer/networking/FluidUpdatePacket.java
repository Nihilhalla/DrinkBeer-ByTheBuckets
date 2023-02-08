package lekavar.lma.drinkbeer.networking;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class FluidUpdatePacket {
    public final BlockPos bePos;
    public final FluidStack fluid;

    public FluidUpdatePacket(BlockPos pos, FluidStack fluid) {
        this.bePos = pos;
        this.fluid = fluid;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(bePos);
        buffer.writeFluidStack(fluid);
    }

    public static FluidUpdatePacket decode(FriendlyByteBuf buffer) {
        return new FluidUpdatePacket(buffer.readBlockPos(), buffer.readFluidStack());
    }

    public static void handle(FluidUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> success.set(ClientAccess.updateFluid(msg.bePos, msg.fluid)));
        });
        ctx.get().setPacketHandled(success.get());
    }
    public class ClientAccess {
        public static boolean updateFluid(BlockPos pos, FluidStack fluid) {
            AtomicBoolean success = new AtomicBoolean(false);
            if (Minecraft.getInstance().level != null) {
                final BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(h -> {
                        if (h instanceof FluidTank tank) {
                            tank.setFluid(fluid);
                            success.set(true);
                        }
                    });
                }
            }
            return success.get();
        }
    }
}
