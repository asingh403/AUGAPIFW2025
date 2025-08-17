!/bin/bash

# 1) Stop tracking the secret file
git rm --cached src/test/resources/config/config.properties
git commit -m "remove config.properties from repo"
git push

# 2) Ignore all such files going forward
printf "\n# secrets\n**/config.properties\n" >> .gitignore
git add .gitignore
git commit -m "ignore config.properties"
git push

