package io.ncbpfluffybear.fluffymachines.items;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import io.ncbpfluffybear.fluffymachines.utils.Utils;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.EnderChest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * This {@link SlimefunItem} transfers items to the facing
 * {@link EnderChest} from the {@link Container} behind it
 *
 * @author NCBPFluffyBear
 */
public class EnderChestInsertionNode extends SlimefunItem {

    private static final Material material = Material.ENDER_CHEST;

    public EnderChestInsertionNode(ItemGroup category, SlimefunItemStack item, RecipeType recipeType,
                                   ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(onPlace());
        addItemHandler(onInteract());
    }

    @Override
    public void preRegister() {
        this.addItemHandler(new BlockTicker() {
            public void tick(Block b, SlimefunItem sf, SlimefunBlockData data) {
                EnderChestInsertionNode.this.tick(b);
            }

            public boolean isSynchronized() {
                return true;
            }
        });
    }

    private void tick(@Nonnull Block b) {
        ItemStack transferItemStack;
        BlockFace face;

        if (b.getRelative(BlockFace.NORTH).getType() == material) {
            face = BlockFace.SOUTH;

        } else if (b.getRelative(BlockFace.SOUTH).getType() == material) {
            face = BlockFace.NORTH;


        } else if (b.getRelative(BlockFace.EAST).getType() == material) {
            face = BlockFace.WEST;


        } else if (b.getRelative(BlockFace.WEST).getType() == material) {
            face = BlockFace.EAST;

        } else {
            return;
        }

        BlockState state = PaperLib.getBlockState(b.getRelative(face), false).getState();

        if (b.getRelative(face).getState() instanceof InventoryHolder) {
            Player p = Bukkit.getOfflinePlayer(UUID.fromString(StorageCacheUtils.getData(b.getLocation(), "owner"))).getPlayer();

            // Ender chest null check necessary because Bukkit yes.
            if (p != null) {

                boolean enderValid = false;
                boolean containerValid = false;
                int enderIndex = -1;
                int containerIndex = -1;

                Inventory containerInv = ((InventoryHolder) state).getInventory();

                for (int i = 0; i < containerInv.getSize(); i++) {

                    if (containerInv.getItem(i) != null) {
                        containerIndex = i;
                        containerValid = true;
                        break;
                    }
                }

                Inventory enderInv = p.getEnderChest();

                for (int i = 0; i < enderInv.getSize(); i++) {

                    if (enderInv.getItem(i) == null) {
                        enderIndex = i;
                        enderValid = true;
                        break;
                    }
                }

                if (enderValid && containerValid) {
                    transferItemStack = containerInv.getItem(containerIndex);
                    containerInv.setItem(containerIndex, null);

                    enderInv.setItem(enderIndex, transferItemStack);
                }
            }
        }
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                Player p = e.getPlayer();
                Block b = e.getBlock();

                if (!e.isCancelled()) {
                    StorageCacheUtils.setData(b.getLocation(), "owner", p.getUniqueId().toString());
                    StorageCacheUtils.setData(b.getLocation(), "playername", p.getDisplayName());
                    Utils.send(p, "&a末地貨運節點已綁定到" + p.getDisplayName()
                        + " &7(UUID: " + p.getUniqueId() + ")");
                }
            }
        };
    }

    private BlockUseHandler onInteract() {
        return e -> {
            Player p = e.getPlayer();
            Block b = e.getClickedBlock().get();
            Utils.send(p, "&e此末地貨運節點是屬於" +
                StorageCacheUtils.getData(b.getLocation(), "playername")
                + " &7(UUID: " + StorageCacheUtils.getData(b.getLocation(), "owner") + ")");
        };
    }
}
