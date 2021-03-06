/*******************************************************************************
 * Copyright (C) 2011, 2015 Philipp Thun <philipp.thun@sap.com> and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Thomas Wolf <thomas.wolf@paranor.ch> - Factored out ResourceState
 *******************************************************************************/
package org.eclipse.egit.ui.internal.decorators;

import org.eclipse.core.resources.IResource;
import org.eclipse.egit.ui.internal.resources.ResourceState;

/**
 * Basic implementation of <code>IDecoratableResource</code>
 *
 * @see IDecoratableResource
 */
public class DecoratableResource extends ResourceState
		implements IDecoratableResource {

	/**
	 * Resource to be decorated
	 */
	protected IResource resource = null;

	/**
	 * Name of the repository of the resource
	 */
	protected String repositoryName = null;

	/**
	 * Current branch of the resource
	 */
	protected String branch = null;

	/**
	 * Branch status relative to remote tracking branch
	 */
	protected String branchStatus = null;


	/**
	 * Constructs a new decoratable resource
	 *
	 * This object represents the state of a resource used as a basis for
	 * decoration.
	 *
	 * @param resource
	 *            resource to be decorated
	 */
	protected DecoratableResource(IResource resource) {
		this.resource = resource;
	}

	@Override
	public int getType() {
		return resource != null ? resource.getType() : 0;
	}

	@Override
	public String getName() {
		return resource != null ? resource.getName() : null;
	}

	@Override
	public String getRepositoryName() {
		return repositoryName;
	}

	@Override
	public String getBranch() {
		return branch;
	}

	@Override
	public String getBranchStatus() {
		return branchStatus;
	}

}
