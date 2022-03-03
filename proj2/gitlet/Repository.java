package gitlet;


import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     * <p>
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File Branch = join(GITLET_DIR, "branch");
    public static final File COMMITS = join(GITLET_DIR, "commits");
    public static final File BLOBS = join(GITLET_DIR, "blobs");
    public static final File STAGES = join(GITLET_DIR, "stages");
    public static final File HEAD = join(GITLET_DIR, "head");
    /* TODO: fill in the rest of this class. */


    public void init() {
        if (GITLET_DIR.exists()) {
            message("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        } else {
            GITLET_DIR.mkdir();
            Branch.mkdir();
            BLOBS.mkdir();
            COMMITS.mkdir();
            try {
                STAGES.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                HEAD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            StagingArea stage = new StagingArea();
            writeObject(STAGES, stage);
            writeContents(HEAD, "master");
            Commit initialCommit = new Commit();
            File currentBranch = join(Branch, getHEAD());
            writeObject(currentBranch, initialCommit);
            File commitFile = join(COMMITS, initialCommit.getID());
            writeObject(commitFile, initialCommit);
        }
    }

    public void add(String file) {
        File toAddFile = join(CWD, file);
        if (toAddFile.exists()) {
            String blob = readContentsAsString(toAddFile);
            String blobID = sha1(blob);
            Commit currentCommit = getCurrentCommit();
            StagingArea stage = getStage();
            if (currentCommit.getTree().containsKey(file) && currentCommit.getTree().get(file).equals(blobID)) {
                if (stage.getAddition().containsKey(file)) {
                    stage.getAddition().remove(file);
                }
            } else {
                stage.getAddition().put(file, blobID);
                writeContents(join(BLOBS, blobID), blob);
            }
            if (stage.getRemoval().contains(file)) {
                stage.getRemoval().remove(file);
            }
            writeObject(STAGES, stage);
        } else {
            message("File does not exist");
            System.exit(0);
        }
    }

    public void rm(String file) {
        Commit currentCommit = getCurrentCommit();
        StagingArea stage = getStage();
        if (!stage.isEmpty()) {
            message("No reason to remove the file.");
            System.exit(0);
        } else {
            if (stage.getAddition().containsKey(file)) {
                stage.getAddition().remove(file);
            }
            if (currentCommit.getTree().containsKey(file)) {
                stage.getRemoval().add(file);
                File toRemove = join(CWD, file);
                if (toRemove.exists()) {
                    toRemove.delete();
                }
            }
            writeObject(STAGES, stage);
        }
    }

    public void commit(String msg) {
        StagingArea stage = getStage();
        if (!stage.isEmpty()) {
            Commit currentCommit = getCurrentCommit();
            String parentID = currentCommit.getID();
            HashMap<String, String> newTree = currentCommit.getTree();
            if (!stage.getAddition().isEmpty()) {
                Set<String> files = stage.getAddition().keySet();
                for (String file : files) {
                    String blob = stage.getAddition().get(file);
                    newTree.put(file, blob);
                }
            }
            if (!stage.getRemoval().isEmpty()) {
                for (String file : stage.getRemoval()) {
                    newTree.remove(file);
                }
            }
            Commit newCommit = new Commit(msg, newTree, parentID);
            File commitFile = join(COMMITS, newCommit.getID());
            writeObject(commitFile, newCommit);
            File currentBranch = join(Branch, getHEAD());
            writeObject(currentBranch, newCommit);
            stage.clear();
            writeObject(STAGES, stage);
        } else {
            message("No changes added to the commit.");
            System.exit(0);
        }
    }

    public void log() {
        Commit curr = getCurrentCommit();
        while (true) {
            System.out.println("===");
            System.out.println("commit " + curr.getID());
            System.out.println("Date: " + curr.getDate());
            System.out.println(curr.getMessage());
            System.out.println();
            if (curr.getParentID() != null) {
                curr = readObject(join(COMMITS, curr.getParentID()), Commit.class);
            } else {
                break;
            }
        }
    }

    public void globallog() {
        List<String> files = plainFilenamesIn(COMMITS);
        if (files != null) {
            for (String file : files) {
                Commit curr = readObject(join(COMMITS, file), Commit.class);
                System.out.println("===");
                System.out.println("commit " + curr.getID());
                System.out.println("Date: " + curr.getDate());
                System.out.println(curr.getMessage());
                System.out.println();
            }
        }
    }

    public void find(String msg) {
        List<String> files = plainFilenamesIn(COMMITS);
        int size = files.size();
        for (String file : files) {
            Commit commit = readObject(join(COMMITS, file), Commit.class);
            if (commit.getMessage().equals(msg)) {
                files.remove(file);
                System.out.println(commit.getMessage());
            }
        }
        if (files.size() == size) {
            message("Found no commit with that message.");
            System.exit(0);
        }
    }

    public void status() {
        StagingArea stage = getStage();
        List<String> branches = plainFilenamesIn(Branch);
        Collections.sort(branches);
        System.out.println("=== Branches ===");
        for (String branch : branches) {
            System.out.println("*" + branch);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String stageFile : stage.getAddition().keySet()) {
            System.out.println(stageFile);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String removeFile : stage.getRemoval()) {
            System.out.println(removeFile);
        }
        System.out.println();
        Set<String> modiFiles = new TreeSet<>();
        Commit curr = getCurrentCommit();
        HashMap<String, String> tree = curr.getTree();
        TreeMap<String, String> addition = (TreeMap<String, String>) stage.getAddition();
        Set<String> removal = stage.getRemoval();
        List<String> files = plainFilenamesIn(CWD);
        for (String trackFile : tree.keySet()) {
            if (files.contains(trackFile)) {
                String blob = readContentsAsString(join(CWD, trackFile));
                String blobID = sha1(blob);
                if (!tree.get(trackFile).equals(blobID) && !addition.containsKey(trackFile)) {
                    modiFiles.add(trackFile + "(modified)");
                }
            } else {
                if (!removal.contains(trackFile)) {
                    modiFiles.add(trackFile + "(deleted)");
                }
            }
        }
        for (String addFile : addition.keySet()) {
            if (files.contains(addFile)) {
                String blob = readContentsAsString(join(CWD, addFile));
                String blobID = sha1();
                if (!addition.get(addFile).equals(blobID)) {
                    modiFiles.add(addFile + "(modified)");
                }
            } else {
                modiFiles.add(addFile + "(deleted)");
            }
        }
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String modiFile : modiFiles) {
            System.out.println(modiFile);
        }
        System.out.println();
        Set<String> untrackFiles = new TreeSet<>();
        for (String file : files) {
            if (!tree.containsKey(file) && !addition.containsKey(file)) {
                untrackFiles.add(file);
            }
        }
        System.out.println("=== Untracked Files ===");
        for (String untrackFile : untrackFiles) {
            System.out.println(untrackFile);
        }
        System.out.println();
    }

    public void checkout1(String file) {
        HashMap<String, String> tree = getCurrentCommit().getTree();
        if (tree.containsKey(file)) {
            String blob = readContentsAsString(join(BLOBS, tree.get(file)));
            writeContents(join(CWD, file), blob);
        } else {
            message("File does not exist in that commit.");
            System.exit(0);
        }
    }

    public void checkout2(String ID, String file) {
        File target = join(COMMITS, ID);
        if (target.exists()) {
            HashMap<String, String> tree = readObject(target, Commit.class).getTree();
            if (tree.containsKey(file)) {
                String blob = readContentsAsString(join(BLOBS, tree.get(file)));
                writeContents(join(CWD, file), blob);
            } else {
                message("File does not exist in that commit.");
                System.exit(0);
            }
        } else {
            message("No commit with that id exists.");
            System.exit(0);
        }
    }

    public void checkout3(String branch) {
        if (branch == getHEAD()) {
            message("No need to checkout the current branch.");
            System.exit(0);
        }
        File branchFile = join(Branch, branch);
        if (branchFile.exists()) {
            Set<String> trackFiles = getCurrentCommit().getTree().keySet();
            Set<String> files = readObject(branchFile, Commit.class).getTree().keySet();
            String branchID = readObject(branchFile, Commit.class).getID();
            for (String file : files) {
                if (trackFiles.contains(file)) {
                    checkout2(branchID, file);
                } else {
                    message("There is an untracked file in the way; delete it, or add and commit it first.");
                    System.exit(0);
                }
            }
            for (String trackFile : trackFiles) {
                if (!files.contains(trackFile)) {
                    join(CWD, trackFile).delete();
                }
            }
            writeContents(HEAD,branch);
            StagingArea stage = getStage();
            stage.clear();
            writeObject(STAGES, stage);
        } else {
            message("No such branch exists.");
            System.exit(0);
        }

    }


    private Commit getCurrentCommit() {
        return readObject(join(Branch, getHEAD()), Commit.class);
    }

    private StagingArea getStage() {
        return readObject(STAGES, StagingArea.class);
    }

    private String getHEAD() {
        return readContentsAsString(HEAD);
    }
}
