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

package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildFileRule;
import org.apache.tools.ant.util.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests the "record" task's "relativeToBaseDir" behaviour
 */
public class RecorderBaseDirTest {

    @Rule
    public final BuildFileRule buildRule = new BuildFileRule();

    private static final String REC_IN = "recorder/";

    /**
     * Utilities used for file operations
     */
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    @Before
    public void setUp() {
        buildRule.configureProject("src/etc/testcases/taskdefs/recorder3.xml");
        // configure a custom basedir for the tests
        final String testTmpDir = buildRule.getProject().getProperty("java.io.tmpdir");
        buildRule.getProject().setBaseDir(new File(testTmpDir));
        System.out.println("Set basedir to " + testTmpDir + " for project "
                + buildRule.getProject().getName());

        buildRule.executeTarget("setUp");
    }

    /**
     * Resolves the given {@code fileName} relative to the {@code src/etc/testcases/taskdefs/}
     * directory.
     *
     * @param fileName The file name
     * @return the resolved file path
     */
    private static File resolveFile(final String fileName) {
        return FILE_UTILS.resolveFile(new File("src/etc/testcases/taskdefs/"), fileName);
    }

    /**
     * Verify that if the "name" of the "record" task is a relative path then setting
     * "relativeToBaseDir" to "true" creates the file under the basedir.
     */
    @Test
    public void testBaseDirRelative() throws IOException {
        buildRule.executeTarget("basedir-relative");
        final File expected = resolveFile(REC_IN + "rectest1.result");
        // expect the file to be generated in the basedir
        final File actual = new File(buildRule.getProject().getBaseDir(), "recorded-out-1.txt");
        assertTrue("content mismatch in files \"" + expected + "\" and \"" + actual + "\"",
                FILE_UTILS.contentEquals(expected, actual, true));
    }

    /**
     * Verify that if the "name" of the "record" task is an absolute path then setting
     * "relativeToBaseDir" to "true" plays no role in where the file is created.
     */
    @Test
    public void testAbsolutePath() throws IOException {
        buildRule.executeTarget("absolute-path");
        final File expected = resolveFile(REC_IN + "rectest1.result");
        // expect the file to be generated at the absolute path used in the target
        final File actual = buildRule.getOutputDir().toPath()
                .resolve("recorded")
                .resolve("recorded-out-2.txt")
                .toFile();
        assertTrue("content mismatch in files \"" + expected + "\" and \"" + actual + "\"",
                FILE_UTILS.contentEquals(expected, actual, true));
    }
}
