package main.java.rs.markisha.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

public class WoodMaterialUtil {

	private static final Map<Material, List<Material>> WOOD_GROUPS = new HashMap<>();
	
	static {
		addWoodGroup(Material.BIRCH_LOG, Material.BIRCH_WOOD);
        addWoodGroup(Material.OAK_LOG, Material.OAK_WOOD);
        addWoodGroup(Material.SPRUCE_LOG, Material.SPRUCE_WOOD);
        addWoodGroup(Material.JUNGLE_LOG, Material.JUNGLE_WOOD);
        addWoodGroup(Material.ACACIA_LOG, Material.ACACIA_WOOD);
        addWoodGroup(Material.DARK_OAK_LOG, Material.DARK_OAK_WOOD);
        addWoodGroup(Material.CHERRY_LOG, Material.CHERRY_WOOD);
        addWoodGroup(Material.PALE_OAK_LOG, Material.PALE_OAK_WOOD);
        
        addWoodGroup(Material.CRIMSON_STEM, Material.CRIMSON_HYPHAE, Material.NETHER_WART_BLOCK, Material.SHROOMLIGHT);
        addWoodGroup(Material.WARPED_STEM, Material.WARPED_HYPHAE, Material.WARPED_WART_BLOCK, Material.SHROOMLIGHT);
        
        addWoodGroup(Material.MANGROVE_LOG, Material.MANGROVE_WOOD, Material.MANGROVE_ROOTS, Material.MUDDY_MANGROVE_ROOTS);
	}
	
	private static void addWoodGroup(Material... materials) {
        List<Material> group = Arrays.asList(materials);
        for (Material material : materials) {
            WOOD_GROUPS.put(material, group);
        }
    }
	
	public static List<Material> getMaterials(Material material) {
        return WOOD_GROUPS.getOrDefault(material, Collections.singletonList(material));
    }
	
	public static boolean isWood(Material material) {
		return WOOD_GROUPS.containsKey(material);
    }
	
	public static boolean isAxe(Material material) {
		return material.name().contains("_AXE");
	}
	
	public static boolean isLeaf(Material material) {
		return material.name().contains("_LEAVES");
	}
	
}
