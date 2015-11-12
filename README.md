# Todo List
A todo list app designed to exemplify some coding practices and third party integrations.  Still
very much a work in progress.

This project plays with some design and architecture ideas I been throwing around, so it doesn't
necessarily represent my typical production level code.

## Configure Parse
In order to use this project, you will need to add a couple string resources used to configure Parse.
I created a resource file that only contains those values.  I then added that file to my git ignore.

Reference: https://parse.com/apps/quickstart#parse_data/mobile/android/native/new

# Add twitter configuration
The application is integrated with Twitter via the Parse SDK.  Twitter configuration values should
be added to the parse_config.xml file in order for the to work.

Reference: https://www.parse.com/docs/android/guide#users-twitter-users

### todolist/app/src/main/res/values/parse_config.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="parse_application_id">application id</string>
    <string name="parse_client_key">client key</string>
    <string name="twitter_customer_key">customer key</string>
    <string name="twitter_secret_key">secret key</string>
</resources>
```
### ~/.gitignore_global
```
app/src/main/res/values/parse_config.xml
```
