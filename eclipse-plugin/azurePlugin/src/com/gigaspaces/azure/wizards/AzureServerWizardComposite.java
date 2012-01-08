package com.gigaspaces.azure.wizards;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jst.server.generic.ui.internal.SWTUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;

/**
 * Composite for configuring Windows Azure server properties.
 * 
 * @author idan
 *
 */
public class AzureServerWizardComposite extends Composite {

	protected String jreLocation;
	protected String webServerLocation;
	protected String webServerPort;
	private final IWizardHandle wizardHandle;

	public AzureServerWizardComposite(Composite parent, IWizardHandle wizardHandle) {
		super(parent, SWT.NONE);
		this.wizardHandle = wizardHandle;
		
		wizardHandle.setTitle("Windows Azure Server");
		wizardHandle.setDescription("Windows Azure server configuration");
		
		createControls();
	}

	private void createControls() {
		
		final Composite parent = this;
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		this.setLayout(layout);
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.horizontalSpan = 1;
        
        GridData fillData = new GridData(SWT.FILL, SWT.FILL, true, true);
        fillData.heightHint = 100;
        
        final Composite topComposite = new Composite(parent, SWT.NONE);
        topComposite.setLayout(new GridLayout(3, false));
        topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Paths
        final Text jreLocationText = SWTUtil.createLabeledPath("JDK location:", "<default>", topComposite);
        jreLocationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				jreLocation = jreLocationText.getText(); 
			}
        });
        
        final Text webServerLocationText = SWTUtil.createLabeledPath("Web server location:", "<default>", topComposite);
        webServerLocationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				webServerLocation = webServerLocationText.getText(); 
			}
        });
        
        // Web server port
        final Text webServerPortText = SWTUtil.createLabeledText("Web server port:", "8080", topComposite);
        webServerPortText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				webServerPort = webServerPortText.getText();
				validate();
			}
        });
        
        // Servers
        final Group serversGroup = new Group(parent, SWT.NONE);
        serversGroup.setLayout(new GridLayout(1, false));
        serversGroup.setLayoutData(gridData);
        serversGroup.setText("Web Servers");
        
        final Tree serversTree = new Tree(serversGroup, SWT.BORDER);
        serversTree.setLayoutData(fillData);
		final TreeItem tomcatRootItem = new TreeItem(serversTree, SWT.NONE);
		tomcatRootItem.setText("Apache Tomcat");
		new TreeItem(tomcatRootItem, SWT.NONE).setText("Tomcat v5.0 Server");
		new TreeItem(tomcatRootItem, SWT.NONE).setText("Tomcat v5.5 Server");
		new TreeItem(tomcatRootItem, SWT.NONE).setText("Tomcat v6.0 Server");
		new TreeItem(tomcatRootItem, SWT.NONE).setText("Tomcat v7.0 Server");
		final TreeItem jettyRootItem = new TreeItem(serversTree, SWT.NONE);
		jettyRootItem.setText("Jetty");
		new TreeItem(jettyRootItem, SWT.NONE).setText("Jetty WebServer 8.1.0");
				
		// Roles
		final Group rolesGroup = new Group(parent, SWT.NONE);
		rolesGroup.setText("Roles");
        rolesGroup.setLayout(new GridLayout(1, false));
        rolesGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        
		final Table rolesTable = new Table(rolesGroup, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		rolesTable.setLayoutData(fillData);
		rolesTable.setHeaderVisible(true);
		rolesTable.setLinesVisible(true);
		
		new TableColumn(rolesTable, SWT.NONE).setText("Name");
		new TableColumn(rolesTable, SWT.NONE).setText("VM Size");
		new TableColumn(rolesTable, SWT.NONE).setText("Instances");
		
		new TableItem(rolesTable, SWT.NONE, 0).setText(new String[] { "WorkerRole1", "Small", "1" });
		
		rolesTable.getColumn(0).pack();
		rolesTable.getColumn(1).pack();
		rolesTable.getColumn(2).pack();	

	}
	
	protected void validate() {
		if (webServerPort != null && webServerPort.length() > 0) {
			try {
				Integer.parseInt(webServerPort);
				wizardHandle.setMessage("", IMessageProvider.NONE);
			} catch (NumberFormatException e) {
				wizardHandle.setMessage("Web server port should be a numeric value", IMessageProvider.ERROR);
			}
		}
	}

	public String getJreLocation() {
		return jreLocation;
	}
	
	public String getWebServerLocation() {
		return webServerLocation;
	}
	
	public String getWebServerPort() {
		return webServerPort;
	}
}
