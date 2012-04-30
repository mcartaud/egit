 * Copyright (C) 2012, IBM Corporation and others.
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import org.eclipse.egit.core.CoreText;
import org.eclipse.egit.core.op.ResetOperation;
import org.eclipse.egit.core.op.ResetOperation.ResetType;
import org.eclipse.egit.core.op.TagOperation;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TagBuilder;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.IO;
import org.eclipse.jgit.util.RawParseUtils;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.junit.After;
	private static Repository repo;

	private static final String TAG_NAME = "savepoint";

	private static final String PATCH_FILE = "test.patch";

	private static final String EXPECTED_PATCH_CONTENT = //
	"diff --git a/GeneralProject/folder/test.txt b/GeneralProject/folder/test.txt\n"
			+ "index e256dbb..d070357 100644\n"
			+ "--- a/GeneralProject/folder/test.txt\n"
			+ "+++ b/GeneralProject/folder/test.txt\n"
			+ "@@ -1 +1 @@\n"
			+ "-oldContent\n"
			+ "\\ No newline at end of file\n"
			+ "+newContent\n" //
			+ "\\ No newline at end of file";

	private static final String EXPECTED_WORKSPACE_PATCH_CONTENT = //
	"### Eclipse Workspace Patch 1.0\n" //
			+ "#P "	+ PROJ1	+ "\n"
			+ "diff --git folder/test.txt folder/test.txt\n" //
			+ "index e256dbb..d070357 100644\n" //
			+ "--- folder/test.txt\n" //
			+ "+++ folder/test.txt\n" //
			+ "@@ -1 +1 @@\n" //
			+ "-oldContent\n" //
			+ "\\ No newline at end of file\n" //
			+ "+newContent\n" //
			+ "\\ No newline at end of file\n"
			+ "diff --git folder/test2.txt folder/test2.txt\n"
			+ "deleted file mode 100644\n" //
			+ "index 8f4e8d3..0000000\n" //
			+ "--- folder/test2.txt\n" //
			+ "+++ /dev/null\n" //
			+ "@@ -1 +0,0 @@\n" //
			+ "-Some more content\n" //
			+ "\\ No newline at end of file\n" //
			+ "#P " + PROJ2 + "\n" //
			+ "diff --git test.txt test.txt\n" //
			+ "new file mode 100644\n" //
			+ "index 0000000..dbe9dba\n" //
			+ "--- /dev/null\n" //
			+ "+++ test.txt\n" //
			+ "@@ -0,0 +1 @@\n" //
			+ "+Hello, world\n" //
			+ "\\ No newline at end of file";

		File gitDir = createProjectAndCommitToRepository();
		repo = new FileRepository(gitDir);
		CommitOperation cop = new CommitOperation(commitables, untracked,
		cop.setAmending(true);
		cop.execute(null);
		TagBuilder tag = new TagBuilder();
		tag.setTag(TAG_NAME);
		tag.setTagger(RawParseUtils.parsePersonIdent(TestUtil.TESTAUTHOR));
		tag.setMessage("I'm a savepoint");
		tag.setObjectId(repo.resolve(repo.getFullBranch()),
				Constants.OBJ_COMMIT);
		TagOperation top = new TagOperation(repo, tag, false);
		top.execute(null);

		waitInUI();
	private static IFile[] getAllFiles() {
	@AfterClass
	public static void shutdown() {
		perspective.activate();
	}

	@After
	public void rollback() throws Exception {
		ResetOperation rop = new ResetOperation(repo, TAG_NAME, ResetType.HARD);
		rop.execute(null);
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(PROJ1);
		project.getFile(PATCH_FILE).delete(true, null);
	}

	@Test
	public void testNoChanges() throws Exception {
		CreatePatchWizard.openWizard(PROJ1);
		NoChangesPopup popup = new NoChangesPopup(
				bot.shell(UIText.GitCreatePatchAction_cannotCreatePatch));
		popup.cancelPopup();
	}

	@Test
	public void testNoChangesInSelection() throws Exception {
		IFile fileToStage = touch(PROJ1, "folder/test.txt", "new content in "
				+ PROJ1);
		stage(fileToStage);
		touch(PROJ2, "folder/test.txt", "new content in " + PROJ2);

		CreatePatchWizard.openWizard(PROJ1);

		NoChangesPopup popup = new NoChangesPopup(
				bot.shell(UIText.GitCreatePatchAction_cannotCreatePatch));
		popup.cancelPopup();
	}



		bot.waitUntil(Conditions.shellCloses(createPatchWizard.getShell()));

		assertClipboard(EXPECTED_PATCH_CONTENT);
	}

	@Test
	public void testFilesystem() throws Exception {
		touchAndSubmit("oldContent", null);
		touch("newContent");
		CreatePatchWizard createPatchWizard = openCreatePatchWizard();
		LocationPage locationPage = createPatchWizard.getLocationPage();
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(PROJ1);
		File patch = new File(project.getLocation().toFile(), PATCH_FILE);
		locationPage.selectFilesystem(patch.toString());
		createPatchWizard.finish();

		bot.waitUntil(Conditions.shellCloses(createPatchWizard.getShell()));

		byte[] bytes = IO.readFully(patch);
		String patchContent = new String(bytes, "UTF-8");

		assertEquals(EXPECTED_PATCH_CONTENT, patchContent);
	}

	@Test
	public void testWorkspace() throws Exception {
		touchAndSubmit("oldContent", null);
		touch("newContent");
		waitInUI();

		CreatePatchWizard createPatchWizard = openCreatePatchWizard();
		LocationPage locationPage = createPatchWizard.getLocationPage();
		locationPage.selectWorkspace("/" + PROJ1 + "/" + PATCH_FILE);
		createPatchWizard.finish();

		bot.waitUntil(Conditions.shellCloses(createPatchWizard.getShell()));

		IFile patch = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(PROJ1).getFile(PATCH_FILE);
		byte[] bytes = IO.readFully(patch.getLocation().toFile());
		String patchContent = new String(bytes, patch.getCharset());

		assertEquals(EXPECTED_PATCH_CONTENT, patchContent);
	}

	@Test
	public void testWorkspaceHeader() throws Exception {
		touchAndSubmit("oldContent", null);
		touch("newContent");
		URI fileLocationUri = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(PROJ1).getFolder(FOLDER).getFile(FILE2)
				.getLocationURI();
		FileUtils.delete(new File(fileLocationUri));
		IProject secondProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(PROJ2);
		IFile newFile = secondProject.getFile(FILE1);
		newFile.create(
				new ByteArrayInputStream("Hello, world".getBytes(secondProject
						.getDefaultCharset())), false, null);
		waitInUI();

		CreatePatchWizard createPatchWizard = openCreatePatchWizard();
		LocationPage locationPage = createPatchWizard.getLocationPage();
		locationPage.selectWorkspace("/" + PROJ1 + "/" + PATCH_FILE);
		OptionsPage optionsPage = locationPage.nextToOptionsPage();
		optionsPage.setFormat(CoreText.DiffHeaderFormat_Workspace);
		createPatchWizard.finish();

		bot.waitUntil(Conditions.shellCloses(createPatchWizard.getShell()));

		IFile patch = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(PROJ1).getFile(PATCH_FILE);
		byte[] bytes = IO.readFully(patch.getLocation().toFile());
		String patchContent = new String(bytes, patch.getCharset());

		assertEquals(EXPECTED_WORKSPACE_PATCH_CONTENT, patchContent);
		CreatePatchWizard.openWizard(PROJ1, PROJ2);