package com.joi.xsurvival.maps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class Monsters {
	
	public static Monsters instance;
	NPCRegistry registry = CitizensAPI.getNPCRegistry();
	List<NPC> monsters = new ArrayList<NPC>();
	
	static {
		instance = new Monsters();
	}
	
	public static Monsters get() {
		return instance;
	}
	
	public List<NPC> getMonsters() {
		return monsters;
	}
	
	public void spawnZombie(Location loc) {
		NPC npc = registry.createNPC(EntityType.ZOMBIE, "§2Zombie");
		npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
		monsters.add(npc);
		npc.spawn(loc);
	}
	
	public void spawnSkeleton(Location loc) {
		NPC npc = registry.createNPC(EntityType.SKELETON, "§Skeleton");
		npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
		monsters.add(npc);
		npc.spawn(loc);
	}

}
