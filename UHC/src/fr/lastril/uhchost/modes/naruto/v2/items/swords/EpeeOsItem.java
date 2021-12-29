package fr.lastril.uhchost.modes.naruto.v2.items.swords;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class EpeeOsItem {

    private final ItemStack item;

    public EpeeOsItem(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();

        NBTTagList modifiers = new NBTTagList();
        NBTTagCompound damage = new NBTTagCompound();

        damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
        damage.set("Name", new NBTTagString("generic.attackDamage"));
        damage.set("Amount", new NBTTagDouble(12.00)); //13.25
        damage.set("Operation", new NBTTagInt(0));
        damage.set("UUIDLeast", new NBTTagInt(894654));
        damage.set("UUIDMost", new NBTTagInt(2872));

        modifiers.add(damage);
        compound.set("AttributeModifiers", modifiers);
        nmsStack.setTag(compound);

        this.item = CraftItemStack.asBukkitCopy(nmsStack);
    }

    public ItemStack getItem(){
        return this.item;
    }
}
