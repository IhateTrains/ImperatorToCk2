rem **Create blankMod**
del "..\Debug\ImperatorToCK2\defaultOutput" /Q /S /F
rmdir "..\Debug\ImperatorToCK2\defaultOutput" /Q /S
xcopy "DataFiles\defaultOutput" "..\Debug\ImperatorToCK2\defaultOutput" /Y /E /I

copy "DataFiles\Readme.txt" "..\Debug\ImperatorToCK2"
copy "DataFiles\license.txt" "..\Debug\ImperatorToCK2"

git rev-parse HEAD > ..\Debug\commit_id.txt
