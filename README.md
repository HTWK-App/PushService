HTWK Push Microservice - Preview
=================================
[![Build Status](https://snap-ci.com/HTWK-App/PushService/branch/master/build_image)](https://snap-ci.com/HTWK-App/PushService/branch/master)
[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](https://github.com/HTWK-App/PushService/blob/master/LICENSE)
[![Language](https://img.shields.io/badge/language-Scala%20(2.11.7)-blue.svg)](http://www.scala-lang.org/)
[![Framework](https://img.shields.io/badge/framework-PlayFramework%20(2.3.10)-blue.svg)](https://www.playframework.com/)
[![Lines of Code](https://img.shields.io/badge/loc-~103-lightgrey.svg)]()

This microservice adds the ability to push messages to android devices, running the HTWK-App (Push branch). It's just a preview and not everything is working by now. The Idea was to NOT show a notification at every push, only at a selection of pushed messages. Unfortunately, this isn't possible with the default Cordova Push-Plugin.

### Using this Service ###

Once your service is running all you need to do is to issue commands to one of the following URLs at:

```
curl -X PUT    http://localhost:9000/regid/<id>  - Register Device ID (issued at App start)
curl -X DELETE http://localhost:9000/regid/<id>  - Unregister Device ID
curl -X POST   http://localhost:9000/pushto/<id> - Push a default message to Device with ID id
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
