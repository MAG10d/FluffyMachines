package io.ncbpfluffybear.fluffymachines.listeners;

import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.ncbpfluffybear.fluffymachines.machines.AutoCraftingTable;
import io.ncbpfluffybear.fluffymachines.machines.SmartFactory;
import io.ncbpfluffybear.fluffymachines.utils.Utils;
import net.guizhanss.guizhanlib.minecraft.helper.inventory.ItemStackHelper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Optional;

public class KeyedCrafterListener implements Listener {

    public KeyedCrafterListener() {
    }

    @EventHandler
    private void onSmartFactoryInteract(PlayerRightClickEvent e) {
        Optional<Block> clickedBlock = e.getClickedBlock();

        if (e.getHand() == EquipmentSlot.HAND && e.useBlock() != Event.Result.DENY && clickedBlock.isPresent() && e.getPlayer().isSneaking()) {
            Optional<SlimefunItem> slimefunBlock = e.getSlimefunBlock();

            if (!slimefunBlock.isPresent()) {
                return;
            }

            SlimefunItem sfBlock = slimefunBlock.get();
            ItemStack item = e.getItem();
            Player p = e.getPlayer();
            SlimefunItem key = SlimefunItem.getByItem(item);
            Block b = clickedBlock.get();

            // Handle SmartFactory recipe setting
            if (sfBlock instanceof SmartFactory) {

                if (isCargoNode(key)) {
                    return;
                }
                e.cancel();

                if (key == null) {
                    Utils.send(p, "&c該機器不支持原版物品!");
                    return;
                }

                if (SmartFactory.getAcceptedItems().contains((SlimefunItemStack) key.getItem())) {

                    StorageCacheUtils.setData(b.getLocation(), "recipe", key.getId());
                    StorageCacheUtils.getMenu(b.getLocation()).replaceExistingItem(SmartFactory.RECIPE_SLOT,
                            SmartFactory.getDisplayItem(key, ((RecipeDisplayItem) sfBlock).getDisplayRecipes())
                    );
                    Utils.send(p, "&a已設置目標物品為 " + key.getItemName());
                } else {
                    Utils.send(p, "&c目標物品暫不支持!");
                }

            } else if (sfBlock instanceof AutoCraftingTable) {

                if (isCargoNode(key)) {
                    return;
                }
                e.cancel();

                if (item.getType() == Material.AIR) {
                    Utils.send(p, "&c手持物品Shift+右鍵點擊機器以設置配方");
                    return;
                }

                StorageCacheUtils.getMenu(b.getLocation()).replaceExistingItem(AutoCraftingTable.KEY_SLOT,
                        AutoCraftingTable.createKeyItem(item.getType())
                );

                Utils.send(p, "&a已設置目標物品為 " + ItemStackHelper.getDisplayName(item)
                );
            }
        }
    }

    private boolean isCargoNode(@Nullable SlimefunItem recipe) {
        return recipe != null && (recipe.getItem() == SlimefunItems.CARGO_INPUT_NODE
                || recipe.getItem() == SlimefunItems.CARGO_OUTPUT_NODE || recipe.getItem() == SlimefunItems.CARGO_OUTPUT_NODE_2);
    }
}
