/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.guing.common.components.tree;

import java.util.HashSet;
import javax.inject.Inject;
import org.opentcs.guing.base.model.ModelComponent;
import org.opentcs.guing.common.application.ComponentsManager;
import org.opentcs.guing.common.components.tree.elements.UserObject;
import org.opentcs.guing.common.persistence.ModelManager;
import org.opentcs.thirdparty.guing.common.jhotdraw.application.action.edit.UndoRedoManager;

/**
 * The TreeViewPanel for components.
 *
 * @author Philipp Seifert (Philipp.Seifert@iml.fraunhofer.de)
 */
public class ComponentsTreeViewPanel
    extends AbstractTreeViewPanel {

  /**
   * Creates a new instance.
   *
   * @param undoRedoManager The undo redo manager
   */
  @Inject
  public ComponentsTreeViewPanel(UndoRedoManager undoRedoManager,
                                 ModelManager modelManager,
                                 ComponentsManager componentsManager) {
    super(undoRedoManager, modelManager, componentsManager);
  }

  @Override // EditableComponent
  public void cutSelectedItems() {
    bufferSelectedItems(true);
  }

  @Override // EditableComponent
  public void copySelectedItems() {
    bufferSelectedItems(false);
  }

  @Override // EditableComponent
  public void pasteBufferedItems() {
    restoreItems(bufferedUserObjects, bufferedFigures);
    // Also make "Paste" undoable
    fUndoRedoManager.addEdit(
        new AbstractTreeViewPanel.PasteEdit(bufferedUserObjects, bufferedFigures)
    );
  }

  @Override // EditableComponent
  public void duplicate() {
    bufferSelectedItems(false);
    restoreItems(bufferedUserObjects, bufferedFigures);
    fUndoRedoManager.addEdit(
        new AbstractTreeViewPanel.PasteEdit(bufferedUserObjects, bufferedFigures)
    );
  }

  @Override // EditableComponent
  public void delete() {
    bufferSelectedItems(true);

    if (bufferedUserObjects.isEmpty() && bufferedFigures.isEmpty()) {
      return; // nothing to undo/redo
    }

    fUndoRedoManager.addEdit(
        new AbstractTreeViewPanel.DeleteEdit(bufferedUserObjects, bufferedFigures)
    );
  }

  @Override // EditableComponent
  public void selectAll() {
    // Sample implementation (HH 2014-04-08):
    // Select all components in the currently focused tree folder
    // TODO: select all components except folders
    UserObject selectedItem = getSelectedItem();

    if (selectedItem != null) {
      ModelComponent parent = selectedItem.getParent();

      if (parent != null) {
        selectItems(new HashSet<>(parent.getChildComponents()));
      }
    }
  }

  @Override // EditableComponent
  public void clearSelection() {
    // Not used for our tree:
    // JTree's default action for <Ctrl> + <Shift> + A already does the job.
  }

  @Override // EditableComponent
  public boolean isSelectionEmpty() {
    // Not used for tree ?
    return true;
  }
}
