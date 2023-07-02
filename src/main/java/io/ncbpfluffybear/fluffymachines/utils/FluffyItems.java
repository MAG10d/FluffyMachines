package io.ncbpfluffybear.fluffymachines.utils;

import dev.j3fftw.extrautils.utils.LoreBuilderDynamic;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineTier;
import io.github.thebusybiscuit.slimefun4.core.attributes.MachineType;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ColoredFireworkStar;
import io.ncbpfluffybear.fluffymachines.items.FireproofRune;
import io.ncbpfluffybear.fluffymachines.items.MiniBarrel;
import io.ncbpfluffybear.fluffymachines.items.tools.FluffyWrench;
import io.ncbpfluffybear.fluffymachines.items.tools.PortableCharger;
import io.ncbpfluffybear.fluffymachines.machines.AdvancedAutoDisenchanter;
import io.ncbpfluffybear.fluffymachines.machines.AdvancedChargingBench;
import io.ncbpfluffybear.fluffymachines.machines.AutoAncientAltar;
import io.ncbpfluffybear.fluffymachines.machines.AutoCraftingTable;
import io.ncbpfluffybear.fluffymachines.machines.AutoTableSaw;
import io.ncbpfluffybear.fluffymachines.machines.BackpackLoader;
import io.ncbpfluffybear.fluffymachines.machines.BackpackUnloader;
import io.ncbpfluffybear.fluffymachines.machines.ElectricDustFabricator;
import io.ncbpfluffybear.fluffymachines.machines.ElectricDustRecycler;
import io.ncbpfluffybear.fluffymachines.machines.SmartFactory;
import io.ncbpfluffybear.fluffymachines.machines.WaterSprinkler;
import io.ncbpfluffybear.fluffymachines.multiblocks.CrankGenerator;
import io.ncbpfluffybear.fluffymachines.objects.AutoCrafter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/**
 * Specifies all plugin items
 */
public class FluffyItems {

    private FluffyItems() {
    }

    // Barrels
    public static final SlimefunItemStack MINI_FLUFFY_BARREL = new SlimefunItemStack(
        "MINI_FLUFFY_BARREL",
        Material.COMPOSTER,
        "&e迷你蓬鬆箱子",
        "",
        "&7可以儲存大量物品",
        "&7可以更改容量",
        "",
        "&b最大容量: &e" + MiniBarrel.getDisplayCapacity() + " 個物品"
    );

    // Portable Chargers
    public static final SlimefunItemStack SMALL_PORTABLE_CHARGER = new SlimefunItemStack(
        "SMALL_PORTABLE_CHARGER",
        Material.BRICK,
        "&e一階便攜充電器",
        "",
        "&7可以給手中物品/裝備充電",
        "",
        "&e充電速度: &7" + PortableCharger.Type.SMALL.chargeSpeed + " J/s",
        LoreBuilder.powerCharged(0, PortableCharger.Type.SMALL.chargeCapacity)
    );

    public static final SlimefunItemStack MEDIUM_PORTABLE_CHARGER = new SlimefunItemStack(
        "MEDIUM_PORTABLE_CHARGER",
        Material.IRON_INGOT,
        "&6二階便攜充電器",
        "",
        "&7可以給手中物品/裝備充電",
        "",
        "&e充電速度: &7" + PortableCharger.Type.MEDIUM.chargeSpeed + " J/s",
        LoreBuilder.powerCharged(0, PortableCharger.Type.MEDIUM.chargeCapacity)
    );

    public static final SlimefunItemStack BIG_PORTABLE_CHARGER = new SlimefunItemStack(
        "BIG_PORTABLE_CHARGER",
        Material.GOLD_INGOT,
        "&a三階便攜充電器",
        "",
        "&7可以給手中物品/裝備充電",
        "",
        "&e充電速度: &7" + PortableCharger.Type.BIG.chargeSpeed + " J/s",
        LoreBuilder.powerCharged(0, PortableCharger.Type.BIG.chargeCapacity)
    );

    public static final SlimefunItemStack LARGE_PORTABLE_CHARGER = new SlimefunItemStack(
        "LARGE_PORTABLE_CHARGER",
        Material.NETHER_BRICK,
        "&2四階便攜充電器",
        "",
        "&7可以給手中物品/裝備充電",
        "",
        "&e充電速度: &7" + PortableCharger.Type.LARGE.chargeSpeed + " J/s",
        LoreBuilder.powerCharged(0, PortableCharger.Type.LARGE.chargeCapacity)
    );

    public static final SlimefunItemStack CARBONADO_PORTABLE_CHARGER = new SlimefunItemStack(
        "CARBONADO_PORTABLE_CHARGER",
        Material.NETHERITE_INGOT,
        "&4五階便攜充電器",
        "",
        "&7可以給手中物品/裝備充電",
        "",
        "&e充電速度: &7" + PortableCharger.Type.CARBONADO.chargeSpeed + " J/s",
        LoreBuilder.powerCharged(0, PortableCharger.Type.CARBONADO.chargeCapacity)
    );

    // Items
    public static final SlimefunItemStack ANCIENT_BOOK = new SlimefunItemStack(
        "ANCIENT_BOOK",
        Material.BOOK,
        "&6遠古之書",
        "",
        "&7在&c高級全自動驅魔機&7中應用",
        "",
        "&6&o凝聚千年精華"
    );
    public static final SlimefunItemStack HELICOPTER_HAT = new SlimefunItemStack(
        "HELICOPTER_HAT",
        Material.LEATHER_HELMET, Color.AQUA,
        "&1直升機帽",
        "",
        "&7brrrrrrrrRRRRRRRR",
        "",
        "&e蹲下&7使用"
    );
    public static final SlimefunItemStack WATERING_CAN = new SlimefunItemStack(
        "WATERING_CAN",
        "6484da45301625dee79ae29ff513efa583f1ed838033f20db80963cedf8aeb0e",
        "&b噴壺",
        "",
        "&f給植物澆水",
        "",
        "&7> &e右鍵單擊&7灌滿噴壺",
        "&7> &e右鍵單擊&7加速植物生長.",
        "&7> &e右鍵單擊&7生長慢些",
        "",
        "&a剩餘水量: &e0"
    );
    public static final SlimefunItemStack ENDER_CHEST_EXTRACTION_NODE = new SlimefunItemStack(
        "ENDER_CHEST_EXTRACTION_NODE",
        "e707c7f6c3a056a377d4120028405fdd09acfcd5ae804bfde0f653be866afe39",
        "&6末影貨運節點(輸出)",
        "",
        "&7請把這個機器放在&5末影箱&7的一側",
        "",
        "&7會從&5末影箱&7輸入物品",
        "&7從相鄰的&6箱子&7放入物品"
    );
    public static final SlimefunItemStack ENDER_CHEST_INSERTION_NODE = new SlimefunItemStack(
        "ENDER_CHEST_INSERTION_NODE",
        "7e5dc50c0186d53381d9430a2eff4c38f816b8791890c7471ffdb65ba202bc5",
        "&b末影貨運節點(輸入)",
        "",
        "&7請把這個機器放在&5末影箱&7的一側",
        "",
        "&7會從&5末影箱&7輸出物品",
        "&7從相鄰的&6箱子&7吸取物品"
    );
    // Machines
    public static final SlimefunItemStack AUTO_CRAFTING_TABLE = new SlimefunItemStack(
        "AUTO_CRAFTING_TABLE",
        Material.CRAFTING_TABLE,
        "&6全自動工作檯(原版)",
        "",
        "&7全自動製造&f原版&7物品",
        "",
        LoreBuilderDynamic.powerBuffer(AutoCraftingTable.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(AutoCraftingTable.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack AUTO_ANCIENT_ALTAR = new SlimefunItemStack(
        "AUTO_ANCIENT_ALTAR",
        Material.ENCHANTING_TABLE,
        "&5全自動遠古祭壇",
        "",
        "&7全自動製造&5遠古祭壇&7物品",
        "",
        LoreBuilderDynamic.powerBuffer(AutoAncientAltar.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(AutoAncientAltar.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack AUTO_TABLE_SAW = new SlimefunItemStack(
        "AUTO_TABLE_SAW",
        Material.STONECUTTER,
        "&6全自動切石機",
        "",
        "&7全自動製造&6切石機&7物品",
        "",
        LoreBuilderDynamic.powerBuffer(AutoTableSaw.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(AutoTableSaw.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack WATER_SPRINKER = new SlimefunItemStack(
        "WATER_SPRINKLER",
        "d6b13d69d1929dcf8edf99f3901415217c6a567d3a6ead12f75a4de3ed835e85",
        "&b灑水機",
        "",
        "&7biu~",
        "",
        LoreBuilderDynamic.powerBuffer(WaterSprinkler.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(WaterSprinkler.ENERGY_CONSUMPTION) + " 每個作物"
    );
    public static final SlimefunItemStack GENERATOR_CORE = new SlimefunItemStack(
        "GENERATOR_CORE",
        Material.BLAST_FURNACE,
        "&7發電機芯",
        "",
        "&7發電機的組件"
    );
    public static final SlimefunItemStack CRANK_GENERATOR = new SlimefunItemStack(
        "CRANK_GENERATOR",
        Material.BLAST_FURNACE,
        "&7手搖發電機",
        "",
        "&e右鍵單擊&7拉杆發電",
        "",
        LoreBuilderDynamic.power(CrankGenerator.RATE, " 每次使用"),
        LoreBuilderDynamic.powerBuffer(CrankGenerator.CAPACITY),
        "",
        Utils.multiBlockWarning()
    );

    public static final SlimefunItemStack FOUNDRY = new SlimefunItemStack(
        "FOUNDRY",
        Material.BLAST_FURNACE,
        "&c鑄造廠",
        "",
        "&e儲存礦粉和錠",
        "&7可儲存138,240個錠",
        "",
        Utils.multiBlockWarning()
    );

    public static final SlimefunItemStack BACKPACK_UNLOADER = new SlimefunItemStack(
        "BACKPACK_UNLOADER",
        Material.BROWN_STAINED_GLASS,
        "&e背包卸載機",
        "",
        "&7從背包裡卸載物品",
        "",
        LoreBuilderDynamic.powerBuffer(BackpackUnloader.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(BackpackUnloader.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack BACKPACK_LOADER = new SlimefunItemStack(
        "BACKPACK_LOADER",
        Material.ORANGE_STAINED_GLASS,
        "&e背包裝載機",
        "",
        "&7從背包裡裝載物品",
        "",
        LoreBuilderDynamic.powerBuffer(BackpackLoader.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(BackpackLoader.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack UPGRADED_EXPLOSIVE_PICKAXE = new SlimefunItemStack(
        "UPGRADED_EXPLOSIVE_PICKAXE",
        Material.DIAMOND_PICKAXE,
        "&e&l改進爆破鎬",
        "",
        "&7挖掘5x5範圍"
    );
    public static final SlimefunItemStack UPGRADED_EXPLOSIVE_SHOVEL = new SlimefunItemStack(
        "UPGRADED_EXPLOSIVE_SHOVEL",
        Material.DIAMOND_SHOVEL,
        "&e&l改進爆破鏟",
        "",
        "&7挖掘5x5範圍"
    );
    public static final SlimefunItemStack FIREPROOF_RUNE = new SlimefunItemStack(
        "FIREPROOF_RUNE",
        new ColoredFireworkStar(Color.fromRGB(255, 165, 0),
            "&7古代符文&8&l[&c&l防火&8&l]",
            "",
            "&e此物品與其他物品丟在地上",
            "&e物品將會&c防火",
            ""
        )
    );
    public static final SlimefunItemStack SUPERHEATED_FURNACE = new SlimefunItemStack(
        "SUPERHEATED_FURNACE",
        Material.BLAST_FURNACE,
        "&c超熱爐",
        "",
        "&7鑄造厂部件",
        "&c請不要用爆炸工具打破!"
    );
    public static final SlimefunItemStack AUTO_ENHANCED_CRAFTING_TABLE = new SlimefunItemStack(
        "AUTO_ENHANCED_CRAFTING_TABLE",
        Material.CRAFTING_TABLE,
        "&e自動增強型工作檯",
        "",
        "&7全自動製造&e增強型工作檯&7物品",
        "",
        LoreBuilderDynamic.powerBuffer(AutoCrafter.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(AutoCrafter.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack AUTO_MAGIC_WORKBENCH = new SlimefunItemStack(
        "AUTO_MAGIC_WORKBENCH",
        Material.BOOKSHELF,
        "&6自動魔法工作檯",
        "",
        "&7全自動製造&6魔法工作檯&7物品",
        "",
        LoreBuilderDynamic.powerBuffer(AutoCrafter.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(AutoCrafter.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack AUTO_ARMOR_FORGE = new SlimefunItemStack(
        "AUTO_ARMOR_FORGE",
        Material.SMITHING_TABLE,
        "&7全自動盔甲鍛造機",
        "",
        "&7全自動製造&6盔甲鍛造機&7物品",
        "",
        LoreBuilderDynamic.powerBuffer(AutoCrafter.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(AutoCrafter.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack ADVANCED_AUTO_DISENCHANTER = new SlimefunItemStack(
        "ADVANCED_AUTO_DISENCHANTER",
        Material.ENCHANTING_TABLE,
        "&c高級全自動驅魔機",
        "",
        "&7從一個物品中移除第一個附魔",
        "&7需要一本&6遠古之書&7來操作",
        "",
        LoreBuilderDynamic.powerBuffer(AdvancedAutoDisenchanter.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(AdvancedAutoDisenchanter.ENERGY_CONSUMPTION)
    );
    public static final SlimefunItemStack SCYTHE = new SlimefunItemStack(
        "SCYTHE",
        Material.IRON_HOE,
        "&e鐮刀",
        "",
        "&7一次性打破5個農作物"
    );
    public static final SlimefunItemStack UPGRADED_LUMBER_AXE = new SlimefunItemStack(
        "UPGRADED_LUMBER_AXE",
        Material.DIAMOND_AXE,
        "&6&l改進斧頭",
        "",
        "&7一次砍下整棵樹",
        "&7右鍵給樹削皮"
    );
    public static final SlimefunItemStack DOLLY = new SlimefunItemStack(
        "DOLLY",
        Material.MINECART,
        "&b箱子搬運車",
        "",
        "&7右鍵撿起箱子",
        "",
        PlayerBackpack.LORE_OWNER
    );

    public static final SlimefunItemStack WARP_PAD = new SlimefunItemStack(
        "WARP_PAD",
        Material.SMOKER,
        "&6傳送裝置",
        "",
        "&7用此物品傳送到另一個傳送裝置",
        "&7需要傳送裝置配置器來配置",
        "",
        "&7在傳送裝置上蹲下來進行傳送"
    );

    public static final SlimefunItemStack WARP_PAD_CONFIGURATOR = new SlimefunItemStack(
        "WARP_PAD_CONFIGURATOR",
        Material.BLAZE_ROD,
        "&6傳送裝置配置器",
        "",
        "&e蹲下右鍵&7來設置終點",
        "&e右鍵&7來設置起點",
        "",
        "&e傳送坐標:&7無"
    );

    public static final SlimefunItemStack ELECTRIC_DUST_FABRICATOR = new SlimefunItemStack(
        "ELECTRIC_DUST_FABRICATOR",
        Material.BLAST_FURNACE,
        "&6粉塵製造機",
        "",
        "&7一台磨石,篩粉和洗礦三合一的機器",
        LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
        LoreBuilder.speed(10),
        LoreBuilderDynamic.powerBuffer(ElectricDustFabricator.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(ElectricDustFabricator.ENERGY_CONSUMPTION)
    );

    public static final SlimefunItemStack ELECTRIC_DUST_RECYCLER = new SlimefunItemStack(
        "ELECTRIC_DUST_RECYCLER",
        Material.IRON_BLOCK,
        "&f粉塵回收機",
        "",
        "&7將粉塵回收為篩礦",
        LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
        LoreBuilder.speed(1),
        LoreBuilderDynamic.powerBuffer(ElectricDustRecycler.CAPACITY),
        LoreBuilderDynamic.powerPerSecond(ElectricDustRecycler.ENERGY_CONSUMPTION)
    );

    public static final SlimefunItemStack ALTERNATE_ELEVATOR_PLATE = new SlimefunItemStack(
        "ALTERNATE_ELEVATOR_PLATE",
        Material.POLISHED_BLACKSTONE_PRESSURE_PLATE,
        "&3備用電梯板",
        "",
        "&f每成需要放置一個",
        "&f電梯都要在同一條垂直線.",
        "",
        "&e右鍵單擊&7來命名",
        ""
    );

    public static final SlimefunItemStack FLUFFY_WRENCH = new SlimefunItemStack(
        "FLUFFY_WRENCH",
        FluffyWrench.Wrench.DEFAULT.getMaterial(),
        "&6扳手",
        "",
        "&7快速拆除Slimefun機器",
        "",
        "&e左鍵點擊/右鍵點擊&7進行拆除"
    );

    public static final SlimefunItemStack REINFORCED_FLUFFY_WRENCH = new SlimefunItemStack(
        "REINFORCED_FLUFFY_WRENCH",
        FluffyWrench.Wrench.REINFORCED.getMaterial(),
        "&b改進扳手",
        "",
        "&7快速拆除Slimefun貨物節點和機器",
        "",
        "&e左鍵點擊/右鍵點擊&7進行拆除"
    );

    public static final SlimefunItemStack CARBONADO_FLUFFY_WRENCH = new SlimefunItemStack(
        "CARBONADO_FLUFFY_WRENCH",
        FluffyWrench.Wrench.CARBONADO.getMaterial(),
        "&7精製扳手",
        "",
        "&7快速拆除Slimefun的各種方塊",
        "",
        "&e左鍵點擊/右鍵點擊&7進行拆除",
        "",
        LoreBuilder.powerCharged(0, FluffyWrench.Wrench.CARBONADO.getMaxCharge())
    );

    public static final SlimefunItemStack PAXEL = new SlimefunItemStack(
        "PAXEL",
        Material.DIAMOND_PICKAXE,
        "&b多功能工具",
        "",
        "&7鎬子,斧頭,鏟子隨意切換!"
    );

    public static final SlimefunItemStack ADVANCED_CHARGING_BENCH = new SlimefunItemStack(
        "ADVANCED_CHARGING_BENCH",
        Material.SMITHING_TABLE,
        "&c高級充電台",
        "",
        "&7給物品充電",
        "&7可以使用&6高級充電台升級卡&7升級"
    );

    public static final SlimefunItemStack ACB_UPGRADE_CARD = new SlimefunItemStack(
        "ACB_UPGRADE_CARD",
        Material.PAPER,
        "&6高級充電台升級卡",
        "",
        "&e右鍵點擊&c高級充電台&7以進行升級",
        "",
        "&6充電速度&a+" + AdvancedChargingBench.CHARGE + "J",
        "&6容量&a+" + AdvancedChargingBench.CAPACITY + "J",
        "&6能源消耗&c+" + AdvancedChargingBench.ENERGY_CONSUMPTION + "J"
    );

    public static final SlimefunItemStack CARGO_MANIPULATOR = new SlimefunItemStack(
        "CARGO_MANIPULATOR",
        Material.SEA_PICKLE,
        "&9貨運配置器",
        "",
        "&e右鍵點擊&7複製貨運節點配置",
        "&e左鍵點擊&7應用貨運節點配置",
        "&eShift+右鍵點擊&7清除貨運節點配置"
    );

    public static final SlimefunItemStack EXP_DISPENSER = new SlimefunItemStack(
        "EXP_DISPENSER",
        Material.DISPENSER,
        "&a經驗收集器",
        "",
        "&7右鍵點擊以收集發射器中",
        "&7以及發射器面對的蓬鬆箱子中",
        "&7所有經驗瓶中的經驗",
        "",
        Utils.multiBlockWarning()
    );

    public static final SlimefunItemStack SMART_FACTORY = new SlimefunItemStack(
        "SMART_FACTORY",
        Material.SMOKER,
        "&c智慧工廠",
        "",
        "&7多合一機器",
        "&7可以從原材料直接合成指定資源",
        "",
        LoreBuilder.machine(MachineTier.END_GAME, MachineType.MACHINE),
        LoreBuilder.speed(1),
        LoreBuilderDynamic.powerBuffer(SmartFactory.getEnergyCapacity()),
        LoreBuilderDynamic.powerPerSecond(SmartFactory.getEnergyConsumption())
    );


    private static final Enchantment glowEnchant = Enchantment.getByKey(Constants.GLOW_ENCHANT);

    static {
        FireproofRune.setFireproof(FIREPROOF_RUNE);
        SMALL_PORTABLE_CHARGER.addEnchantment(glowEnchant, 1);
        MEDIUM_PORTABLE_CHARGER.addEnchantment(glowEnchant, 1);
        BIG_PORTABLE_CHARGER.addEnchantment(glowEnchant, 1);
        LARGE_PORTABLE_CHARGER.addEnchantment(glowEnchant, 1);
        CARBONADO_PORTABLE_CHARGER.addEnchantment(glowEnchant, 1);
    }
}
