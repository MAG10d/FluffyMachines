package io.ncbpfluffybear.fluffymachines.machines;

import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.TableSaw;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This {@link SlimefunItem} is an electric version
 * of the {@link TableSaw}
 *
 * @author NCBPFluffyBear
 */
public class AutoTableSaw extends SlimefunItem implements EnergyNetComponent {

    public static final int ENERGY_CONSUMPTION = 128;
    public static final int CAPACITY = ENERGY_CONSUMPTION * 3;
    private final int[] border = {0, 1, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 22, 27, 31, 36, 40, 45,
        46, 47, 48, 49, 50, 51, 52, 53};
    private final int[] inputBorder = {19, 20, 21, 28, 30, 37, 38, 39,};
    private final int[] outputBorder = {23, 24, 25, 26, 32, 35, 41, 42, 43, 44};
    private final int[] inputSlots = {29};
    private final int[] outputSlots = {33, 34};

    private final Map<ItemStack, ItemStack> tableSawRecipes = new HashMap<>();

    public AutoTableSaw(ItemGroup category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        for (Material log : Tag.LOGS.getValues()) {
            Optional<Material> planks = getPlanks(log);

            planks.ifPresent(material -> tableSawRecipes.put(new ItemStack(log), new ItemStack(material, 8)));
        }

        for (Material plank : Tag.PLANKS.getValues()) {
            tableSawRecipes.put(new ItemStack(plank), new ItemStack(Material.STICK, 4));
        }

        new BlockMenuPreset(getId(), "&6全自動台鋸") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
                SlimefunBlockData blockData = StorageCacheUtils.getBlock(b.getLocation());
                if (blockData.getData("enabled") == null || String.valueOf(false).equals(blockData.getData("enabled"))) {
                    menu.replaceExistingItem(6, new CustomItemStack(Material.GUNPOWDER, "&7啟用: &4\u2718",
                        "", "&e> 點擊開啟")
                    );
                    menu.addMenuClickHandler(6, (p, slot, item, action) -> {
                        blockData.setData("enabled", String.valueOf(true));
                        newInstance(menu, b);
                        return false;
                    });
                } else {
                    menu.replaceExistingItem(6, new CustomItemStack(Material.REDSTONE, "&7啟用: &2\u2714",
                        "", "&e> 點擊關閉"));
                    menu.addMenuClickHandler(6, (p, slot, item, action) -> {
                        blockData.setData("enabled", String.valueOf(false));
                        newInstance(menu, b);
                        return false;
                    });
                }
            }

            @Override
            public boolean canOpen(@Nonnull Block b, @Nonnull Player p) {
                return p.hasPermission("slimefun.inventory.bypass")
                    || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(),
                    Interaction.INTERACT_BLOCK
                );
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.WITHDRAW) {
                    return outputSlots;
                } else {
                    return inputSlots;
                }
            }
        };

        addItemHandler(onPlace());
        addItemHandler(onBreak());

    }

    private BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@Nonnull BlockBreakEvent e, @Nonnull ItemStack item, @Nonnull List<ItemStack> drops) {
                Block b = e.getBlock();
                BlockMenu inv = StorageCacheUtils.getMenu(b.getLocation());
                Location location = b.getLocation();

                if (inv != null) {
                    inv.dropItems(location, inputSlots);
                    inv.dropItems(location, outputSlots);
                }
            }
        };
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(true) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                StorageCacheUtils.setData(e.getBlock().getLocation(), "enabled", String.valueOf(false));
            }

            @Override
            public void onBlockPlacerPlace(@Nonnull BlockPlacerPlaceEvent e) {
                StorageCacheUtils.setData(e.getBlock().getLocation(), "enabled", String.valueOf(false));
            }
        };
    }

    protected void constructMenu(BlockMenuPreset preset) {

        borders(preset, border, inputBorder, outputBorder);
        preset.addItem(2, new CustomItemStack(new ItemStack(Material.STONECUTTER), "&e使用方法", "",
                "&b把將要製作的物品配方放入裡面",
                "&4僅支持台鉅的配方"
            ),
            ChestMenuUtils.getEmptyClickHandler());
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    public int getEnergyConsumption() {
        return ENERGY_CONSUMPTION;
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, SlimefunBlockData data) {
                AutoTableSaw.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        });
    }

    protected void tick(Block block) {
        if (String.valueOf(false).equals(StorageCacheUtils.getData(block.getLocation(), "enabled"))) {
            return;
        }

        if (getCharge(block.getLocation()) < getEnergyConsumption()) {
            return;
        }

        BlockMenu menu = StorageCacheUtils.getMenu(block.getLocation());
        tableSawRecipes.forEach((input, output) -> {
            if (menu.getItemInSlot(inputSlots[0]) != null
                && SlimefunUtils.isItemSimilar(menu.getItemInSlot(inputSlots[0]), input, true, false)
                && menu.fits(output, outputSlots)) {

                menu.consumeItem(inputSlots[0]);
                menu.pushItem(output.clone(), outputSlots);
            }
        });
    }

    /**
     * Method that finds planks associated with a log
     *
     * @author TheBusyBiscuit
     */
    private @Nonnull
    Optional<Material> getPlanks(@Nonnull Material log) {
        String materialName = log.name().replace("STRIPPED_", "");
        materialName = materialName.substring(0, materialName.lastIndexOf('_')) + "_PLANKS";
        return Optional.ofNullable(Material.getMaterial(materialName));
    }

    static void borders(BlockMenuPreset preset, int[] border, int[] inputBorder, int[] outputBorder) {
        for (int i : border) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                (p, slot, item, action) -> false);
        }

        for (int i : inputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
                (p, slot, item, action) -> false);
        }

        for (int i : outputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
                (p, slot, item, action) -> false);
        }
    }

}
