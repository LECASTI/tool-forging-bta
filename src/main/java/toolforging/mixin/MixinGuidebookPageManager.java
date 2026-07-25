package toolforging.mixin;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookPageManager;
import net.minecraft.client.gui.guidebook.GuidebookSections;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Mixin to ensure the Recipe Booklet Back Cover page is placed at the VERY END of the book pages list
 * so the reverse face cover page renders correctly when flipping to the end of the Recipe Booklet.
 */
@Mixin(value = GuidebookPageManager.class, remap = false)
public class MixinGuidebookPageManager {

    @Shadow
    private List<GuidebookPage> pages;

    @Inject(method = "updatePages", at = @At("RETURN"))
    private void onUpdatePages(CallbackInfo ci) {
        if (pages != null && GuidebookSections.COVER != null) {
            List<GuidebookPage> coverPages = GuidebookSections.COVER.getPages();
            if (coverPages != null && coverPages.size() > 1) {
                GuidebookPage backCover = coverPages.get(1);
                if (pages.contains(backCover)) {
                    pages.remove(backCover);
                    pages.add(backCover);
                }
            }
        }
    }
}
