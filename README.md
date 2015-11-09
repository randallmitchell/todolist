# todolist
A todo list app designed to exemplify some coding practices and third party integrations.  Still very much a work in progress.

# Configure Parse
In order to use this repo, you will need to add a couple string resources used to configure Parse.  I created a resource file that only contains those values.  I then added that file to my git ignore.

### todolist/app/src/main/res/values/parse_config.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="parse_application_id">application id</string>
    <string name="parse_client_key">client_key</string>
</resources>
```
### ~/.gitignore_global
```
...
app/src/main/res/values/parse_config.xml
```
