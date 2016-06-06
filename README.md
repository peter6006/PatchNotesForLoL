# PatchNotesForLoL

PatchNotesForLoL is a mobile App (only Android) coded using the RIOT API for developers.

This App will look for the last patch of the game, format it and then display it. It will also send a notify to your mobile when the new patch is out if you want it. The only data the App will need for working is the server and language to see the patch, it will never ask you for personal information, accounts or passwords. Its totally free (with Ads) and dont have micro pays inside the application.

### Example of API call:
I only use one call to Static Data and save the response in a variable:
```sh
java.util.List<String> listOfVersions = StaticDataMethod.getDataVersions("euw", "MY-API-KEY-GO-HERE");
```
This return an array of versions (["6.11.1","6.10.1","6.9.1"...]), i split every element on the array to match the way RIOT publish the patch, so 6.11.1 will be 6.11 and on:
```sh
String[] version = listOfVersions.get(i).split("\\.");
patch = version[0] + "." + version[1];
```
Then, if the app want to know if there is a new version, it will compare the last version in the array with the version saved locally, if the new version is higher, the app will ask the RIOT official page if the page is ready to read. If all is correcto, the app will notify the user about the new patch.
When the user want to read the patch, the app re-do the same logic to make sure all is OK.

The sites where the App collect the data are the API and the official web site of each server+language.

You can download the [App here](https://play.google.com/store/apps/details?id=peter.skydev.lolpatch.free)

Feel free to ask any doubt about the code, report an issue, make a sugestion... 

### Email
peter60006@gmail.com
