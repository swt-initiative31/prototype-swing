/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 *
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Composite;

class CCompositeImplementation extends JPanel implements CComposite {

  protected Composite handle;
  protected JPanel contentPane;
  protected JScrollPane scrollPane;

  @Override
public Container getSwingComponent() {
    return contentPane;
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

  public CCompositeImplementation(Composite composite, int style) {
    super(new BorderLayout(0, 0));
    this.handle = composite;
    init(style);
  }

  @Override
public boolean isFocusable() {
    return contentPane.isFocusable();
  }

  @Override
public void requestFocus() {
    contentPane.requestFocus();
  }

  @Override
public void setCursor(Cursor cursor) {
    contentPane.setCursor(cursor);
  }

  protected void init(int style) {
    if((style & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    } else {
      setBorder(null);
    }
    JPanel panel = new JPanel(null) {
      protected Graphics graphics;
      @Override
	public Graphics getGraphics() {
        Graphics g;
        if(graphics != null) {
          g = graphics.create();
        } else {
          g = super.getGraphics();
        }
        return g;
      }
      @Override
	public boolean isOptimizedDrawingEnabled() {
    	  return false;
//        return getComponentCount() < 2 || Utils.isFlatLayout(handle);
      }
      @Override
	public boolean isOpaque() {
        return (CCompositeImplementation.this == null || backgroundImageIcon == null) && super.isOpaque();
      }
      @Override
	protected void paintComponent (Graphics g) {
        graphics = g;
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, g);
        if(!(getParent() instanceof JViewport)) {
          Utils.paintTiledImage(this, g, backgroundImageIcon);
        }
        super.paintComponent(g);
        handle.processEvent(new PaintEvent(this, PaintEvent.PAINT, null));
        putClientProperty(Utils.SWTSwingGraphics2DClientProperty, null);
        graphics = null;
      }
      @Override
	public Color getBackground() {
        return CCompositeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
      }
      @Override
	public Color getForeground() {
        return CCompositeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
      }
      @Override
	public Font getFont() {
        return CCompositeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
      }
      @Override
	public Cursor getCursor() {
        if(Utils.globalCursor != null) {
          return Utils.globalCursor;
        }
        return CCompositeImplementation.this != null && userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
      }
      @Override
	protected void processEvent(AWTEvent e) {
        if(Utils.redispatchEvent(getSWTHandle(), e)) {
          return;
        }
        super.processEvent(e);
      }
    };
    userAttributeHandler = new UserAttributeHandler(panel);
    if((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
      JScrollPane scrollPane = new UnmanagedScrollPane((style & SWT.V_SCROLL) != 0? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS: JScrollPane.VERTICAL_SCROLLBAR_NEVER, (style & SWT.H_SCROLL) != 0? JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS: JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
        @Override
		protected DisconnectedViewport createDisconnectedViewport() {
          return new DisconnectedViewport() {
            protected boolean isCreated = true;
            @Override
			public boolean isOpaque() {
              return backgroundImageIcon == null && super.isOpaque();
            }
            @Override
			protected void paintComponent(Graphics g) {
              Utils.paintTiledImage(this, g, backgroundImageIcon);
              super.paintComponent(g);
            }
            @Override
			public Color getBackground() {
              return isCreated? userAttributeHandler.background: super.getBackground();
            }
          };
        }
      };
      this.scrollPane = scrollPane;
      scrollPane.setBorder(null);
      add(scrollPane, BorderLayout.CENTER);
      scrollPane.getViewport().setView(panel);
    } else {
      add(panel, BorderLayout.CENTER);
    }
    contentPane = panel;
    contentPane.setFocusable (true);
    Utils.installMouseListener(contentPane, handle);
    Utils.installKeyListener(contentPane, handle);
    Utils.installFocusListener(contentPane, handle);
    Utils.installComponentListener(this, handle);
  }

  @Override
public Container getClientArea() {
    return contentPane;
  }

  @Override
public JScrollBar getVerticalScrollBar() {
    return scrollPane == null? null: scrollPane.getVerticalScrollBar();
  }

  @Override
public JScrollBar getHorizontalScrollBar() {
    return scrollPane == null? null: scrollPane.getHorizontalScrollBar();
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
    case NO_BACKGROUND_INHERITANCE:
      setOpaque(true);
      contentPane.setOpaque(true);
      if(scrollPane != null) {
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
      }
      break;
    case BACKGROUND_INHERITANCE:
      setOpaque(false);
      contentPane.setOpaque(false);
      if(scrollPane != null) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
      }
      break;
    }
  }

}

/**
 * The composite equivalent on the Swing side.
 * @version 1.0 2005.08.31
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CComposite extends CScrollable {

  public static class Factory {
    private Factory() {}

    public static CComposite newInstance(Composite composite, int style) {
      return new CCompositeImplementation(composite, style);
    }

  }

}
