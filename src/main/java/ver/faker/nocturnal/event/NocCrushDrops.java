package ver.faker.nocturnal.event;

import com.simibubi.create.AllDamageTypes;
import net.midnight.hemodynamics.api.HemoEntityTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import ver.faker.nocturnal.Nocturnal;
import ver.faker.nocturnal.register.NocItems;

@EventBusSubscriber(modid = Nocturnal.ID)
public class NocCrushDrops {

    private static final float CHANCE = 0.10f;

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!event.getSource().is(AllDamageTypes.CRUSH)) return;
        if (!hasLungs(entity)) return;
        if (entity.getRandom().nextFloat() >= CHANCE) return;

        ItemEntity drop = new ItemEntity(entity.level(),
                entity.getX(), entity.getY(), entity.getZ(),
                new ItemStack(NocItems.ANIMAL_LUNG.get()));
        drop.setDefaultPickUpDelay();
        event.getDrops().add(drop);
    }

    private static boolean hasLungs(LivingEntity entity) {
        var type = entity.getType();
        if (type.is(HemoEntityTags.BLOOD_TRACE)) return false;
        if (type.is(EntityTypeTags.SKELETONS)) return false;
        if (type.is(HemoEntityTags.BLOOD_MEDIUM)) return false;
        if (type.is(HemoEntityTags.BLOOD_LOW)) return false;
        if (type.is(HemoEntityTags.BLOOD_FULL)) return true;
        return entity instanceof Animal
                || entity instanceof AbstractVillager;
    }
}