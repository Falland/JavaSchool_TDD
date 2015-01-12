package com.db.javaschool.tdd.babakov.homework;

import java.util.Collection;

public interface RunnableNode {
    void run();
    Collection<RunnableNode> getChildNodes();
}
