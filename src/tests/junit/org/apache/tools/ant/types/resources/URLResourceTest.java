/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.types.resources;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class URLResourceTest {

    /**
     * @see "https://bz.apache.org/bugzilla/show_bug.cgi?id=69680"
     */
    @Test
    public void getNameOnlyStripsLeadingPathSeparator() {
        assertEquals("foo", new URLResource("file:/foo").getName());
        assertEquals("foo", new URLResource("file://host/foo").getName());
        assertEquals("file:/foo!/bar", new URLResource("jar:file:/foo!/bar").getName());
    }
}
