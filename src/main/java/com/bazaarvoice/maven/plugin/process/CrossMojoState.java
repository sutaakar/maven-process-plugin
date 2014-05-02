package com.bazaarvoice.maven.plugin.process;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CrossMojoState {
    private static final String PROCESSES = "processes";

    @SuppressWarnings("unchecked")
    public static void addProcess(ExecProcess emoProcess, Map pluginContext) {
        if (pluginContext.get(PROCESSES) != null) {
            // Pre-pend to the list in a stack implementation
            ((List<ExecProcess>) pluginContext.get(PROCESSES)).add(0, emoProcess);
            return;
        }
        pluginContext.put(PROCESSES, Lists.newArrayList(emoProcess));
    }

    @SuppressWarnings("unchecked")
    public static List<ExecProcess> getProcesses(Map pluginContext) {
        if (pluginContext.get(PROCESSES) == null) {
            return Collections.emptyList();
        }
        return (List<ExecProcess>) pluginContext.get(PROCESSES);
    }
}
