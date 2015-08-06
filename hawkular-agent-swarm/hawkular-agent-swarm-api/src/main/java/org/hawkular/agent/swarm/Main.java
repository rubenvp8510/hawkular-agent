/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.agent.swarm;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.wildfly.swarm.container.Container;

public class Main {

    public static void main(String[] args) throws Exception {

        Properties configurationProperties = new Properties();

        if (args.length < 1) {
            System.out.println("No configuration file specified, using defaults");
        } else {
            File configFile = new File(args[0]);
            if (!configFile.isFile()) {
                throw new IllegalArgumentException("Invalid configuration file: " + configFile);
            }
            configurationProperties.load(new FileInputStream(configFile));
        }

        AgentFraction agentFraction = configure(configurationProperties);

        Container container = new Container();
        container.subsystem(agentFraction);
        container.start();
    }

    private static AgentFraction configure(Properties props) throws Exception {

        AgentFraction fraction = new AgentFraction();

        fraction.availDispatcherBufferSize(val(props, "availDispatcherBufferSize", 500));
        fraction.availDispatcherMaxBatchSize(val(props, "availDispatcherMaxBatchSize", 50));
        fraction.metricDispatcherBufferSize(val(props, "metricDispatcherBufferSize", 1000));
        fraction.metricDispatcherMaxBatchSize(val(props, "metricDispatcherMaxBatchSize", 100));
        fraction.numAvailSchedulerThreads(val(props, "numAvailSchedulerThreads", 2));
        fraction.numMetricSchedulerThreads(val(props, "numMetricSchedulerThreads", 2));
        fraction.numDmrSchedulerThreads(val(props, "numDmrSchedulerThreads", 2));

        fraction.diagnostics(new Diagnostics());
        fraction.diagnostics().enabled(val(props, "diagnostics.enabled", true));
        fraction.diagnostics().reportTo(val(props, "diagnostics.reportTo", "LOG"));
        fraction.diagnostics().interval(val(props, "diagnostics.interval", 1));
        fraction.diagnostics().timeUnits(val(props, "diagnostics.timeUnits", "minutes"));

        fraction.storageAdapter(new StorageAdapter());
        fraction.storageAdapter().type(val(props, "storageAdapter.type", "HAWKULAR"));
        fraction.storageAdapter().username(val(props, "storageAdapter.username", "jdoe"));
        fraction.storageAdapter().password(val(props, "storageAdapter.password", "password"));
        fraction.storageAdapter().url(val(props, "storageAdapter.url", "http://127.0.0.1:8080"));
        fraction.storageAdapter().tenantId(val(props, "storageAdapter.tenantId", (String) null));

        //        fraction.dmrAvailSet();
        //        fraction.dmrMetricSet();
        //        fraction.dmrResourceTypeSet();
        //
        //        fraction.managedServers(new ManagedServers());
        //        fraction.managedServers().localDmr(a);
        //        fraction.managedServers().remoteDmr(a);

        return fraction;
    }

    private static String val(Properties props, String key, String defaultValue) {
        return (String) props.getOrDefault(key, defaultValue);
    }

    private static int val(Properties props, String key, int defaultValue) {
        return Integer.valueOf((String) props.getOrDefault(key, String.valueOf(defaultValue))).intValue();
    }

    private static boolean val(Properties props, String key, boolean defaultValue) {
        return Boolean.valueOf((String) props.getOrDefault(key, String.valueOf(defaultValue))).booleanValue();
    }
}
