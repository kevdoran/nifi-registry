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
package org.apache.nifi.registry.service;

import org.apache.nifi.registry.db.entity.BucketEntity;
import org.apache.nifi.registry.db.entity.FlowEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class IdResolver {

    public static final int PREFIX_LENGTH = 2;
    public static final String ID_PREFIX = "i=";
    public static final String NAME_PREFIX = "n=";
    public static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    private static final Pattern uuidPattern = Pattern.compile(UUID_REGEX);
    private static final Logger logger = LoggerFactory.getLogger(IdResolver.class);

    private MetadataService metadataService;

    @Autowired
    public IdResolver(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    /**
     * TODO, add documentation and logging
     */
    public Optional<String> resolveBucketId(final String bucketIdOrName) {
        if (bucketIdOrName == null) {
            throw new IllegalArgumentException("bucketIdOrName must not be null");
        }

        final Optional<String> bucketIdOptional;
        if (isExplicitId(bucketIdOrName)) {
            bucketIdOptional = Optional.of(removePrefix(bucketIdOrName));
        } else if (isExplicitName(bucketIdOrName)) {
            bucketIdOptional = getBucketIdByName(urlDecode(removePrefix(bucketIdOrName)));
        } else {
            bucketIdOptional = isUUID(bucketIdOrName)
                    ? Optional.of(bucketIdOrName)
                    : getBucketIdByName(bucketIdOrName);
        }

        return bucketIdOptional;
    }

    /**
     * TODO, add documentation and logging
     */
    public Optional<String> resolveFlowId(final String bucketId, String flowIdOrName) {
        if (bucketId == null || flowIdOrName == null) {
            throw new IllegalArgumentException("bucketId and flowIdOrName must not be null");
        }


        final Optional<String> flowIdOptional;
        if (isExplicitId(flowIdOrName)) {
            flowIdOptional = Optional.of(removePrefix(flowIdOrName));
        } else if (isExplicitName(flowIdOrName)) {
            flowIdOptional = getFlowIdByBucketAndName(bucketId, urlDecode(removePrefix(flowIdOrName)));
        } else {
            flowIdOptional = isUUID(flowIdOrName)
                    ? Optional.of(flowIdOrName)
                    : getFlowIdByBucketAndName(bucketId, flowIdOrName);
        }

        return flowIdOptional;
    }

    /**
     * TODO, add documentation and logging
     */
    public Optional<String> getBucketIdByName(String bucketName) {
        if (bucketName == null) {
            throw new IllegalArgumentException("Bucket name must not be null");
        }

        final Optional<String> bucketId = metadataService.getBucketsByName(bucketName).stream().findFirst().map(BucketEntity::getId);
        return bucketId;
    }

    /**
     * TODO, add documentation and logging
     */
    public Optional<String> getFlowIdByBucketAndName(String bucketId, String flowName) {
        if (bucketId == null || flowName == null) {
            throw new IllegalArgumentException("Bucket ID and flow name must not be null");
        }

        final Optional<String> flowId = metadataService.getFlowsByName(bucketId, flowName).stream().findFirst().map(FlowEntity::getId);
        return flowId;
    }

    private static boolean isExplicitId(final String string) {
        return string.startsWith(ID_PREFIX);
    }

    private static boolean isExplicitName(final String string) {
        return string.startsWith(NAME_PREFIX);
    }

    private static String removePrefix(final String string) {
        return string.substring(PREFIX_LENGTH);
    }

    private static String urlDecode(final String string) {
        try {
            return URLDecoder.decode(string, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not decode argument", e);
        }

    }

    private static boolean isUUID(final String string) {
        return uuidPattern.matcher(string).matches();
    }

}
