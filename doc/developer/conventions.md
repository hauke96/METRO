# Definitions
## Tools (MVC like)
A _tool_ is a set of classes building a UI component. Every tool is split into three classes.

##### Tool class
The tool class (in MVC like the controller) is the main class everyone uses to create the component. It also creates the _view_ and the _model_ of the component.
This class holds all services and other external classes needed.

##### View class
This is the UI-component of the tool created by the _tool class_. It just has some getters on its interface returning certain UI elements.

##### Model class
The model class holds all the data and is used by the controller which passes them into the view if needed.

## Services
A service is class holding certain values. E.g. the `TrainLineService` would hold a set of objects of type `TrainLine` which is a kind of material or value object.

Every service has an interface defining the methods usable for the client.

# Naming conventions
The bold part is always the important part, everything else is _dummy..._ or something.

## Tools
Here's how the three classes should be named:
* Tool class: Dummy__Tool__
* View class: Dummy__View__
* Model class: Dummy__Model__

## Services
Services are named just with _service_ in the end:

* Dummy__Service__

# Structural conventions
A class should be structured as followed:

1. Class header
	1. Import statements
	2. Javadoc describing what this class is (indlucing the `@author` tag)
	3. Class name
2. Fields
	1. Just group them together by access modifiers
3. Constructor
	1. Most general top (e.g. the one without any parameter)
	2. Most specific bottom (e.g. with all parameters needed)
4. Methods
	1. Any kind of init methods
	2. Getter and Setter (first a getter like `getName()` followed by setter `setName(String name)`). Other sounding methods like `isActive()` are actually getter.
	3. Changing methods like `draw()`
	4. Events like `mouseDown(int x, int y, int button)`
