/*
 * SonarQube PMD Plugin
 * Copyright (C) 2012-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.pmd;

import java.io.File;
import java.util.Iterator;

import net.sourceforge.pmd.RuleViolation;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

import static java.lang.System.out;

public class PmdSensor implements Sensor {

    private final ActiveRules profile;
    private final PmdExecutor executor;
    private final PmdViolationRecorder pmdViolationRecorder;
    private final FileSystem fs;

    public PmdSensor(
        ActiveRules profile,
        PmdExecutor executor,
        PmdViolationRecorder pmdViolationRecorder,
        FileSystem fs
    ) {
        this.profile = profile;
        this.executor = executor;
        this.pmdViolationRecorder = pmdViolationRecorder;
        this.fs = fs;
        out.printf("--- PmdSensor - ActiveRules class: %s\n", profile.getClass().getName());
    }

    private boolean shouldExecuteOnProject() {
        return (hasFilesToCheck(Type.MAIN, PmdConstants.REPOSITORY_KEY))
            || (hasFilesToCheck(Type.TEST, PmdConstants.TEST_REPOSITORY_KEY));
    }

    private boolean hasFilesToCheck(Type type, String repositoryKey) {
        FilePredicates predicates = fs.predicates();
        // // ???
        // Iterator<InputFile> iter = fs.inputFiles(
        //     predicates.and(
        //         predicates.hasLanguage(PmdConstants.LANGUAGE_KEY),
        //         predicates.hasType(type)
        //     )
        // ).iterator();
        // while (iter.hasNext()) {
        //     out.printf("--- file path: %s\n", iter.next().toString());
        // }
        final boolean hasMatchingFiles = fs.hasFiles(
            predicates.and(
                predicates.hasLanguage(PmdConstants.LANGUAGE_KEY),
                predicates.hasType(type)
            )
        );
        return hasMatchingFiles
            && !profile.findByRepository(repositoryKey).isEmpty();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        out.println("--- PmdSensor.describe");
        descriptor.onlyOnLanguage(PmdConstants.LANGUAGE_KEY)
                .name("PmdSensor");
    }

    @Override
    public void execute(SensorContext context) {
        out.println("--- PmdSensor.execute"); // ???
        // return;
        if (shouldExecuteOnProject()) {
            out.printf("--- should execute\n");
            for (RuleViolation violation : executor.execute()) {
                out.printf("--- violation: %s\n", violation.getRule().getName());
                pmdViolationRecorder.saveViolation(violation, context);
            }
        }
    }
}
