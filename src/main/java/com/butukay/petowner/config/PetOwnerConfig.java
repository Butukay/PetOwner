package com.butukay.petowner.config;

public class PetOwnerConfig {
    private boolean enabled = true;
    private Mode usageMode = Mode.TOGGLE;
    private boolean alwaysShown = true;
    private boolean actionBar = true;
    private boolean showToggleMessage = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Mode getUsageMode() {
        return usageMode;
    }

    public void setUsageMode(Mode usageMode) {
        this.usageMode = usageMode;
    }

    public boolean isAlwaysShown() {
        return alwaysShown;
    }

    public void setAlwaysShown(boolean alwaysShown) {
        this.alwaysShown = alwaysShown;
    }

    public boolean isActionBar() {
        return actionBar;
    }

    public void setActionBar(boolean actionBar) {
        this.actionBar = actionBar;
    }

    public boolean isShowToggleMessage() {
        return showToggleMessage;
    }

    public void setShowToggleMessage(boolean showToggleMessage) {
        this.showToggleMessage = showToggleMessage;
    }

    public enum Mode {
        TOGGLE,
        HOLD,
        CLICK,
    }
}
