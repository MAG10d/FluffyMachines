package io.ncbpfluffybear.fluffymachines.items.tools;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.ncbpfluffybear.fluffymachines.FluffyMachines;
import io.ncbpfluffybear.fluffymachines.utils.FluffyItems;
import io.ncbpfluffybear.fluffymachines.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class WarpPadConfigurator extends SlimefunItem implements HologramOwner, Listener {

    private final NamespacedKey xCoord = new NamespacedKey(FluffyMachines.getInstance(), "xCoordinate");
    private final NamespacedKey yCoord = new NamespacedKey(FluffyMachines.getInstance(), "yCoordinate");
    private final NamespacedKey zCoord = new NamespacedKey(FluffyMachines.getInstance(), "zCoordinate");
    private final NamespacedKey world = new NamespacedKey(FluffyMachines.getInstance(), "world");

    private static final int LORE_COORDINATE_INDEX = 4;
    private final ItemSetting<Integer> MAX_DISTANCE = new ItemSetting<>(this, "max-distance", 100);

    public WarpPadConfigurator(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        Bukkit.getPluginManager().registerEvents(this, FluffyMachines.getInstance());

        addItemSetting(MAX_DISTANCE);

    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {

        if (e.getClickedBlock() == null || e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Block b = e.getClickedBlock();
        Player p = e.getPlayer();

        SlimefunBlockData blockData = StorageCacheUtils.getBlock(b.getLocation());
        if (blockData != null && blockData.getSfId().equals(FluffyItems.WARP_PAD.getItem().getId())
            && Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.PLACE_BLOCK)) {
            if (SlimefunUtils.isItemSimilar(p.getInventory().getItemInMainHand(), FluffyItems.WARP_PAD_CONFIGURATOR,
                false)) {

                ItemStack item = p.getInventory().getItemInMainHand();
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                PersistentDataContainer pdc = meta.getPersistentDataContainer();

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    // Destination
                    if (p.isSneaking()) {
                        pdc.set(world, PersistentDataType.STRING, b.getWorld().getName());

                        pdc.set(xCoord, PersistentDataType.INTEGER, b.getX());
                        pdc.set(yCoord, PersistentDataType.INTEGER, b.getY());
                        pdc.set(zCoord, PersistentDataType.INTEGER, b.getZ());
                        lore.set(LORE_COORDINATE_INDEX, ChatColor.translateAlternateColorCodes(
                            '&', "&e連接點坐標: &7" + b.getX() + ", " + b.getY() + ", " + b.getZ()));

                        meta.setLore(lore);
                        item.setItemMeta(meta);

                        updateHologram(b, "&a&l終點");
                        blockData.setData("type", "destination");
                        Utils.send(p, "&3此傳送裝置已標記為&a終點&3並綁定到傳送裝置");

                        // Origin
                    } else if (pdc.has(world, PersistentDataType.STRING) && b.getWorld().getName().equals(
                        pdc.get(world, PersistentDataType.STRING))) {
                        int x = pdc.getOrDefault(xCoord, PersistentDataType.INTEGER, 0);
                        int y = pdc.getOrDefault(yCoord, PersistentDataType.INTEGER, 0);
                        int z = pdc.getOrDefault(zCoord, PersistentDataType.INTEGER, 0);

                        if (Math.abs(x - b.getX()) > MAX_DISTANCE.getValue()
                            || Math.abs(z - b.getZ()) > MAX_DISTANCE.getValue()) {

                            Utils.send(p, "&c傳送裝置之間的距離不能超過"
                                + MAX_DISTANCE.getValue() + "個方塊!");

                            return;
                        }

                        registerOrigin(b, x, y, z);

                        Utils.send(p, "&3此傳送裝置已標記為&a起點&3並綁定到傳送裝置" +
                            "");

                    } else {

                        Utils.send(p, "&c蹲下右鍵傳送裝置設定終點,然後點擊另一個" +
                            " " + "傳送裝置來設置起點!");
                    }

                }

            } else {
                Utils.send(p, "&c使用傳送裝置配置器來配置傳送裝置");
            }
        }
    }

    private void registerOrigin(Block b, int x, int y, int z) {
        SlimefunBlockData blockData = StorageCacheUtils.getBlock(b.getLocation());
        blockData.setData("type", "origin");

        blockData.setData("x", String.valueOf(x));
        blockData.setData("y", String.valueOf(y));
        blockData.setData("z", String.valueOf(z));

        updateHologram(b, "&a&l起點");
    }
}
