/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.registry.model.authorization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("topLevelPermissions")
public class TopLevelPermissions {

    private Permissions bucketsPermissions;
    private Permissions tenantsPermissions;
    private Permissions policiesPermissions;
    private Permissions proxyPermissions;

//    @XmlTransient
//    @ApiModelProperty("The access that the current user has to any top level resources (a logical 'OR' of all other values)")
//    public Permissions getHasAny() {
//        return new Permissions()
//                .withCanRead(bucketsPermissions.getCanRead()
//                        || tenantsPermissions.getCanRead()
//                        || policiesPermissions.getCanRead()
//                        || proxyPermissions.getCanRead())
//                .withCanWrite(bucketsPermissions.getCanWrite()
//                        || tenantsPermissions.getCanWrite()
//                        || policiesPermissions.getCanWrite()
//                        || proxyPermissions.getCanWrite())
//                .withCanDelete(bucketsPermissions.getCanDelete()
//                        || tenantsPermissions.getCanDelete()
//                        || policiesPermissions.getCanDelete()
//                        || proxyPermissions.getCanDelete());
//    }

    @ApiModelProperty("The access that the current user has to the top level /buckets resource of this NiFi Registry (i.e., access to all buckets)")
    public Permissions getBucketsPermissions() {
        return bucketsPermissions;
    }

    public void setBucketsPermissions(Permissions bucketsPermissions) {
        this.bucketsPermissions = bucketsPermissions;
    }

    @ApiModelProperty("The access that the current user has to the top level /tenants resource of this NiFi Registry")
    public Permissions getTenantsPermissions() {
        return tenantsPermissions;
    }

    public void setTenantsPermissions(Permissions tenantsPermissions) {
        this.tenantsPermissions = tenantsPermissions;
    }

    @ApiModelProperty("The access that the current user has to the top level /policies resource of this NiFi Registry")
    public Permissions getPoliciesPermissions() {
        return policiesPermissions;
    }

    public void setPoliciesPermissions(Permissions policiesPermissions) {
        this.policiesPermissions = policiesPermissions;
    }

    @ApiModelProperty("The access that the current user has to the top level /proxy resource of this NiFi Registry")
    public Permissions getProxyPermissions() {
        return proxyPermissions;
    }

    public void setProxyPermissions(Permissions proxyPermissions) {
        this.proxyPermissions = proxyPermissions;
    }
}
