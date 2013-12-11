eccApplication
==============

Java based exploration of elliptic curve groups and how they can be used for elliptic curve cryptography
ToC:
1) About the program, ECC, and its purpose
2) about the code.

*** please see \dist\javadoc  for full information about the code***


1)
This is an attempt to allow for an exploration of elliptic curve cryptography and how the underlying mathematics works.

Unfortunately, a txt file is not sufficient to explain finite fields, elliptic curves, and point arithmetic which would be necessary to fully appreciate this. Check out the ECC tutorial at Certicom (http://www.certicom.com/index.php/ecc-tutorial) for a better introduction.

Briefly,
the application allows the user to choose a finite field  (modulo a prime p) to work with, and two constants a and b which will form the elliptic curve equation.

The points on that elliptic curve are then calculated and presented to the user.

By clicking on points, you can create a sum to explore point addition. Each click increase the number of times you are adding that point to the equation (starting with the point at infinity as the identity point).

You can also explore the order of a point by clicking on it until it sums to itself.

The program also explores the Discrete Logarithm problem which is of utmost importance to the security of ECC.

The tabs on the right can be used to navigate between the addition, Discrete Logarithm Problem, and a basic Quiz functionality to test understanding.




2) The code structure uses the MVC framework to separate the model of the Elliptic Curve Group and the mathematical operations which can be performed within the group, from the GUI.
The folders are as follows:

	- ECCToolBox: This contains a collection of miscellaneous constants and utility functions which are useful throughout the program but not tied to a specific purpose.

	-ECGroups: This defines Elliptic Curve points, curves, finite fields and their elements (FieldElementFactory), and the algorithms for exploring the DLP and performing group operations.

	-EllipticCurveCryptography: this is just a main method which launches the gui.

	-GUIAttempt2: Obviosuly attempt1 wound up being rubbish. I'm content with attempt2. This contains one class which interacts with ECGroups (called ECGModel) and then all the View and Controller classes which connect to the model. There's some interesting container structure, for example in the way the grid of ECPoints is displayed. The ECPoints are drawn onto a GraphView at a relative distance from each other based on a zoom coefficient (retrieved via a controller), and is resized dynamically to ensure all points are drawn. The view is contained in a JScrollPane to allow the user to view the entire group. 

	-Quiz: This contains the full functionality (except for the GUI) for a general purpose multiple choice Quiz implementation, as well as the class ECGQuiz which creates questions specifically based on a given EllipticCurveGroup.This could easily be adapted for use with any quiz program, with a text or graphical interface.

