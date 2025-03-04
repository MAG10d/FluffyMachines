package io.ncbpfluffybear.fluffymachines.items.tools;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.libraries.unirest.json.JSONObject;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.ncbpfluffybear.fluffymachines.FluffyMachines;
import io.ncbpfluffybear.fluffymachines.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * used to quickly manipulate cargo nodes
 *
 * @author NCBPFluffyBear
 */
public class CargoManipulator extends SimpleSlimefunItem<ItemUseHandler> implements Listener {

    private static final int[] CARGO_SLOTS = {19, 20, 21, 28, 29, 30, 37, 38, 39};
    private Map<Player, Pair<JSONObject, ItemStack[]>> storedFilters = new HashMap<>();

    public CargoManipulator(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        Bukkit.getPluginManager().registerEvents(this, FluffyMachines.getInstance());
    }

    @Nonnull
    @Override
    public ItemUseHandler getItemHandler() {
        return e -> e.setUseBlock(Event.Result.DENY); // Prevent opening inventories
    }

    @EventHandler
    private void onCargoManipulatorUse(PlayerInteractEvent e) {

        ItemStack manipulator = e.getItem();

        // Check item is cargo manipulator
        if (manipulator == null || !this.isItem(manipulator)) {
            return;
        }

        e.setCancelled(true);

        Action act = e.getAction();
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        // Check if targeted block is cargo node
        SlimefunItemStack nodeType = getCargoNodeType(b);
        if (nodeType == null || (
            nodeType != SlimefunItems.CARGO_OUTPUT_NODE &&
                nodeType != SlimefunItems.CARGO_OUTPUT_NODE_2 &&
                nodeType != SlimefunItems.CARGO_INPUT_NODE
        )) {
            return;
        }

        if (!Slimefun.getProtectionManager().hasPermission(e.getPlayer(), b.getLocation(), Interaction.INTERACT_BLOCK)) {
            return;
        }

        if (act == Action.RIGHT_CLICK_BLOCK) {
            if (p.isSneaking()) {
                clearNode(b, p, getCargoNodeType(b));
            } else {
                copyNode(b, p, getCargoNodeType(b));
            }
        } else {
            pasteNode(b, p, getCargoNodeType(b));
        }
    }

    /**
     * Copy's a node's data into the manipulator. Cargo inventories stored in map.
     * Action: Right Click Block
     */
    private void copyNode(Block parent, Player p, SlimefunItemStack nodeType) {
        // Copy BlockStorage data
        SlimefunBlockData blockData = StorageCacheUtils.getBlock(parent.getLocation());
        JSONObject nodeData = new JSONObject(blockData.getAllData());

        ItemStack[] filterItems = new ItemStack[9];
        if (nodeType != SlimefunItems.CARGO_OUTPUT_NODE) { // No inventory
            // Copy inventory into map
            BlockMenu parentInventory = blockData.getBlockMenu();
            for (int i = 0; i < 9; i++) { // Iterate through all slots in cargo filter
                ItemStack menuItem = parentInventory.getItemInSlot(CARGO_SLOTS[i]);
                if (menuItem != null) {
                    filterItems[i] = new CustomItemStack(menuItem, 1);
                } else {
                    filterItems[i] = null;
                }
            }
        }

        storedFilters.put(p, new Pair<>(nodeData, filterItems)); // Save cargo slots into map

        Utils.send(p, "&a已複製 " + SlimefunItem.getById((String) nodeData.get("id")).getItemName() + " &a的設置.");
        createParticle(parent, Color.fromRGB(255, 252, 51)); // Bright Yellow
    }

    /**
     * Pastes stored node contents
     * Action: Left Click
     */
    private void pasteNode(Block child, Player p, SlimefunItemStack nodeType) {
        Pair<JSONObject, ItemStack[]> nodeSettings = storedFilters.getOrDefault(p, null);

        // No data saved yet
        if (nodeSettings == null) {
            Utils.send(p, "&c你還沒有複製貨運節點配置.");
            return;
        }

        // Get saved data
        JSONObject jsonData = nodeSettings.getFirstValue();

        SlimefunItemStack savedNodeType = (SlimefunItemStack) SlimefunItem.getById((String) jsonData.get("id")).getItem();
        if (savedNodeType != nodeType) {
            Utils.send(p, "&c你當前複製的是 " + savedNodeType.getDisplayName() + " &c的配置," +
                " &c無法應用到 " + nodeType.getDisplayName() + "&c!");
            createParticle(child, Color.RED);
            return;
        }

        // Set the data
        SlimefunBlockData blockData = StorageCacheUtils.getBlock(child.getLocation());
        jsonData.toMap().forEach((k, v) -> blockData.setData(k, v.toString()));

        if (nodeType != SlimefunItems.CARGO_OUTPUT_NODE) {
            // Set the filter
            BlockMenu nodeMenu = blockData.getBlockMenu();
            ItemStack[] filterItems = nodeSettings.getSecondValue();
            Inventory playerInventory = p.getInventory();

            for (int i = 0; i < 9; i++) {

                // Check if item already exists in slot
                if (SlimefunUtils.isItemSimilar(filterItems[i], nodeMenu.getItemInSlot(CARGO_SLOTS[i]), true, false)) {
                    continue;
                }

                // Drop item in filter slot
                clearFilterSlot(nodeMenu, CARGO_SLOTS[i], p);

                // No need to insert new items in
                if (filterItems[i] == null) {
                    continue;
                }

                // Check if item not in inventory
                if (!SlimefunUtils.containsSimilarItem(playerInventory, filterItems[i], true)) {
                    createParticle(child, Color.AQUA);
                    Utils.send(p, "&c你沒有過濾器物品 " + Utils.getViewableName(filterItems[i]) + "&c. 已跳過.");
                    continue;
                }

                // Consume item in player inventory
                for (ItemStack playerItem : playerInventory) {
                    if (SlimefunUtils.isItemSimilar(playerItem, filterItems[i], false, false)) {
                        playerItem.setAmount(playerItem.getAmount() - 1);

                        // Insert item into node menu
                        nodeMenu.replaceExistingItem(CARGO_SLOTS[i], new CustomItemStack(playerItem, 1));
                        break;
                    }
                }
            }
        }

        // Force menu update
        Utils.send(p, "&a已應用 " + savedNodeType.getDisplayName() + " &a的設置.");
        createParticle(child, Color.LIME);

    }

    /**
     * Clears the data of a targeted node
     * Action: Sneak + Right Click Block
     */
    private void clearNode(Block node, Player p, SlimefunItemStack nodeType) {
        // Clear node settings
        SlimefunBlockData blockData = StorageCacheUtils.getBlock(node.getLocation());
        blockData.setData("owner", p.getUniqueId().toString());
        blockData.setData("frequency", "0");

        // These settings are only for Input and Advanced Output nodes
        if (nodeType != SlimefunItems.CARGO_OUTPUT_NODE) {
            // AbstractFilterNode settings
            blockData.setData("index", "0");
            blockData.setData("filter-type", "whitelist");
            blockData.setData("filter-lore", String.valueOf(true));
            blockData.setData("filter-durability", String.valueOf(false));

            if (nodeType == SlimefunItems.CARGO_INPUT_NODE) {
                // CargoInputNode settings
                blockData.setData("round-robin", String.valueOf(false));
                blockData.setData("smart-fill", String.valueOf(false));
            }

            clearNodeFilter(node, p);

            Utils.send(p, "&a該貨運節點配置已清除");
            createParticle(node, Color.fromRGB(255, 152, 56)); // Light orange
        }
    }

    private void clearNodeFilter(Block node, Player p) {
        // Empty filter contents
        BlockMenu nodeMenu = StorageCacheUtils.getMenu(node.getLocation());
        for (int i = 0; i < 9; i++) {
            clearFilterSlot(nodeMenu, CARGO_SLOTS[i], p);
        }
    }

    private void clearFilterSlot(BlockMenu nodeMenu, int slot, Player p) {
        ItemStack filterItem = nodeMenu.getItemInSlot(slot);
        if (filterItem != null) {
            Utils.giveOrDropItem(p, filterItem); // Give player item in filter
            nodeMenu.replaceExistingItem(slot, null); // Clear item in filter
        }
    }

    /**
     * Get the SlimefunItemStack of the cargo node
     */
    private SlimefunItemStack getCargoNodeType(Block b) {
        if (b == null) {
            return null;
        }

        SlimefunItem item = StorageCacheUtils.getSfItem(b.getLocation());
        return item == null ? null : (SlimefunItemStack) item.getItem();
    }

    private void createParticle(Block b, Color color) {
        Particle.DustOptions dustOption = new Particle.DustOptions(color, 1);
        b.getLocation().getWorld().spawnParticle(Particle.REDSTONE, b.getLocation().add(0.5, 0.5, 0.5), 1, dustOption);
    }
}
