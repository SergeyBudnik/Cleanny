package com.bdev.cleanny.imports.checker;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

@Mojo(name = "check_imports")
public class CheckImportsMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "error-imports", required = true)
    String [] errorImports;
    @Parameter(property = "warn-imports", required = true)
    String [] warnImports;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("CLEANNY: Checking imports. Started.");

        long t1 = System.currentTimeMillis();

        File srcRoot = new File(project.getBuild().getSourceDirectory());

        Collection<File> allFiles = FileUtils.listFiles(srcRoot, new String[] {"java"}, true);

        for (File f : allFiles) {
            processAllFileImports(f);
        }

        long t2 = System.currentTimeMillis();

        getLog().info(String.format("CLEANNY: Checking imports. Finished in %d ms.", t2 - t1));
    }

    private void processAllFileImports(File f) throws MojoExecutionException {
        CheckImportsMojoState state = CheckImportsMojoState.START;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }

                state = state.getCurrentState(line);

                if (state == CheckImportsMojoState.ON_IMPORTS) {
                    processSingleFileImport(f, line);
                } else if (state == CheckImportsMojoState.FINISH) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processSingleFileImport(File f, String importLine) throws MojoExecutionException {
        for (String errorImport : errorImports) {
            if (importLine.contains(errorImport)) {
                getLog().error(String.format("CLEANNY: Checking imports. Failing build; reason: '%s' is prohibited in file '%s'.", importLine, f.getName()));
                throw new MojoExecutionException(importLine + " is prohibited");
            }
        }

        for (String warnImport : warnImports) {
            if (importLine.contains(warnImport)) {
                getLog().warn(String.format("CLEANNY: Checking imports. Warn on build; reason: '%s' is not recommended in file %s.", importLine, f.getName()));
            }
        }
    }
}
