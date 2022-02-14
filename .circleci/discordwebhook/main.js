 /*
  * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
  * Copyright (c) 2022 Pentium plus.
  */



 const axios = require("axios").default

 const branch = process.argv[2]
 const version = process.argv[3]
 const build = process.argv[4]
 const compareUrl = process.argv[5]

 const downloadUrl = "https://meteorclient.com/download?devBuild=" + build

 axios
     .get(compareUrl)
     .then(res => {
         let success = true
         let description = ""

         description += "**Branch:** " + branch
         description += "\n**Status:** " + (success ? "success" : "failure")

         let changes = "\n\n**Changes:**"
         let hasChanges = false
         for (let i in res.data.commits) {
             let commit = res.data.commits[i]

             changes += "\n- [`" + commit.sha.substring(0, 7) + "`](https://github.com/MeteorDevelopment/meteor-client/commit/" + commit.sha + ") *" + commit.commit.message + "*"
             hasChanges = true
         }
         if (hasChanges) description += changes

         if (success) {
             description += "\n\n**Download:** [meteor-client-" + version + "-" + build + "](" + downloadUrl + ")"
         }

         const webhook = {
             username: "Dev Builds",
             avatar_url: "https://meteorclient.com/icon.png",
             embeds: [{
                 title: "Pentium plus v" + version + " build #" + build,
                 description: description,
                 url: "https://meteorclient.com",
                 color: success ? 3066993 : 15158332
             }]
         };

         axios.post(process.env.discord_webhook, webhook);
         axios.post(process.env.guilded_webhook, webhook);

         axios.post("https://meteorclient.com/api/setDevBuild?devBuild=" + build, {}, { headers: { "Authorization": process.env.server_token } })
     })