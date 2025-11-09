package dev.khanhtimn.gravitychanger;

import com.bawnorton.mixinsquared.MixinSquaredBootstrap;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.fallenbreath.conditionalmixin.api.checker.RestrictionChecker;
import me.fallenbreath.conditionalmixin.api.checker.RestrictionCheckers;
import me.fallenbreath.conditionalmixin.api.mixin.AnnotationCleaner;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class GravityMixinPlugin implements IMixinConfigPlugin {

    protected final RestrictionChecker restrictionChecker = RestrictionCheckers.memorized();
    private final AnnotationCleaner annotationCleaner = AnnotationCleaner.create(Restriction.class);

    public GravityMixinPlugin() {
        this.restrictionChecker.setFailureCallback(this::onRestrictionCheckFailed);
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return this.restrictionChecker.checkRestriction(mixinClassName);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        this.annotationCleaner.onPreApply(targetClass);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        this.annotationCleaner.onPostApply(targetClass);
    }

    /**
     * go override it and do something you want, e.g. logging
     */
    protected void onRestrictionCheckFailed(String mixinClassName, String reason)
    {
    }
}
