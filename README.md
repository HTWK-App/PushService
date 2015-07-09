HTWK Push Microservice - Preview
=================================

This microservice adds the ability to push messages to android devices, running the HTWK-App (Push branch). It's just a preview and not everything is working by now. The Idea was to NOT show a notification at every push, only at a selection of pushed messages. Unfortunately, this isn't possible with the default Cordova Push-Plugin.

### Using this Service ###

Once your service is running all you need to do is to issue commands to one of the following URLs at:

```
curl -X PUT    http://localhos:9000/regid/<id>  - Register Device ID (issued at App start)
curl -X DELETE http://localhos:9000/regid/<id>  - Unregister Device ID
curl -X POST   http://localhos:9000/pushto/<id> - Push a default message to Device with ID id
```

### Compilation/Running the Server  ###

Install the [Typesafe Acticator](//www.playframework.com/documentation/2.3.x/Installing).

For **developement mode**, execute the following commands:

```
# May take some time...
activator update
activator run
```

To package the application for **production mode**, execute the following command. You will be told where the resulting dist zip is placed. Inside this zip, theres a run script. Start it and your ready to go.

```
activator dist
```

Please note that this application need a key to push to Googles GCM Servers. This key is NOT (!!!) included in this repository. Add your key inside the file ** app/controllers/GCM.scala ** at line 25.