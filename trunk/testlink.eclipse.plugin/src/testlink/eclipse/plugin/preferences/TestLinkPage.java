package testlink.eclipse.plugin.preferences;


import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import testlink.eclipse.plugin.Activator;


/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class TestLinkPage extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage
{

	public TestLinkPage()
	{
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		String msg = "\n" +
		             "Project Name - The default project that is open by TestLink.\n" +
		             "Dev Key - TestLink API Acces key. See TestLink API documentation.\n" +
		             "TestLink URL - See TestLink API documentation.\n" +
		             "User Login Name - Used to assign new test case.\n" +
		             "Plan Prepare Class - See TestPlanPrepare interface javadoc.\n" +
		             "Report Results - Default results reporting to TestLink flag.\n" +
		             "External Path (Optional) - Made available to TestPlanPrepare.\n\n";             
		setDescription(msg);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors()
	{

		addField(
				new StringFieldEditor(PreferenceConstants.P_DEFAULT_PROJECT_NAME,
				"Project Name:", getFieldEditorParent()));
		
		addField(
			new StringFieldEditor(PreferenceConstants.P_DEV_KEY,
			"Dev Key:", getFieldEditorParent()));
		
		addField(
			new StringFieldEditor(PreferenceConstants.P_TESTLINK_URL,
			"TestLink URL:", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PreferenceConstants.P_TEST_CASE_CREATION_USER,
				"User Login Name:", getFieldEditorParent()));
		
		addField(
			new StringFieldEditor(PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS,
			"Plan Prepare Class:", getFieldEditorParent()));
		
		addField(
			new BooleanFieldEditor(PreferenceConstants.P_REPORT_RESULTS_AFTER_TEST,
			"Enable TestLink API results reporting after test.", getFieldEditorParent()));
		
		addField(
			new DirectoryFieldEditor(PreferenceConstants.P_OPTIONAL_EXTERNAL_CONFIG_FILE, 
			"External Path:",
			getFieldEditorParent()));

		/*
		 addField(new RadioGroupFieldEditor(
		 PreferenceConstants.P_CHOICE,
		 "An example of a multiple-choice preference",
		 1,
		 new String[][] { { "&Choice 1", "choice1" }, {
		 "C&hoice 2", "choice2" }
		 }, getFieldEditorParent()));
		 */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(
		IWorkbench workbench)
	{}
	
}
