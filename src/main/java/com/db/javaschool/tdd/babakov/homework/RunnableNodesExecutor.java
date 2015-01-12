package com.db.javaschool.tdd.babakov.homework;

import java.util.concurrent.ExecutionException;

public interface RunnableNodesExecutor {
    void execute(RunnableNode firstNode) throws CycleFoundException, ExecutionException, InterruptedException;


}
