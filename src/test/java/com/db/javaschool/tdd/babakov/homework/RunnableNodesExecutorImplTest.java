package com.db.javaschool.tdd.babakov.homework;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RunnableNodesExecutorImplTest {
    RunnableNodesExecutor executor;

    @Before
    public void init() {
        executor = new RunnableNodesExecutorImpl();
    }


    @Test(expected = CycleFoundException.class, timeout = 100)
    public void testExecute_cyclicDependenciesTest() throws Exception {
        RunnableNode cyclicTask = mock(RunnableNode.class);
        RunnableNode cyclicSubtask = mock(RunnableNode.class);
        when(cyclicTask.getChildNodes()).thenReturn(Collections.singletonList(cyclicSubtask));
        when(cyclicSubtask.getChildNodes()).thenReturn(Collections.singletonList(cyclicTask));
        executor.execute(cyclicTask);
    }

    @Test(timeout = 100)
    public void testExecute_onePossibleExecutionWay() throws Exception {
        RunnableNode mainNode = mock(RunnableNode.class);
        RunnableNode subNode = mock(RunnableNode.class);
        when(mainNode.getChildNodes()).thenReturn(Collections.singletonList(subNode));
        executor.execute(mainNode);

        InOrder inOrder = inOrder(mainNode, subNode);
        inOrder.verify(mainNode).run();
        inOrder.verify(subNode).run();
    }

    @Test(timeout = 100)
    public void testExecute_fewPossibleExecutionWays() throws Exception {
        RunnableNode mainNode = mock(RunnableNode.class);
        RunnableNode subtask1 = mock(RunnableNode.class);
        RunnableNode subtask2 = mock(RunnableNode.class);
        when(mainNode.getChildNodes()).thenReturn(Arrays.asList(subtask1, subtask2));

        executor.execute(mainNode);

        InOrder inOrder1 = inOrder(mainNode, subtask1);
        inOrder1.verify(mainNode).run();
        inOrder1.verify(subtask1).run();

        InOrder inOrder2 = inOrder(mainNode, subtask2);
        inOrder2.verify(mainNode).run();
        inOrder2.verify(subtask2).run();
    }
}