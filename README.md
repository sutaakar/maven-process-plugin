process-exec-maven-plugin
==========================

Process Executor Plugin

## Arguments
* arguments: Commandline arguments as you would provide when starting a process builder in Java. So, for example to run something like this
    ```bash
    java -jar drop-wizard-app.jar server config.yaml
    ```
    set arguments as:
    <arguments>
        <argument>java</argument>
        <argument>-jar</argument>
        <argument>drop-wizard-app.jar</argument>
        <argument>server</argument>
        <argument>config.yaml</argument>
    </arguments>


* name: Given name to the process to start
* workingDir: Give a working directory for your process to start in. Could be same as name.
* waitForInterrupt: Optional. Setting this value to true will pause your build after starting every process to give you a chance to manually play with your system.

## POM example:
    ```
    <profiles>
        <profile>
            <id>create-qa-dimsum</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>com.bazaarvoice.maven.plugins</groupId>
                        <artifactId>process-exec-maven-plugin</artifactId>
                        <version>{plugin.version}</version>
                        <executions>
                            <!--Start process 1, eg., a dropwizard app dependency-->
                            <execution>
                                <id>emodb-shovel-process</id>
                                <phase>pre-integration-test</phase>
                                <goals>
                                    <goal>start</goal>
                                </goals>
                                <configuration>
                                    <name>Switchboard2</name>
                                    <workingDir>switchboard2</workingDir>
                                    <waitForInterrupt>false</waitForInterrupt>
                                    <healthcheckUrl>http://localhost:8381/healthcheck</healthcheckUrl>
                                    <arguments>
                                        <argument>java</argument>
                                        <argument>-jar</argument>
                                        <argument>${basedir}/../../app/target/switchboard-${project.version}.jar</argument>
                                        <argument>server</argument>
                                        <argument>${basedir}/bin/switchboard.yaml</argument>
                                    </arguments>
                                </configuration>
                            </execution>
                            <!--Start process 2, eg., another dropwizard app dependency-->
                            <execution>
                                <id>emodb-shovel-process</id>
                                <phase>pre-integration-test</phase>
                                <goals>
                                    <goal>start</goal>
                                </goals>
                                <configuration>
                                    <name>emodb-shovel</name>
                                    <workingDir>shovel</workingDir>
                                    <waitForInterrupt>false</waitForInterrupt>
                                    <healthcheckUrl>http://localhost:8181/healthcheck</healthcheckUrl>
                                    <arguments>
                                        <argument>java</argument>
                                        <argument>-jar</argument>
                                        <argument>${basedir}/../../app/target/emodb-shovel-app-${project.version}.jar</argument>
                                        <argument>server</argument>
                                        <argument>${basedir}/bin/config-local-dc.yaml</argument>
                                    </arguments>
                                </configuration>
                            </execution>
                            <!--Stop all processes in reverse order-->
                            <execution>
                                <id>stop-all</id>
                                <phase>post-integration-test</phase>
                                <goals>
                                    <goal>stop-all</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
    ```



