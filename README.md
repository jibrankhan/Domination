Domination
==========

An implementation of a plan recognition agent for RISK in the 'Domination' game developed by yura.net.

-------------------------------------------------------------------------------------------------------

After cloning this repo and setting it up in your IDE of choice (I recommend Netbeans for this project). In Netbeans you shall find the package com.google... etc does not exist error.

To fix this is a bit of a pain for this project but essentially Netbeans 'seems' to fails to update its build path for the project whilee there are calls to a lib not contained in its build path. The result is an error proclaiming that the jar is not there despite that it actually is.

TO FIX:

Remove (I highly suggest copying it to somewhere) the planrecognition file in Domination\src\net\yura\domination\engine\ai

Now there will be some errors in the RiskGame.java file. Comment out (not delete) all the lines that are causing errors.

Now build the project.

After a sucessful build:

1) Copy the guava-14.0-rc1.jar located in Domination\lib 

2) Paste the JAR into the location Domination\build\game\lib

Bulld the project again.

Now put back the planrecognition folder from where you removed it and uncomment all the lines you commented out in RiskGame.java

Build again.

That should be it working! Enjoy!

