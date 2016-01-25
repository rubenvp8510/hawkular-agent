/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
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
package org.hawkular.agent.monitor.inventory;

/**
 * Defines an operation that can be executed on the managed resource.
 *
 * The {@link #getName()} is the user-visible name (e.g. a human-readable, descriptive name).
 * The {@link #getOperationName()} is the actual operation that is to be executed on the managed resource.
 * For example, {@link #getName()} could return "Deploy Your Application" with the actual operation
 * to be executed on the managed resource, {@link #getOperationName()}, being "deploy-app".
 *
 * @author John Mazzitelli
 *
 * @param <L> the type of the protocol specific location, typically a subclass of {@link NodeLocation}
 */
public final class Operation<L> extends NodeLocationProvider<L> {

    private final String operationName;

    public Operation(ID id, Name name, L location, String operationName) {
        super(id, name, location);
        this.operationName = operationName;
    }

    /**
     * @return The actual operation to be executed on the managed resource. This is the operation name
     *         that is known to the managed resource and is what the managed resource expects to see
     *         when being asked to execute this operation.
     *         This is not the user-visible name (see {@link #getName()} for that).
     */
    public String getOperationName() {
        return operationName;
    }

}
