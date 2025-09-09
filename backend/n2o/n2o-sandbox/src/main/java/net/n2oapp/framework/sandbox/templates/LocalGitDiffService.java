package net.n2oapp.framework.sandbox.templates;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LocalGitDiffService {

    public static void collectChangedPackagesFromPackage(File gitRoot, String resourcesPath, String oldTag, String newTag,
                                                         Set<String> maybeDeletedPackages, Set<String> changedPackages) throws IOException, InterruptedException {
        List<String> diffOutput = runGitCommand(gitRoot, "diff", "--name-status", oldTag, newTag, "--", resourcesPath);
        for (String line : diffOutput) {
            String path = null;
            String changes = "";
            if (line != null && !line.isBlank()) {
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    changes = parts[0];
                    String candidatePath = parts[parts.length - 1];
                    if (candidatePath.startsWith(resourcesPath)) {
                        path = candidatePath;
                    }
                }
            }
            if (path == null) {
                continue;
            }

            String pkg = extractDirectory(resourcesPath, path);
            if (pkg != null && !pkg.isBlank()) {
                if (changes.equals("D")) {
                    maybeDeletedPackages.add(pkg);
                } else {
                    changedPackages.add(pkg);
                }
            }
        }

        changedPackages.remove("menu");
    }

    public static List<String> runGitCommand(File workingDir, String... args) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("git");
        command.addAll(Arrays.asList(args));

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDir);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        List<String> output = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        }
        int code = p.waitFor();
        if (code != 0) {
            throw new IOException("Git command failed: " + String.join(" ", command) + "\nOutput: " + String.join("\n", output));
        }
        return output;
    }

    private static String extractDirectory(String basePath, String fullPath) {
        String normalized = fullPath.replace('\\', '/');
        String base = basePath.replace('\\', '/');
        if (!normalized.startsWith(base)) {
            return null;
        }
        String remainder = normalized.substring(base.length());
        if (remainder.startsWith("/")) {
            remainder = remainder.substring(1);
        }
        int lastSlash = remainder.lastIndexOf('/');
        if (lastSlash <= 0) {
            return null;
        }
        return remainder.substring(0, lastSlash);
    }

}
