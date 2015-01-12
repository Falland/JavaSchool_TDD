package com.db.javaschool.tdd.babakov.homework;


import java.util.*;
import java.util.concurrent.*;

public class RunnableNodesExecutorImpl implements RunnableNodesExecutor {

    private class AdditionalInfoForRunningNode {
        Collection<CountDownLatch> dependentOnLatches = new ArrayList<>();
        Collection<RunnableNode> dependentOnNodes = new ArrayList<>();
        CountDownLatch ownLatch = new CountDownLatch(1);
    }

    Map<RunnableNode, AdditionalInfoForRunningNode> node2Info = new ConcurrentHashMap<>();

    @Override
    public void execute(RunnableNode firstNode) throws CycleFoundException, ExecutionException, InterruptedException {
        prepareMapsAndCheckForCycle(firstNode);

        Deque<RunnableNode> deq = new ConcurrentLinkedDeque<>();
        deq.add(firstNode);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        while (!deq.isEmpty()) {
            final RunnableNode currentNode = deq.pollFirst();

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        AdditionalInfoForRunningNode info = node2Info.get(currentNode);
                        for (CountDownLatch latche : info.dependentOnLatches) {
                            latche.await();
                        }

                        currentNode.run();
                        info.ownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (currentNode.getChildNodes() != null) {
                deq.addAll(currentNode.getChildNodes());
            }
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    private void prepareMapsAndCheckForCycle(RunnableNode firstNode) throws CycleFoundException {
        Deque<RunnableNode> deq = new ConcurrentLinkedDeque<>();
        deq.add(firstNode);

        while (!deq.isEmpty()) {
            RunnableNode currentNode = deq.pollFirst();
            AdditionalInfoForRunningNode info = createRecordIfAbsent(currentNode);

            if (currentNode == firstNode) {
                info.ownLatch.countDown();
            }

            if (currentNode.getChildNodes() == null) {
                continue;
            }

            for (RunnableNode node : currentNode.getChildNodes()) {
                AdditionalInfoForRunningNode cninfo = createRecordIfAbsent(node);

                cninfo.dependentOnLatches.add(info.ownLatch);
                cninfo.dependentOnNodes.add(currentNode);

                cninfo.dependentOnNodes.addAll(info.dependentOnNodes);

                if (cninfo.dependentOnNodes.contains(node)) {
                    throw new CycleFoundException();
                }

                deq.add(node);
            }
        }


    }

    private AdditionalInfoForRunningNode createRecordIfAbsent(RunnableNode node) {
        AdditionalInfoForRunningNode info = node2Info.get(node);
        if (info == null) {
            info = new AdditionalInfoForRunningNode();
            node2Info.put(node, info);
        }
        return info;
    }
}
