##LIST OF KNOWN ISSUES

* Plant Comments
    * Our system does not limit the comment size.
    * When comments are larger than 32,767 characters, they are larger than the maximum
     allowed size within on Excel Cell, thus breaking the export data to Excel button.
* Unhandle Memory sizes
    * Giant comments (of the MB variety) can cause our meager system to run out of memory.
    * Uploading huge, huge pictures also cause the system to run out of memory.
* Uploaded Images
    * If the database is updated, the plants that had images attached will no longer show
     the images (they are still stored, but due to the storage method, they are attached via uploadID)
    * Once a plant is given an image, there is no way to remove the image, only replace.
    * Images are not compressed (a 16 MB takes longer to load than a 3 MB file,
     a 50kB may even be of sufficient quality.)
* Admin
    * Every time the admin signs in, they have to allow offline access.
* Miscellaneous
    * There are leftover files that can be safely removed (things like "users.seed.json")
    * There is an ugly error suppression setup for when plants do not have a picture to display
    * Interaction counts do not properly update when using Internet Explorer
    * Some methods are not properly tested
    * The server sends the entire plant object each time the client
     requests the plant. This is not secure and only the Bed number, common name, and cultivar name 
     need to be shared
    * On the import spreadsheet page, an unhelpful "File upload failed." This could be a more helpful message.
    * Once a bed is chosen, there is no obvious way to go back to having no bed selected.