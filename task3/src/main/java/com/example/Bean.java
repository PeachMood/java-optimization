package com.example;

import java.util.ArrayList;
import java.util.List;

public class Bean {
    private final byte[] bytes = new byte[10000];
    private final List<String> list = new ArrayList<>();
    private final int primitive = 200;
    private final Bean bean = this;
}
