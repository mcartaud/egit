/*******************************************************************************
 * Copyright (C) 2008, Tor Arne Vestbø <torarnv@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Thomas Wolf <thomas.wolf@paranor.ch> - Factored out IResourceState
 *******************************************************************************/

package org.eclipse.egit.ui.internal.decorators;

import org.eclipse.core.resources.IResource;
import org.eclipse.egit.ui.internal.resources.IResourceState;

/**
 * Represents the state of a resource that can be used as a basis for decoration
 */
public interface IDecoratableResource extends IResourceState {

	/**
	 * Gets the type of the resource as defined by {@link IResource}
	 *
	 * @return the type of the resource
	 */
	int getType();

	/**
	 * Gets the name of the resource
	 *
	 * @return the name of the resource
	 */
	String getName();

	/**
	 * Gets the name of the repository of the resource
	 *
	 * @return the name of the current branch, or <code>null</code> if not
	 *         applicable
	 */
	String getRepositoryName();

	/**
	 * Gets the current branch of the resource if applicable
	 *
	 * @return the name of the current branch, or <code>null</code> if not
	 *         applicable
	 */
	String getBranch();

	/**
	 * @return a symbol indicating the branch status relative to the remote
	 *         tracking branch, or <code>null</code> if not applicable
	 */
	String getBranchStatus();

}
