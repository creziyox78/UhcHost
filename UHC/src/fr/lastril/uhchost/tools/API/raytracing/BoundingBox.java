package fr.lastril.uhchost.tools.API.raytracing;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class BoundingBox {
    private final Vector min, max;

    public BoundingBox(Vector min, Vector max) {
        this.min = min;
        this.max = max;
    }

    public BoundingBox(org.bukkit.block.Block block) {
        IBlockData blockData = ((CraftWorld) block.getWorld()).getHandle()
                .getType(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        Block blockNative = blockData.getBlock();
        blockNative.updateShape((IBlockAccess) ((CraftWorld) block.getWorld()).getHandle(),
                new BlockPosition(block.getX(), block.getY(), block.getZ()));
        this.min = new Vector(block.getX() + blockNative.B(), block.getY() + blockNative.D(),
                block.getZ() + blockNative.F());
        this.max = new Vector(block.getX() + blockNative.C(), block.getY() + blockNative.E(),
                block.getZ() + blockNative.G());
    }

    public BoundingBox(Entity entity) {
        AxisAlignedBB bb = ((CraftEntity) entity).getHandle().getBoundingBox();
        this.min = new Vector(bb.a, bb.b, bb.c);
        this.max = new Vector(bb.d, bb.e, bb.f);
    }

    public BoundingBox(AxisAlignedBB bb) {
        this.min = new Vector(bb.a, bb.b, bb.c);
        this.max = new Vector(bb.d, bb.e, bb.f);
    }

    public Vector midPoint() {
        return this.max.clone().add(this.min).multiply(0.5D);
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }
}

