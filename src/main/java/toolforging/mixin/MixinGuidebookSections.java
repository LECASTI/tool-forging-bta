package toolforging.mixin;

import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.gui.guidebook.GuidebookSections;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toolforging.ToolForgingClient;

import java.util.List;

/**
 * Mixin to ensure the Reforging Anvil section bookmark tab and page numbers
 * sit at the VERY END of the Recipe Booklet (after Creatures/Mobs) without breaking cover pages.
 */
@Mixin(value = GuidebookSections.class, remap = false)
public class MixinGuidebookSections {

    @Shadow
    private static List<GuidebookSection> sectionList;

    @Inject(method = "init", at = @At("RETURN"))
    private static void onInit(CallbackInfo ci) {
        if (ToolForgingClient.SECTION_REFORGING != null && sectionList != null) {
            int index = sectionList.indexOf(ToolForgingClient.SECTION_REFORGING);
            if (index != -1 && index < sectionList.size() - 1) {
                for (int i = index; i < sectionList.size() - 1; i++) {
                    sectionList.set(i, sectionList.get(i + 1));
                }
                sectionList.set(sectionList.size() - 1, ToolForgingClient.SECTION_REFORGING);
            }
        }
    }
}
