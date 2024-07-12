/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 *
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

class CTabFolderImplementation extends JTabbedPane implements CTabFolder {

  protected TabFolder handle;

  @Override
public Container getSwingComponent() {
    return this;
  }

  @Override
public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;

  @Override
public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }

  public CTabFolderImplementation(TabFolder tabFolder, int style) {
    this.handle = tabFolder;
    userAttributeHandler = new UserAttributeHandler(this);
    init(style);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    } else {
      setBorder(null);
    }
    setTabPlacement((style & SWT.BOTTOM) != 0? BOTTOM: TOP);
    setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addChangeListener(e -> handle.processEvent(e));
  }

  @Override
public Container getClientArea() {
    return this;
  }

  @Override
public JScrollBar getHorizontalScrollBar() {
    return null;
  }

  @Override
public JScrollBar getVerticalScrollBar() {
    return null;
  }

  @Override
public Color getBackground() {
    return userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
  }
  @Override
public Color getForeground() {
    return userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
  }
  @Override
public Font getFont() {
    return userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
  }
  @Override
public Cursor getCursor() {
    if(Utils.globalCursor != null) {
      return Utils.globalCursor;
    }
    return userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
  }

  protected ImageIcon backgroundImageIcon;

  @Override
public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  @Override
public void setBackgroundInheritance(int backgroundInheritanceType) {
    switch(backgroundInheritanceType) {
    case PREFERRED_BACKGROUND_INHERITANCE:
    case NO_BACKGROUND_INHERITANCE: {
      Object isContentOpaqueObject = UIManager.get("TabbedPane.contentOpaque");
      UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
      updateUI();
      UIManager.put("TabbedPane.contentOpaque", isContentOpaqueObject);
      setOpaque(true);
      break;
    }
    case BACKGROUND_INHERITANCE: {
      Object isContentOpaqueObject = UIManager.get("TabbedPane.contentOpaque");
      UIManager.put("TabbedPane.contentOpaque", Boolean.FALSE);
      updateUI();
      UIManager.put("TabbedPane.contentOpaque", isContentOpaqueObject);
      setOpaque(false);
      break;
    }
    }
  }

  @Override
public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    int count = getTabCount();
    if(count > 0) {
      Rectangle bounds = getUI().getTabBounds(this, count - 1);
      if (bounds != null) {
    	  size.width = Math.max(size.width, bounds.x + bounds.width + 10);
      }
    }
    return size;
  }

}

public interface CTabFolder extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CTabFolder newInstance(TabFolder tabFolder, int style) {
      return new CTabFolderImplementation(tabFolder, style);
    }

  }

  public void setTitleAt(int index, String title);

  public void setMnemonicAt(int tabIndex, int mnemonic);

  public void setIconAt(int index, Icon icon);

  public int getSelectedIndex();

  public void setSelectedIndex(int index);

  public void setToolTipTextAt(int index, String toolTipText);

}
