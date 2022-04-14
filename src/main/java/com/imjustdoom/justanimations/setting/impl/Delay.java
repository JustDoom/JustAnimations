package com.imjustdoom.justanimations.setting.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Delay {

    private String path;
    private int delay;

    public Delay(int delay) {
        this.delay = delay;
    }
}