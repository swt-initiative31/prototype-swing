# SWT on Swing

This repository is a fork of the original [SWT repository](https://github.com/eclipse-platform/eclipse.platform.swt) containing prototyping work on exchanging the adaptation of native, basic widgets with Swing widgets.

In the following, you find information about the current state of the prototype and how to try it out. Note that this prototype is currently not further developed.


## Getting Started

To test the prototype, an Eclipse IDE has to be set up and the SWT projects from this repository have to be imported into it to start example applications.

Note that the Swing prototype is currently on tested on Windows and may not work on MacOS and Linux.

### Setup

1. Set up a recent Eclipse IDE for Committers (currently 2024-09 or newer including nightly builds), e.g.:
   - [Eclipse IDE for Committers 2024-09](https://www.eclipse.org/downloads/packages/release/2024-09/r/eclipse-ide-eclipse-committers)
   - [Eclipse SDK Development Environment](https://github.com/eclipse-platform/eclipse.platform?tab=readme-ov-file#how-to-contribute)
2. Clone this repository
3. Start the downloaded IDE and import the following projects:
   - The SWT bundle `org.eclipse.swt`
   - The Swing fragment `org.eclipse.swt.swing`
   - The OS-specific fragment `org.eclipse.swt.$WS.$OS.$ARCH` with the placeholders according to your environment, such as `org.eclipse.swt.win32.win32.x86_64` for the Windows fragment; this is only to _disable_ that native fragment and ensure that the Swing one is used (the OS-specific fragment has been modified to not match the actual system)
   - _Optional:_ For testing purposes `org.eclipse.swt.examples`

The resulting workspace should look something like this:
![Workspace after setup](readme_images/workspace_after_setup.png)

### Examples

Starting with this, you can try out the SWT implementation with whatever application you want by also importing the according plug-ins into your workspace. The best starting point is the SWT `ControlExample`, a demo application containing all basic SWT widgets with configurability for most of their functionality, to see how the widgets look like and behave. It is also possible to start an Eclipse application based on Swing (see screenshots in [the documentataion](https://github.com/swt-initiative31/documents/blob/main/results/swing.md)), but this is currently complicated to set up.

#### `ControlExample`

The `ControlExample` is part of the example project. It is placed in `org.eclipse.swt.examples.controlexample`. You can run this class as a Java application. Everything you see in this application is rendered with SWT using only Swing.

#### Theming

You can apply any Swing theme of your choice and it will be applied to the application. For example, in the `ControlExample`, define the theme in the `main` method of the `ControlExample` class right after instantiating the `ControlExample` class within that method. A Swing-provided theme can, e.g., be set via:
````java
UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
````
More sophisticated third-party themes can also be used by just placing the JAR of that theme on the classpath and setting up the theme at the same line of code.


## State

Note that this is work-in-progress prototyping work. The implementation is not (supposed to be) production ready. There are compile errors and it is, in particular, not easy to start an Eclipse application yet. Still, in the `ControlExample` you can experience every basic widget working.
