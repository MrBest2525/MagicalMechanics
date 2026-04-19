package io.github.mrbest2525.magicalmechanics.core.gui;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface SoraUICoreLiveViewer {
    ResourceLocation liveViewer(ISoraUICoreDrawingElement.ISoraUIDrawingLiveViewerElement<?> element);
}
