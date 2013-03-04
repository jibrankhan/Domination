Domination
==========

An implementation of a plan recognition agent for RISK in the 'Domination' game developed by yura.net.

-----------------------------------------------------------------------------------------------

After cloning this repo and setting it up in your IDE of choice (I recommend Netbeans for this project). In Netbeans you shall find the package com.google... etc does not exist error.

To fix this is a bit of a pain for this project but essentially Netbeans 'seems' to fails to update its build path for the project whilee there are calls to a lib not contained in its build path. The result is an error proclaiming that the jar is not there despite that it actually is.

To fix this follow these steps

1. Comment out everything in the planrecognition file in Domination\src\net\yura\domination\engine\ai

2. Now there will be some errors in a few files in the project. Comment out (not delete) all the lines that are causing these errors.

3. Now build the project.

After a sucessful build:

5. Copy the guava-14.0-rc1.jar located in Domination\lib 

6. Paste the JAR into the location Domination\build\game\lib

7. Bulld the project again.

8. Now uncomment everything you had commented previously.

9. Build the project again.

That should be it working! Enjoy!

