package org.screamingsandals.lib.bukkit.utils.nms;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.craftbukkit.MinecraftComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ClassStorage {

	public static final boolean NMS_BASED_SERVER = safeGetClass("org.bukkit.craftbukkit.Main") != null;
	public static final String NMS_VERSION = checkNMSVersion();

	// CraftBukkit classes
	public static final class CB {
		public static final Class<?> CraftItemStack = safeGetClass("{obc}.inventory.CraftItemStack");
		public static final Class<?> CraftMagicNumbers = safeGetClass("{obc}.util.CraftMagicNumbers");
		public static final Class<?> CraftVector = safeGetClass("{obc}.util.CraftVector");
		public static final Class<?> CraftPersistentDataContainer = safeGetClass("{obc}.persistence.CraftPersistentDataContainer");
		public static final Class<?> CraftMetaItem = safeGetClass("{obc}.inventory.CraftMetaItem");
		public static final Class<?> CraftPersistentDataTypeRegistry = safeGetClass("{obc}.persistence.CraftPersistentDataTypeRegistry");
	}
	
	private static String checkNMSVersion() {
		/* if NMS is not found, finding class will fail, but we still need some string */
		String nmsVersion = "nms_not_found"; 
		
		if (NMS_BASED_SERVER) {
			String packName = Bukkit.getServer().getClass().getPackage().getName();
			nmsVersion = packName.substring(packName.lastIndexOf('.') + 1);
		}
		
		return nmsVersion;
	}
	
	public static Class<?> safeGetClass(String... clazz) {
		return Reflect.getClassSafe(
				Map.of(
						"{obc}", "org.bukkit.craftbukkit." + NMS_VERSION
				),
				clazz
		);
	}
	
	public static Object getHandle(Object obj) {
		return Reflect.getMethod(obj, "getHandle").invoke();
	}
	
	public static Object getPlayerConnection(Player player) {
		return Reflect
				.getMethod(player, "getHandle")
				.invokeResulted()
				.getField(ServerPlayerAccessor.getFieldConnection());
	}

	public static Object getMethodProfiler(World world) {
		return getMethodProfiler(getHandle(world));
	}

	public static Object getMethodProfiler(Object handler) {
		Object methodProfiler = Reflect.fastInvoke(handler, LevelAccessor.getMethodGetProfiler1());
		if (methodProfiler == null) {
			methodProfiler = Reflect.getField(handler, LevelAccessor.getFieldField_72984_F());
		}
		return methodProfiler;
	}

	public static Object obtainNewPathfinderSelector(Object handler) {
		try {
			Object world = Reflect.fastInvoke(handler, EntityAccessor.getMethodGetCommandSenderWorld1());
			try {
				// 1.17
				return Reflect.fastInvoke(world, LevelAccessor.getMethodGetProfilerSupplier1());
			} catch (Throwable ignored) {
				try {
					// 1.16
					return GoalSelectorAccessor.getConstructor0().newInstance((Supplier<?>) () -> getMethodProfiler(world));
				} catch (Throwable ignore) {
					// Pre 1.16
					return GoalSelectorAccessor.getType().getConstructors()[0].newInstance(getMethodProfiler(world));
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}


	public static Object getVectorToNMS(Vector3Df vector3f) {
		try {
			return RotationsAccessor.getConstructor0().newInstance(vector3f.getX(), vector3f.getY(), vector3f.getZ());
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	public static Vector3Df getVectorFromNMS(Object vector3f) {
		Preconditions.checkNotNull(vector3f, "Vector is null!");
		try {
			return new Vector3Df(
					(float) Reflect.fastInvoke(vector3f, RotationsAccessor.getMethodGetX1()),
					(float) Reflect.fastInvoke(vector3f, RotationsAccessor.getMethodGetY1()),
					(float) Reflect.fastInvoke(vector3f, RotationsAccessor.getMethodGetZ1())
			);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public static Object asMinecraftComponent(Component component) {
		try {
			return MinecraftComponentSerializer.get().serialize(component);
		} catch (Exception ignored) { // current Adventure is facing some weird bug on non-adventure native server software, let's do temporary workaround
			return Reflect.fastInvoke(Component_i_SerializerAccessor.getMethodM_130701_1(), (Object) GsonComponentSerializer.gson().serialize(component));
		}
	}

	public static Object stackAsNMS(ItemStack item) {
		Preconditions.checkNotNull(item, "Item is null!");
		return Reflect.getMethod(CB.CraftItemStack, "asNMSCopy", ItemStack.class).invokeStatic(item);
	}

	public static Object getDataWatcher(Object handler) {
		Preconditions.checkNotNull(handler, "Handler is null!");
		return Reflect.fastInvoke(handler, EntityAccessor.getMethodGetEntityData1());
	}

	public static int getEntityTypeId(String key, Class<?> clazz) {
		var registry = Reflect.getFieldResulted(RegistryAccessor.getFieldENTITY_TYPE());

		if (registry.isPresent()) {
			// 1.14+
			var optional = Reflect.fastInvoke(EntityTypeAccessor.getMethodByString1(), (Object) key);

			if (optional instanceof Optional) {
				return registry.fastInvokeResulted(RegistryAccessor.getMethodGetId1(), ((Optional<?>) optional).orElse(null)).asOptional(Integer.class).orElse(0);
			}

			// 1.13.X
			var nullable = Reflect.fastInvoke(EntityTypeAccessor.getMethodFunc_200713_a1(), (Object) key);
			return registry.fastInvokeResulted(RegistryAccessor.getMethodGetId1(), nullable).asOptional(Integer.class).orElse(0);
		} else {
			// 1.11 - 1.12.2
			if (EntityTypeAccessor.getFieldField_191308_b() != null) {
				return Reflect.getFieldResulted(EntityTypeAccessor.getFieldField_191308_b()).fastInvokeResulted(MappedRegistryAccessor.getMethodFunc_148757_b1(), clazz).asOptional(Integer.class).orElse(0);
			}

			// 1.9.4 - 1.10.2
			return (int) Reflect.getFieldResulted(EntityTypeAccessor.getFieldField_75624_e()).as(Map.class).get(clazz);
		}
	}
}
