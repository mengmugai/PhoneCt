package com.mmg.phonect.main.adapters.main;

import com.mmg.phonect.common.ui.adapters.TagAdapter;

public class AppListTag implements TagAdapter.Tag {

    private final String name;
    private final Type type;

    public enum Type {XPOSEDMODULE, HOOK_FRAMEWORK,}

    public AppListTag(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}
