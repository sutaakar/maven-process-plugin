package com.bazaarvoice.maven.plugin.process;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.IOException;

import static com.google.inject.internal.util.Preconditions.checkNotNull;

@Mojo (name = "start", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class ProcessStartMojo extends AbstractProcessMojo {

    @Override
    public void execute()
            throws MojoExecutionException, MojoFailureException {
        for(String arg : arguments) {
            getLog().info("arg: " + arg);
        }
        try {
            startProcess();
            if (waitForInterrupt) {
                sleepUntilInterrupted();
            }
        } catch (Exception e) {
            getLog().error(e);
        }
    }

    private void startProcess() {
        final ExecProcess exec = new ExecProcess(name);
        if (null != processLogFile) {
            exec.setProcessLogFile(new File(processLogFile));
        }
        getLog().info("Starting process: " + exec.getName());
        exec.execute(processWorkingDirectory(), getLog(), arguments);
        CrossMojoState.addProcess(exec, getPluginContext());
        ProcessHealthCondition.waitSecondsUntilHealthy(healthcheckUrl, waitAfterLaunch);
        getLog().info("Started process: " + exec.getName());
    }

    private File processWorkingDirectory() {
        if (workingDir == null) {
            return ensureDirectory(new File(project.getBuild().getDirectory()));
        }
        return ensureDirectory(new File(project.getBuild().getDirectory(), workingDir));
    }

    private void sleepUntilInterrupted() throws IOException {
        getLog().info("Hit ENTER on the console to continue the build.");

        for (;;) {
            int ch = System.in.read();
            if (ch == -1 || ch == '\n') {
                break;
            }
        }
    }

}
