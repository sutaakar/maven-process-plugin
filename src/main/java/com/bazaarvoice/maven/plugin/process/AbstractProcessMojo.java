package com.bazaarvoice.maven.plugin.process;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

public abstract class AbstractProcessMojo extends AbstractMojo {
    @Component
    protected MavenProject project;

    @Parameter (property = "exec.arguments")
    protected String[] arguments;

    @Parameter(property = "exec.workingDir")
    protected String workingDir;

    @Parameter(property = "exec.name")
    protected String name;

    @Parameter(property = "exec.healthcheckUrl")
    protected String healthcheckUrl;

    @Parameter(defaultValue = "false", property = "exec.waitForInterrupt")
    protected boolean waitForInterrupt;

    protected static File ensureDirectory(File dir) {
        if (!dir.mkdirs() && !dir.isDirectory()) {
            throw new RuntimeException("couldn't create directories: " + dir);
        }
        return dir;
    }

}
