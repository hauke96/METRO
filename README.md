[![Build Status](https://travis-ci.org/hauke96/METRO.svg?branch=master)](https://travis-ci.org/hauke96/METRO)
# METRO

`METRO` is a small and simple rapid transit simulator. You can build tracks and stations, buy trains and realize a massive transport system by creating an efficient railway network.

In addition to this page, there's also a [wiki](https://github.com/hauke96/METRO/wiki) for more detailed non-tech information.

## For developer

* The documentation as website: http://hauke-stieler.de/public/METRO/doc/
* The diagrams as HTML version: http://hauke-stieler.de/public/METRO/digrams
 
#### Contribute
There's currently only one thing you can contribute: Report bugs, errors and feature wishes.
If you have something you want to tell me, just go into the [`issues`](https://github.com/hauke96/METRO/issues) section and create a new ticket. I'll process the ticket as soon as possible.

## For Player
* The wiki: https://github.com/hauke96/METRO/wiki

If you have compiled the source into a jar or started the project via you favorite IDE, there will appear the main menu. Just click on Play and the game starts. Currently there are no save games implemented (this comes later).

#### Settings
Pressing `ESC -> Settings` will lead you into the settings window. There are some options available yet (e.g. resolution) and maybe there'll be more in the future. If you've troubles with changed settings, just remove the `settings.cfg` file and start the game (METRO will generate a default new one).

#### Strange circles
The circles show the population in this region. The more people live there, the more people wants to travel. The population index (between 0 and 9) is shown by hovering a ring with the cursor.

#### Important words
* `track` - Tracks have no function. A train can move on tracks, but only if you tell them.
* `line` - You can define a train line (or just line) to tell a train where to move.
* `station` - You can tell a train to stop there. Passengers can now enter or leave the train.
* `node` - A railway node (or just node) is just a point where a train or station can be. If two nodes are next to each other there will appear a line (s.o.). A train just moves from node to node.

## Little guide
This is a small guide showing you how to do the most important things.

#### Create tracks and stations
Now you have the normal map (with the grid and these strange circles), the info bar in the top-left corner, the tool bar on the left side and some debug-information on the top.

To built new tracks (the first thing to do), just click on the second button on the tool bar. Now you can choose a start point (first click) and an end point (second click). By right clicking you can cancel the building process.

These tracks need stations so click on the first button to build stations (just click). When you're finished just click right to go back to the normal mode.

#### Create line
To tell a train where to drive, you must define train lines to do that. Just click on the third icon in the tool bar to open the train line dialog.

A line can have a name, a color and an amount of railway nodes. Click on `Create line` to create a new line. After clicking on that button, you can click on each node that should be in this line.

##### (In)valid lines
To finish the process, the line has to be `valid`. If one of these conditions is true, the line is ~not~ valid:
* There's no node in the line
* There's only one node in the line
* There's at least one gap in the line

Normally a small message label will be visible, telling you whats wrong with the settings you've chosen.
##### Edit lines
It's also possible to edit a line. Just click on a line in the list and then on the `Edit` button. 

#### Create new trains
To buy a new train you can click on the last button on the toolbar which will lead you into the train management where you can buy new trains, edit existing or sell trains. On the top half of the dialog there are two lists: The line list on the right and the train list (showing all trains of the selected line) on the left. The list on the bottom shows all train models you can buy.

##### Train Models
There will be many train models you can choose between. Some are more expensive but also more lucrative than others and so on.
One important part of the trains is their lifetime. They don't get any damage or something but the money you have to pay each month to pay the operating costs, is rising with each month. After some years the costs of a train are so high that the earning of a full train is less than its costs.

On that point you can decide between increasing rising the price per ticket or buying a new train (you can also buy the same model again).
> There's already a 'buy'- but no 'manage'- or any other functionality yet.
