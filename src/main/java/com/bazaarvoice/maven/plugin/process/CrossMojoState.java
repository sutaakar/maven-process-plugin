package com.bazaarvoice.maven.plugin.process;

import java.util.Map;
import java.util.Stack;

public class CrossMojoState {
    private static final String PROCESSES = "processes";

    @SuppressWarnings("unchecked")
    public static void addProcess(ExecProcess process, Map pluginContext) {
        getProcesses(pluginContext).push(process);
//        if (pluginContext.get(PROCESSES) != null) {
//            ((Stack<ExecProcess>) pluginContext.get(PROCESSES)).push(process);
//            return;
//        }
//        Stack<ExecProcess> stack = new Stack<ExecProcess>();
//        stack.push(process);
//        pluginContext.put(PROCESSES, stack);
    }

    @SuppressWarnings("unchecked")
    public static Stack<ExecProcess> getProcesses(Map pluginContext) {
        Stack<ExecProcess> processes = (Stack<ExecProcess>) pluginContext.get(PROCESSES);
        if (processes == null) {
            processes = new Stack<ExecProcess>();
            pluginContext.put(PROCESSES, processes);
        }
        return processes;
    }
}
