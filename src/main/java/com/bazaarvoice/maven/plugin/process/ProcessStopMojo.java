package com.bazaarvoice.maven.plugin.process;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.Stack;

@Mojo (name = "stop-all", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class ProcessStopMojo extends AbstractProcessMojo {

    @Override
    public void execute()
            throws MojoExecutionException, MojoFailureException {
        for(String arg : arguments) {
            getLog().info("arg: " + arg);
        }
        try {
            stopAllProcesses();
        } catch (Exception e) {
            getLog().error(e);
        }
    }

    private void stopAllProcesses() {
        getLog().info("Stopping all processes ...");
        Stack<ExecProcess> processesStack = CrossMojoState.getProcesses(getPluginContext());
        while(!processesStack.isEmpty()) {
            ExecProcess execProcess = processesStack.pop();
            if (execProcess != null) {
                getLog().info("Stopping process: " + execProcess.getName());
                execProcess.destroy();
                execProcess.waitFor();
                getLog().info("Stopped process: " + execProcess.getName());
            }
        }
    }

}
